package clueGame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
public abstract class Player {
	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	protected String playerName;
	protected boolean accuseTime = false;
	private int row;
	private int column;
	private Color color;
	public boolean isAccuseTime() {
		return accuseTime;
	}
	private boolean isHuman = false;
	private ArrayList<Card> playerHand = new ArrayList<Card>();
/**
 * This method draws the players on the board
 * @param g
 */
	public void drawPlayers(Graphics2D g) {
		g.setColor(color);
		g.fillOval(column*WIDTH,row*HEIGHT, WIDTH, HEIGHT);
	}
	public void addHand(Card c) {
		playerHand.add(c);
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
/**
 * This method checks to see if a suggestion matches the cards in the other
 * players hands, and based off of that information, determines if a suggestion
 * can be disproved.  If it is disproved it returns that card so the accuser
 * knows what card they correctly suggested
 * @param suggestion
 * @return
 */
	public Card disproveSuggestion(Solution suggestion) {
		String personCheck = suggestion.getPerson();
		String weaponCheck = suggestion.getWeapon();
		String roomCheck = suggestion.getRoom();
//		System.out.println("Running disproveSuggestion");
//			for (Card c : playerHand) {
//				System.out.println("For player " + playerName);
//				System.out.println("Card in hand is " + c.getCardName());
//			}
		
		//System.out.println("Checking disprove suggestion");
		//System.out.println("Our person weapon and room are " + personCheck + " " + weaponCheck + " " + roomCheck);
		int correctCount = 0;
		ArrayList<Card> matches = new ArrayList<Card>();
		for (Card c : playerHand) {
			//System.out.println("Current card is " + c.getCardName());
			if(c.getCardName().equals(personCheck)) {
				correctCount++;
				matches.add(c);
			}
			else if (c.getCardName().equals(weaponCheck)) {
				correctCount++;
				matches.add(c);
			}
			else if (c.getCardName().equals(roomCheck)) {
				correctCount++;
				matches.add(c);
			}
		}
		if (correctCount == 1) {
			return matches.get(0);
		}
		else if (correctCount > 1) {
			Random rand = new Random();
			int n = rand.nextInt(correctCount);
			return matches.get(n);
		}
		return null;
	}
	public Color convertColor(String strColor) {
		
		 Color color;
		 try {
		 // We can use reflection to convert the string to a color
		 Field field = Class.forName("java.awt.Color").getField(strColor.trim());
		 color = (Color)field.get(null);
		 } catch (Exception e) {
		 color = null; // Not defined
		 }
		 return color;
		}
	public void clearHand() {
		playerHand = new ArrayList<Card>();
	}
	public abstract boolean isHuman();
	
	public ArrayList<Card> getPlayerHand() {
		return playerHand;
	}
	public abstract void fillLists(Board board);
	public abstract Card createSuggestion(Board board, String r, int index);
	public abstract void makeMove(Set<BoardCell> targets, BoardCell startCell);
	public void lastSuggestion(String p, String r, String w) {}
	public abstract Solution getGuess();
	
}
