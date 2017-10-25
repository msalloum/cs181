#!/usr/bin/env python

import sys
import math
import random
import pyspark.mllib.linalg
from operator import add
from os.path import join, isfile, dirname
from pyspark.mllib.linalg import SparseVector, Vectors
from pyspark import SparkConf, SparkContext
###################################################################
def parseRating(line):
    """ Parses a rating record in MovieLens format userId::movieId::rating::timestamp ."""
    fields = line.strip().split(",")
    return  int(fields[1]), (int(fields[0]), float(fields[2]))                  
###################################################################
""" Given two Sparse Vectors, compute cosine similarity between them"""
def cosineSimilarity(a,b) :
    aMagnitude = math.sqrt(float(sum([aVal**2 for aVal in a.values])))
    bMagnitude = math.sqrt(float(sum([bVal**2 for bVal in b.values])))
    a2 = SparseVector.parse(str(a))
    resultNumerator  = a2.dot(b)
    resultDenominator = aMagnitude*bMagnitude
    if resultDenominator == 0:
        return 0
    return resultNumerator/resultDenominator
###################################################################
# Step 1 - Set up environment
conf = SparkConf().setAppName("ColloaborativeKNN").set("spark.executor.memory", "4g")
sc = SparkContext(conf=conf)

# Step 2 - Load and parse the data
movieLensHomeDir = sys.argv[1] # load ratings and movie titles

# 'ratings' is an RDD of ( movieId, (userId,rating) )
ratings, test = sc.textFile(join(movieLensHomeDir, "ratings.csv")).randomSplit([0.2, 0.3])
header = ratings.first()
ratingsRDD = ratings.filter(lambda x: x!=header).map(parseRating)

# Step 3 - Print Ratings RDD Information    
numRatings = ratingsRDD .count()
numUsers = ratingsRDD .map(lambda r: r[0]).distinct().count() 
numMovies = ratingsRDD .map(lambda r: r[1]).distinct().count()
print "******************************************"
print "Got %d ratings from %d users on %d movies." % (numRatings, numUsers, numMovies)
print "Num Movies:", numMovies
print "Num Ratings", numRatings
print "******************************************"

# Step 4 - compute mean and subtract from row
# ratingRDD form  (movieID,   ( userId, rating) ) 
meanRDD = ratingsRDD.map(lambda (x,y): (x, y[1])).reduceByKey(add)# Calculate the numerators (i.e. the SUMs).
countsByKey = ratingsRDD.countByKey()
avgRDD = meanRDD.map(lambda x: (x[0], x[1]/float(countsByKey[x[0]]))) # Divide each SUM by it's denominator (i.e. COUNT)
combinedRDD = ratingsRDD.join(avgRDD).cache()

# combined RDD is of [(4, ((2, 2.0), 3.4))] form 
normalizedRatingRDD = combinedRDD.map(lambda (x,y) : (x,(y[0][0], y[0][1]-y[1])))
sparseRatingRDD = normalizedRatingRDD.groupByKey().map(lambda (x,y) : (x, Vectors.sparse(numUsers, y)))


## Step 5 - Perform Recommendation
for i in range(0, 10) :  
    ## 1) select a random movie, and associated rating vector 
   
    randomMovieTuple = sparseRatingRDD.takeSample(False, 1)[0]   # tuple ( movieID, sparseVector )

    randMovieId = randomMovieTuple[0]  # extract movieId
    randMovieVector = SparseVector.parse(str(randomMovieTuple[1])) # SparseVector associated with that movie

    # From this movie vector, we will randomly select a userId and set their rating to zero.
    # The idea would be to try to predict that rating and see how close we come to the actual value 
    predVecValues = randMovieVector.values
    predVecIndices = randMovieVector.indices
    index = random.randint(0, len(predVecValues)-1 )
    predVecValues[index] = 0 # set rating to zero
    randUserId = predVecIndices[index]
    
    ## 2) compute cosine simularity with "randMovieVector" and each vector in RDD
    # result is a RDD of (cosSimValue, movieId)
    result = sparseRatingRDD.map(lambda v : (v[0], cosineSimilarity(v[1], randMovieVector)))\
              .map(lambda x: (x[1], x[0]))\
              .sortByKey(ascending=False)
     
    movieTitles = [x[1] for x in result.take(100)]

    ## 3) get predicted rating 
    ratingsOfUser = ratingsRDD.filter(lambda x: x[1][0] == randUserId and x[0] in movieTitles)\
                            .map(lambda x: x[1][1])
    sumList = ratingsOfUser.take(5)  # 5 = K 

    actualRating = ratingsRDD.filter(lambda x: int(x[0]) == randMovieId and x[1][0] == randUserId )\
                           .map(lambda x: x[1][1])

    print "Prediction for userId = %d for movieId= %s" % (randUserId, randMovieId)
    print "Predicted Rating:", sum(sumList)/len(sumList), "Actual Rating:", actualRating.take(1)


# clean up
sc.stop()
