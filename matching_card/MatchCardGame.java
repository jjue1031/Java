import java.util.Random;

public class MatchCardGame{
	// game size
	public final int n;
	// cards in the game
	public char[] cards;
	// whether a card is face up
	public boolean[] isFlip;
	// cards chosen in each turn
	public int[] cardInEachRound = {-1,-1};
	// total number of flips take to complete the game
	public int totalNumberOfFlips;

	public MatchCardGame(int n){
		// set the game size
		this.n=n;
		cards = new char[n];
		isFlip = new boolean[n];
		// first card of the game
		char firstcard = 'a';

		// set the card in the game
		for (int i=0; i<cards.length; i++){
			cards[i]=firstcard;
			if (i%4 == 3) firstcard++;
		}
	}

	// print the game to the screen
	public String boardToString(){
		String result="";

		for (int i=0; i<cards.length; i++){
			// four cards in each row
			if (isFlip[i]){
				if (i!=0 && i%4 == 3) result=result + cards[i] + "(" + i + ") "+"\n";
				else result=result + cards[i] + "(" + i + ") |";
			}
			else{
				if (i!=0 && i%4 == 3) result=result+ "X" + "(" + i + ") "+"\n";
				else result=result + "X" + "(" + i + ") |";			
			}
		}
		return result;
	}

	// check the card chosen is a valid one and store the card chosen in each turn
	public boolean flip(int i){
		if (i<0 || i>=n || isFlip[i]) return false;
		else{
			// cards chosen in each round
			if (totalNumberOfFlips%2==0) cardInEachRound[0]= i;
			else cardInEachRound[1]= i;

			totalNumberOfFlips++;
			// make card face up
			isFlip[i] = true;
			return true;
		}	
	}

	// whether the 2 cards were a match
	public boolean wasMatch(){	
		return cards[cardInEachRound[0]]==cards[cardInEachRound[1]];
	}


	//flip back the mismatched cards
	public void flipMismatch(){
		isFlip[cardInEachRound[0]]=false;
		isFlip[cardInEachRound[1]]=false;
	}

	// return the total flips take to complete the game
	public int getFlips(){
		return totalNumberOfFlips;
	}
	
	// when cards are all face up, game is over
	public boolean gameOver(){
		// whether all cards are face up
		boolean allUp=true;

		for (int i=0; i<cards.length; i++){
			if (isFlip[i]== false)  allUp=false;
		}
		return allUp;
	}

	// shuffle cards
	public void shuffleCards (){
	  	for(int i=n-1; i>0; i--){
	  		// temporary char used to store the exchanged card
	  		char exchangedCard=cards[i];

	  		//Fisher-Yates shuffle method
	  		Random rand=new Random();

	  		int cardChosen=rand.nextInt(i+1);

	  		cards[i]=cards[cardChosen];
	  		cards[cardChosen]=exchangedCard;
	  	}
	}
}