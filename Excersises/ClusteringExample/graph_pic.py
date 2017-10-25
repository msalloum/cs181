from __future__ import print_function
from pyspark import SparkContext
from pyspark.mllib.clustering import PowerIterationClustering, PowerIterationClusteringModel

if __name__ == "__main__":
    sc = SparkContext(appName="PowerIterationClusteringExample")  # SparkContext

    # Load and parse the data
    data = sc.textFile("pic_data.txt")
    similarities = data.map(lambda line: tuple([float(x) for x in line.split(' ')]))

    # Cluster the data into two classes using PowerIterationClustering
    model = PowerIterationClustering.train(similarities, 2, 25)

    model.assignments().foreach(lambda x: print(str(x.id) + " -> " + str(x.cluster)))

    # Save and load model
    #model.save(sc, "PICModel")
    #sameModel = PowerIterationClusteringModel\
    #    .load(sc, "PICModel")
    sc.stop()
    