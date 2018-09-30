/**
 * Authors:
 * Andrew Chen
 * Jordyn McGrath
 */

package tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class initTests {
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 18;
	public static final int NUM_COLS = 17;
	
	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("src/clueGame/Clue_Gameboard.csv", "src/clueGame/ClueRooms.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}
	@Test
	public void testRooms() {
		// Get the map of initial => room 
		Map<Character, String> legend = board.getLegend();
		/**
		 * tests to make sure that we load the correct number of rooms
		 */
		assertEquals(LEGEND_SIZE, legend.size());
		/** Tests that the data was loaded correctly
		 * 
		 */
		assertEquals("TrapHouse", legend.get('T'));
		assertEquals("Kitchen", legend.get('K'));
		assertEquals("Closet", legend.get('X'));
		assertEquals("Pool", legend.get('P'));
		assertEquals("Walkway", legend.get('W'));
	}
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLS, board.getNumColumns());		
	}
	@Test
	public void FourDoorDirections() {
		/**
		 * tests that the direction of the doors is correct at these locations
		 */
		clueGame.BoardCell room = board.getCellAt(3, 2);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(11, 2);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(10, 14);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(7, 10);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		/**
		 * checks that rooms are not considered doors
		 */
		room = board.getCellAt(0,0);
		assertFalse(room.isDoorway());	
		/**
		 * checks that walkways are not considered doors
		 */
		BoardCell cell = board.getCellAt(17,6);
		assertFalse(cell.isDoorway());		

	}
	
	@Test
	public void testNumberOfDoorways() 
	{
		/**
		 * Tests that we have the correct number of doors
		 */
		int numDoors = 0;
		for (int row=0; row<board.getNumRows(); row++)
			for (int col=0; col<board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(11, numDoors);
	}
	
	@Test
	public void testRoomInitials() {
		
		/**
		 * Checks that at those cell locations, that they are in the room
		 * they are supposed to be in
		 */
		assertEquals('T', board.getCellAt(0, 0).getInitial());
		assertEquals('K', board.getCellAt(9, 1).getInitial());
		assertEquals('H', board.getCellAt(16, 1).getInitial());
		assertEquals('S', board.getCellAt(1, 9).getInitial());
		assertEquals('L', board.getCellAt(15, 9).getInitial());
		// Test a walkway
		assertEquals('W', board.getCellAt(0, 5).getInitial());
		// Test the closet
		assertEquals('X', board.getCellAt(3,7).getInitial());
	}


}
