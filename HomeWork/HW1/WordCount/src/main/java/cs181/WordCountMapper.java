package cs181;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

// Worked with Annalise Ko



/**
 * Word Count Mapper 
 * Receives lines of text, splits each line into words, and generates key, value pairs. Where 
 * the key is the word, and the value is just 1. The counts for a given key will be aggregated in the reducer. 
 *
 * @param  Raw text
 * @return < Key , 1 >
 * 
 */

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	    private final IntWritable one = new IntWritable(1);
	    private Text word = new Text();
	    private String pattern= "^[a-zA-Z][a-z0-9]*$";
	    
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	        
	    	String line = value.toString();  /* get line of text from variable 'value' and convert to string */
	    	
	    /* Put stop words into an Array
	     * 
	     * List of Stop Words selected from
	     * "Google History" (the Google stop words): http://www.ranks.nl/stopwords
	     * 
	     * */
	    	String[] stopWordsArray = new String[]{"i","a","about","an","and,","are","as","at","be","by","for","from",
	    											"how","in","is","it","of","on","or","that","the","this","to",
	    											"was","what","when","where","who","with","the"};
	    /* Convert Array into a List of Strings */
	    	List<String> stopWords = Arrays.asList(stopWordsArray);

	    	/* Lets use a string tokenizer to split line by words using a pattern matcher */
	        StringTokenizer tokenizer = new StringTokenizer(line); 
	        
	        while (tokenizer.hasMoreTokens()) {
	            word.set(tokenizer.nextToken());
	            String stringWord = word.toString().toLowerCase();
	            
	            /* for each word, output the word as the key, and value as 1 */
	            /* AND check if the word is not a stop word */
	            if (stringWord.matches(pattern) && !(stopWords.contains(stringWord))){
	                context.write(new Text(stringWord), one);
	            }
	            
	        }
	    }
	}