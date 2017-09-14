package cs181;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
 
public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	/* 	Input to Reducer 1 
	*		Key:	k 
	*		Value: 	M id val   &   V val 
	*
	*/

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			
		// NEED LOOP WITH CODE TO ADD TO ARRAY LIST 
		double vVal = 0;
		ArrayList<String> mList = new ArrayList<String> ();
					
		//Loop through each reducer value 
		for (Iterator<Text> it = values.iterator(); it.hasNext();) {
			String nextVal = it.next().toString();
			String parts[] = nextVal.split("\t");

			// M - transition Matrix
			if (parts.length > 2)  { // M i val
				mList.add(parts[1] + "\t" + parts[2]);
			} else {// Vector: V  val			
				vVal = Double.parseDouble(parts[1]);
			}
		}
					
		for (int i = 0; i < mList.size(); i++) { 
			String parts[] = mList.get(i).split("\t"); 
			String rowId = parts[0].trim(); 
			double val = Double.parseDouble(parts[1]);

			//Key = matrix row id 
			//Value = matrix item * vector 
			context.write( new Text(rowId), new Text(String.valueOf(val*vVal))); 
		}
	}

}
