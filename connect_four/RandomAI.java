package hw4;

import java.util.Random;

public class RandomAI implements CFAI{
	public int nextMove(CFGame g){
		Random rand = new Random();

		int columnChosenIndex=0;
		
		int[][] currentState=g.getState();

		// randomly generate a valid column index and check if the column is full
		do{
      		columnChosenIndex=rand.nextInt(7)+1;
		
        	if (currentState[columnChosenIndex-1][5]==0) return columnChosenIndex;
      	}while(true);

    }	

    // return the name of random player
	public String getName (){
		return "Random Player";
	}

}