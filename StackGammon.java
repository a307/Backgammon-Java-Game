
/*
 * Sean B
 *
 * 
 * October 27 2020
 */

public class StackGammon {
	
	public static final String BG_ANSI_GREEN = "\u001B[42m";
	public static final String ANSI_WHITE = "\u001B[37m";  
    public static final String ANSI_RESET = "\u001B[0m";

	public static void main(String[] args) {
		
		//instance of board
		Board<Character> gameboard = new Board<Character>();
		
		char white = 'W', black = 'B';
		
		int turns = 1;
		
		boolean exit = false;
		
		//start game 
		gameboard.setupGame(white, black);
		
		//display board
		gameboard.displayBoard();
		
		//game loop
		while(!exit) {
			
			//human's turn (white stones)
			if(turns %2== 1) {
				
				gameboard.playerTurn();
				
				if(gameboard.hasWon(white)) {
					
					System.out.println(ANSI_WHITE + BG_ANSI_GREEN + "\nPlayer "
							+ "has won!!\n" + ANSI_RESET);
					
					exit = true;
					
				}	
				
			} else { //computer's turn (black stones)
				
				gameboard.computerTurn();
				
				if(gameboard.hasWon(black)) {
					
					System.out.println(ANSI_WHITE + BG_ANSI_GREEN + "\nComputer "
							+ "has won!!\n" + ANSI_RESET);
					
					exit = true;
					
				}
				
			}
			
			turns ++;
			
		}
		
	}

}














