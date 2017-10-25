#!/usr/bin/env python

# Partners on this assignment: Patrick McDonough and Annalise Ko

import sys
import time
import pyspark
import pyspark.streaming
import mmh3
import math

def compute_width_depth(delta, epsilon):
    """Takes two parameters (delta and epsilon), which define the accuracy and error probability 
       of the sketch, and it should return two values (width and depth)."""

    d = 2/ epsilon      #  d represents the depth of the 2D array, or the number of hash functions used

    w = math.log(1/ delta)    #  w represents the width of the 2D arra

    return int(d), int(w)


class CountMinSketch(object):
    # CM - 2d array - with given width and depth
    # w 
    # d 
    # list of array storing hash seed   (mmh3 + seed ) % w 

    def __init__(self, width, depth) :
        super(CountMinSketch, self).__init__()
        self.width = width
        self.depth = depth
        self.cm = [[0]*width]*depth


    def increment (self, key):
        """increment the corresponding counter for this item. The hash functions used is up to you, and 
           they can be automatically generated."""
        w = self.width
        d = self.depth
        for i in range(d):
            index = mmh3.hash(key, i)%w  # get index
            self.cm[i][index] += 1
        return self


    def estimate (self, key):
        """estimate the count for the given value."""
        w = self.width
        d = self.depth
        hashArray = []
        for i in range(d):
            index = mmh3.hash(key, i)%w
            hashArray.append(self.cm[i][index])
        return min(hashArray)


    def merge (self, CountMin):
        """merges a CountMin sketch with the current sketch (inorder to merge sketches across 
           partitions)"""
        w = self.width
        d = self.depth
        cm3 = CountMinSketch(w, d)
        for i in range(d):
            for j in range(w):
                cm3.cm[i][j] = self.cm[i][j] + CountMin.cm[i][j]
        return cm3


# # Testing part
# width, depth = compute_width_depth(0.01, 0.01)
# sketch = CountMinSketch(width, depth)
# sketch.increment("one")
# sketch.increment("two")
# sketch.increment("one")
# sketch.increment("two")
# sketch.increment("three")


# sketch2 = CountMinSketch(width, depth)
# sketch2.increment("one")
# sketch2.increment("two")
# sketch2.increment("two")
# sketch2.increment("three")
# sketch2.increment("three")

# sketch3 = sketch.merge(sketch2)

# print "Number of one:", sketch3.estimate("one")
# print "Number of two:", sketch3.estimate("two")
# print "Number of three:", sketch3.estimate("three")