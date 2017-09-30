package homework_5;

import java.io.*;
import java.util.*;

public class YelpAnalysis
{
	public static void main(String[] args)
	{
		FileInputStream instream = null;
		BufferedInputStream bis = null;
		
		// maintain the top 10 businesses (in terms of review) by a minimum priority queue
		PriorityQueue<Business> businessQueue = new PriorityQueue<Business>();
		// whitelist contain the words that we will filter out in the analysis
		HashSet<String> whitelist = new HashSet<String>(
				Arrays.asList("a","an","the",
				"is","are","was","were",
				"i","me","my","you","he","his","she","her","we","our","it",
				"and","but","this","that",
				"to","of","for","with","in"));
		HashMap<String,Integer> CorpusCount = new HashMap<String,Integer>();
		HashSet<String> DocCorpus = new HashSet<String>();
		
		try
		{
			// set up the input the stream and its buffered stream
			instream = new FileInputStream("/Users/lingjuexie/Downloads/yelpDatasetParsed_10000.txt");
			bis = new BufferedInputStream(instream);
			
			readBusiness(bis,businessQueue,CorpusCount,DocCorpus,whitelist);
			
			// close the two streams (if we are able to)
			bis.close();
			instream.close();
		}
		catch(IOException e) // catch possible IOExceptions
		{
			System.out.println("File not found");
		}
		
		for(int i=0;i<10;i++)
		{
			ArrayList<Map.Entry<String,Double>> tfidfScores = new ArrayList<Map.Entry<String,Double>>();
			// pop out a business from the priority queue
			Business b = businessQueue.poll();
			
			// calculate the Tf-idf scores for the words in review, and sort them
			b.getTfidfScore(tfidfScores,CorpusCount,whitelist,5);
			sortByTfidf(tfidfScores);
			
			System.out.println(b);
			printTopWords(tfidfScores,30);
		}
	}
	
	private static void readBusiness(BufferedInputStream bis, PriorityQueue<Business> businessQueue,
			Map<String,Integer> Corpus, HashSet<String> DocCorpus, HashSet<String> whitelist)
	{
		StringBuilder strb = new StringBuilder();
		String[] fields = new String[4]; // record the 4 fields for the current business
		try
		{
			int count = 0;
			while(true)
			{
				int charcode = bis.read();
				if(charcode == -1) break;
				char c = (char) charcode;
				// skip left bracket and newline characters (do not act as separator or provide information)
				if(c != '{' && c != '\n' && c!= '\r')
				{
					if(c == ',' || c == '}') // when separator comes, create String
					{
						fields[count] = strb.toString();
						count++;
						if(count >= 4) // all fields for current business have been read
						{
							// add words in the review to corpus
							addDocumentCount(fields[3], Corpus, DocCorpus, whitelist);
							// if the queue is not full, add the current Business into it
							if(businessQueue.size() < 10)
								businessQueue.add(new Business(fields));
							else
							{
								/* compare the current Business's review length 
								with the top element in the minimum queue */
								if(fields[3].length() > businessQueue.peek().getReviewLength())
								{
									// if larger, pop the top element and insert current business
									businessQueue.poll();
									businessQueue.add(new Business(fields));
								}
							}
							count = 0;
						}
						// clear the StringBuilder
						strb.delete(0, strb.length());
					}
					// add the character to the StringBuilder (if it is not a separator)
					else strb.append(c);
				}
			}
		}
		catch(IOException e)
		{
			System.out.println("IO Exception");
			return;
		}
	}
	
	private static void addDocumentCount(String content, Map<String,Integer> Corpus, 
			HashSet<String> DocCorpus, HashSet<String> whitelist)
	{
		// create a Scanner to read from the content
		String word = "";
		Scanner scn = new Scanner(content);
		scn.useDelimiter(" ");
		  
		while(scn.hasNext())
		{
			word = scn.next();
			if(!whitelist.contains(word)) // skip words in whitelist
			{
				// if the word has already appeared, also skip it
				if(!DocCorpus.contains(word))  
				{
					if(Corpus.containsKey(word))
						Corpus.put(word,Corpus.get(word)+1); // if Corpus contains the word, increase its frequency
					else Corpus.put(word, 1); // otherwise, put the word in Corpus
					DocCorpus.add(word);
				}
			}
		}
		DocCorpus.clear();
		scn.close();
	}
	
	public static <K,V> void printTopWords(List<Map.Entry<K,V>> list, int n)
	{
		for(int i=0;i<n;i++)
		{
			K key = list.get(i).getKey();
			V val = list.get(i).getValue();
			// formatting the output - two digits after the decimal point
			System.out.format("("+key.toString()+",%.2f)",val);
			
		}
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------");
	}
	
	public static <K,V extends Comparable<V>> void sortByTfidf(List<Map.Entry<K,V>> list)
	{
		Collections.sort(list, new ScoreComparator<K,V>());
	}
	
	private static class ScoreComparator<K,V extends Comparable<V>> implements Comparator<Map.Entry<K,V>>
	{
		public int compare(Map.Entry<K,V> o1, Map.Entry<K,V> o2)
		{
			// Reversely compare the map entries (larger value, higher priority)
			return o2.getValue().compareTo(o1.getValue());
		}
	}
}
