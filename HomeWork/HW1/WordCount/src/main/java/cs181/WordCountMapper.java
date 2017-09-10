package cs181;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

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
	    private String pattern= "^[a-z][a-z0-9]*$";
	    
	    // a list of all the stop words
	    private static final String[] stopwords = {"a", "ab", "as", "able", "about", "above", "according", "accordingly", "across", 
	    		"actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", 
	    		"along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", 
	    		"anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", 
	    		"appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", 
	    		"awfully", "b", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", 
	    		"being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", 
	    		"but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", 
	    		"changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", 
	    		"contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", 
	    		"described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", 
	    		"downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", 
	    		"especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", 
	    		"ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", 
	    		"follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", 
	    		"getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", 
	    		"happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", 
	    		"her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", 
	    		"himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", 
	    		"ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", 
	    		"inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", 
	    		"just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", 
	    		"least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", 
	    		"ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", 
	    		"most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", 
	    		"need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", 
	    		"noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", 
	    		"oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", 
	    		"ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", 
	    		"per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", 
	    		"quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", 
	    		"relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", 
	    		"see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", 
	    		"serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", 
	    		"some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", 
	    		"soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", 
	    		"taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", 
	    		"theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", 
	    		"therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", 
	    		"this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", 
	    		"together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", 
	    		"un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", 
	    		"useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", 
	    		"was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", 
	    		"what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", 
	    		"whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos",
	    		"whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", 
	    		"wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", 
	    		"yourself", "yourselves", "zero"};
	    private static final List<String> stopwordsList = Arrays.asList(stopwords);
	    
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	        
	    	String line = value.toString();  /* get line of text from variable 'value' and convert to string */
	    	
	    	/* Lets use a string tokenizer to split line by words using a pattern matcher */
	        StringTokenizer tokenizer = new StringTokenizer(line); 
	        
	        while (tokenizer.hasMoreTokens()) {
	            word.set(tokenizer.nextToken());
	            String stringWord = word.toString().toLowerCase();
	            
	            // if the incoming word is in the stopwordsList, skip it
	            if(stopwordsList.contains(stringWord)) {
	            		continue;
	            }
	            
	            /* for each word, output the word as the key, and value as 1 */
	            if (stringWord.matches(pattern)){
	                context.write(new Text(stringWord), one);
	            }
	            
	        }
	    }
	}