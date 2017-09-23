package cs181;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
        
public class WordCount {
            
	public static void main(String[] args) throws Exception {
		
		if (args.length < 2) {
			System.err.println("Usage: hadoop jar /path/to/jar/WordCount-0.0.1-SNAPSHOT.jar <inputPath> <outputPath>" );
		}
		Configuration conf = new Configuration();
	        
	    Job job = new Job(conf);
	  
	    job.setJarByClass(WordCount.class);
	    
	    /* Mapper generated Key/Value data types */ 
	    job.setOutputKeyClass(Text.class); 
	    job.setOutputValueClass(IntWritable.class);
	        
	    job.setMapperClass(WordCountMapper.class);
	    job.setCombinerClass(WordCountReduce.class);
	    job.setReducerClass(WordCountReduce.class);
	        
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	        
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	        
	    job.waitForCompletion(true);
	}
        
}