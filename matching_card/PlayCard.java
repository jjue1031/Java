import java.util.Random;
import java.util.ArrayList;

public class PlayCard {
  public static void main(String[] args) {
    //set up reader to take inputs
    java.util.Scanner reader = new java.util.Scanner (System.in);
    
    //game size
    int n = 16; 
    MatchCardGame g1 = new MatchCardGame(n);
    g1.shuffleCards();
    
    // repeat the fliping progress until all cards are face up
    while(!g1.gameOver()) {
      //print board status
      System.out.println(g1.boardToString());
      
      //ask for a card to flip until we get a valid one
      System.out.println("Which card to play?");
      while(!g1.flip(reader.nextInt())) {}
      
      //print board status
      System.out.println(g1.boardToString());
      
      //ask for a card to flip until we get a valid one
      while(!g1.flip(reader.nextInt())) {}
      
      //say whether the 2 cards were a match
      if(g1.wasMatch()) {
        System.out.println("Was a match!");
      } else {
        //print board to show mismatched cards
        System.out.println(g1.boardToString());		
        System.out.println("Was not a match.");
        //flip back the mismatched cards
        g1.flipMismatch();
      }
    }
    
    //Report the score
    System.out.println("The game took " + g1.getFlips() + " flips.");

   
    // Using the AIs
    int count;
    MatchCardGame g2 = new MatchCardGame(n);
    g2.shuffleCards();
    // use count to store the totalflips
    count = playRandom(g2);
    System.out.println("The bad AI took " + count + " flips.");



    MatchCardGame g3 = new MatchCardGame(n);
    g3.shuffleCards();
    // use count to store the totalflips
    count = playGood(g3);
    System.out.println("The good AI took " + count + " flips.");
    
    // Using MCs
    // AI plays a total of N times
    int N = 1000;
    System.out.println("The bad AI took " + randomMC(N) + " flips on average.");
    System.out.println("The good AI took " + goodMC(N) + " flips on average.");
}

	

	// bad AI
	public static int playRandom(MatchCardGame g){

		// repeat the fliping progress until all cards are face up
		while(!g.gameOver()) {

      Random rand = new Random();
      // use cardChosen to store the card Chosen each time
      int cardChosen;

      // randomly use a card until we get a face-down card
      do{
      	cardChosen=rand.nextInt(g.n);
      }while(g.isFlip[cardChosen]);
      
      // check the card chosen is a valid one and store the card chosen in each turn
      while(!g.flip(cardChosen)) {}

      // randomly use a card until we get a face-down card
			do{
      	cardChosen=rand.nextInt(g.n);
      }while(g.isFlip[cardChosen]);

      while(!g.flip(cardChosen)){}
      
      //whether the 2 cards were a match
      if(!g.wasMatch()) {
        g.flipMismatch();
      }
	  }

	  // return the total number of flips bad AI takes
	  return g.getFlips();

	}


	// good AI
	public static int playGood(MatchCardGame g){

		// store the cards have been flipped
		ArrayList<Integer> cardKnown = new ArrayList<>();

		// repeat the fliping progress until all cards are face up
		while(!g.gameOver()) {
		
		  Random rand = new Random();
		  // use cardChosen to store the card Chosen each time
		  int cardChosen;

		  // in odd number of flip, if a known pair exists, use array cardToChoose to store the pair
		  int[] cardToChoose = {-1,-1};
		  // whether find a matched pair in odd number of flip
			boolean matchFound = false;

			// whether in known face-down card exists a matched pair
			for (int i=0; i<cardKnown.size()-1; i++){
				for (int j=i+1; j<cardKnown.size();j++){
					// if a matched pair found, skip the loop
					if(matchFound) continue;
					if(g.cards[cardKnown.get(i)]==g.cards[cardKnown.get(j)] && g.isFlip[cardKnown.get(i)]==false && g.isFlip[cardKnown.get(j)]==false){
						cardToChoose[0]=cardKnown.get(i);
						cardToChoose[1]=cardKnown.get(j);
						matchFound =true;
					}
				}
			}

			// if no matched pair found, make the array to default value
			if(!matchFound) {
				cardToChoose[0]=-1;
				cardToChoose[1]=-1;
			}

			// if matched pair found
			if(cardToChoose[0]!=-1){
				// the first one in the array will be the one to choose in the odd number of flip
				cardChosen=cardToChoose[0];
				// add the chosen card to the cardKnown arraylist
				cardKnown.add(cardChosen);
			} 
			// if no matched pair found
			else{
				// randomly use a card until we get a face-down card
				do{
	    	cardChosen=rand.nextInt(g.n);
	    	}while(g.isFlip[cardChosen]||cardKnown.contains(cardChosen));
	    	// add the chosen card to the cardKnown arraylist
	    	cardKnown.add(cardChosen);
			}
	      
			// check whether the card is valid and store the card chosen in each turn
	    while(!g.flip(cardChosen)) {}
	 
	 		// if odd number of flip randomly choosing a card, whether in known card, there exists a matched one
	  	boolean matchExist=false;

	  	// if a matched pair found in odd number of flips
	  	if (cardToChoose[1]!=-1){
	  		cardChosen=cardToChoose[1];
	  		cardKnown.add(cardChosen);
	  	} 
	  	// whether in known card, there exists one matches the one chosen in odd number of flip
	  	else {
	  		for (int i=0; i<cardKnown.size(); i++){
	  			if (g.cards[cardKnown.get(i)]==g.cards[cardChosen] && g.isFlip[cardKnown.get(i)]==false){
	  				cardChosen=cardKnown.get(i);
	  				cardKnown.add(cardChosen);
	  				matchExist=true;
	  				// when a matched card found, break the loop
	  				break;
	  			}
	  			else matchExist=false;
	  		}

  			// if no matched card found, randomly choose an unknown card
  			if (matchExist==false){
  				do{
    				cardChosen=rand.nextInt(g.n);
    			}while(g.isFlip[cardChosen]||cardKnown.contains(cardChosen));

    			cardKnown.add(cardChosen);
  			} 
	  	}
		

	    while(!g.flip(cardChosen)){}
	  
	    //say whether the 2 cards were a match
	    if(!g.wasMatch()) {
	      //flip back the mismatched cards
	      g.flipMismatch();
	    }

	  }

	  // return the total number of flips good AI takes
	  return g.getFlips();
	}


// returns the average number of flips to good AI takes tocomplete the games
	public static double goodMC(int N){
		// initialize the total flips to 0
		double total=0;
		// game size
		int n=32;
		MatchCardGame g = new MatchCardGame(n);

		for(int i=0;i<N;i++){
			g.shuffleCards();
			// accumulate the total flips
			total+=playGood(g);
		}
		return total/N;
	}


	// returns the average number of flips to bad AI takes tocomplete the games
	public static double randomMC(int N){
		double total=0;
		int n=32;
		MatchCardGame g = new MatchCardGame(n);

		for(int i=0;i<N;i++){
			g.shuffleCards();
			total+=playRandom(g);
		}
		return total/N;
	}

}























