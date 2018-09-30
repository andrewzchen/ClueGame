package clueGame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.junit.BeforeClass;

public class GameControlGUI extends JFrame {
	private int dieRoll;
	private JTextField myTurn;
	private JTextField roll;
	private JTextField textGuess;
	private JTextField response;
	private JTextField guess_2;
	private JTextField hand;
	private JTextField people;
	String roomSuggestion;
	private JDialog accuse;
	private JPanel names;
	private JPanel rooms;
	private JPanel weapons;
	private JComboBox personGuess;
	private JComboBox roomGuess;
	private JComboBox weaponGuess;
	private static Board board;
	private JOptionPane detectiveNotesPane;
	private JOptionPane splashScreen;
	private JDialog detectiveNotes;
	private JFrame j;
	private int turnCounter = 0;
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
	}
	
	
	public GameControlGUI() throws FileNotFoundException {
		setUp();
		/**
		 * we roll the die here, so that when game starts there is a dieRoll we can use
		 */
		rollDie();
		board.setDieRoll(dieRoll);
		JMenuBar file = createFile();
		JPanel topPanel = createTopPanel();
		JPanel bottomPanel = createBottomPanel();
		JPanel boardPanel = Board.getInstance();
		JPanel myCardsPanel = createMyCardsPanel();
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2,1));
		mainPanel.add(topPanel);
		mainPanel.add(bottomPanel);
		add(boardPanel,BorderLayout.CENTER);
		add(myCardsPanel,BorderLayout.EAST);
		add(mainPanel,BorderLayout.SOUTH);
		setTitle("Clue Game Console");
		setSize(700, 850);
		setJMenuBar(file);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
		/**
		 * This method creates our detective Notes, which is a JDialog.
		 * @
		 */
private JDialog detectiveNotesDialog() {
	detectiveNotes = new JDialog();
	detectiveNotes.setTitle("Detective Notes");
	detectiveNotes.setLayout(new GridLayout(3, 2));
	detectiveNotesPeople();
	detectiveNotesRooms();
	detectiveNotesWeapons();
	detectiveNotesSetup();
	detectiveNotes.add(names);
	detectiveNotes.add(personGuess);
	detectiveNotes.add(rooms);
	detectiveNotes.add(roomGuess);
	detectiveNotes.add(weapons);
	detectiveNotes.add(weaponGuess);
	return detectiveNotes;
}
/**
 * This method sets up our 3 JComboBoxes for the Person Guess, Room Guess, and Weapon Guess.
 */
private void detectiveNotesSetup() {
	String[] peopleNames = {"21 Savage", "Lil Peep", "Bruce Lee", "Beyonce Knowles", "Emma Watson", "Selena Gomez", "Jeremy Lin"};
	String[] roomNames = {"TrapHouse", "Kitchen", "Ballroom", "Movie", "Pool", "Study", "Bedroom", "Living room", "Hall"};
	String[] weaponNames = {"Chainsaw", "Pyrex", "Shank", "Poison", "Razor", "Xanax", "Nunchuks"};
	personGuess = new JComboBox(peopleNames);
	roomGuess = new JComboBox(roomNames);
	weaponGuess = new JComboBox(weaponNames);
	personGuess.setBorder(new TitledBorder (new EtchedBorder(), "Person Guess"));
	roomGuess.setBorder(new TitledBorder (new EtchedBorder(), "Room Guess"));
	weaponGuess.setBorder(new TitledBorder (new EtchedBorder(), "Weapon Guess"));
}
/**
 * These next 3 methods set up our 3 panels, which contain the checkboxes for People, Rooms, and Weapons.
 */
private void detectiveNotesPeople() {
	names = new JPanel();
	names.setLayout(new GridLayout(4,2));
	names.setBorder(new TitledBorder (new EtchedBorder(), "People"));
	JCheckBox b1 = new JCheckBox("21 Savage");
	JCheckBox b2 = new JCheckBox("Lil Peep");
	JCheckBox b3 = new JCheckBox("Bruce Lee");
	JCheckBox b4 = new JCheckBox("Beyonce Knowles");
	JCheckBox b5 = new JCheckBox("Emma Watson");
	JCheckBox b6 = new JCheckBox("Selena Gomez");
	JCheckBox b7 = new JCheckBox("Jeremy Lin");
	names.add(b1);
	names.add(b2);
	names.add(b3);
	names.add(b4);
	names.add(b5);
	names.add(b6);
	names.add(b7);
}
private void detectiveNotesWeapons() {
	weapons = new JPanel();
	weapons.setLayout(new GridLayout(4,2));
	weapons.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
	JCheckBox b1 = new JCheckBox("Chainsaw");
	JCheckBox b2 = new JCheckBox("Pyrex");
	JCheckBox b3 = new JCheckBox("Shank");
	JCheckBox b4 = new JCheckBox("Poison");
	JCheckBox b5 = new JCheckBox("Razor");
	JCheckBox b6 = new JCheckBox("Xanax");
	JCheckBox b7 = new JCheckBox("Nunchuks");
	weapons.add(b1);
	weapons.add(b2);
	weapons.add(b3);
	weapons.add(b4);
	weapons.add(b5);
	weapons.add(b6);
	weapons.add(b7);
}
private void detectiveNotesRooms() {
	rooms = new JPanel();
	rooms.setLayout(new GridLayout(5,2));
	rooms.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
	JCheckBox b1 = new JCheckBox("TrapHouse");
	JCheckBox b2 = new JCheckBox("Kitchen");
	JCheckBox b3 = new JCheckBox("Ballroom");
	JCheckBox b4 = new JCheckBox("Pool");
	JCheckBox b5 = new JCheckBox("Movie");
	JCheckBox b6 = new JCheckBox("Study");
	JCheckBox b7 = new JCheckBox("Bedroom");
	JCheckBox b8 = new JCheckBox("Living room");
	JCheckBox b9 = new JCheckBox("Hall");
	rooms.add(b1);
	rooms.add(b2);
	rooms.add(b3);
	rooms.add(b4);
	rooms.add(b5);
	rooms.add(b6);
	rooms.add(b7);
	rooms.add(b8);
	rooms.add(b9);
}
/**
 * This method creates our menu bar. Under the code for i1, we set up an actionPerformed method that creates our detective notes if they click on Show Notes.
 * @return
 */
private JMenuBar createFile() {
	JMenu detectiveNotes;
	JMenuItem i1;
	JMenuItem i2;
	JMenuBar file = new JMenuBar();
	detectiveNotes = new JMenu("File");
	i1 = new JMenuItem(new AbstractAction("Show Notes") {
		public void actionPerformed (ActionEvent e) {
			JDialog notes = detectiveNotesDialog();
			notes.setSize(new Dimension(500, 500));
			notes.setVisible(true);
		}
	});
	i2 = new JMenuItem("Exit");
	detectiveNotes.add(i1);
	detectiveNotes.add(i2);
	file.add(detectiveNotes);
	return file;
}
private JPanel createTopPanel() {
	JPanel panel = new JPanel();
	panel.setLayout(new GridLayout(1,3));
	JLabel turnLabel = new JLabel("Whose turn?");
	myTurn = new JTextField(board.getPlayerList().get(0).getPlayerName(),5);
	myTurn.setEditable(false);
	panel.add(turnLabel);
	panel.add(myTurn);
	JButton nextPlayer = new JButton("Next player");
	JButton accusation = new JButton("Make an accusation");
	nextPlayer.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			/**
			 * this if statement makes sure in order to go to next player, human player's turn is done or if it is a computer player's turn
			 */
			if (board.getPlayerList().get(turnCounter).isHuman() == false || board.isPlayerTurnDone() == true) {
			int playerSize = board.getPlayerList().size();
			int r = board.getPlayerList().get(turnCounter).getRow();
			int c = board.getPlayerList().get(turnCounter).getColumn();
			board.calcTargets(r, c, dieRoll); 
			Set <BoardCell> t = board.getTargets();
			BoardCell b = new BoardCell(r, c);
			if (board.getPlayerList().get((turnCounter + 1) % playerSize).isHuman()) {
				rollDie();
				roll.setText(Integer.toString(dieRoll));
				board.setDieRoll(dieRoll);
				board.setHumPlayer(true);
				board.getPlayerList().get(turnCounter).makeMove(t, b);
				repaint();
			}
			else {
				//turnCounter++;
				if (board.getPlayerList().get(turnCounter).isAccuseTime()) {
					if(board.getPlayerList().get(turnCounter).getGuess().equals(board.getSolution())) {
						splashScreen.showMessageDialog(null, "Player: " + board.getPlayerList().get(turnCounter).getPlayerName() + " wins!!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						splashScreen.showMessageDialog(null, "Player: " + board.getPlayerList().get(turnCounter).getPlayerName() + " made and incorrect accusation:(" , "Gameover", JOptionPane.INFORMATION_MESSAGE );
					}
				}
			//	board.setHumPlayer(false);
				board.getPlayerList().get(turnCounter).makeMove(t, b);
				int newR = board.getPlayerList().get(turnCounter).getRow();
				int newC = board.getPlayerList().get(turnCounter).getColumn();
				Solution tempS = new Solution();
				if (board.getPlayerList().get(turnCounter).isHuman() == false) {
				if (board.getCellAt(newR, newC).isDoorway()) {
					for(Character l : board.getLegend().keySet()) {
						if (board.getCellAt(board.getPlayerList().get(turnCounter).getRow(), board.getPlayerList().get(turnCounter).getColumn()).getInitial() == l) {
							roomSuggestion = board.getLegend().get(l);
						}
					}
					Card responseCard = board.getPlayerList().get(turnCounter).createSuggestion(board, roomSuggestion, turnCounter);
					tempS = board.getPlayerList().get(turnCounter).getGuess();
					guess_2.setText(tempS.getPerson() + " " + tempS.getRoom() + " " + tempS.getWeapon());
					response.setText(responseCard.getCardName());

				}
				}
				rollDie();
				roll.setText(Integer.toString(dieRoll));
				board.setHumPlayer(false);
				repaint();
			}
			turnCounter = (turnCounter + 1) % board.getPlayerList().size();
			myTurn.setText(board.getPlayerList().get(turnCounter).getPlayerName());
			board.setPlayerTurnDone(false);
		}
			else {
			splashScreen.showMessageDialog(null, "Before going to the next player, finish your turn", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	});
	accusation.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (turnCounter != 0 || board.isPlayerTurnDone() == true) {
				splashScreen.showMessageDialog(null, "You can only make an accusation when it is the start of your turn", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JDialog accusePanel = makeAccusation();
				accusePanel.setSize(new Dimension(500, 500));
				accusePanel.setVisible(true);

			}
		}
	});
	panel.add(nextPlayer);
	panel.add(accusation);
	return panel;
}
private JDialog makeAccusation() {
	accuse = new JDialog();
	String[] weaponNames = {"Chainsaw", "Pyrex", "Shank", "Poison", "Razor", "Xanax", "Nunchuks"};
	String[] peopleNames = {"21 Savage", "Lil Peep", "Bruce Lee", "Beyonce Knowles", "Emma Watson", "Selena Gomez", "Jeremy Lin"};
	String[] roomNames = {"TrapHouse", "Kitchen", "Ballroom", "Movie", "Pool", "Study", "Bedroom", "Living room", "Hall"};
	JComboBox personGuess = new JComboBox(peopleNames);
	JComboBox weaponGuess = new JComboBox(weaponNames);
	JComboBox roomGuess = new JComboBox(roomNames);
	JButton submit = new JButton("Submit");
	JButton cancel = new JButton("Cancel");	
	accuse.setTitle("Make An Accusation");
	accuse.setLayout(new GridLayout(4,2));
	JLabel yourRoom = new JLabel("Room");
	JLabel person = new JLabel("Person");
	JLabel weapon = new JLabel("Weapon");
	submit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String personSol = (String) personGuess.getSelectedItem();
			String weaponSol =  (String) weaponGuess.getSelectedItem();
			String roomSol = (String) roomGuess.getSelectedItem();
			Solution s = new Solution();
			s.setPerson(personSol);
			s.setRoom(roomSol);
			s.setWeapon(weaponSol);
			Solution sol = new Solution();
			sol = board.getSolution();
			if (personSol.equals(sol.getPerson()) && weaponSol.equals(sol.getWeapon()) && roomSol.equals(sol.getRoom())) {
				splashScreen.showMessageDialog(null, "Your accusation is correct", "You win!", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				splashScreen.showMessageDialog(null, "Your accusation is incorrect", "Fail", JOptionPane.INFORMATION_MESSAGE);
			}
			accuse.dispose();
			board.setPlayerTurnDone(true);
			board.setHumPlayer(false);
			repaint();
		}
	});
	cancel.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			accuse.dispose();
//			System.out.println("Working");
		}
	});
	accuse.add(yourRoom);
	accuse.add(roomGuess);
	accuse.add(person);
	accuse.add(personGuess);
	accuse.add(weapon);
	accuse.add(weaponGuess);
	accuse.add(submit);
	accuse.add(cancel);
return accuse;
}
private void rollDie() {
	Random rand = new Random();
	dieRoll = rand.nextInt(5) + 1;
}
private JPanel createBottomPanel() {
	JPanel panel = new JPanel();
	JPanel die = new JPanel();
	JPanel guess = new JPanel();
	JPanel guessResult = new JPanel();
	panel.setLayout(new GridLayout(1,3));
	JLabel dieLabel = new JLabel("Roll");
	JLabel guessLabel = new JLabel("Guess");
	JLabel responseLabel = new JLabel("Response");
	roll = new JTextField(10);
	roll.setText(Integer.toString(dieRoll));
	roll.setEditable(false);
	guess_2 = new JTextField(15);
//	guess_2.setText();
	guess_2.setEditable(false);
	response = new JTextField(10);
	response.setText("No guess yet");
	response.setEditable(false);
	die.setBorder(new TitledBorder (new EtchedBorder(), "Die"));
	guess.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
	guessResult.setBorder(new TitledBorder( new EtchedBorder(), "Guess Result"));
	die.add(dieLabel);
	die.add(roll);
	guess.add(guessLabel);
	guess.add(guess_2);
	guessResult.add(responseLabel);
	guessResult.add(response);
	panel.add(die);
	panel.add(guess);
	panel.add(guessResult);
	return panel;
}

private JPanel createMyCardsPanel() {
	JPanel panel = new JPanel();
	JPanel peoplePanel = new JPanel();
	peoplePanel.setLayout(new GridLayout(3,1));
	JPanel roomsPanel = new JPanel();
	roomsPanel.setLayout(new GridLayout(3,1));
	JPanel weaponsPanel = new JPanel();
	weaponsPanel.setLayout(new GridLayout(3,1));
	panel.setLayout(new GridLayout(3,0));
	if(board.getTempName().size() >= 1) {
		for(int i = 0; i < board.getTempName().size(); i++) {
			JTextField nextName = new JTextField(board.getTempName().get(i), 10);
			nextName.setEditable(false);
			peoplePanel.add(nextName);
		}
	}
	else if(board.getTempName().size() == 0) {
		JTextField noName = new JTextField(10);
		noName.setEditable(false);
		peoplePanel.add(noName);
	}
	if(board.getTempRoom().size() >= 1) {
		for(int i = 0; i < board.getTempRoom().size(); i++) {
			JTextField nextRoom = new JTextField(board.getTempRoom().get(i),10);
			nextRoom.setEditable(false);
			roomsPanel.add(nextRoom);
		}
	}
	else if(board.getTempRoom().size() == 0) {
		JTextField noRoom = new JTextField(10);
		noRoom.setEditable(false);
		roomsPanel.add(noRoom);
	}
	if(board.getTempWeapon().size() >= 1) {
		for(int i = 0; i < board.getTempWeapon().size(); i++) {
			JTextField nextWeapon = new JTextField(board.getTempWeapon().get(i),10);
			nextWeapon.setEditable(false);
			weaponsPanel.add(nextWeapon);
		}
	}
	else if(board.getTempWeapon().size() == 0) {
		JTextField noWeapon = new JTextField(10);
		noWeapon.setEditable(false);
		weaponsPanel.add(noWeapon);
	}
	panel.setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
	peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
	roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
	weaponsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
	panel.add(peoplePanel);	
	panel.add(roomsPanel);
	panel.add(weaponsPanel);
	return panel;
}
public int getDieRoll() {
	return dieRoll;
}
public static void main (String[] args) throws FileNotFoundException {
	GameControlGUI g = new GameControlGUI();
	}
}
