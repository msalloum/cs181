# HW 2 - Page Rank

The objective of this assignment is to gain more experience with MapReduce. We will implement an iterative version of the PageRank algorithm. 
You are given the starter code for this assignment, and your task is to complete the following classes:
1. Mapper1.java
2. Reducer1.java
3. Reducer2.java


-----------

##  Part 1 - Understanding the format of the input files

We will be using the Stanford Web Graph dataset as our input graph, found here  (http://snap.stanford.edu/data/web-Stanford.html). This graph has ~ 281,903
nodes and ~2,312,497 edges. You should be able to apply page rank on any graph, as long as you generate the input files in the format required by this starter code. 

You are given two files, web-Stanford_V.txt and web-Stanford_M.txt, derived from the original Stanford Web Graph dataset. 

1. web-Stanford_M.txt  represents the transition matrix of this graph. 
2. web-Stanford_V.txt represents the distribution of a random surfer after some time (initially, each node has a value of 1/n)

## Part 2 - Completing the starter code

Import the starter-code as a "Maven Project" to eclipse, which should be similar to the instructions you followed in HW1. Examine the main class, PageRank, which defines the two MapReduce jobs. Proceed to complete the following 3 files:  Mapper1.java , Reducer1.java and Reducer2.java. 


## Part 3 - Running your job on Hadoop

As in HW1, we will use Docker to run our Hadoop virtual machine, so you will need to start Docker as you did in HW1. Be sure you are 'mounting' the HW2 folder instead of the HW1 folder. 

>>> docker run -v /Users/msalloum/MSALLOUMGIT/cs181/HomeWork/HW2:/mnt/cs181 -it sequenceiq/hadoop-docker:2.7.1 /etc/bootstrap.sh -bash

Once the image is loaded and the Hadoop cluster is setup, we need to copy the input files onto the distributed file system.  At the shell / terminal window, type: 

>>>  export HADOOP_HOME=/usr/local/hadoop
>>>  $HADOOP_HOME/bin/hdfs dfs -mkdir data

The above command creates a directory called 'data' on the distributed file system. Next, we will place our input file in that directory. 

>>>	$HADOOP_HOME/bin/hdfs dfs -put /mnt/cs181/pageRank/web-Stanford_M.txt  data/
>>>	$HADOOP_HOME/bin/hdfs dfs -put /mnt/cs181/pageRank/web-Stanford_V.txt  data/

The above command copies the files from our local directory to the distributed file system directory, so that our Map Reduce job can access the input data. Now, we can run our job. 

The format of the command is "/usr/local/hadoop/bin/hadoop   jar   <jarFileName>   <className>   <numIterations>  <TransitionMatrixFile> <VectorFile>  ".

The jar file (Java Archive used to package multiple Java files) is found under the pageRank/target directory. 

>>>	$HADOOP_HOME/bin/hadoop jar /mnt/cs181/pageRank/target/pageRank-0.0.1-SNAPSHOT.jar  cs181.PageRank   1  data/web-Stanford_M.txt  data/web-Stanford_V.txt

So the above command should start our Map Reduce job. It might take a while for the job to complete fully, so wait a 2-3 minutes. Note, when we ran the job we had to specify the path to our jar file,  the name of the main class we want to run, and the  input and output directories. 

The output will be stored in the file "data/web-Stanford_V.txt". To see the output, use the following command

>>> $HADOOP_HOME/bin/hdfs dfs -text data/web-Stanford_V.txt/*

Note, in the main class PageRank, we are over-writing this file (web-Stanford_V.txt) at the end of each iteration. If your job runs successfully with numIterations = 1, then go ahead an re-run the job setting the number of iterations to 2 or 3. We will not set-it to a large value,because it will take sometime for the job to complete on our limited workstation/laptop. So, as long as your code runs successfully for two iterations, then you should be set. 

For 5 extra points, update the code to account for dead-ends and spider-traps in the directed graph. Use the taxation method we discussed in class. 

## Submission 

Submit all your code and the job output as a ZIP file via Sakai. Note, if the ZIP file contains the JAR file for the job, it might be too large to upload to SAKAI, so be sure to exclude the JAR file from the submission. If you decide to use Github, you still must add a submission via SAKAI and include your Github repository link. 


