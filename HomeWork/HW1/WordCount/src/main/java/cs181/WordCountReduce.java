package cs181;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Word Count Reducer 
 *
 * @param  < Key , Values> where Key = Word and Values are the counts
 * @return < Key , Total Count>
 * 
 */

public class WordCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
	    
	    public void reduce(Text key, Iterable<IntWritable> values, Context context)
	    		throws IOException, InterruptedException {
	        
	    	int sum = 0;
	    	
	    	/* Iterate through each value for this given key, and compute the sum */
	        for (IntWritable val : values) {
	            sum += val.get();
	        }
	        
	        /* Output Key and total count for that key */
	        context.write(key, new IntWritable(sum));
	    }
}