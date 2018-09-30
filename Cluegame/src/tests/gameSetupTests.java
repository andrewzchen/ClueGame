package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.awt.Color;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;

public class gameSetupTests {
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
	}
	/**
	 * Test that checks that the player has a name, color, start cell, and if they are human or not
	 */
	@Test
	public void testLoadingOfPeople0() {
		ArrayList <Player> people = board.getPlayerList();
		Player p = people.get(0);
		assertEquals("Jeremy Lin", p.getPlayerName());
		assertEquals(Color.green, p.getColor());
		assertEquals(5, p.getRow());
		assertEquals(11, p.getColumn());
		assertEquals(true, p.isHuman());
	}
	/**
	 * Test that checks that the player has a name, color, start cell, and if they are human or not
	 */
	@Test
	public void testLoadingOfPeople6() {
	ArrayList <Player> people = board.getPlayerList();
	Player p = people.get(6);
	assertEquals("21 Savage", p.getPlayerName());
	assertEquals(Color.black, p.getColor());
	assertEquals(6, p.getRow());
	assertEquals(3, p.getColumn());
	assertEquals(false, p.isHuman());
	}
	
	/**
		 * Test that checks that the player has a name, color, start cell, and if they are human or not
		 */
	@Test
	public void testLoadingOfPeople3() {
		ArrayList <Player> people = board.getPlayerList();
		Player p = people.get(3);
		assertEquals("Beyonce Knowles", p.getPlayerName());
		assertEquals(Color.magenta, p.getColor());
		assertEquals(8, p.getRow());
		assertEquals(7, p.getColumn());
		assertEquals(false, p.isHuman());
	}
	
	@Test
	public void testLoadingDeck() {
		ArrayList<Card> cards = board.getCardDeck();
		
		/**
		 * Test that checks there are the correct amount of cards
		 */
		assertEquals(23, cards.size());
		int counterPerson= 0;
		int counterWeapon = 0;
		int counterRoom = 0;
		ArrayList<String> testList = new ArrayList<String>();
		/**
		 * A method to separate the cardDeck into decks that are specific to their ENUM
		 * Used to ensure the solution has one of each type of card
		 */
		for(Card c : cards) {
			testList.add(c.getCardName());
			if(c.getCardType() == CardType.PERSON) {
				counterPerson++;
			}
			else if(c.getCardType() == CardType.WEAPON) {
				counterWeapon++;
			}
			else if(c.getCardType() == CardType.ROOM) {
				counterRoom++;
			}
		}
		/**
		 * Test that checks if it contains the right amount of cards in each category of ENUM
		 */
		assertEquals(7, counterPerson);
		assertEquals(7, counterWeapon);
		assertEquals(9, counterRoom);
		/**
		 * Test to check that it contains these 3 player names
		 */
		assertTrue(testList.contains("Xanax"));
		assertTrue(testList.contains("Bruce Lee"));
		assertTrue(testList.contains("Hall"));
	}
	
	@Test
	public void testDealingCards() {
		ArrayList<Card> cards = board.getCardDeck();
		int counter = 0;
		/**
		 * deal function
		 */
		board.dealCards();
		/**
		 * Checks for the deck to be empty following the deal
		 */
		assertEquals(0, cards.size());
		/**
		 * need to include checking that everyone has relatively the same amount of cards in their hand
		 */
		ArrayList<Player> playerTest = board.getPlayerList();
		int max = playerTest.get(0).getPlayerHand().size();
		assertTrue(max - playerTest.get(1).getPlayerHand().size() <= 1);
		assertTrue(max - playerTest.get(2).getPlayerHand().size() <= 1);
		assertTrue(max - playerTest.get(3).getPlayerHand().size() <= 1);
		assertTrue(max - playerTest.get(4).getPlayerHand().size() <= 1);
		assertTrue(max - playerTest.get(5).getPlayerHand().size() <= 1);
		/**
		 * Checks that no duplicate cards are given
		 */
		for(Player p: playerTest) {
			ArrayList<Card> currentHand = p.getPlayerHand();
			for(Card c : currentHand) {
				if(c.equals("Razor")) {
					counter++;
				}
			}
		}
		assertTrue(counter < 2);
	}
}
