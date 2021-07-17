
/*
 * Sean B
 * 
 * 
 * October 27 2020
 */

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Board<T> {
	
	//this the array of the board that contains each point (including the 2 homes)
	public Stack<T>[] points = (Stack<T>[])(new Stack[26]);
	
	//Bars, black stone count, white stone count, homes, dice
	public int barBlack = 0, barWhite = 0, blackCount = 15, whiteCount = 15,
			homeBlack = 0, homeWhite = 0, die1, die2;
	
	public T white, black;
	
	//colours
	public final static String BG_ANSI_RED = "\u001B[41m";
	public final static String BG_ANSI_BLUE = "\u001B[44m";
	public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE = "\u001B[37m";  
    public static final String BG_ANSI_BLACK = "\u001B[40m";
    public static final String BG_ANSI_WHITE = "\u001B[47m";    
	
	//checks for win
	public boolean hasWon(T stoneType) {
		
		//checking if all stones are in the correct home
		
		if(stoneType.equals(white) && (homeWhite == whiteCount))
			return true;
		else if(stoneType.equals(black) && (homeBlack == blackCount))
			return true;
		
		return false;
		
	}
	
	//moves stones
	public boolean move(int distance, int stone, T stoneType) {
		
		int newSpot;
				
		//move stone if its valid
		if(!isValid(distance, stone, stoneType)) {
			
			System.out.println(ANSI_WHITE + BG_ANSI_RED + "\nMove is not valid.\n"
					+ ANSI_RESET);
			
			return false;
			
		}
		
		//check for bar
		checkForBar(distance, stone, stoneType);
		
		//check home
		checkHome(distance, stone, stoneType);
		
		if(stoneType.equals(white)) {
			
			newSpot = stone + distance;
			
		} else {
			
			newSpot = stone - distance;
			
			//remove negatives
			if(newSpot < 0)
				newSpot = newSpot * (-1);
			
		}
		
		//pop from selected stack
		T displacedStone = points[stone].pop();
		
		//push onto new stack
		points[newSpot].push(displacedStone);
	
		return true;
		
	}
	
	//check/add for home
	public void checkHome(int distance, int stone, T stoneType) {
		
		int newSpot;
		
		if(stoneType.equals(white)) {
			
			newSpot = stone + distance;
			
		} else {
			
			newSpot = stone - distance;
			
			//remove negatives
			if(newSpot < 0)
				newSpot = newSpot * (-1);
			
		}
		
		if(stoneType.equals(white) && newSpot == 25)
			homeWhite += 1;	
		else if(stoneType.equals(black) && newSpot == 0)
			homeBlack += 1;
		
	}
	
	//bar checking/adding method
	public void checkForBar(int distance, int stone, T stoneType) {
		
		int newSpot;
		
		if(stoneType.equals(white)) {
			
			newSpot = stone + distance;
			
		} else {
			
			newSpot = stone - distance;
			
			//remove negatives
			if(newSpot < 0)
				newSpot = newSpot * (-1);
			
		}
		
		//check for bar
		if(!(points[newSpot].isEmpty())) { 
			
			if(!(stoneType.equals(points[newSpot].peek()))) {
				
				if(points[newSpot].size() == 1) {
					
					//if conditions are met add to bar and pop stone
					
					points[newSpot].pop();
					
					if(stoneType.equals(white)) {
						
						//add to bar remove from count
						
						barBlack += 1;
						
						blackCount -= 1;
					
						System.out.println(ANSI_WHITE + BG_ANSI_RED + "\nA BLACK stone has"
								+ " been bumped to the bar!\n" + ANSI_RESET);
					
					} else {
						
						//add to bar remove from count
						
						barWhite += 1;
						
						whiteCount -= 1;
						
						System.out.println(ANSI_WHITE + BG_ANSI_RED + "\nA WHITE stone has"
								+ " been bumped to the bar!\n" + ANSI_RESET);
						
					}
					
				}
				
			}
		
		}
		
	}
	
	//checks if stuck in the bar
	public boolean hasStonesInBar(T stoneType) {
		
		if(stoneType.equals(white) && barWhite > 0)	
			return true;
		else if(stoneType.equals(black) && barBlack > 0)
			return true;
		
		return false;
		
	}
	
	//checks if move is valid
	public boolean isValid(int distance, int stone, T stoneType) {
		
		int newSpot;
		
		if(stoneType.equals(white)) {
			
			newSpot = stone + distance;
			
		} else {
			
			newSpot = stone - distance;
			
			//remove negatives
			if(newSpot < 0)
				newSpot = newSpot * (-1);
			
		}
		
		if(stone > 24 || stone < 1)
			return false;
		else if(points[stone].isEmpty())//check if stack is empty
			return false;
		else if(!(stoneType.equals(points[stone].peek())))//check if piece matches player's pieces
			return false;
		else if((newSpot > 25) && (stoneType.equals(white))) //checks for over shooting
			return false;
		else if((newSpot < 0) && (stoneType.equals(black)))// checks for over shooting
			return false;
		else if(!(points[newSpot].isEmpty())) { //checks if new spot has more than 1 opposing stone
		
			if(!(stoneType.equals(points[newSpot].peek()))) {
				
				if(points[newSpot].size()>1)
					return false;
				
			}
			
		}
		
		return true;
		
	}
	
	//human's turn (white)
	public void playerTurn() {
		
		boolean display;
		
		System.out.println("\n******************************");
		System.out.println("*        PLAYER MOVE         *");
		System.out.println("******************************");
				
		//roll dice
		rollDice();
		
		//check if player has stones in the bar
		if(hasStonesInBar(white)) {
			
			System.out.println(ANSI_WHITE + BG_ANSI_RED 
					+ "You are trapped in the bar!" + ANSI_RESET);
			
			//check for doubles
			if(die1 == die2) {
				
				System.out.println(ANSI_WHITE + BG_ANSI_BLUE + "You rolled doubles! One"
						+ " stone removed from the bar." + ANSI_RESET);
				
				//remove a stone from the bar 
				barWhite -= 1;
				
			}
			
			//press enter to continue
			Scanner scany = new Scanner(System.in);
			
			System.out.println("Press 1 to continue");
			
			String doesnotMatter = scany.nextLine(); 
			
		} else {
		
			//die 1 move
			System.out.println("MOVE a stone " + die1 + " moves. (DICE #1)");
			
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Move which stone? which point? (1-24): ");
			
			int playerMove1 = scan.nextInt();
			
			//move piece
			display = move(die1, playerMove1, white);
			
			//display board again
			if(display)
				displayBoard();
			
			//die 2 move
			System.out.println("\nMOVE a stone " + die2 + " moves. (DICE #2)");
			
			Scanner scan1 = new Scanner(System.in);
			
			System.out.println("Move which stone? which point? (1-24): ");
			
			int playerMove2 = scan1.nextInt();
			
			//move piece
			display = move(die2, playerMove2, white);
			
			//display board again
			if(display)
				displayBoard();
		
		}
		
	}
	
	//computer's turn (black)
	public void computerTurn() {
		
		boolean display;
		
		int computerMove1 = 0, computerMove2 = 0;
		
		boolean backup = true;
		
		System.out.println("\n******************************");
		System.out.println("*       COMPUTER MOVE        *");
		System.out.println("******************************");
		
		//roll dice
		rollDice();
		
		if(hasStonesInBar(black)) {
			
			System.out.println(ANSI_WHITE + BG_ANSI_RED + "Computer is"
					+ " trapped in the bar!" + ANSI_RESET);
			
			//check for doubles
			if(die1 == die2) {
				
				System.out.println(ANSI_WHITE + BG_ANSI_BLUE + "Computer rolled doubles! One"
						+ " stone removed from the bar." + ANSI_RESET);
				
				//remove a stone from the bar 
				barBlack -= 1;
				
			}
			
		} else {
			
			boolean endSearch = false;
			
			int count = 1;
			
			//DICE 1---------------------------------------------------
			
			while((!endSearch) && count < 25) {
				
				computerMove1 = count;
				
				//check if move is valid??????????????
				if(isValid(die1, computerMove1, black)) {
					
					//ensure no random override of move
					backup = false;
					
					//break loop 
					endSearch = true;
					
				}
				
				//add to count
				count ++;
				
			}
			
			//adding 1/5 chance of doing random move override
			Random rand = new Random();
			
			if(rand.nextInt(5) == 3)
				backup = true;
			
			//back up random move if no valid moves are found
			if(backup) {
				
				//if there is no valid move then do a random one
				Random random = new Random();
				
				computerMove1 = random.nextInt(24) + 1;
							
			}
			
			//complete dice 1 move
			move(die1, computerMove1, black);
			
			//DICE 2----------------------------------------------------
			
			//reset count and back up and search
			count = 1;
			backup = true;
			endSearch = false;
			
			while((!endSearch) && count < 25) {
				
				computerMove2 = count;
				
				//check if move is valid??????????????
				if(isValid(die2, computerMove2, black)) {
					
					//if move is valid then do it
					//move(die2, computerMove2, black);
					
					//ensure no random override
					backup = false;
					
					//end loop
					endSearch = true;
					
				}
				
				//add to count
				count ++;
				
			}
			
			//adding 1/5 chance of doing random move override
			Random rand2 = new Random();
			
			if(rand2.nextInt(5) == 3)
				backup = true;
			
			//back up random move
			if(backup) {
								
				//if there is no valid move then do a random one
				Random random = new Random();
				
				computerMove2 = random.nextInt(24) + 1;
							
			}
			
			//complete dice 2 move
			move(die2, computerMove2, black);
			
			//display move
			displayBoard();
			
		}
		
	}
	
	//dice rolling method
	public void rollDice() {
		
		Random random = new Random();
		
		die1 = random.nextInt(6) + 1;
		
		die2 = random.nextInt(6) + 1;
		
		System.out.println("\n******** [ THE ROLL ] ********");
		System.out.println("==> DICE #1 player rolled a " + die1);
		System.out.println("==> DICE #2 player rolled a " + die2 + "\n");	
		
	}
	
	//displays entire game board
	public void displayBoard() {
		
		//board
		System.out.println("\n******* [ THE BOARD ] ********\n");
		
		for(int i = 0; i < 26; i ++) {
									
			//if stack ain't empta' display' it
			if(!points[i].isEmpty()) {
				
				if(i < 10)
					System.out.print(" " + i + ": ");
				else
					System.out.print(i + ": ");
				
				//display amount of stones at each point via size of stack
				for(int j = 0; j < points[i].size(); j ++) {
	
					if(points[i].peek().equals(white)) {
						
						System.out.print(ANSI_BLACK + BG_ANSI_WHITE 
								+ "" + points[i].peek() + "" + ANSI_RESET);
						
					} else {
						
						System.out.print(ANSI_WHITE + BG_ANSI_BLACK 
								+ "" + points[i].peek() + "" + ANSI_RESET);
						
					}
					
				}
				
				System.out.println();
				
			}
			else {
				
				if(i < 10)
					System.out.println(" " + i + ": ");
				else
					System.out.println(i + ": ");
				
			}
				
		}
		
		//bar and homes
		System.out.println("\n******* [ THE STATS ] ********\n");
		System.out.println("Human player has " + barWhite 
				+ " stones in bar");
		System.out.println("Computer player has " + barBlack 
				+ " stones in bar");
		System.out.println("Human player has " + homeWhite 
				+ " stones at home point 25");
		System.out.println("Computer player has " + homeBlack 
				+ " stones at home point 0");
		
	}
	
	//Initialises and generates game board stone starting places
	public void setupGame(T white, T black) {
		
		//set to match global variables
		this.white = white;
		
		this.black = black;
		
		//Initialise a stack for each point
		for(int i = 0; i < 26; i ++)
			points[i] = new Stack<T>();
		
		//add to each stack/point for initial start
		
		//white 1
		points[1].push(white);
		points[1].push(white);
	
		//black 6
		points[6].push(black);
		points[6].push(black);
		points[6].push(black);
		points[6].push(black);
		points[6].push(black);
		
		//black 8
		points[8].push(black);
		points[8].push(black);
		points[8].push(black);
		
		//white 12
		points[12].push(white);
		points[12].push(white);
		points[12].push(white);
		points[12].push(white);
		points[12].push(white);
		
		//black 13
		points[13].push(black);
		points[13].push(black);
		points[13].push(black);
		points[13].push(black);
		points[13].push(black);
		
		//white 17
		points[17].push(white);
		points[17].push(white);
		points[17].push(white);
		
		//white 19
		points[19].push(white);
		points[19].push(white);
		points[19].push(white);
		points[19].push(white);
		points[19].push(white);
		
		//black 24
		points[24].push(black);
		points[24].push(black);
		
	}
	
}
