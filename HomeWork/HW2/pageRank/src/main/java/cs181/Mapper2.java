package cs181;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
 
public class Mapper2 extends Mapper<LongWritable, Text, Text, Text> {  
	
	/* This is called a pass-through mapper. Each job, must be composed of a Mapper and Reducer,
	 * thus while Job2 does not require a Mapper, its needed to add the second reducer 
	 * 
	 * YOU DON'T NEED TO MODIFY THIS CLASS!
	 * 
	 */
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		
		String line = value.toString();
		String parts[] = line.split("\t");
		context.write(new Text(parts[0]), new Text(parts[1]));
		
	}

}
