import sys
import time

import pyspark
import pyspark.streaming

# common setup
def printItems(items):
    for item in items:
        print item.encode('utf-8')
        
def main():
    sc = pyspark.SparkContext(master="local[%i]" % 2)
    sc.setLogLevel("ERROR")
    ssc = pyspark.streaming.StreamingContext(sc, 10)

    socket_stream = ssc.socketTextStream("127.0.0.1", 5555)
    
    lines = socket_stream.window(10)
    #lines.pprint()
    
    #hashtags = lines.flatMap(lambda line: line.split(" "))\
    #                .filter(lambda word: word.lower().startswith('#'))\
    # hashtags.pprint()                   

    counts = lines.flatMap(lambda line: line.split(" ")) \
                  .filter(lambda word: word.lower().startswith("#"))\
                  .map(lambda word: (word, 1)) \
                  .reduceByKey(lambda a, b: a+b)\
                  .transform(lambda rdd: rdd.sortBy(lambda a: a[1],ascending=False))
    counts.pprint()

    ssc.start()
    ssc.awaitTermination()

if __name__ == "__main__":
    main()
