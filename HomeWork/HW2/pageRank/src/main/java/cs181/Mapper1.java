<<<<<<< HEAD
package cs181;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
   
public class Mapper1 extends Mapper<LongWritable, Text, Text, Text> {      
		
	/* The map function, where each call to the function receives just 1 line from the input files. 
	 * Recall, we had two input files feed-in to our map reduce job, both the adjacency matrix and the vector file. 
	 * This, our code must contain some logic to differentiate between the two inputs, and output the appropriate key-value pair.
	 * 
	 * Input :    Adjacency Matrix Format       ->	M  \t  i	\t	j		\t value 
	 * 			  Vector Format					->	V  \t  j	\t  value 
	 * 
	 * Output :   Key-Value Pairs               
	 * 			  Key ->   	j
	 * 			  Value -> 	M 	\t 	i 	\t 	value    or   
	 * 						V 	\t  value 
	 */

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException { 
		
		String input  = value.toString();
		String[] indicesAndValue = input.split("\t"); // tab delimited

		Text outputKey = new Text();
		Text outputValue = new Text();
		
		if (indicesAndValue[0].equals("M")) {
			outputKey.set(indicesAndValue[2]);
			outputValue.set("M\t"+indicesAndValue[1]+"\t"+indicesAndValue[3]);
		}
		else {
			outputKey.set(indicesAndValue[1]);
			outputValue.set("V\t"+indicesAndValue[2]);
		}
		context.write(outputKey, outputValue);
	}
}
=======
package cs181;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
   
public class Mapper1 extends Mapper<LongWritable, Text, Text, Text> {      
		
	/* TODO - Implement the map function, where each call to the function receives just 1 line from the input files. 
	 * Recall, we had two input files feed-in to our map reduce job, both the adjacency matrix and the vector file. 
	 * This, our code must contain some logic to differentiate between the two inputs, and output the appropriate key-value pair.
	 * 
	 * Input :    Adjacency Matrix Format       ->	M  \t  i	\t	j		\t value 
	 * 			  Vector Format					->	V  \t  j	\t  value 
	 * 
	 * Output :   Key-Value Pairs               
	 * 			  Key ->   	j
	 * 			  Value -> 	M 	\t 	i 	\t 	value    or   
	 * 						V 	\t  value 
	 */

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException { 
		

		String input  = value.toString();
		String[] indicesAndValue = input.split("\t"); // tab delimited

		Text outputKey = new Text();
		Text outputValue = new Text();

		if (indicesAndValue[0] == "M"){
			outputKey.set(indicesAndValue[2]);
			outputValue.set("M\t" + indicesAndValue[1] + "\t" + indicesAndValue[3]);
		}
		else { //'V'
			outputKey.set(indicesAndValue[1]);
			outputValue.set("V\t" + indicesAndValue[1]);
		}
		context.write (outputKey, outputValue);
	}
}
>>>>>>> upstream/master
