package hw4;

import java.lang.Math;


public class LingjueAI implements CFAI{

	public int nextMove(CFGame g){
		// intialize the first empty row index of each column to -1
		int[] currentRows= new int[]{-1,-1,-1,-1,-1,-1,-1};
		int[][] currentBoard=g.getState();

		// get the first empty row index of each column
		for(int c=0; c<7; c++){
			for (int r=0; r<6; r++){
				if (currentBoard[c][r]==0){
					currentRows[c]=r;
					break;
				} 
			}
		}

		// whether this AI is red or black
		int myPiece=g.isRedTurn() ? 1 : -1;
		int[] myBoardColumnsMax= new int[7];

		// get an array of potential max connected piece of each column
		for(int c=0; c<7; c++){
			myBoardColumnsMax[c]= getFourDirectionMaxSum(currentBoard, myPiece, currentRows[c], c);
		}

		int rivalPiece=-myPiece;
		int[] rivalBoardColumnsMax= new int[7];

		// get an array of rival's potential max connected piece of each column
		for(int c=0; c<7; c++){
			rivalBoardColumnsMax[c]= getFourDirectionMaxSum(currentBoard, rivalPiece, currentRows[c], c);
		}

		int  myMaxIndex=0,  rivalMaxIndex=0;

		// get index of the column which has potential max connected piece
		myMaxIndex=maxIndex(myBoardColumnsMax);

		rivalMaxIndex=maxIndex(rivalBoardColumnsMax);

		// first check if any column has potential four or more than four conntected piece
		if(myBoardColumnsMax[myMaxIndex]>=4) return myMaxIndex+1;

		// second check if any column has potential four or more than four conntected piece for rival
		else if(rivalBoardColumnsMax[rivalMaxIndex]>=4) return rivalMaxIndex+1;

		// else put the piece which will maximize the number of connected piece
		else return myMaxIndex+1;
    }	

    // return the name of the AI
	public String getName (){
		return "Lingjue Player";
	}



	// if choose this column, return the max sum of connected piece in the four directions
	private int getFourDirectionMaxSum(int[][] state, int piece, int currentRow, int currentColumn){
		// current column is full
		if (currentRow==-1) return -1;

		// initialize the number of pieces connect in each four direction to 1
		int horizonalConnect=1, verticalConnect=1, diagonalFirstConnect=1, diagonalSecondConnect=1;

	    // horizonal && right
	    for (int i=currentColumn+1;i<7;i++){
	      if (state[i][currentRow]==piece) horizonalConnect++;
	      else break;
	    }

	    //horizonal && left
	    for (int i=currentColumn-1;i>=0;i--){
	      if (state[i][currentRow]==piece) horizonalConnect++;
	      else break;
	    }



	    //vertical && up
	    for (int i=currentRow+1;i<6;i++){
	      if (state[currentColumn][i]==piece) verticalConnect++;
	      else break;
	    }

	    //vertical && down
	    for (int i=currentRow-1;i>=0;i--){
	      if (state[currentColumn][i]==piece) verticalConnect++;
	      else break;
	    }



	    //diagonal && upright
	    for (int i=currentRow-1, j=currentColumn+1; i>=0 && j<7 ; i--,j++){

	      if (state[j][i]==piece) diagonalFirstConnect++;
	      else break;
	    }

	    //diagonal && downleft
	    for (int i=currentRow+1, j=currentColumn-1; i<6 && j>=0 ; i++,j--){

	      if (state[j][i]==piece) diagonalFirstConnect++;
	      else break;
	    }



	    //diagonal && upleft
	    for (int i=currentRow-1, j=currentColumn-1; i>=0 && j>=0 ; i--,j--){

	      if (state[j][i]==piece) diagonalSecondConnect++;
	      else break;
	    }

	    //diagonal && downright
	    for (int i=currentRow+1, j=currentColumn+1; i<6 && j<7 ; i++,j++){

	      if (state[j][i]==piece) diagonalSecondConnect++;
	      else break;
	    }

	    // return the max sum of connected piece in the four directions
	    return Math.max(Math.max(Math.max(horizonalConnect,verticalConnect),diagonalFirstConnect),diagonalSecondConnect);
	}


	// find the column has the potential max sum of connected pieces and return its index
	private int maxIndex(int[] i_arr) {
    	int max = i_arr[0];
    	int maxIndex=0;
    	for (int i = 0; i < i_arr.length; i++) {
        	if (i_arr[i] > max) {
            max = i_arr[i];
            maxIndex=i;
        	}
    	}
    	return maxIndex;
	}
}

