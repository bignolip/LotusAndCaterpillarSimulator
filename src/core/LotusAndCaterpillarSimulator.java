package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * A very simple simulator for the "The White Lotus and Caterpillar Game" on Hackerrank: 
 * 		https://www.hackerrank.com/challenges/the-white-lotus-and-caterpillar-game
 * 
 * There are two players that each control a single piece on a n x m grid.
 * 
 * Player1 starts somewhere along the top row.
 * 
 * Player2 starts somewhere along the bottom row.
 * 
 * Player1 can move like a King piece in Chess.
 * 
 * Player2 can move one cell left or right or remain stationary.
 * 
 * The game ends when Player1's piece occupies the same cell as Player2's piece.
 * 
 * Player1's goal is to win in the smallest number of moves (Note:  this simulator is not perfect).
 * 
 * @author Perrin G. Bignoli
 *
 */
public class LotusAndCaterpillarSimulator {
	/**
	 * This is a very simple tuple class
	 * 
	 * @author Perrin G. Bignoli
	 *
	 * @param <T1> First item type
	 * @param <T2> Second item type
	 */
	public static class Pair<T1, T2> {
		public T1 v1;
		public T2 v2;
		
		public Pair(T1 v1, T2 v2) {
			this.v1 = v1;
			this.v2 = v2;
		}
		
		public Pair(Pair<T1, T2> pair) {
			this.v1 = pair.v1;
			this.v2 = pair.v2;
		}
		
		public boolean equals(Object o) {
			if (o instanceof Pair<?, ?>)
			{
				Pair<?, ?> cast = (Pair<?, ?>) o;
				
				if (this.v1 == null) {
					if (cast.v1 != null) {
						return false;
					}
				}
				else if (cast.v1 == null) {
					return false;
				}
				else if (!this.v1.equals(cast.v1)) {
					return false;
				}
				
				if (this.v2 == null) {
					if (cast.v2 != null) {
						return true;
					}
				}
				else if (cast.v2 == null) {
					return false;
				}
				else if (!this.v2.equals(cast.v2)) {
					return false;
				}
				
				return true;
			}
			else {
				return false;
			}
		}
		
		public String toString() {
			return "<" + this.v1 + ", " + this.v2 + ">";
		}
	}
	
	/*
	 * For a particular game, only the row and column sizes need to be stored
	 */
	private int rows, cols;
	
	public LotusAndCaterpillarSimulator(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}
	
	/*
	 * Calculates the Chebyshev Distance between the pieces:  http://en.wikipedia.org/wiki/Chebyshev_distance
	 */
	public static Integer chebyshevDistance(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
		Integer p1X = p1.v1;
		Integer p1Y = p1.v2;
		
		Integer p2X = p2.v1;
		Integer p2Y = p2.v2;
		
		Integer abs_delta_x = Math.abs(p1X - p2X);
		Integer abs_delta_y = Math.abs(p1Y - p2Y);
		
		return Math.max(abs_delta_x, abs_delta_y);
	}
	
	/*
	 * Calculates all of the possible combinations of starting positions for Player1 and Player2
	 */
	private List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> getPossibleStartingCoordCombos() {
		List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> possibleStartingCoordCombos = new LinkedList<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>();
		
		List<Pair<Integer, Integer>> possibleP1Positions = new LinkedList<Pair<Integer, Integer>>();
		List<Pair<Integer, Integer>> possibleP2Positions = new LinkedList<Pair<Integer, Integer>>();
		
		for (int i = 0 ; i < this.cols ; i++) {
			possibleP1Positions.add(new Pair<Integer, Integer>(i, 0));
			possibleP2Positions.add(new Pair<Integer, Integer>(i, this.rows-1));
		}
		
		for (int i = 0 ; i < this.cols ; i++) {
			for (int j = i ; j < this.cols ; j++) {
				possibleStartingCoordCombos.add(new Pair<Pair<Integer, Integer>, Pair<Integer,Integer>>(possibleP1Positions.get(i), possibleP2Positions.get(j)));
			}
		}
		
		return possibleStartingCoordCombos;
	}
	
	/*
	 * Get all of the valid next moves for P1 (since P2 can't move up or down, P1 should never move up)
	 */
	private List<Pair<Integer, Integer>> getValidP1MovementCells(Pair<Integer, Integer> p1Position) {
		List<Pair<Integer, Integer>> newCoordinates = new LinkedList<Pair<Integer, Integer>>();
		
		Integer p1X = p1Position.v1;
		Integer p1Y = p1Position.v2;
		
		if (p1X > 0) {
			if (p1Y < this.rows-1) {
				newCoordinates.add(new Pair<Integer, Integer>(p1X-1, p1Y+1));
			}
			
			newCoordinates.add(new Pair<Integer, Integer>(p1X-1, p1Y));
		}
		
		if (p1X < this.cols-1) {
			if (p1Y < this.rows-1) {
				newCoordinates.add(new Pair<Integer, Integer>(p1X+1, p1Y+1));
			}
			
			newCoordinates.add(new Pair<Integer, Integer>(p1X+1, p1Y));
		}
		
		if (p1Y < this.rows-1) {
			newCoordinates.add(new Pair<Integer, Integer>(p1X, p1Y+1));
		}
		
		return newCoordinates;
	}
	
	/*
	 * Get all of the valid next moves for P2
	 */
	private List<Pair<Integer, Integer>> getValidP2MovementCells(Pair<Integer, Integer> p2Position) {
		List<Pair<Integer, Integer>> newCoordinates = new LinkedList<Pair<Integer, Integer>>();
		
		newCoordinates.add(new Pair<Integer, Integer>(p2Position));
		
		Integer p2X = p2Position.v1;
		Integer p2Y = p2Position.v2;
		
		if (p2X > 0) {
			newCoordinates.add(new Pair<Integer, Integer>(p2X-1, p2Y));
		}
		
		if (p2X < this.cols-1) {
			newCoordinates.add(new Pair<Integer, Integer>(p2X+1, p2Y));
		}
		
		return newCoordinates;
	}
	
	/*
	 * Run the simulation for all combinations of starting positions.
	 * 
	 * Return a tuple that contains:  each starting position with the number of p1 moves to win and the average number of moves over all runs
	 */
	public Pair<List<Pair<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>, Integer>>, Double> simulateAllPossibleStartingCombos() throws IOException {
		List<Pair<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>, Integer>> results = new LinkedList<Pair<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>, Integer>>();
		
		List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> startingCoords = getPossibleStartingCoordCombos();
		
		int numStartingCombos = startingCoords.size();
		
		int curResult;
		
		Pair<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>, Integer> curRecord;
		
		Pair<Integer, Integer> p1Start;
		Pair<Integer, Integer> p2Start;
		
		int sum = 0;
		
		for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> curStartingCoords : startingCoords) {
			p1Start = curStartingCoords.v1;
			p2Start = curStartingCoords.v2;
			
			curResult = this.simulate(p1Start, p2Start);
			
			sum += curResult;
			
			System.out.println("--------------------------------------");
			System.out.println(curResult);
			System.out.println("--------------------------------------");
			
			curRecord = new Pair<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>, Integer>(curStartingCoords, curResult);
			
			results.add(curRecord);
		}
		
		Double avg = (double)sum / (double)numStartingCombos;
		
		return new Pair<List<Pair<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>, Integer>>, Double>(results, avg);
	}
	
	/*
	 * Run a simulation from a particular starting point
	 */
	public int simulate(Pair<Integer, Integer> p1Start, Pair<Integer, Integer> p2Start) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int minP1Moves = 0;
		
		Pair<Integer, Integer> p1Position = new Pair<Integer, Integer>(p1Start);
		Pair<Integer, Integer> p2Position = new Pair<Integer, Integer>(p2Start);
		
		List<Pair<Integer, Integer>> validP1Moves;
		List<Pair<Integer, Integer>> validP2Moves;
		
		Pair<Integer, Integer> curBestP1Move = null;
		Pair<Integer, Integer> curBestP2Move = null;
		
		Integer curMoveX;
		
		Integer curMinCDistance;
		Integer curMaxCDistance;
		Integer curCDistance;
		
		String curGridState;
		
		while (true) {
			curGridState = this.getGridAsString(p1Position, p2Position);
			
			System.out.println(curGridState);
			
			curMinCDistance = null;
			
			validP1Moves = this.getValidP1MovementCells(p1Position);
			
			for (Pair<Integer, Integer> curP1Move : validP1Moves) {
				if (curP1Move.equals(p2Position)) {
					
					curBestP1Move = curP1Move;
					
					break;
				}
				
				curMoveX = curP1Move.v1;
				
				if ((curMoveX == 0) || (curMoveX == (this.cols-1))) {
					continue;
				}
				
				curCDistance = LotusAndCaterpillarSimulator.chebyshevDistance(curP1Move, p2Position);
				
				if (curMinCDistance == null) {
					curMinCDistance = curCDistance;
					
					curBestP1Move = curP1Move;
				}
				else if (curCDistance < curMinCDistance) {
					curMinCDistance = curCDistance;
					
					curBestP1Move = curP1Move;
				}
			}
			
			p1Position = curBestP1Move;
			
			minP1Moves++;
			
			if (p1Position.equals(p2Position)) {
				curGridState = this.getGridAsString(p1Position, p2Position);
				
				System.out.println(curGridState);
				
				break;
			}
			
			curMaxCDistance = null;
			
			validP2Moves = this.getValidP2MovementCells(p2Position);
			
			for (Pair<Integer, Integer> curP2Move : validP2Moves) {
				curCDistance = LotusAndCaterpillarSimulator.chebyshevDistance(curP2Move, p1Position);
				
				if (curMaxCDistance == null) {
					curMaxCDistance = curCDistance;
					
					curBestP2Move = curP2Move;
				}
				else if (curCDistance > curMaxCDistance) {
					curMaxCDistance = curCDistance;
					
					curBestP2Move = curP2Move;
				}
			}
			
			p2Position = curBestP2Move;
			
	        br.readLine();
		}
		
		return minP1Moves;
	}
	
	/*
	 * Returns a String representation of the current game state
	 */
	public String getGridAsString(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
		StringBuffer buf = new StringBuffer();
		
		char curChar;
		
		for (int i = 0 ; i < this.rows ; i++) {
			for (int j = 0 ; j < this.cols ; j++) {
				if ((p1.v1 == j) && (p1.v2 == i)) {
					curChar = 'C';
				}
				else if ((p2.v1 == j) && (p2.v2 == i)) {
					curChar = 'L';
				}
				else {
					curChar = '0';
				}
				
				buf.append(curChar);
				buf.append(' ');
			}
			
			buf.append("\n");
		}
		
		return buf.toString();
	}
	
	public static void main(String[] args) throws IOException {
		int numArgs = args.length;
		
		if (numArgs != 4) {
			System.err.println("Illegal arguments!");
			
			System.exit(1);
		}
		
		String rowsString = null;
		String colsString = null;
		
		for (int i = 0 ; i < numArgs ; i+=2) {
			if (args[i].equals("-rows")) {
				rowsString = args[i+1];
			}
			else if (args[i].equals("-cols")) {
				colsString = args[i+1];
			}
			else {
				System.err.println("Illegal arguments!");
				
				System.exit(1);
			}
		}
		
		if ((rowsString == null) || (colsString == null)) {
			System.err.println("Illegal arguments!");
			
			System.exit(1);
		}
		
		int rows = Integer.parseInt(rowsString);
		int cols = Integer.parseInt(colsString);
		
		LotusAndCaterpillarSimulator g1 = new LotusAndCaterpillarSimulator(rows, cols);
		
		Double expectedNumberOfMoves = g1.simulateAllPossibleStartingCombos().v2;
		
		System.out.println("--------------------------------------");
		System.out.println("Expected number of P1 moves:  " + expectedNumberOfMoves);
		System.out.println("--------------------------------------");
	}
}
