package tests;

/* Authors: Andrew Chen and Jordyn McGrath
 * This program tests that adjacencies and targets are calculated correctly.
 */

import java.util.Set;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("src/clueGame/Clue_GameBoard.csv", "src/clueGame/ClueRooms.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}

	// Test locations within rooms
	// These cells are BLUE on the planning spreadsheet
	@Test
	public void testAdjacenciesInsideRooms()
	{
		// Test a corner
		Set<BoardCell> testList = board.getAdjList(0, 0);
		assertEquals(0, testList.size());
		// Test one that has walkway underneath
		testList = board.getAdjList(5, 0);
		assertEquals(0, testList.size());
		// Test one that has walkway above
		testList = board.getAdjList(14, 15);
		assertEquals(0, testList.size());
		// Test one that is in middle of room
		testList = board.getAdjList(10, 1);
		assertEquals(0, testList.size());
		// Test one beside a door
		testList = board.getAdjList(16, 4);
		assertEquals(0, testList.size());
		// Test one in a corner of room
		testList = board.getAdjList(17, 12);
		assertEquals(0, testList.size());
	}

	// Test locations that are Doorways
	// These tests are PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY RIGHT 
		Set<BoardCell> testList = board.getAdjList(3, 2);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(3, 3)));
		// TEST DOORWAY LEFT 
		testList = board.getAdjList(3, 13);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(3, 12)));
		//TEST DOORWAY DOWN
		testList = board.getAdjList(10, 10);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(11, 10)));
		//TEST DOORWAY UP
		testList = board.getAdjList(14, 3);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(13, 3)));
		//TEST DOORWAY DOWN, WHERE THERE"S A WALKWAY TO THE RIGHT
		testList = board.getAdjList(5, 10);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(6, 10)));
		
	}
	
	// Test locations that are adjacent to a doorway with needed direction
	// These tests are WHITE in planning spreadsheet
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction RIGHT
		Set<BoardCell> testList = board.getAdjList(3, 3);
		assertTrue(testList.contains(board.getCellAt(4, 3)));
		assertTrue(testList.contains(board.getCellAt(2, 3)));
		assertTrue(testList.contains(board.getCellAt(3, 4)));
		assertTrue(testList.contains(board.getCellAt(3, 2)));
		assertEquals(4, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(13, 3);
		assertTrue(testList.contains(board.getCellAt(13, 2)));
		assertTrue(testList.contains(board.getCellAt(13, 4)));
		assertTrue(testList.contains(board.getCellAt(14, 3)));
		assertTrue(testList.contains(board.getCellAt(12, 3)));
		assertEquals(4, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(3, 12);
		assertTrue(testList.contains(board.getCellAt(2, 12)));
		assertTrue(testList.contains(board.getCellAt(4, 12)));
		assertTrue(testList.contains(board.getCellAt(3, 13)));
		assertEquals(3, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(11, 10);
		assertTrue(testList.contains(board.getCellAt(11, 9)));
		assertTrue(testList.contains(board.getCellAt(11, 11)));
		assertTrue(testList.contains(board.getCellAt(10, 10)));
		assertTrue(testList.contains(board.getCellAt(12, 10)));
		assertEquals(4, testList.size());
	}

	// Test locations on each edge of the board
	// Some of these locations also only have walkways as adjacent locations
    // Some of these locations are also beside a room cell that is not a doorway
	// These tests are GREY on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board, just one walkway piece
		// Location only has walkway as adjacent location
		// Location is beside a room cell that is not a doorway
		Set<BoardCell> testList = board.getAdjList(0, 8);
		assertTrue(testList.contains(board.getCellAt(1, 8)));
		assertEquals(1, testList.size());
		
		// Test on left edge of board, two walkway pieces
		// Location is beside a room cell that is not a doorway
		testList = board.getAdjList(13, 0);
		assertTrue(testList.contains(board.getCellAt(13, 1)));
		assertTrue(testList.contains(board.getCellAt(12, 0)));
		assertEquals(2, testList.size());

		// Test on bottom edge of board, two walkway pieces
		testList = board.getAdjList(17, 7);
		assertTrue(testList.contains(board.getCellAt(17, 6)));
		assertTrue(testList.contains(board.getCellAt(16, 7)));
		assertEquals(2, testList.size());

		// Test on right edge of board
		testList = board.getAdjList(7,16);
		assertTrue(testList.contains(board.getCellAt(6, 16)));
		assertTrue(testList.contains(board.getCellAt(8, 16)));
		assertTrue(testList.contains(board.getCellAt(7, 15)));
		assertEquals(3, testList.size());
	}
	
	@Test
	public void testTargetsalongWalkways() {
		// Test target along walkway from 1 step away
		board.calcTargets(0, 5, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(1, 5)));
		assertTrue(targets.contains(board.getCellAt(0, 4)));
		// Test target along walkway from 2 steps away
		board.calcTargets(0, 5, 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(2, 5)));
		assertTrue(targets.contains(board.getCellAt(1, 4)));
		assertTrue(targets.contains(board.getCellAt(0, 3)));
		// Test target along walkway from 3 steps away
		board.calcTargets(0, 5, 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCellAt(1, 3)));
		assertTrue(targets.contains(board.getCellAt(2, 4)));
		assertTrue(targets.contains(board.getCellAt(3, 5)));
		assertTrue(targets.contains(board.getCellAt(0, 4)));
		assertTrue(targets.contains(board.getCellAt(1, 5)));
		// Test target along walkway from 4 steps away
		board.calcTargets(0, 5, 4);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(0, 3)));
		assertTrue(targets.contains(board.getCellAt(1, 4)));
		assertTrue(targets.contains(board.getCellAt(2, 3)));
		assertTrue(targets.contains(board.getCellAt(2, 5)));
		assertTrue(targets.contains(board.getCellAt(3, 4)));
		assertTrue(targets.contains(board.getCellAt(4, 5)));
	}
	
	// Test getting into a room
	// These are LIGHT BLUE on the planning spreadsheet

	@Test 
	public void testTargetsIntoRoom()
	{
		// One room is exactly 2 away
		board.calcTargets(16, 7, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		// directly left into the doorway 
		assertTrue(targets.contains(board.getCellAt(16, 5)));
		// directly up and down
		assertTrue(targets.contains(board.getCellAt(14, 7)));
		// one up/down, one left/right
		assertTrue(targets.contains(board.getCellAt(17, 6)));
		assertTrue(targets.contains(board.getCellAt(15, 6)));
	}
	
	// Test getting into room, doesn't require all steps
	// These are PINK on the planning spreadsheet
	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.calcTargets(12, 14, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(6, targets.size());
		// directly up and down
		assertTrue(targets.contains(board.getCellAt(13, 14)));
		assertTrue(targets.contains(board.getCellAt(13, 15)));
		// directly right (can't go left)
		assertTrue(targets.contains(board.getCellAt(12,12)));
		assertTrue(targets.contains(board.getCellAt(13,13)));
		assertTrue(targets.contains(board.getCellAt(11,13)));
		assertTrue(targets.contains(board.getCellAt(12,16)));
		// right then down
		
	}

	// Test getting out of a room
	// These are PINK on the planning spreadsheet
	@Test
	public void testRoomExit()
	{
		// Take one step, essentially just the adj list
		board.calcTargets(11, 2, 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(12, 2)));
		// Take two steps
		board.calcTargets(11, 2, 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(12, 1)));
		assertTrue(targets.contains(board.getCellAt(12, 3)));
		assertTrue(targets.contains(board.getCellAt(13, 2)));
	}

}
