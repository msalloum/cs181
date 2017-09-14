package cs181;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
   
/*
 * 
 * 
 */
public class PageRank {

	
	@SuppressWarnings("deprecation")
	public static void main(String args[]) throws IOException,InterruptedException, ClassNotFoundException {
		 
		if (args.length < 3) {
			System.err.println("Usage: hadoop jar /path/to/jar/ cs181.PageRank <numIterations> <adjacencyMatrixPath> <vectorPath> " );
		}
		
		int numIterations = Integer.parseInt(args[0]);
		Path adjacencyMatrix = new Path(args[1]); // Adjacency Matrix File of format:    M  \t  v_id  \t  d_id  \t  value
		Path vector = new Path(args[2]); // Vector File of format :   V  \t  v_id  \t  value 
		Path tempOutput = new Path("tempOut"); // Intermediate output from Job 1, to be used as input to Job2
		
		Configuration conf = new Configuration();
		
		FileSystem fs = FileSystem.get(conf);
		
		for (int i=0; i< numIterations ; i++) { 
			System.out.println("Iter: " + i + " -- Job 1");
			Job job1 = new Job(conf, "Pagerank - iterator step 1");
			job1.setJarByClass(PageRank.class);
			job1.setOutputKeyClass(Text.class);
			job1.setOutputValueClass(Text.class);
		        
			job1.setMapperClass(Mapper1.class);			// Name of Mapper class 
			job1.setReducerClass(Reducer1.class);		// Name of Reducer class
			job1.setNumReduceTasks(10); // Specifies the number of reducers

			job1.setInputFormatClass(TextInputFormat.class);  // format of input to MR
			job1.setOutputFormatClass(TextOutputFormat.class); // format of output from MR
		        
			// Specify input files 
			FileInputFormat.addInputPath(job1, adjacencyMatrix);  	// input 1 
			FileInputFormat.addInputPath(job1, vector);  	// input 2
		        
			fs.delete(tempOutput, true);  // delete intermediate output before creating directory 
			FileOutputFormat.setOutputPath(job1, tempOutput);
		            
			/* A chain of jobs --> set to TRUE
			 * Independent jobs --> set to FALSE
			 */
			boolean success = job1.waitForCompletion(true); 
		        
			
			if (success) {  // if the 1st job succeeds, then run job 2
				System.out.println(i + " --> Starting Job2");
				Job job2 = new Job(conf, "Pagerank - iterator step 2");	
		        	  
				job2.setJarByClass(PageRank.class);
				job2.setOutputKeyClass(Text.class);
				job2.setOutputValueClass(Text.class);
				
				job2.setMapperClass(Mapper2.class);		// mapper 2
				job2.setReducerClass(Reducer2.class); 	// reducer 2
				job2.setNumReduceTasks(10);
				
				/* Define the format of the input and output files to our MR job */
				job2.setInputFormatClass(TextInputFormat.class);
				job2.setOutputFormatClass(TextOutputFormat.class);
				
				FileInputFormat.addInputPath(job2, tempOutput);		// input to job2 is the output of job1
				fs.delete(vector, true); // delete the original 'vector' file as we will overwrite it in Job2
				FileOutputFormat.setOutputPath(job2, vector);
				success = job2.waitForCompletion(true); 
			}
		}	
		fs.close();
		
	}
}
