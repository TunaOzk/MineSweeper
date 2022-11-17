import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
public class MineSweeper
{
	private static String[][] userBoard;
	private static int[][] backgroundBoard;
	private static int row;
	private static int column;
	private static int nums_of_mines = 0;
	private static Random rand = new Random();
	private static boolean isGameOver;

	public static void cellAction(int rowNum, int columnNum, char letter)
	{
		if(cellIsOpened(rowNum, columnNum))
			if(letter == 'F')
				System.out.println("Open cells cannot be flagged.");
			else
				System.out.println("Only flagged cells can be unflagged.");
		else
			if(letter == 'F')
				if(userBoard[rowNum][columnNum] == "F")
					System.out.println("Cell is already flagged.");
				else
					userBoard[rowNum][columnNum] = "F";
			else
				if(userBoard[rowNum][columnNum] == "F")
					userBoard[rowNum][columnNum] = "o";
				else
					System.out.println("Only flagged cells can be unflagged.");
	}
	public static void printBoard()
	{
		// Counter determines whether all free cells are opened.Closed cells numbers should equal to number of the mines.
		int oCounter = 0;
		for(int i = 0; i < row; i++)
		{	
			for(int j = 0; j < column; j++)
			{
				if(userBoard[i][j] == "o" || userBoard[i][j] == "F")
					oCounter++;
				System.out.print(userBoard[i][j] + " ");				
			}
			System.out.println();
		}
		if(oCounter == nums_of_mines)
		{
			System.out.println("Congratulations , you won.");
			isGameOver = true;
		}
		System.out.println();
	}
	public static boolean cellIsOpened(int rowNum, int columnNum)
	{
		if(userBoard[rowNum][columnNum] == "o" || userBoard[rowNum][columnNum] == "F")
			return false;
		else
			return true;
	}
	public static void openCell(int rowNum, int columnNum)
	{

		if(cellIsOpened(rowNum, columnNum))
			System.out.println("Cell is already open.");
		else if(userBoard[rowNum][columnNum] == "F")
			System.out.println("Flagged cells cannot be opened.");
		else
		{
			ArrayList<Integer> adjacentCells = new ArrayList<Integer>();
			adjacentCells = getAdjacentCells(rowNum, columnNum);			
			if(backgroundBoard[rowNum][columnNum] == - 1)
			{
				for(int i = 0; i < row; i++)
					for(int j = 0; j < column; j++)
						if(backgroundBoard[i][j] == - 1)
							userBoard[i][j] = "X";
				System.out.println("You lost , better luck next time.");
				isGameOver = true;
			}
			else if(backgroundBoard[rowNum][columnNum] == 0)
			{
				userBoard[rowNum][columnNum] = "-";
				// Should keep eye on empty cells that are adjacent to each other.
				for(int k = 0; k < adjacentCells.size() - 1; k += 2)
					if(cellIsOpened(adjacentCells.get(k), adjacentCells.get(k + 1)) == false)
						if(backgroundBoard[adjacentCells.get(k)][adjacentCells.get(k + 1)] == 0)
							openCell(adjacentCells.get(k), adjacentCells.get(k + 1)); //Adjacented empty(0) cells should also be opened.
						else
							userBoard[adjacentCells.get(k)][adjacentCells.get(k + 1)] = Integer.toString(backgroundBoard[adjacentCells.get(k)][adjacentCells.get(k + 1)]);
			}
			else
				userBoard[rowNum][columnNum] = Integer.toString(backgroundBoard[rowNum][columnNum]);
		}
	}
	public static ArrayList<Integer> getAdjacentCells(int rowNum, int columnNum)
	{
		// Keeping adjacentCells' coordinates respectively in an integer arraylist.
		ArrayList<Integer> adjacentCells = new ArrayList<Integer>();
		for(int i = 0; i < row; i++)
			for(int j = 0; j < column; j++)
				if(Math.abs(rowNum - i) <= 1 && Math.abs(columnNum - j) <= 1)
					// Cell itself shouldn't be in the arraylist.
					if(!(rowNum == i && columnNum == j))
					{
						adjacentCells.add(i);
						adjacentCells.add(j);				
					}		
		return adjacentCells;
	}

	public static void setBackgroundBoard()
	{
		ArrayList<Integer> adjacentCells = new ArrayList<Integer>();
		//Initializing the background board.
		for(int i = 0; i < row; i++)
			for(int j = 0; j < column; j++)
				backgroundBoard [i][j] = 0;
		// Placing the mines into background board. "-1" represents the mines, "0" represents empty cells.
		for(int i = 0; i < nums_of_mines; i++)
		{
			int num1 = rand.nextInt(row);
			int num2 = rand.nextInt(column);
			if(backgroundBoard [num1][num2] == 0)
				backgroundBoard [num1][num2] = -1;
			else
				i--;
		}
		// Placing the other numbers.
		for(int i = 0; i < row; i++)
			for(int j = 0; j < column; j++)
				if(backgroundBoard[i][j] == - 1)
				{
					// Adjacent cells' values should be increased if they are a neighbor cell to mine.
					adjacentCells = getAdjacentCells(i, j);					
					for(int k = 0; k < adjacentCells.size() - 1; k += 2)
						if(backgroundBoard[adjacentCells.get(k)][adjacentCells.get(k + 1)] != - 1)
							backgroundBoard[adjacentCells.get(k)][adjacentCells.get(k + 1)]++;
				}
	}
	public static void main(String[] args)
	{
		String boardSize, diff;
		Scanner keyb = new Scanner(System.in);
		
		System.out.println("::WELCOME::");
		System.out.print("Please enter the sizes of the board (m x n) :");
		boardSize = keyb.nextLine();
		System.out.print("Please select the difficulty (E, M, H) :");
		diff = keyb.nextLine();

		row = Integer.parseInt(boardSize.substring(0, boardSize.indexOf("x") - 1));
		column  = Integer.parseInt(boardSize.substring(boardSize.indexOf("x") + 2, boardSize.length()));
		userBoard = new String[row][column];
		backgroundBoard = new int[row][column];
		//Initializing the user board.
		for(int i = 0; i < row; i++)
			for(int j = 0; j < column; j++)
				userBoard [i][j] = "o";

		// Adjusting the number of mines according to difficulty.
		if(diff.equals("E"))
			nums_of_mines = row * column * 15 / 100;
		else if(diff.equals("M"))
			nums_of_mines = row * column * 25 / 100;
		else
			nums_of_mines = row * column * 40 / 100;

		setBackgroundBoard();
		printBoard();
		isGameOver = false;
		while(!isGameOver)
		{
			System.out.print("Please make a move:");
			String moves = keyb.nextLine();
			int rowNum = Integer.parseInt(moves.substring(0, moves.indexOf(",")));
			int columnNum;
			if(moves.contains(" "))
				columnNum = Integer.parseInt(moves.substring(moves.indexOf(",") + 1, moves.indexOf(" ")));
			else
				columnNum = Integer.parseInt(moves.substring(moves.indexOf(",") + 1, moves.length()));
			char action;
			rowNum = row - rowNum;
			columnNum = columnNum - 1;
			if(moves.charAt(moves.length() - 1) == 'F' || moves.charAt(moves.length() - 1) == 'U')
			{
				action = moves.charAt(moves.length() - 1);
				cellAction(rowNum, columnNum, action);
				printBoard();
			}
			else
			{
				openCell(rowNum, columnNum);
				printBoard();
			}
		}
	}
}