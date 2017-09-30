package homework_5;

import java.util.*;

public class Business implements Comparable<Business>
{
  private String businessID;
  private String businessName;
  private String businessAddress;
  private String review;
  private int reviewCharCount;
  
  // use a String array to initialize an instance
  public Business(String[] fields)
  {
	  businessID = fields[0];
	  businessName = fields[1];
	  businessAddress = fields[2];
	  review = fields[3];
	  reviewCharCount = review.length();
  }
  
  public String toString() 
  {
    return "-------------------------------------------------------------------------------\n"
          + "Business ID: " + businessID + "\n"
          + "Business Name: " + businessName + "\n"
          + "Business Address: " + businessAddress + "\n"
          + "Character Count: " + reviewCharCount + "\n";
  }
  
  public int getReviewLength() {return reviewCharCount;}
  
  // The business with shorter review length is prior to that with longer review length
  public int compareTo(Business that)
  {
	  if(this.reviewCharCount < that.reviewCharCount) return -1;
	  else if(this.reviewCharCount > that.reviewCharCount) return 1;
	  else return 0;
  }
  
  public void getTfidfScore(List<Map.Entry<String,Double>> tfidfScores, Map<String,Integer> Corpus, 
		  HashSet<String> whitelist, int lowerbd)
  {
	  // obtain the frequency map
	  HashMap<String,Integer> freqmap = getFreqMap(whitelist);
	  for(String word: freqmap.keySet())
	  {
		  // go over each entry in frequency map and calculate its score
		  if(Corpus.get(word) >= lowerbd)
		  {
			  double score = (double)(freqmap.get(word))/(double)(Corpus.get(word));
			  tfidfScores.add(new AbstractMap.SimpleEntry<String,Double>(word,score));
		  }
		  else tfidfScores.add(new AbstractMap.SimpleEntry<String,Double>(word,0.0));
	  }
  }
  
  // obtain a Frequency map from the review
  private HashMap<String,Integer> getFreqMap(HashSet<String> whitelist)
  {
	  HashMap<String,Integer> freqmap = new HashMap<String,Integer>();
	  String word = "";
	  // use a Scanner to read from review
	  Scanner scn = new Scanner(review);
	  scn.useDelimiter(" ");
	  
	  while(scn.hasNext())
	  {
		  word = scn.next();
		  if(!whitelist.contains(word)) // skip the words in whitelist
		  {
			  if(freqmap.containsKey(word))
				  freqmap.put(word,freqmap.get(word)+1); // if the map contains the word, increase its frequency
			  else freqmap.put(word, 1); // otherwise, put the word in the map
		  }
	  }
	  scn.close();
	  return freqmap;
  }
}