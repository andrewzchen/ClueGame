package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class gameActionTests {
	private static Board board;
	@BeforeClass
	public static void setUp() throws FileNotFoundException {
		/**
		 * Board is singleton, get the only instance
		 */
		board = Board.getInstance();
		/**
		 * set the file names to use my config files
		 */
		board.setDeckFiles("src/Possible_Players.txt", "src/PlayerNames.txt", "src/Possible_Weapons.txt", "src/RoomNames.txt");		 // change cluerooms.txt
		/**
		 *  Initialize will load BOTH config files 
		 */
		board.loadConfigFiles();
		board.setConfigFiles("src/clueGame/Clue_Gameboard.csv", "src/clueGame/ClueRooms.txt");	
		board.initialize();
		board.setUpTestHand();
	}
	/**
	 * Test that if a room is the last visited, a random choice is made 
	 * prevents bouncing from one room to another repeatedly
	 */
@Test
public void testTargetRoomSelection() {
		ComputerPlayer player = new ComputerPlayer();
		/**
		 * Test that if a room is in the list of targets, that it is always chosen
		 */
		board.calcTargets(3, 3, 3);
		BoardCell temp = board.getCellAt(3, 3);
		for (int i = 0; i < 100; i++) {
			BoardCell selected = player.pickLocation(board.getTargets(), temp );
			if (selected != board.getCellAt(3, 2)) {
				System.out.print(selected);
				fail("Wrong target was selected");
			}
		}	
		board.calcTargets(11, 2, 4);
		BoardCell temp2 = board.getCellAt(11, 2);
		boolean loc_14_3 = false;
		boolean loc_13_0 = false;
		boolean loc_10_3 = false;
		boolean loc_12_3 = false;
		boolean loc_12_1 = false;
		boolean loc_13_2 = false;
		boolean loc_11_4 = false;
		boolean loc_13_4 = false;
		boolean loc_12_5 = false;
		for(int i = 0; i < 100; i++) {
			BoardCell selected2 = player.pickLocation(board.getTargets(), temp2);
			if(selected2 == board.getCellAt(14, 3)) {
				loc_14_3 = true;
			}
			else if(selected2 == board.getCellAt(13, 0)) {
				loc_13_0 = true;
			}
			else if(selected2 == board.getCellAt(10, 3)) {
				loc_10_3 = true;
			}
			else if(selected2 == board.getCellAt(12, 3)) {
				loc_12_3 = true;
			}
			else if(selected2 == board.getCellAt(12, 1)) {
				loc_12_1 = true;
			}
			else if(selected2 == board.getCellAt(13, 2)) {
				loc_13_2 = true;
			}
			else if(selected2 == board.getCellAt(11, 4)) {
				loc_11_4 = true;
			}
			else if(selected2 == board.getCellAt(13, 4)) {
				loc_13_4 = true;
			}
			else if(selected2 == board.getCellAt(12, 5)) {
				loc_12_5 = true;
			}
		}
		assertTrue(loc_14_3);
		assertTrue(loc_13_0);
		assertTrue(loc_10_3);
		assertTrue(loc_12_3);
		assertTrue(loc_12_1);
		assertTrue(loc_13_2);
		assertTrue(loc_11_4);
		assertTrue(loc_13_4);
		assertTrue(loc_12_5);
	}
/**
 * test that a random cell is selected given no rooms to enter
 */
@Test
public void testRandomCellSelection() {
	ComputerPlayer player = new ComputerPlayer();
	board.calcTargets(12, 0, 2);
	boolean loc_13_1 = false;
	boolean loc_12_2 = false;
	BoardCell temp = board.getCellAt(12, 0);
	for(int i = 0; i < 100; i++) {
		BoardCell selected = player.pickLocation(board.getTargets(),temp);
		if(selected == board.getCellAt(13, 1)) {
			loc_13_1 = true;
		}
		else if (selected == board.getCellAt(12, 2)) {
			loc_12_2 = true;
		}
	}
	assertTrue(loc_13_1);
	assertTrue(loc_12_2);
}
/**
	 * Test that a correct solution passes
	 */
@Test
public void testCorrectAccusation() {
	ComputerPlayer player = new ComputerPlayer();
	Solution solution1 = new Solution();
	solution1.setPerson("Bruce Lee");
	solution1.setRoom("H");
	solution1.setWeapon("Xanax");
	player.makeAccusation("Bruce Lee", "Xanax", "H");
	Solution selectedS = player.getGuess();
	assertEquals(solution1.getPerson(), selectedS.getPerson());
	assertEquals(solution1.getWeapon(), selectedS.getWeapon());
	assertEquals(solution1.getRoom(), selectedS.getRoom());
	
}
/**
	 * Test that a solution with the wrong person will fail
	 */
@Test
public void testWrongPersonAccusation() {
	ComputerPlayer player = new ComputerPlayer();
	Solution solution1 = new Solution();
	solution1.setPerson("Bruce Lee");
	solution1.setRoom("H");
	solution1.setWeapon("Xanax");
	player.makeAccusation("21 Savage", "Xanax", "H");
	Solution selectedS = player.getGuess();
	assertNotEquals(solution1.getPerson(), selectedS.getPerson());
	assertEquals(solution1.getWeapon(), selectedS.getWeapon());
	assertEquals(solution1.getRoom(), selectedS.getRoom());
}
/**
	 * Test that a solution with the wrong weapon will fail
	 */
@Test
public void testWrongWeaponAccusation() {
	ComputerPlayer player = new ComputerPlayer();
	Solution solution1 = new Solution();
	solution1.setPerson("Bruce Lee");
	solution1.setRoom("H");
	solution1.setWeapon("Xanax");
	player.makeAccusation("Bruce Lee", "Razor", "H");
	Solution selectedS = player.getGuess();
	assertEquals(solution1.getPerson(), selectedS.getPerson());
	assertNotEquals(solution1.getWeapon(), selectedS.getWeapon());
	assertEquals(solution1.getRoom(), selectedS.getRoom());
}
/**
 * test for when an accusation has the room incorrect
 */
@Test
public void testWrongRoomAccusation() {
	ComputerPlayer player = new ComputerPlayer();
	/**
	 * Test that a solution with the wrong room will fail
	 */
	Solution solution1 = new Solution();
	solution1.setPerson("Bruce Lee");
	solution1.setRoom("H");
	solution1.setWeapon("Xanax");
	player.makeAccusation("Bruce Lee", "Xanax", "K");
	Solution selectedS = player.getGuess();
	assertEquals(solution1.getPerson(), selectedS.getPerson());
	assertEquals(solution1.getWeapon(), selectedS.getWeapon());
	assertNotEquals(solution1.getRoom(), selectedS.getRoom());
}
/**
 * test for when room matches current location
 */
@Test
public void testRoomSuggestion() {
	ComputerPlayer player = new ComputerPlayer();
	player.fillLists(board);
	/**
	 * create boardcell in the room called Library ('L')
	 */
	
	BoardCell b3 = board.getCellAt(16, 9);
	char temp = b3.getInitial();
	String room = "";
	for (char c : board.getLegend().keySet()) {
		if (c == temp) {
			room = board.getLegend().get(c);
		}
	}
	player.createSuggestion(board, "Living room", 1);
	Solution selectedS = player.getGuess();
	String selected1 = selectedS.getRoom();
	assertEquals(room, selected1);
}
/**
 * test for only one weapon as option
 */
@Test
public void testLastWeapon() {
	ComputerPlayer player = new ComputerPlayer();
	player.fillLists(board);
	player.testingComparison();
	player.createSuggestion(board, "Living Room", 1);
	String lastWeapon = "Razor";
	Solution selectedS = player.getGuess();
	String selected = selectedS.getWeapon();
	assertEquals(lastWeapon, selected);
	}
/**
 * test for only one person as option
 */
@Test
public void testLastPerson() {
	ComputerPlayer player = new ComputerPlayer();
	player.fillLists(board);
	player.testingComparison();
	player.createSuggestion(board, "Living Room", 1);
	String lastPerson = "Bruce Lee";
	Solution selectedS = player.getGuess();
	String selected = selectedS.getPerson();
	assertEquals(lastPerson, selected);
}
/**
 * test for random weapon
 */
@Test
public void randomWeapon() {
	ComputerPlayer player = new ComputerPlayer();
	player.fillLists(board);
	boolean chainsaw = false;
	boolean pyrex = false;
	boolean shank = false;
	boolean poison = false;
	boolean razor = false;
	boolean xanax = false;
	boolean nunchuks = false;
	
	for(int i = 0; i < 100; i++) {
		player.createSuggestion(board, "Living Room", 1);
		Solution selectedS = player.getGuess();
		String selected = selectedS.getWeapon();
		if(selected.equals("Chainsaw")) {
			chainsaw = true;
		}
		else if(selected.equals("Pyrex")) {
			pyrex = true;
		}
		else if(selected.equals("Shank")) {
			shank = true;
		}
		else if(selected.equals("Poison")) {
			poison = true;
		}
		else if(selected.equals("Razor")) {
			razor = true;
		}
		else if(selected.equals("Xanax")) {
			xanax = true;
		}
		else if(selected.equals("Nunchuks")) {
			nunchuks = true;
		}
	}
	
	assertTrue(chainsaw);
	assertTrue(pyrex);
	assertTrue(shank);
	assertTrue(poison);
	assertTrue(razor);
	assertTrue(xanax);
	assertTrue(nunchuks);
}
/**
 * test for random person
 */
@Test
public void randomPerson() {
	ComputerPlayer player = new ComputerPlayer();
	player.fillLists(board);
	boolean Savage = false;
	boolean LilPeep = false;
	boolean BruceLee = false;
	boolean Beyonce = false;
	boolean Emma = false;
	boolean Selena = false;
	boolean Jeremy = false;
	
	for(int i = 0; i < 100; i++) {
		player.createSuggestion(board, "Living Room", 1);
		Solution selectedS = player.getGuess();
		String selected = selectedS.getPerson();
		if(selected.equals("21 Savage")) {
			Savage = true;
		}
		else if(selected.equals("Lil Peep")) {
			LilPeep = true;
		}
		else if(selected.equals("Bruce Lee")) {
			BruceLee = true;
		}
		else if(selected.equals("Beyonce Knowles")) {
			Beyonce = true;
		}
		else if(selected.equals("Emma Watson")) {
			Emma = true;
		}
		else if(selected.equals("Selena Gomez")) {
			Selena = true;
		}
		else if(selected.equals("Jeremy Lin")) {
			Jeremy = true;
		}
	}
	
	assertTrue(Savage);
	assertTrue(LilPeep);
	assertTrue(BruceLee);
	assertTrue(Beyonce);
	assertTrue(Emma);
	assertTrue(Selena);
	assertTrue(Jeremy);
}
/**
 * This test checks if a player has only one matching card, then it should be returned by disproveSuggestion
 */
@Test
public void disproveOneMatching() {
	ComputerPlayer player = new ComputerPlayer();
	Solution s = new Solution ();
	s.setWeapon("Razor");
	Card razor = new Card();
	razor.setCardName("Razor");
	razor.setCardType(CardType.WEAPON);
	player.addHand(razor);
	Card c = new Card();
	if (player.disproveSuggestion(s) != null) {
		c = player.disproveSuggestion(s);
	};
	assertEquals(c.getCardName(), s.getWeapon());
}
/**
 * Method checks that if a player has > 1 matching card, returning card should be chosen randomly
 */
@Test
public void disproveTwoMatching() {
	ComputerPlayer player = new ComputerPlayer();
	Solution s = new Solution ();
	s.setWeapon("Razor");
	s.setRoom("K");
	Card razor = new Card();
	razor.setCardName("Razor");
	razor.setCardType(CardType.WEAPON);
	Card k = new Card();
	k.setCardName("K");
	k.setCardType(CardType.ROOM);
	player.addHand(razor);
	player.addHand(k);
	Card c = new Card();
	boolean razorTest = false;
	boolean kTest = false;
	for (int i = 0; i < 10; ++i) {
	if (player.disproveSuggestion(s) != null) {
		c = player.disproveSuggestion(s);
		if (c.getCardName() == s.getWeapon()) {
			razorTest = true;
		}
		else if (c.getCardName() == s.getRoom()) {
			kTest = true;
		}
	}
	}
	assertTrue(razorTest);
	assertTrue(kTest);
}
/**
 * If player has no matching cards, null is returned
 */
@Test
public void disproveNoMatching () {
	ComputerPlayer player = new ComputerPlayer();
	Solution s = new Solution();
	s.setPerson("Bruce Lee died in 1973");
	assertEquals(null, player.disproveSuggestion(s));
}
/**
 * test that no one can disprove a suggestion using handleSuggestion()
 */
@Test
public void noDisprove() {
	Solution s = new Solution();
	s.setPerson("Nobody");
	s.setRoom("Bathtub");
	s.setWeapon("Sword");
	assertEquals(null, board.handleSuggestion(s, 0));
	}
/**
 * test for checking if only the accuser has a card that could
 * disprove a suggestion using handleSuggestion()
 */
@Test
public void onlyAccuserHas() {
	Solution s = new Solution();
	s.setPerson("Bruce Lee");
	assertEquals(null,board.handleSuggestion(s, 1));
}
/**
 * test that only the human can disprove a suggestion using handleSuggestion()
 */
@Test
public void onlyHumanhas() {
	Solution s = new Solution();
	s.setWeapon("Nunchuks");
	Card c = new Card();
	c.setCardName("Nunchuks");
	c.setCardType(CardType.WEAPON);
	assertEquals(c.getCardName(), board.handleSuggestion(s, 1).getCardName());
	assertEquals(c.getCardType(), board.handleSuggestion(s, 1).getCardType());
	
}
/**
 * test that only the human can disprove a suggestion but the human is also the accuser
 */
@Test
public void humanAsAccuser() {
		Solution s2 = new Solution();
		s2.setWeapon("Nunchuks");
		assertEquals(null, board.handleSuggestion(s2, 0));
	}
/**
 * test for when two players can disprove
 * Ensures that the first person who can disprove in order does
 */
@Test
public void twoDisprove() {
	Solution s = new Solution();
	s.setPerson("Beyonce");
	s.setWeapon("Pyrex");
	Card c = new Card();
	c.setCardName("Beyonce");
	c.setCardType(CardType.PERSON);
	assertEquals(c.getCardName(),board.handleSuggestion(s, 0).getCardName());
	assertEquals(c.getCardType(),board.handleSuggestion(s, 0).getCardType());
	}
/**
 * test for when a human and a computer can disprove, but the computer disproves
 * the suggestion
 */
@Test
public void humanAndComputer() {
	Solution s = new Solution();
	s.setPerson("Beyonce");
	s.setWeapon("Nunchuks");
	Card c = new Card();
	c.setCardName("Beyonce");
	c.setCardType(CardType.PERSON);
	assertEquals(c.getCardName(),board.handleSuggestion(s, 0).getCardName());
	assertEquals(c.getCardType(),board.handleSuggestion(s, 0).getCardType());
}
}



