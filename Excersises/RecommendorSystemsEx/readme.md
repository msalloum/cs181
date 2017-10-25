# Recommender System Examples

We will try to implement a movie recommender system using the MovieLens dataset found here : <https://grouplens.org/datasets/movielens/>. We will walk-through a Databricks tutorial that uses Alternating Least Squares approach, and then look at a simple approach that uses K Nearest Neighbor. 

-- Description of Dataset --
A small snapshot of the MovieLens dataset is included in the folder called 'ml-lates-small'. It contains two main files, movies.csv and ratings.csv. The file 'movies.csv' contains information about the movie, such as the title, year, etc. The file 'ratings.csv' contains the user ratings. You can download a larger dataset from the MovieLens website. 

-- Part 1 -- 
For this first part, we will follow the Databricks tutorial found here: <https://databricks-training.s3.amazonaws.com/movie-recommendation-with-mllib.html>


The tutorial walks you through building a collaborative recommender systems using MLIB (<https://spark.apache.org/docs/latest/mllib-collaborative-filtering.html>). It bases the approach on Alternating Least Squares 

-- Part 2 -- 
For this second part, we implement a KNN approach to collaborative filtering as described in the text book Mining of Massive Datasets. 







