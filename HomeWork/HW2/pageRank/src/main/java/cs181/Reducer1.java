<<<<<<< HEAD
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
=======
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
			
		
		double v_j = 0;
		ArrayList<String> mList = new ArrayList<String> ();
					
		// Loop through values, to add m_ij term to mList and save v_j to variable v_j
		for (Iterator<Text> i = values.iterator(); i.hasNext();) {
		    String inputLine = i.next().toString();
		    String[] inputList = inputLine.split("\t");
		    if (inputList[0] == "M") {
		    	//double m_ij = Double.parseDouble(inputList[1]);
		    	String m_ij = inputList[1] + "\t" + inputList[3]; // i <tab> value
		    	mList.add(m_ij);
		    }
		    else {
		    	v_j = Double.parseDouble(inputList[1]);
		    }
		}
		// Then Iterate through the terms in mList, to multiply each term by variable v_j.
		for (Iterator<String> i = mList.iterator(); i.hasNext();) {
			String m_ijString = i.next();
			String[] m_ijList = m_ijString.split("\t");
			String m_i = m_ijList[0];
			double m_ij = Double.parseDouble(m_ijList[1]);
			Text outputKey = new Text();
			Text outputValue = new Text();
			// Each output is a key-value pair  ( i  ,   m_ij * v_j)
			outputKey.set(m_i);
			outputValue.set(Double.toString(m_ij*v_j));
			context.write(outputKey, outputValue);
		}
		
	}

}
>>>>>>> upstream/master
