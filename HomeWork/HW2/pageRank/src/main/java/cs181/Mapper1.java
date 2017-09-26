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
		
		/* If the value is from the matrix: Tuple is ("M", i, j, value) */
		if (indicesAndValue[0].equals("M")) {
			outputKey.set(  indicesAndValue[2]  ); // key is j
			outputValue.set( "M\t" + indicesAndValue[1] + "\t" + indicesAndValue[3] );  // value is (i, value)
		}
		/* If the value is from the vector: Tuple is ("V", j, value) */
		else {
			outputKey.set(  indicesAndValue[1]  ); // key is j
			outputValue.set( "V\t"+indicesAndValue[2] );  // value is (v_k)	
		}
		/* Note, use 'context.write (outputKey, outputValue) to output a key-value pair */
		context.write(outputKey, outputValue);
	}
}
