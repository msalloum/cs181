package cs181;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
 
public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	/* TODO - Implement the reduce function. 
	 * 
	 * 
	 * Input :    Adjacency Matrix Format       ->	( j   ,   M  \t  i	\t value 
	 * 			  Vector Format					->	( j   ,   V  \t   value )
	 * 
	 * Output :   Key-Value Pairs               
	 * 			  Key ->   	i
	 * 			  Value -> 	M_ij * V_j  
	 * 						
	 */

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			
		
		double vVal = 0;
		ArrayList<String> mList = new ArrayList<String> ();
					
		// Loop through values, to add m_ij term to mList and save v_j to variable v_j
		// Then Iterate through the terms in mList, to multiply each term by variable v_j.
		// Each output is a key-value pair  ( i  ,   m_ij * v_j)
		
	}

}
