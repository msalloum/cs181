package cs181;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
 
public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	/* The reduce function. 
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

		for (Text val : values) {
			String strVal  = val.toString();
			String[] value = strVal.split("\t");
			
			if (value[0].equals("V")) {
				vVal += Double.parseDouble(value[1]);
			}
			else {
				mList.add(strVal);
			}
		}
		
		for (String val : mList) {
			String[] value = val.split("\t");
			context.write(new Text(value[1]), new Text(Double.toString(vVal*Double.parseDouble(value[2]))));
		}
	}
}
