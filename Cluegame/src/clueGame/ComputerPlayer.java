package clueGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	private Solution guess = new Solution();
	//private String guessPerson;
	//private String guessWeapon;
	String finalPerson;
	String finalRoom;
	String finalWeapon;
	public String getFinalPerson() {
		return finalPerson;
	}
	public String getFinalRoom() {
		return finalRoom;
	}
	public String getFinalWeapon() {
		return finalWeapon;
	}
	private ArrayList<Card> unknownPersons;
	private ArrayList<Card> unknownWeapons;
	private ArrayList<Card> unknownRooms;
	//private char guessRoom;
	
public Solution getGuess() {
		return guess;
	}
/**
 * This method was only made for testing purposes
 * Should be commented out when the game is actually being played
 */
public void testingComparison() {
	int i = 0;
	while(i < unknownWeapons.size() && unknownWeapons.size() != 0 ) {
		if(unknownWeapons.get(i).getCardName().equals("Razor") == false) {
			unknownWeapons.remove(i);
			i = 0;
		}
		else {
			i++;
		}
	}
	while(i < unknownPersons.size() && unknownPersons.size() != 0 ) {
		if(unknownPersons.get(i).getCardName().equals("Bruce Lee") == false) {
			unknownPersons.remove(i);
			i = 0;
		}
		else {
			i++;
		}
	}
	
	for(int j = 0; j < unknownPersons.size(); j++) {
		if(unknownPersons.get(j).getCardName().equals("Bruce Lee") == false) {
			unknownPersons.remove(j);
		}
	}
}
/**
 * This method fills the lists for the weapons and players that the computer 
 * will go through as the game progresses to eliminate possible suggestions
 * @param board
 */
public void fillLists(Board board) {
	unknownPersons = board.getPlayerCards();
	unknownWeapons = board.getWeaponCards();
	unknownRooms = board.getRoomCards();
//	for(Card c : getPlayerHand()) {
//		if(c.getCardType() == CardType.PERSON) {
//			unknownPersons.remove(c);
//		}
//		else if(c.getCardType() == CardType.WEAPON) {
//			unknownWeapons.remove(c);
//	}
//	}
}
/**
 * Method that chooses a cell to move to based off of the starting position of the cell
 * This method makes it so it is not possible to bounce back and forth directly between
 * rooms, but randomizes the chance if a room was last visited
 * @param targets
 * @param startCell
 * @return
 */
public BoardCell pickLocation (Set<BoardCell> targets, BoardCell startCell) {
	for (BoardCell b : targets) {
		if (b.isDoorway() == true && startCell.isDoorway() == false) {
			return b;
		}
	}
	
	Random rand = new Random();
	int cellCounter = 0;
	int counter = rand.nextInt(targets.size());
	
	for(BoardCell b : targets) {
		if (cellCounter == counter) {
			return b;
	}
		else {
			cellCounter++;
		}
	}
	
	return null;
}
//public void makeMove(Set <BoardCell> targets, BoardCell startCell) {
//	BoardCell b = pickLocation(targets, startCell);
//	setRow(b.getRow());
//	setColumn(b.getCol());
//}
/**
 * Method that makes an accusation for the player
 * @param p
 * @param w
 * @param r
 */
public void makeAccusation(String p, String w, String r) {
	guess.setPerson(p);
	guess.setWeapon(w);
	guess.setRoom(r);
}
/**
 * Method that looks at the unknown weapons and persons lists and randomly
 * picks a card from each list
 * @param r
 * @return
 */
public Card createSuggestion(Board board, String r, int index) {
	Random rand = new Random();
	int n = rand.nextInt(unknownPersons.size());
	int m = rand.nextInt(unknownWeapons.size());
	String guessPerson = unknownPersons.get(n).getCardName();
	String guessWeapon = unknownWeapons.get(m).getCardName();
	guess.setPerson(guessPerson);
	guess.setWeapon(guessWeapon);
	guess.setRoom(r);
	Card c = new Card();
	if (board.handleSuggestion(guess, index) != null) {
		c = board.handleSuggestion(guess, index);
		if (c.getCardType() == CardType.PERSON) {
		unknownPersons.remove(guessPerson);
		}
		else if (c.getCardType() == CardType.WEAPON) {
		unknownWeapons.remove(guessWeapon);
		}
		else if (c.getCardType() == CardType.ROOM) {
			unknownRooms.remove(r);
		}
	}
	else {
		accuseTime = true;
//		lastSuggestion(unknownPersons.get(0).getCardName(), unknownWeapons.get(0).getCardName(), unknownRooms.get(0).getCardName());
		
		makeAccusation(unknownPersons.get(0).getCardName(), unknownWeapons.get(0).getCardName(), unknownRooms.get(0).getCardName());
	}
	return c;
}
public boolean isHuman() {
	return false;
}
@Override
public void makeMove(Set <BoardCell> targets, BoardCell startCell) {
	BoardCell b = pickLocation(targets, startCell);
	setRow(b.getRow());
	setColumn(b.getCol());
}
@Override
public void lastSuggestion(String p, String r, String w) {
	finalPerson = p;
	finalRoom = r;
	finalWeapon = w;

	}
}
