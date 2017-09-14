package cs181;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
 
public class Reducer2 extends Reducer<Text, Text, Text, Text> {
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		
		// Input to Reducer 2
		// Key: row Id
		// Value: M * v

		double sum = (double) 0;
	
		//LOOP COMPUTING SUM 
		for (Iterator<Text> it = values.iterator(); it.hasNext();) { 
			double val = Double.parseDouble(it.next().toString()); 
			sum += val;
		}
		context.write(new Text() , new Text("V\t" + key + "\t" + String.valueOf(sum)));				
	}
}

