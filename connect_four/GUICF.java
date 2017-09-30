package hw4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GUICF extends CFGame{
	CFAI ai1, ai2;
	// the panel contains 42 labels
	MyBoard panelDown = new MyBoard();
	// the button in the AI vs AI game
	JButton aIbutton = new JButton("Play");
	// the seven button in human vs AI game
	JButton[] humanButton = new JButton[7];
	// store whether the AI is red or black in human vs AI game
	int aiIndex;
	int aIcolor = -1;

	public class MyBoard extends JPanel{
		// create 42 labels
		private final JLabel[][] labels = new JLabel[7][6];

		// MyBoard constructor
		public MyBoard(){
		}

		// custom board setup
		public void setUp() {
			this.setLayout(new GridLayout(6,7));

			// set up the 42 labels
			for(int j=5; j>=0; j--){
				for (int i=0; i<7; i++){
					JLabel label = new JLabel(" ");
					label.setBackground(new Color(255,255,255));
					label.setOpaque(true);
					label.setBorder(BorderFactory.createLineBorder(Color.black));
					labels[i][j]= label;
					this.add(label);
				}
			}
		}

		// change the color of the label according to the color of the player
		public void paint(int x, int y, int color){
			if (color==-1) labels[x][y].setBackground(new Color(0,0,0));
			else if (color==1) labels[x][y].setBackground(new Color(255,0,0));
		}
	}


	// AI vs human
	public GUICF(CFAI ai){
		Random rand= new Random();
		ai1=ai;
		// randomly choose who play first
		aiIndex=rand.nextInt(2);
		if (aiIndex==0) aIcolor=1;

		// set up the frame
		JFrame frame=  new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane=frame.getContentPane();
		JPanel panelUp= new JPanel();
		panelUp.setLayout(new GridLayout(1,7));
		pane.add(panelUp,BorderLayout.NORTH);

		// add 7 buttons to the top of the panel
		for(int i=0; i<7; i++){
			humanButton[i]= new JButton("\u2193");
			// add listener to each button
			humanButton[i].addActionListener(new humanBottonListener());
			panelUp.add(humanButton[i]);
		}
		
		// use the customed board
		panelDown.setUp();
		pane.add(panelDown);

		frame.setSize(300,300);
		frame.setVisible(true);


		// if AI is red, let it play the first turn
		if (aIcolor == 1){
			GUICF.super.play(ai.nextMove(GUICF.this));
			// change the color of the label
			panelDown.paint(GUICF.super.getCurrentColumnIndex(),GUICF.super.getCurrentRowIndex(),1);
			// check if the game is over
			GUICF.super.isGameOver();
		}
	}

	// listener in human vs AI game
	class humanBottonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){

			for(int i=0; i<7; i++){
				// check which button has been chosen
				if(event.getSource()==humanButton[i]){
					GUICF.super.play(i+1);
					// change the color the label
					panelDown.paint(i,GUICF.super.getCurrentRowIndex(),-aIcolor);

					// check if the game is over
					if(GUICF.super.isGameOver()) {
						gameEnds();
						getWinner();
					}
					break;
				}	
			}

			// let AI make its move
			GUICF.super.play(ai1.nextMove(GUICF.this));
			panelDown.paint(GUICF.super.getCurrentColumnIndex(),GUICF.super.getCurrentRowIndex(),aIcolor);

			if(GUICF.super.isGameOver()) {
				gameEnds();
				getWinner();
			}

		}
	}

	

	// AI vs AI game
	public GUICF(CFAI ai1, CFAI ai2){
		// based on the random number, pick the red player
		Random rand= new Random();
		int aiIndex=rand.nextInt(2);

		if (aiIndex==0){
			this.ai1=ai1;
			this.ai2=ai2;
		}else {
			this.ai2=ai1;
			this.ai1=ai2;
		}

		// set up the frame
		JFrame frame= new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane=frame.getContentPane();
		JPanel panelUp= new JPanel();
		pane.add(panelUp,BorderLayout.NORTH);

		
		// add listener to the play button
		aIbutton.addActionListener(new AiVsAiBottonListener());
		panelUp.add(aIbutton);

		// use the customed board
		panelDown.setUp();
		pane.add(panelDown);


		frame.setSize(300,300);
		frame.setVisible(true);
	}


	// the listener in AI vs AI game
	class AiVsAiBottonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
		
			// check whose turn, then make move and change the label color
			if(GUICF.super.isRedTurn()){
				GUICF.super.play(ai1.nextMove(GUICF.this));
				panelDown.paint(GUICF.super.getCurrentColumnIndex(),GUICF.super.getCurrentRowIndex(),1);
			}
			else {
				GUICF.super.play(ai2.nextMove(GUICF.this));
				panelDown.paint(GUICF.super.getCurrentColumnIndex(),GUICF.super.getCurrentRowIndex(),-1);
			}

			// check if game is over
			if(GUICF.super.isGameOver()) {
				gameEnds();
				getWinner();
			}
		}
	}

	// if the game is over, disable all the buttons
	public void gameEnds(){
		aIbutton.setEnabled(false);
		for(int i=0; i<7; i++){
			if (humanButton[i]!= null)
				humanButton[i].setEnabled(false);
		}
	}

	// if the game is over, print the winner to the console
	public void getWinner(){
		if(GUICF.super.winner()==1) System.out.println("Game is over. Red wins.");
		else if(GUICF.super.winner()==-1)  System.out.println("Game is over. Black wins.");
		else System.out.println("Game is over. Draw.");
	}



	public static void main(String[] args) { 
		CFAI ai1 = new LingjueAI ();
		CFAI ai2 = new RandomAI (); 

		GUICF game = new GUICF(ai1);
	}
}













