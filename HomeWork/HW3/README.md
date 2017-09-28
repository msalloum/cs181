# HW 3 - CountMin Sketch 

This assignment will focus on Stream Processing Algorithms, specifically the Count-Min sketch. To learn more about the Count-Min sketch, refer to the following resources:

1.  http://theory.stanford.edu/~tim/s15/l/l2.pdf   (Lecture Notes on Count Min Sketch)
2.  https://people.cs.umass.edu/~mcgregor/711S12/countmin.pdf  (Original paper on Count Min Sketch)

This is a pair programming assingment. If you work with a partner, you must make sure you have both names on all code submitted, as well as a comment on Sakai or the github page. One submission per group is fine. 

-----------

## Installation of Spark

For this assignment, our goal is to stream tweets using the Twitter API and compute the frequency of hashtags using the Count-Min sketch. 

## Part 1 - Running BloomFilter example

Under the TwitterStream folder, you will find a standalone implementation of BloomFilter, sample PySpark code to stream tweets using the Twitter API and print the top hashtags, and finally an integrated spark / bloomfilter example. 

Please try the provided examples before proceeding to work on the homework. 


## Part 2 - Implement CountMin Sketch 

In the first part of the homework, you will implement a probabilistic data structure called a Count-Min sketch. The idea behind probabilistic data structures is that you can get an approximate answer very close to the actual answer while using less memory than a general hash function! Count-Min sketch is one such data structure and it is used to get the frequency of elements within a certain accuracy. 

A Count-Min sketch consists of a two-dimensional array (width x depth) of integer counters. The width represents the number of counters in a single row while the depth represents the number of such rows.

Now lets look at some pseudocode to update the frequency of a single data item stored in a count min sketch

```(sh)
  increment(item) {
      Calculate 'd' hash values for this item where 'd' is the depth of the Count-Min sketch.
      Each hash value here will be a number between 0 and 'width'
      ...
      hashes[1] = get_hash(item)          
      ...
      
      Increment the corresponding counter which is pointed to by each of the hash values. 
      i.e. if hashes[1] was equal to 4, then we would increment counter[4] corresponding to the first row of the  sketch
  }
```
  
To summarize, we update 'd' different counters when an item is inserted into the Count-Min sketch. Now lets take a look at how we can estimate the frequency of a particular item

```(sh)
   estimate(item) {
      Calculate 'd' hash values for this item similar to what we did for increment.          
      Return the minimum value among all the counters these hash values point to.
      
      i.e if we had depth = 2 and hashes[1] = 4, and hashes[2] = 2, the we would return the
      minimum of counter[4] from the first row and counter[2] from the second row.
   }
```

Implementation

You are asked to implement the Count-Min algorithm in the language of your choice on Spark (I would recommend Python because the BloomFilter example is in python). The Count-Min sketch should take two parameters, delta and epsilon. These two parameters should be used to determine w and d. Where w denotes the number of buckets (size of the hash tables), and d is the number of hash tables.

At minimum, your implementation should include 4 functions:

compute_width_depth(delta, epsilon) - Takes two parameters (delta and epsilon), which define the accuracy and error probability of the sketch, and it should return two values (width and depth). 

	d = 2/ delta      #  d represents the depth of the 2D array, or the number of hash functions used

	w = (log 1/ epsilon)    #  w represents the width of the 2D arra

increment (item)  - increment the corresponding counter for this item. The hash functions used is up to you, and they can be automatically generated. 

estimate (item)  - estimate the count for the given value.

merge (sefl, CountMin) - merges a CountMin sketch with the current sketch (inorder to merge sketches across partitions). 

# Part 3 - Integrate CountMin sketch with Spark

Similar to the BloomFilter example, write code to use CountMin Sketch algorithm with Spark streaming to compute the frequency of hashtags. 


## Submission 

Submit all your code and the job output as a ZIP file via Sakai. Note, if the ZIP file contains the JAR file for the job, it might be too large to upload to SAKAI, so be sure to exclude the JAR file from the submission. If you decide to use Github, you still must add a submission via SAKAI and include your Github repository link. 
