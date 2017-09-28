#!/usr/bin/env python
import sys
import time
import pyspark
import pyspark.streaming
import bloomfilter as bf


# parameters
N = 1000000         # number of items
W = 6               # number of workers
P = 18              # number of parititons

K = 10              # number of unique keys

def make_bloomFilter(L):
    """Create a new empty bloom filter, and add elements in L"""
    bloom = bf.BloomFilter(100, 10)
    for x in L:
        bloom.add(x)
    return bloom

def mightContain(items):
    """Count all items in a partition and return a single sketch."""
    bloom = make_bloomFilter(["#RIPHefner","#KCAColombia", "#HeyMa"])
    result = 0
    for item in items:
        if item in bloom : 
            result += 1
    return result

def main():
    """
    Generate multiple streams of data using W workers and P partitions.
    Create a bloomfilter on each of the P partitions.
    """
    # Create a local StreamingContext with two working thread and batch interval of 1 second
    sc = pyspark.SparkContext("local[2]", "Spark Bloom Filter")
    ssc = pyspark.streaming.StreamingContext(sc, 1)

    # Create a DStream that will connect to hostname:port, like localhost:9999
    socket_stream = ssc.socketTextStream("127.0.0.1", 5555)
    lines = socket_stream.window(10)
    counts = lines.flatMap(lambda text: text.split(" "))\
                  .map(lambda text: text.encode('utf-8'))\
                  .filter(lambda word: word.lower().startswith("#"))\
                  .map(mightContain)
    counts.pprint()

    ssc.start()
    ssc.awaitTermination()

if __name__ == "__main__":
    main()
