"""
Count frequent tweet hashtags using Spark Streaming and Count-Min Sketch.
"""
# File from Sakai resources

import sys
import time

import pyspark
import pyspark.streaming

import CountMin as cm

# parameters

N = 10000000
W = 6
P = 18
# FREQUENCIES = {
#     "x": 100,
#     "y": 50,
#     "z": 25,
# }
DELTA = 1e-3
EPSILON = 1e-3

# common setup

WIDTH, DEPTH = cm.compute_width_depth(delta=DELTA, epsilon=EPSILON)
# SEEDS = cm.make_seeds()


def make_sketch():
    """Create a new empty sketch using global parameters."""
    return cm.CountMinSketch(width=WIDTH, depth=DEPTH)
    # return cm.CountMinSketch(width=WIDTH, depth=DEPTH, seeds=SEEDS)


def do_count(items):
    """Count all items in a partition and return a single sketch."""
    sketch = make_sketch()
    for item in items:
        sketch.increment(item.lower())
    return [sketch]


def add_sketches(s1, s2):
    """Combine two sketches (reduce phase)."""
    accumulator = make_sketch()
    accumulator.merge(s1)
    accumulator.merge(s2)
    return accumulator


def main():
    print "WIDTH = %i, DEPTH = %i" % (WIDTH, DEPTH)
    # print "SEEDS = %s" % (SEEDS,)

    totals = make_sketch()

    # def display_hitters():
    #     print
    #     print "=" * 60
    #     print "TOP HITTERS".center(60)
    #     print "=" * 60
    #     for rank, (key, count) in enumerate(totals.get_hitters(), 1):
    #         print "#%2d  %20.20s : %5d" % (rank, key, count)
    #     print "=" * 60
    #     print

    def accumulate_rdd(rdd):
        sketches = rdd.collect()
        print "accumulating %d sketches from RDD" % len(sketches)
        for sketch in sketches:
            totals.merge(sketch)

        #testing on hashtags
        print "#WorldSeries2017", totals.estimate("#WorldSeries2017")
        print "#Halloween", totals.estimate("#Halloween")


    sc = pyspark.SparkContext(master="local[%i]" % W)
    sc.setLogLevel("ERROR")
    ssc = pyspark.streaming.StreamingContext(sc, 10)

    socket_stream = ssc.socketTextStream("127.0.0.1", 5555)

    lines = socket_stream.window(10)

    (lines
         .flatMap(lambda text: text.split(" "))
         .filter(lambda word: word.lower().startswith('#'))
         .map(lambda x : x.encode('utf-8'))
         .mapPartitions(do_count)
         .reduce(add_sketches)
         .foreachRDD(accumulate_rdd)
    )

    ssc.start()
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        ssc.stop()

    # display_hitters()


if __name__ == "__main__":
    main()
