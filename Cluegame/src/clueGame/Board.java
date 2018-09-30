/**
 * Authors:


 * Andrew Chen
 * Jordyn McGrath
 */
package clueGame;

import static org.junit.Assert.fail;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clueGame.BoardCell;

public class Board  extends JPanel implements MouseListener{
	public static final int MAX_BOARD_SIZE = 50; 
	/**
	 * we initialize numRows and numColumns as MAX_BOARD_SIZE so we can initialize our board. Otherwise, our getCellAt would return null, and our tests would have null pointer
	 * exceptions instead of failing.
	 */
	private int numRows = MAX_BOARD_SIZE; 
	private int numColumns = MAX_BOARD_SIZE;
	private static final int BOX_WIDTH = 50;
	public static final int BOX_HEIGHT = 50;
	private BoardCell[][] board;
	private Map<Character, String> legend;
	//We initially setup boardSetup to false because there is initially no board, and there will not be one until the user inputs the config files for the project
	private boolean boardSetup = false;
	private Map<BoardCell, Set<BoardCell>> adjMatrix;
	private int dieRoll;
	private String boardConfigFile;
	private String roomConfigFile;
	private String personFile;
	private String personNameFile; // only has names of person
	private String weaponFile;
	private String roomFile;
	private String sugRoom;
	private String sugWeapon;
	private String sugPerson;
	private Solution solution;
	private Card guessCard;
	private boolean humPlayer = true;
	private ArrayList<String> tempName = new ArrayList<String>();
	private ArrayList<String> tempRoom= new ArrayList<String>();
	private ArrayList<String> tempWeapon= new ArrayList<String>();
	private ArrayList<Card> playerCards;
	private ArrayList<Card> weaponCards;
	private ArrayList<Card> roomCards;
	private ArrayList <Card> cardDeck;
	private ArrayList <Player> playerList;
	private ArrayList<Player> players;
	private Set<BoardCell> visited;
	private Set<BoardCell> possibleTargets;
	private ArrayList<Player> testPlayerList  = new ArrayList<Player>();
	private boolean playerTurnDone = false;
	ComputerPlayer player = new ComputerPlayer();
	ComputerPlayer player2 = new ComputerPlayer();
	ComputerPlayer player3 = new ComputerPlayer();
	HumanPlayer humanPlayer = new HumanPlayer();
	private JOptionPane splashScreen = new JOptionPane();
	// A constant that will account for a single shift in any direction along the board
	private static final int CELL_SHIFT = 1;
	// variable used for singleton pattern
	private static Board theInstance = new Board();
		// constructor is private to ensure only one can be created
	private Board() {}
		// this method returns the only Board
	public static Board getInstance() {
			return theInstance;
	}
	
	/**
	 * calls loadRoomConfig and loadBoardConfig in order to setup the game board
	 * and to run the try catch while also running a unit test
	 */
	public void initialize()  {
		try{
		loadRoomConfig();
		loadBoardConfig();
		calcAdjacencies();
		dealCards();
		splashScreen();
		displayCards();
		fillLists();
		addMouseListener(this);
	}catch (FileNotFoundException e) {
		e.printStackTrace();
	}	
	catch (BadConfigFormatException e) {
		e.printStackTrace();
	}
	
	}
	public void fillLists() {
		for (Player p : playerList) {
			if (p.isHuman() == false) {
				p.fillLists(theInstance);
			}
		}
	}
	/**
	 * if the board has not been initialized we will read in from the files and create a board
	 * the board is then populated with all of the letters 
	 * @throws FileNotFoundException
	 * @throws BadConfigFormatException
	 */
	public void loadRoomConfig() throws FileNotFoundException, BadConfigFormatException {
		if (boardSetup == false) {
		int counter = 0;
		Scanner sc_1 = new Scanner(new File(boardConfigFile));
		while (sc_1.hasNextLine()) {
			String in = sc_1.nextLine();
			String array1[] = in.split(",");
			counter++;
			numColumns = array1.length;
		}
		sc_1.close();
		numRows = counter;
		
		legend = new HashMap<Character, String>();
		board = new BoardCell[numRows][numColumns];
		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				board[i][j] = new BoardCell(i,j);
			}
		}
		boardSetup = true;
		}
		Scanner sc2 = new Scanner(new File(roomConfigFile));
		try {
		while (sc2.hasNextLine()) {
			String in = sc2.nextLine();
			String array1[] = in.split(", ");
			char key = array1[0].charAt(0);
			legend.put(key, array1[1]);
			String temp = array1[2].replaceAll("\\s+","");
			if (temp.equals("Card") || temp.equals("Other")) {
			}
			else {
				throw new BadConfigFormatException("There is a room type in the config file that is not Card or Other");
			}
		}
		}
		finally {
			if (sc2 != null) {
		sc2.close();
			}
		}
	}
	
	/**
	 * sets up the board in the case of a file not found or if there is something 
	 * wrong with the configuration file we will throw an error.  If not then, the board
	 * is populated with all of the letters (rooms) and the enumerations are instantiated 
	 * for their specific letter
	 * @throws FileNotFoundException
	 * @throws BadConfigFormatException
	 */
	public void loadBoardConfig() throws FileNotFoundException, BadConfigFormatException {
		if(boardSetup == false) {
			int counter = 0;
			Scanner sc_1 = new Scanner(new File(boardConfigFile));
			while (sc_1.hasNextLine()) {
				String in = sc_1.nextLine();
				String array1[] = in.split(",");
				counter++;
				numColumns = array1.length;
			}
			sc_1.close();
			numRows = counter;
		
		legend = new HashMap<Character, String>();
		board = new BoardCell[numRows][numColumns];
		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				board[i][j] = new BoardCell(i,j);
			}
		}
		boardSetup = true;
		}
		int row = 0;
		Scanner sc = new Scanner( new File(boardConfigFile));
		try {
		while (sc.hasNextLine()) {
		String in_2 = sc.nextLine();

		String array1[] = in_2.split(",");
		if (array1.length != numColumns) {
			throw new BadConfigFormatException("Number of columns in each row is not the same");
		}
		int col = 0;
		for (String s : array1) {
			char c = s.charAt(0);
			boolean badRoom = true;
			for (char test : legend.keySet()) {
				if (c == test) {
					badRoom = false;
				}
				else if (legend.keySet().size() == 0) {
					badRoom = false;
				}
			}
			if (badRoom == true) {
				throw new BadConfigFormatException("A room specified in the config file is not in the legend");
			}
			board[row][col].setInitial(c);
			if (s.length() > CELL_SHIFT) {
			char direction = s.charAt(1);
			switch (direction) {
			case 'R':
				board[row][col].setDoorDirection(DoorDirection.RIGHT);
				break;
			case 'U':
				board[row][col].setDoorDirection(DoorDirection.UP);
				break;
			case 'D':
				board[row][col].setDoorDirection(DoorDirection.DOWN);
				break;
			case 'L':
				board[row][col].setDoorDirection(DoorDirection.LEFT);
				break;
			default:
				board[row][col].setDoorDirection(DoorDirection.NONE);
				break;
			}
			}
			else {
				board[row][col].setDoorDirection(DoorDirection.NONE);
			}
			col++;
		}
		row++;
		}
		}
		finally {
			if (sc != null) {
		sc.close();
			}
		}
	}
	/**
	 * sets the files for all the cards to be used
	 * @param personFile
	 * @param personNameFile
	 * @param weaponFile
	 * @param roomFile
	 */
	public void setDeckFiles(String personFile, String personNameFile, String weaponFile, String roomFile) {
		this.personFile = personFile;
		this.personNameFile = personNameFile;
		this.weaponFile = weaponFile;
		this.roomFile = roomFile;
	}
	/**
	 * loads the files to be used
	 * @throws FileNotFoundException
	 */
	public void loadConfigFiles () throws FileNotFoundException {
		cardDeck = new ArrayList<Card>();
		playerList = new ArrayList<Player>();
		Scanner sc_0 = new Scanner (new File(personFile));
		try {
			while (sc_0.hasNextLine()) {
				//Player p = new Player();
				String in = sc_0.nextLine();
				String array1[] = in.split(",");
				String type = array1[4];
				if (type.equals("Human")) {
					HumanPlayer p = new HumanPlayer();
					p.setPlayerName(array1[0]);
					Color c_1 = p.convertColor(array1[1]);
					p.setColor(c_1);
					int i = Integer.parseInt(array1[2]);
					p.setRow(i);
					int i_2 = Integer.parseInt(array1[3]);
					p.setColumn(i_2);
					playerList.add(p);
				}
				else {
					ComputerPlayer p = new ComputerPlayer();
					p.setPlayerName(array1[0]);
					Color c_1 = p.convertColor(array1[1]);
					p.setColor(c_1);
					int i = Integer.parseInt(array1[2]);
					p.setRow(i);
					int i_2 = Integer.parseInt(array1[3]);
					p.setColumn(i_2);
					playerList.add(p);
				}
//				p.setPlayerName(array1[0]);
//				Color c_1 = p.convertColor(array1[1]);
//				p.setColor(c_1);
//				int i = Integer.parseInt(array1[2]);
//				p.setRow(i);
//				int i_2 = Integer.parseInt(array1[3]);
//				p.setColumn(i_2);
//				playerList.add(p);
			}
		} finally {
			if (sc_0 != null) {
				sc_0.close();
			}
		}
		
		Scanner sc_1 = new Scanner(new File(personNameFile));
		try{
		while (sc_1.hasNextLine()) {
			Card c = new Card();
			String in = sc_1.nextLine();
			c.setCardName(in);
			c.setCardType(CardType.PERSON);
			cardDeck.add(c);
		}
			} finally {
				if(sc_1 != null) {
				sc_1.close();
			}
		}
		Scanner sc_2 = new Scanner(new File(weaponFile));
		try{
		while (sc_2.hasNextLine()) {
			Card c = new Card();
			String in = sc_2.nextLine();
			c.setCardName(in);
			c.setCardType(CardType.WEAPON);
			cardDeck.add(c);
		}
			} finally {
				if(sc_2 != null) {
				sc_2.close();
			}
		}
		Scanner sc_3 = new Scanner(new File(roomFile));
		try{
		while (sc_3.hasNextLine()) {
			Card c = new Card();
			String in = sc_3.nextLine();
			c.setCardName(in);
			c.setCardType(CardType.ROOM);
			cardDeck.add(c);
		}
			} finally {
				if(sc_3 != null) {
				sc_3.close();
			}
		}
	}
	/**
	 * Similar to intBoard method to calculate adjacent lists. The main differences are
	 * that we need to check if B is a doorway (adjacent list is 1, only contains walkway
	 * corresponding to direction), is a room (no adjacent list), or if B is adjacent
	 * to a doorway and can enter (need to check for DoorDirection in this case)
	 * 
	 */
	public void calcAdjacencies() {
		adjMatrix = new HashMap <BoardCell, Set<BoardCell>>();
		BoardCell b;
		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				Set <BoardCell> temp = new HashSet<BoardCell>();
				b = board[i][j];
				if (b.isDoorway()) {
					if (b.getDoorDirection() == DoorDirection.UP) {
						BoardCell bb = getCellAt(b.getRow() - CELL_SHIFT, b.getCol());
						temp.add(bb);
					}
					else if (b.getDoorDirection() == DoorDirection.DOWN) {
						BoardCell bb = getCellAt(b.getRow() + CELL_SHIFT, b.getCol());
						temp.add(bb);
					}
					else if (b.getDoorDirection() == DoorDirection.LEFT) {
						BoardCell bb = getCellAt(b.getRow(), b.getCol() - CELL_SHIFT);
						temp.add(bb);
					}
					else if (b.getDoorDirection() == DoorDirection.RIGHT) {
						BoardCell bb = getCellAt(b.getRow(), b.getCol() + CELL_SHIFT);
						temp.add(bb);
					}
					adjMatrix.put(b, temp);
					continue;
				}
				if (b.isRoom()) {
					adjMatrix.put(b,  temp);
					continue;
				}
				if (b.getRow() + CELL_SHIFT <= numRows - CELL_SHIFT) {
					BoardCell bc = getCellAt(b.getRow() + CELL_SHIFT, b.getCol());
					if (bc.isWalkway()) {
					temp.add(bc);
					}
					else if (bc.isDoorway() && bc.getDoorDirection() == DoorDirection.UP) {
						temp.add(bc);
					}
				}
				if(b.getRow()-CELL_SHIFT >= 0) {
					BoardCell bd = getCellAt(b.getRow()-CELL_SHIFT, b.getCol());
					if (bd.isWalkway()) {
						temp.add(bd);
						}
						else if (bd.isDoorway() && bd.getDoorDirection() == DoorDirection.DOWN) {
							temp.add(bd);
						}
				}
				if(b.getCol()+1 <= numColumns - CELL_SHIFT) {
					BoardCell be = getCellAt(b.getRow(), b.getCol() + CELL_SHIFT);
					if (be.isWalkway()) {
						temp.add(be);
						}
						else if (be.isDoorway() && be.getDoorDirection() == DoorDirection.LEFT) {
							temp.add(be);
						}
				}
				if(b.getCol()-CELL_SHIFT >= 0) {
					BoardCell bf = getCellAt(b.getRow(), b.getCol()-CELL_SHIFT);
					if (bf.isWalkway()) {
						temp.add(bf);
						}
						else if (bf.isDoorway() && bf.getDoorDirection() == DoorDirection.RIGHT) {
							temp.add(bf);
						}
				}
					adjMatrix.put(b,temp);	
			}
		}
	}
	/**
	 * This method is very similar to IntBoard method.  However we have added in the findAllTargets method which immediately adds the BoardCell adjacent to
	 * possibleTargets if the adjacent cell is a doorway. Find all targets being a recursive method so it checks all possibilities within the given path length left.
	 * 
	 */
	public void calcTargets(int row, int col, int pathLength) {
		BoardCell startCell = getCellAt(row,col);
		visited = new HashSet<BoardCell>();
		possibleTargets = new HashSet<BoardCell>();
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	public void findAllTargets(BoardCell thisCell, int numSteps) {
		/**
		 * In this function, we loop through every cell that is adjacent to thisCell. In each loop, we first see if we have visited the cell.
		 * If not, we add the adjacent cell to visited, and call function again with 1 less step. Repeat until numSteps = 1, indicating this
		 * adjacent cell is now a possibleTarget. However, if the cell is a doorway, we immediately add it to possibleTargets, and then continue looping.
		 *  We remove adj at the end, as the loop starts again and selects a new path.
		 */
		for (BoardCell adj : adjMatrix.get(thisCell)) {
			if (visited.contains(adj)) {
				continue;
			}
			else {
				if (adj.isDoorway()) {
					possibleTargets.add(adj);
					visited.add(adj);
					continue;
				}
				visited.add(adj);
				if (numSteps == CELL_SHIFT) {
					possibleTargets.add(adj);
				}
				else {
					findAllTargets(adj, numSteps - CELL_SHIFT);
				}
			}
			visited.remove(adj);
		}
	}
	/**
	 * Sets the private variables boardConfigFile and roomConfigFile to the parameters
	 * of setConfigFiles
	 * @param clueLayout
	 * @param clueLegend
	 */
	public void setConfigFiles(String clueLayout, String clueLegend) {
		boardConfigFile = clueLayout;
		roomConfigFile = clueLegend;
	}
	/**
	 * Method for dealing the cards and creating the solution
	 */
	public void dealCards() {
		
		playerCards = new ArrayList<Card>();
		weaponCards = new ArrayList<Card>();
		roomCards = new ArrayList<Card>();
		for(Card cd : cardDeck) {
			if(cd.getCardType() == CardType.PERSON) {
				playerCards.add(cd);
			}
			else if(cd.getCardType() == CardType.WEAPON) {
				weaponCards.add(cd);
			}
			else if(cd.getCardType() == CardType.ROOM) {
				roomCards.add(cd);
			}
	}
		int numPlayers = 0;
		solution = new Solution();
		Random rand = new Random();
		int g = rand.nextInt(playerCards.size());
		String temp = playerCards.get(g).getCardName();
		solution.setPerson(temp);
		System.out.println("Person in solution is " + temp);
		int f = rand.nextInt(weaponCards.size());
		String temp2 = weaponCards.get(f).getCardName();
		solution.setWeapon(temp2);
		System.out.println("Weapon in solution is " + temp2);
		int t = rand.nextInt(roomCards.size());
		String temp3 = roomCards.get(t).getCardName();
		//char temp4 = temp3.charAt(0);
		solution.setRoom(temp3);
		System.out.println("Room in solution is " + temp3);
		Collections.shuffle(cardDeck);
		for(Card c: cardDeck) {
			if( c.getCardName().equals(solution.getPerson()) || c.getCardName().equals(solution.getRoom()) || c.getCardName().equals(solution.getWeapon())) {
				continue;
			}
			else {
			playerList.get(numPlayers).addHand(c);
			numPlayers = (numPlayers +1) % playerList.size();	
			}
		}
//		for (Player p : playerList) {
//			for (Card c : p.getPlayerHand()) {
//				System.out.println("Card in hand is " + c.getCardName());
//			}
//		}
		cardDeck.clear();
	}
	public Solution getSolution() {
		return solution;
	}
	/**
	 * Method for testing purposes, will not be used in the actual game
	 */
	public void setUpTestHand() {
		for (Player p : playerList) {
			p.clearHand();
		}
		Card razor = new Card();
		Card k = new Card();
		Card bl = new Card();
		razor.setCardName("Razor");
		razor.setCardType(CardType.WEAPON);
		k.setCardName("Kitchen");
		k.setCardType(CardType.ROOM);
		bl.setCardName("Bruce Lee");
		bl.setCardType(CardType.PERSON);
		playerList.get(1).addHand(razor);
		playerList.get(1).addHand(k);
		playerList.get(1).addHand(bl);
		player.addHand(razor);
		player.addHand(k);
		player.addHand(bl);
		// player2
		Card xanax = new Card();
		Card h = new Card();
		Card b = new Card();
		xanax.setCardName("Xanax");
		xanax.setCardType(CardType.WEAPON);
		h.setCardName("Hall");
		h.setCardType(CardType.ROOM);
		b.setCardName("Beyonce");
		b.setCardType(CardType.PERSON);
		playerList.get(2).addHand(xanax);
		playerList.get(2).addHand(h);
		playerList.get(2).addHand(b);
		player2.addHand(xanax);
		player2.addHand(h);
		player2.addHand(b);
		//player3
		Card pyrex = new Card();
		Card l = new Card();
		Card j = new Card();
		pyrex.setCardName("Pyrex");
		pyrex.setCardType(CardType.WEAPON);
		l.setCardName("Living room");
		l.setCardType(CardType.ROOM);
		j.setCardName("Jeremy Lin");
		j.setCardType(CardType.PERSON);
		playerList.get(3).addHand(pyrex);
		playerList.get(3).addHand(l);
		playerList.get(3).addHand(j);
		player3.addHand(pyrex);
		player3.addHand(l);
		player3.addHand(j);
		//humanPlayer
		Card nunchuks = new Card();
		Card t = new Card();
		Card e = new Card();
		nunchuks.setCardName("Nunchuks");
		nunchuks.setCardType(CardType.WEAPON);
		t.setCardName("T");
		t.setCardType(CardType.ROOM);
		e.setCardName("Emma Watson");
		e.setCardType(CardType.PERSON);
		playerList.get(0).addHand(nunchuks);
		playerList.get(0).addHand(t);
		playerList.get(0).addHand(e);
		humanPlayer.addHand(nunchuks);
		humanPlayer.addHand(t);
		humanPlayer.addHand(e);
		testPlayerList.add(player);
		testPlayerList.add(player2);
		testPlayerList.add(player3);
		testPlayerList.add(humanPlayer);
	}
	/**
	 * Method that disproves suggestions based on the order or the players
	 * and what cards they have in their hand at the time
	 */
public Card handleSuggestion (Solution suggestion, int index) {
	for(int i = 0; i < playerList.size(); i++) {
		if(i == index) {
			continue;
		}
		playerList.get(i).disproveSuggestion(suggestion);
	if(playerList.get(i).disproveSuggestion(suggestion) != null) {
		return playerList.get(i).disproveSuggestion(suggestion);
		}
	}
	return null;
}
/**
 * This method calls all of the drawing methods and draws the board in its entirety, including showing the players in their starting positions
 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
					getCellAt(i,j).draw(g2d);
				}
			}
		for(int i = 0; i < numRows;i++) {
			for(int j = 0; j< numColumns; j++) {
				getCellAt(i,j).drawRoomNames(g2d);
				}
			}
	for(int i = 0; i < playerList.size(); i++) {
		playerList.get(i).drawPlayers(g2d);
		}
		if (humPlayer == true) {
		BoardCell b = new BoardCell (playerList.get(0).getRow(),playerList.get(0).getColumn());
		calcTargets(b.getRow(), b.getCol(), dieRoll);
		for (BoardCell c : possibleTargets) {
			getCellAt(c.getRow(),c.getCol()).colorTargets(g2d);
		}
			}
		}
//		repaint();
	
	
	public void setHumPlayer(boolean humPlayer) {
	this.humPlayer = humPlayer;
}
	public void setDieRoll(int dieRoll) {
	this.dieRoll = dieRoll;
}
	public void splashScreen() {
		for(Player p: playerList) {
			if(p.isHuman()) {
				String temp = p.getPlayerName();
				splashScreen.showMessageDialog(null, "You are " + temp + ", press OK to begin playing", "Welcome to Clue Game!", JOptionPane.INFORMATION_MESSAGE);
				break;
			}
		}
	}
	public void displayCards() {
		for (Player p: playerList) {
			if(p.isHuman()) {
				ArrayList<Card> s = p.getPlayerHand();
				for(Card c : s) {
					if(c.getCardType() == CardType.PERSON) {
						
					tempName.add(c.getCardName());
					}
					else if(c.getCardType() == CardType.ROOM) {
					tempRoom.add(c.getCardName());
					}
					else {
					tempWeapon.add(c.getCardName());
					}
				}
			}
		}
	}


	/**
	 * returns the legend map
	 * @return
	 */
	public Map<Character, String> getLegend() {
		return legend;
	}
	/** 
	 * gets the number of rows
	 * @return
	 */
	public int getNumRows() {
		return numRows;
	}
	/**
	 * gets the number of columns
	 * @return
	 */
	public int getNumColumns() {
		return numColumns;
	}
	public Set<BoardCell> getAdjList(int r, int c) {
		BoardCell b = getCellAt(r,c);
		return adjMatrix.get(b);
	}
	public Set<BoardCell> getTargets() {
		return possibleTargets;
	}
	/**
	 *returns the Boardcell at this specific row and column location
	 */
	public BoardCell getCellAt(int row, int col) {
		return board[row][col];
	}
	public ArrayList<Card> getPlayerCards() {
		return playerCards;
	}
	public ArrayList<Card> getWeaponCards() {
		return weaponCards;
	}
	public ArrayList<Card> getRoomCards() {
		return roomCards;
	}
	public ArrayList<Card> getCardDeck() {
		return cardDeck;
	}
	public ArrayList<Player> getPlayerList() {
		return playerList;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public ArrayList<Player> getTestPlayerList() {
		return testPlayerList;
	}
	public ArrayList<String> getTempName() {
		return tempName;
	}
	public ArrayList<String> getTempRoom() {
		return tempRoom;
	}
	public ArrayList<String> getTempWeapon() {
		return tempWeapon;
	}
	public boolean isPlayerTurnDone() {
		return playerTurnDone;
	}
	public void setSugRoom(String sugRoom) {
		this.sugRoom = sugRoom;
	}
	public void setSugWeapon(String sugWeapon) {
		this.sugWeapon = sugWeapon;
	}
	public void setSugPerson(String sugPerson) {
		this.sugPerson = sugPerson;
	}
	public void setPlayerTurnDone(boolean playerTurnDone) {
		this.playerTurnDone = playerTurnDone;
	}
	/**
	 * mouseClicked moves the player when they click a target square. We use getY/30 and
	 * get X/30 because when drawing the board we used x and y * 30. 
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (possibleTargets.contains(getCellAt(e.getY() / 30, e.getX() / 30)) && playerTurnDone == false) {
			playerList.get(0).setRow(e.getY() / 30);
			playerList.get(0).setColumn(e.getX() / 30);
			int tempRow = playerList.get(0).getRow();
			int tempCol = playerList.get(0).getColumn();
			JDialog guessPanel = new JDialog();
			if(getCellAt(playerList.get(0).getRow(), playerList.get(0).getColumn()).isDoorway()) {
				for(Character c : legend.keySet()) {
					if (getCellAt(playerList.get(0).getRow(), playerList.get(0).getColumn()).getInitial() == c) {
						guessPanel = makeGuess(legend.get(c));
					}
					
				}
				
				guessPanel.setSize(new Dimension(500, 500));
				guessPanel.setVisible(true);
			}
			repaint();
			humPlayer = false;
			playerTurnDone = true;
		}
		else if (playerTurnDone == true){
		splashScreen.showMessageDialog(null, "Your turn is over", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			splashScreen.showMessageDialog(null, "Please select a valid target (squares marked red)", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	private JDialog makeGuess(String r) {
		String[] weaponNames = {"Chainsaw", "Pyrex", "Shank", "Poison", "Razor", "Xanax", "Nunchuks"};
		String[] peopleNames = {"21 Savage", "Lil Peep", "Bruce Lee", "Beyonce Knowles", "Emma Watson", "Selena Gomez", "Jeremy Lin"};
		JComboBox personGuess = new JComboBox(peopleNames);
		JComboBox weaponGuess = new JComboBox(weaponNames);
		JButton submit = new JButton("Submit");
		JButton cancel = new JButton("Cancel");	
		JDialog screen = new JDialog();
		screen.setTitle("Make A Guess!");
		screen.setLayout(new GridLayout(4,2));
		JTextField currentRoom = new JTextField();
		currentRoom.setText(r);
		currentRoom.setEditable(false);
		JLabel yourRoom = new JLabel("Your Room");
		JLabel person = new JLabel("Person");
		JLabel weapon = new JLabel("Weapon");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sugPerson= (String) personGuess.getSelectedItem();
				sugWeapon =  (String) weaponGuess.getSelectedItem();
				sugRoom = r;
				Solution s = new Solution();
				s.setPerson(sugPerson);
				s.setRoom(sugRoom);
				s.setWeapon(sugWeapon);
				if (handleSuggestion(s, 0) != null) {
				guessCard = handleSuggestion(s,0);
				splashScreen.showMessageDialog(null, "Your suggestion was disproved by the card " + guessCard.getCardName(), "", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					splashScreen.showMessageDialog(null, "No new clue", "", JOptionPane.INFORMATION_MESSAGE);
				}
				screen.dispose();
			}
		});
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				screen.dispose();
			}
		});
		screen.add(yourRoom);
		screen.add(currentRoom);
		screen.add(person);
		screen.add(personGuess);
		screen.add(weapon);
		screen.add(weaponGuess);
		screen.add(submit);
		screen.add(cancel);
		return screen;
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	// not necessary
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// not necessary
		
	}
	public String getSugRoom() {
		return sugRoom;
	}
	public String getSugWeapon() {
		return sugWeapon;
	}
	public String getSugPerson() {
		return sugPerson;
	}
	
	public Card getGuessCard() {
		return guessCard;
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// not necessary
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// not necessary
		
	}
}
