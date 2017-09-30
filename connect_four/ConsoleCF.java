package hw4;

import java.util.Random;



public class ConsoleCF extends CFGame{
	CFAI ai1, ai2;

	public ConsoleCF(CFAI ai){

		Random rand= new Random();
		CFAI humanPlayer = new HumanPlayer();
		int aiIndex=rand.nextInt(2);
		// based on the random index, pick the red player
		if (aiIndex==0){
			this.ai1=ai;
			this.ai2=humanPlayer;
		}else {
			this.ai2=ai;
			this.ai1=humanPlayer;
		}
	}


	public ConsoleCF(CFAI ai1, CFAI ai2){
		Random rand= new Random();
		int aiIndex=rand.nextInt(2);

		// based on the random index, pick the red player
		if (aiIndex==0){
			this.ai1=ai1;
			this.ai2=ai2;
		}else {
			this.ai2=ai1;
			this.ai1=ai2;
		}
	}


	public void playOut (){
		do{
			// check the turn first, the according player play
			if(super.isRedTurn()) super.play(ai1.nextMove(this));
			else super.play(ai2.nextMove(this));
		// check if the game is over
		}while(!super.isGameOver());
      
	}


	// return the name of the winner
	public String getWinner (){
		int result = super.winner();
		if(result==1) return ai1.getName();
		else if(result==-1) return ai2.getName();
		else return "draw";
	}



	private class HumanPlayer implements CFAI{
		public int nextMove(CFGame g){
			int[][] currentBoard=g.getState();
			// print the current board state to the console
			for (int j=5; j>=0; j--){
     			for (int i=0; i<7; i++){
     				System.out.print(currentBoard[i][j] + "\t");
     				if (i==6) {
     					System.out.println();
     				}
     			}
     		}
			// get human player's input
			java.util.Scanner reader = new java.util.Scanner (System.in);

			System.out.println("Which column to play?");

			int move=reader.nextInt();

			// if the column is out of boundry or is full, let human player choose again
			while(move<1 || move>7 || currentBoard[move-1][5]!=0){
				System.out.println("Invalid column. Please choose another one.");
				move=reader.nextInt();
			} 
      		return move;
	    }	


	    // return human player's name
		public String getName (){
			return "Human Player";
		}

	}


	public static void main(String[] args) { 
		CFAI ai1 = new LingjueAI ();
		CFAI ai2 = new RandomAI (); int n = 1000;
		int winCount = 0;

		for (int i=0; i<n; i++) {
			ConsoleCF game = new ConsoleCF(ai1,ai2);
			game.playOut();
			if (game.getWinner()==ai1.getName())
			winCount ++; 
		}

		System.out.println(ai1.getName() + " wins ");
		System.out.println(((double) winCount)/((double) n)*100 + "%");
		System.out.println(" of the time"); 
	}
}