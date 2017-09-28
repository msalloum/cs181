# Overview

The code in this directory consists of a `BloomFilter` data type and
algorithm, implemented in Python, along with several scripts for using and
testing the data structure in various contexts.

The files are as follows:

  * `bloomfilter.py` -- core datatype and algorithm
  * `bloomfilter_test.py` -- unit test of bloomfilter.py
  * `bloomspark.py` -- test using Spark RDDs (batch processing framework)
  * `tweetstream.py` -- serve streaming tweets on a local port (for Spark streaming)
  * `sparkTwitterStream.py` -- read tweets from local port, process, and print counts of hashtags

# Dependencies

For each dependency, install using: `pip install <library>`

  * `mmh3` -- Murmur3 string hash function
  * `pytest` -- unit testing framework
  * `pyspark` -- Spark APIs for Python
  * `tweepy` -- Twitter APIs for Python
  
You don't have `pip' installed?? Download 'get_pip.py' from https://pip.pypa.io/en/stable/installing/ and run the code using 'python get-pip.py'


If you are working on Windows, then you may consider just sticking with Docker installing the tools directly. The instructions of installing Spark env using Docker is given 

```{sh}
docker pull sequenceiq/spark:1.6.0
``` 
```{sh}
 docker run -v  /Users/msalloum/cs181/HW3:/mnt/cs181   -it -p 8088:8088 -p 8042:8042 -p 4040:4040 -h sandbox sequenceiq/spark:1.6.0 bash
``` 

# Spark Batch Test

This is a test script which plugs the `CountMinSketch` data structure into Spark
using RDDs.  The basic algorithm is as follows:

  1. Generate data items in 18 partitions of data over 6 workers (in parallel),
  2. collect the results of each partition into sketches locally (using MapPartitions)
  3. perform a reduce operation which adds all the partitions together culminating in a single sketch, and
  4. query the resulting sketch.


# Spark Streaming with Twitter

The Twitter component of this project requires two programs: a tweet server and
a spark streaming application.  The tweet server connects to the twitter
streaming API and serves tweet data over a local port.  The twitter streaming
application then connects to that port to parse the data and count the words.

To run the system, first start the tweet server:

```{sh}
python tweetstream.py &
```

Then, invoke the Spark streaming application:

```{sh}
python sparkTwitterStream.py
```

At each 10 second interval, the program will display a frequency table showing
the 100 most popular hashtags along with their counts.

(Both programs can be terminated by pressing `Control-C`.)

The following top 10 list was accumulated after gathering hashtags from tweets:

```
============================================================
                       TOP HITTERS                        
============================================================

# 1            #nbafinals :   771
# 2            #dubnation :   690
# 3          #izmirescort :   431
# 4        #4yearswithbts :   356
# 5           #produce101 :   303
# 6        #veranomtv2017 :   270
# 7      #thisiswhyweplay :   199
# 8                  #bts :   131
# 9    #strengthinnumbers :   128
#10                  #nba :   122
============================================================
```

# Bloom Filter

Bloomfilter algorithm / data structure is implemented in bloomfilter.py. 

To run a test :


```{sh}
python bloomfilter_test.py
```


