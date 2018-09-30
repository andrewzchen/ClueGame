/**
 * Authors:
 * Andrew Chen
 * Jordyn McGrath
 */
package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.util.Set;

public class IntBoard {
private Map <BoardCell, Set<BoardCell>> adjMtx;
private Set<BoardCell> possibleTargets;
private Set<BoardCell> visited;
private BoardCell[][] grid;
private static final int BOARD_LENGTH = 4;
private static final int BOARD_WIDTH = 4;
public IntBoard() {
	grid = new BoardCell[BOARD_LENGTH][BOARD_WIDTH];  
	/**
	 * Setting up our grid. In this case it is 4x4
	 */
	for (int i = 0; i < BOARD_LENGTH; ++i) {
		for (int j = 0; j < BOARD_WIDTH; ++j) {
			grid[i][j] = new BoardCell(i,j);
		}
	}
	adjMtx = new HashMap <BoardCell, Set<BoardCell>>();
	calcAdjacencies();
}
public BoardCell getCell(int row, int col) {
	return grid[row][col];
}
public void calcAdjacencies() {
	/**
	 * In this function, we calculate adjacent cells by adding/subtracting 1 to rows and columns. If row +- 1 and col +- 1 stay within board
	 * then the subsequent cell is added to the set. adjMtx holds a set of adjacent cells for every single cell using a Map data structure.
	 */
	Set<BoardCell> temp;
	BoardCell b;
	for (int i = 0; i < BOARD_LENGTH; ++i) {
		for (int j = 0; j < BOARD_WIDTH; ++j) {
			temp = new HashSet<BoardCell>();
			b = grid[i][j];
			if (b.getRow() + 1 <= BOARD_LENGTH - 1) {
				BoardCell bc = getCell(b.getRow()+1, b.getCol());
				temp.add(bc);
				
			}
			if(b.getRow()-1 >= 0) {
				BoardCell bd = getCell(b.getRow()-1, b.getCol());
				temp.add(bd);
			}
			if(b.getCol()+1 <= BOARD_WIDTH - 1) {
				BoardCell be = getCell(b.getRow(), b.getCol()+1);
				temp.add(be);
			}
			if(b.getCol()-1 >= 0) {
				BoardCell bf = getCell(b.getRow(), b.getCol()-1);
				temp.add(bf);
			}
			adjMtx.put(b,temp);
		}
	}
	
}
public Set<BoardCell> getAdjList(BoardCell b){
	return adjMtx.get(b);
	
	
}
public void calcTargets(BoardCell startCell, int pathLength) {
	/**
	 * in this function, we identify the possible targets, given a startingCell and pathLength. We call findAllTargets, which is a 
	 * recursive function written below.
	 */
	visited = new HashSet<BoardCell>();
	possibleTargets = new HashSet<BoardCell>();
	visited.add(startCell);
	findAllTargets(startCell, pathLength);
}
public void findAllTargets(BoardCell thisCell, int numSteps) {
	/**
	 * In this function, we loop through every cell that is adjacent to thisCell. In each loop, we first see if we have visited the cell.
	 * If not, we add the adjacent cell to visited, and call function again with 1 less step. Repeat until numSteps = 1, indicating this
	 * adjacent cell is now a possibleTarget. We remove adj at the end, as the loop starts again and selects a new path.
	 */
	for (BoardCell adj : adjMtx.get(thisCell)) {
		if (visited.contains(adj)) {
			continue;
		}
		else {
			visited.add(adj);
			if (numSteps == 1) {
				possibleTargets.add(adj);
			}
			else {
				findAllTargets(adj, numSteps - 1);
			}
		}
		visited.remove(adj);
	}
}
public Set<BoardCell> getTargets() {
	return possibleTargets;
}
}
