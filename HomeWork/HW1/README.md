# HW 1 - Our 1st Map Reduce Job

The objective of this assignment is to gain experience with MapReduce. This includes,

1. Configuring a Hadoop cluster on your machine using a Docker image
2. Getting familiar with the Hadoop Distributed File System (HDFS) 
3. Running your 1st Map Reduce Job
4. [SUBMISSION] - Enhancing the MapReduce Job from step 3

-----------

##  Setting up a Hadoop cluster

First, we need to setup Hadoop on our machine. This can be easily done using a Docker image. Follow the following tutorial to download Docker and install the Hadoop docker image. 

[Docker Install](https://github.com/msalloum/cs181/tree/master/HomeWork/HW1/Docker_Hadoop_Setup.pdf)

## Installing Eclipse IDE and maven plugin

Now that we have Hadoop setup on an virtual environment, lets write our first MapReduce job. Actually, we will start with code that we discussed in class and provided here under the WordCount folder.

1.  Download the "WordCount" folder  and the file HuckleberryFinn.txt on to your machine. 

2.  Place these two items under the folder where all cs181 code will be placed. This was specified in the Docker Install tutorial. For example, i choose the folder path: /Users/msalloum/cs181. The reason you need to place your code there is because thats the only directory/folder that the Docker image has access to. 

3.  Finally, run the WordCount MapReduce Job as follows:

At the shell / terminal window, type: 

>>>	$HADOOP_HOME/bin/hdfs dfs -mkdir data

The above command creates a directory called 'data' on the distributed file system. Next, we will place our input file in that directory. 

>>>	$HADOOP_HOME/bin/hdfs dfs -put /mnt/cs181/HuckleberryFinn.txt data/HuckleberryFinn.txt

The above command copies the file HuckleberryFinn.txt from our local directory to the distributed file system directory, so that our Map Reduce job can access the input data. Now, we can run our job. 

The format of the command is " bin/hadoop   jar   <jarFileName>   <className>   <inputFilePath >  <outputFilePath>".

The jar file (Java Archive used to package multiple Java files) is found under the WordCount/target directory. 

>>>	$HADOOP_HOME/bin/hadoop jar /mnt/cs181/WordCount/target/WordCount-0.0.1-SNAPSHOT.jar  cs181.WordCount data/* wordcount_output

So the above command should start our Map Reduce job. It might take a while for the job to complete fully, so wait a 2-3 minutes. Note, when we ran the job we had to specify the path to our jar file,  the name of the main class we want to run, and the  input and output directories. 

The output will be stored in the directory "wordcount_output", so lets take a look at the output. 

>>> bin/hdfs dfs -ls wordcount_output/*

>>> bin/hdfs dfs -text wordcount_output/wordcount_output/part-r-00000



##  Packaging Jar File

In the previous step,  we just ran the JAR file given under the /target directory. But, how can we generate this JAR file????

We will need to compile our WordCount project (given in this folder) including all the dependencies into a Jar file. You can either download maven and compile the project using command line tools, or follow the following tutorial which will walk you through installing Eclipse and a maven Plugin to compile the project. 

[Eclipse/Maven Install](https://github.com/msalloum/cs181/tree/master/HomeWork/HW1/Eclipse_Maven_Setup.pdf)

## Your Turn

Once you have the provided example running, its your turn to add new features to the code. 

1. The Mapper includes minimal processing to be case insensitive and remove punctuation from the text. Did I handle all punctuation cases?  Can we also remove stop words? 

2. One optimization to the MapReduce Job that we discussed is the Combiner. Add a combiner to reduce the number of key-value pairs passed to the Reducer. 


## Submission 

Submit all your code and the job output as a ZIP file via Sakai. Note, if the ZIP file contains the JAR file for the job, it might be too large to upload to SAKAI, so be sure to exclude the JAR file from the submission. You have a chance to get 5 extra points if you create a github repository that includes your homework submission. If you decide to use github, you still must add a submission via SAKAI and include your github repository link. 


