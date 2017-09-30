package hw4;

public class CFGame {
	//state[i][j]= 0 means the i,j slot is empty
	//state[i][j]= 1 means the i,j slot has red
	//state[i][j]=-1 means the i,j slot has black
	private final int[][] state;
	private boolean isRedTurn;

	// store the row and column index of current turn
	private int currentRowIndex, currentColumnIndex;
	// the number of pieces connect in each direction
	private int horizonalConnect, verticalConnect, diagonalFirstConnect, diagonalSecondConnect;
 
	
	{
		// initialize the board state
		state = new int[7][6];
		for (int i=0; i<7; i++)
			for (int j=0; j<6; j++)
				state[i][j] = 0;
		isRedTurn = true; //red goes first
	}
		
	// return the current state
	public int[][] getState() {
		int[][] ret_arr = new int[7][6];
		for (int i=0; i<7; i++)
			for (int j=0; j<6; j++)
				ret_arr[i][j] = state[i][j];
		return ret_arr;
	}
	

	// whether is red or black turn
	public boolean isRedTurn() {
		return isRedTurn;
	}


	public boolean play(int column){

		boolean validInput=true;
		// change column number into array index
		int columnIndex=column-1;

		// check if column index is valid
		if (columnIndex<0 || columnIndex>6) validInput=false;

		if(validInput==true){
			for (int i=0; i<6; i++){
				// if column is valid, change the first row from the up which is empty
				if (state[columnIndex][i]==0){

					state[columnIndex][i]=isRedTurn ? 1 : -1;
					 // store the row and column index of current turn
					currentRowIndex=i;
					currentColumnIndex=columnIndex;

					// if column index is valid and that column is not full
					return true;
				} 
			}   
		} 
		return false; 
	}


	// get column index in current turn, used later in GUI
	public int getCurrentColumnIndex(){
		return currentColumnIndex;
	}

	// get row index in current turn, used later in GUI
	public int getCurrentRowIndex(){
		return currentRowIndex;
	}


	// check if the game is over
	public boolean isGameOver() {

		// store in the piece in current turn
		int piece=state[currentColumnIndex][currentRowIndex];

		// initialize the number of pieces connect in each four direction to 1
		// sum each direction of the latest piece, to see whether there exist at least one direction that has no more four connected pieces
		horizonalConnect=1;
		verticalConnect=1;
		diagonalFirstConnect=1;
		diagonalSecondConnect=1;

		// horizonal && right
		for (int i=currentColumnIndex+1;i<7;i++){
			if (state[i][currentRowIndex]==piece) horizonalConnect++;
			else break;
		}

		//horizonal && left
		for (int i=currentColumnIndex-1;i>=0;i--){
			if (state[i][currentRowIndex]==piece) horizonalConnect++;
			else break;
		}

		// check the sum of connected pieces in horizontal direction
		if (horizonalConnect>=4) return true;



		//vertical && up
		for (int i=currentRowIndex+1;i<6;i++){
			if (state[currentColumnIndex][i]==piece) verticalConnect++;
			else break;
		}

		//vertical && down
		for (int i=currentRowIndex-1;i>=0;i--){
			if (state[currentColumnIndex][i]==piece) verticalConnect++;
			else break;
		}

		// check the sum of connected pieces in vertical direction
		if (verticalConnect>=4) return true;




		//diagonal && upright
		for (int i=currentRowIndex-1, j=currentColumnIndex+1; i>=0 && j<7 ; i--,j++){

			if (state[j][i]==piece) diagonalFirstConnect++;
			else break;
		}

		//diagonal && downleft
		for (int i=currentRowIndex+1, j=currentColumnIndex-1; i<6 && j>=0 ; i++,j--){

			if (state[j][i]==piece) diagonalFirstConnect++;
			else break;
		}

		// check the sum of connected pieces in first diagonal direction
		if (diagonalFirstConnect>=4) return true;



		//diagonal && upleft
		for (int i=currentRowIndex-1, j=currentColumnIndex-1; i>=0 && j>=0 ; i--,j--){

			if (state[j][i]==piece) diagonalSecondConnect++;
			else break;
		}

		//diagonal && downright
		for (int i=currentRowIndex+1, j=currentColumnIndex+1; i<6 && j<7 ; i++,j++){

			if (state[j][i]==piece) diagonalSecondConnect++;
			else break;
		}

		// check the sum of connected pieces in second diagonal direction
		if (diagonalSecondConnect>=4) return true;


		// check if the board is full
		boolean isBoardFull=true;
		for (int i=0; i<7; i++){
			for (int j=0; j<6; j++){
				if (state[i][j] == 0){
					isBoardFull=false;
					break;
				}
			}
		}

		if(isBoardFull) return true;

		// change the player turn
		isRedTurn =!isRedTurn;
		return false;
	}
	
	// return the winner of the game 
	public int winner() {
		if (horizonalConnect>=4 || verticalConnect>=4 || diagonalFirstConnect>=4 || diagonalSecondConnect>=4) return isRedTurn ? 1 : -1;
		else return 0;
	} 
}