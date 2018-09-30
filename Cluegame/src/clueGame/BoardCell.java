/**
 * Authors:
 * Andrew Chen
 * Jordyn McGrath
 */
package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JButton;
import javax.swing.JPanel;

public class BoardCell {
private int row;
private int col;
private char Initial;
private DoorDirection d;
private static final int WIDTH = 30;
private static final int HEIGHT = 30;
private static final int MEDIUM_OVERHANG = 2;
private static final int LARGE_OVERHANG = 3;
private static final int SMALL_OVERHANG = 1;
/**
 * constructor for Boardcell with paramaters for the rows and cols
 * @param row
 * @param col
 */
public BoardCell(int row, int col) {
	this.row = row;
	this.col = col;
}
/**
 * method to draw the cells for the walkways and the rooms
 * @param g
 */
public void draw(Graphics2D g){
		if(isWalkway()) {
			g.setColor(Color.YELLOW);
			g.fillRect(col*WIDTH, row*HEIGHT, WIDTH, HEIGHT);
			g.setColor(Color.BLACK);
			g.drawRect(col*WIDTH, row*HEIGHT, WIDTH, HEIGHT);
			}
		else{
			g.setColor(Color.GRAY);
			g.fillRect(col*WIDTH, row*HEIGHT, WIDTH, HEIGHT);	
			}
}
/**
 * This method draws the room names based on the relative center of the room on the board
 * This method also indicates the door direction for the rooms
 * @param g
 */
public void drawRoomNames(Graphics2D g) {
	if(Initial == 'T'  && row ==2 && col == 0 ) {
		g.setColor(Color.BLUE);
		g.drawString("TRAPHOUSE", col*HEIGHT, row*WIDTH);
	}
	else if(Initial == 'K'  && row == 9 && col == 0 ) {
		g.setColor(Color.BLUE);
		g.drawString("KITCHEN", col*HEIGHT, row*WIDTH);
	}
	else if(Initial == 'H'  && row == 16 && col == 2 ) {
		g.setColor(Color.BLUE);
		g.drawString("HALL", col*HEIGHT, row*WIDTH);
	}
	else if(Initial == 'L'  && row == 15 && col == 9 ) {
		g.setColor(Color.BLUE);
		g.drawString("LIVING ROOM", col*HEIGHT, row*WIDTH);
	}
	else if(Initial == 'B'  && row == 9 && col == 9 ) {
		g.setColor(Color.BLUE);
		g.drawString("BALLROOM", col*HEIGHT, row*WIDTH);
	}
	else if(Initial == 'Y'  && row ==5 && col == 14) {
		g.setColor(Color.BLUE);
		g.drawString("BEDROOM", col*HEIGHT, row*WIDTH);
	}
	else if(Initial == 'M'  && row == 10 && col == 15 ) {
		g.setColor(Color.BLUE);
		g.drawString("MOVIE", col*HEIGHT, row*WIDTH);
	}
	else if(Initial == 'P'  && row == 15 && col == 15 ) {
		g.setColor(Color.BLUE);
		g.drawString("POOL", col*HEIGHT, row*WIDTH);
	}
	else if(Initial == 'S'  && row == 2 && col == 10 ) {
		g.setColor(Color.BLUE);
		g.drawString("STUDY", col*HEIGHT, row*WIDTH);
	}
	else if(isDoorway()) {
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.BLUE);
		if(getDoorDirection() == DoorDirection.DOWN) {
		g.draw(new Line2D.Float(col*WIDTH+MEDIUM_OVERHANG, row*HEIGHT + HEIGHT-LARGE_OVERHANG, WIDTH*col+WIDTH -LARGE_OVERHANG, row*HEIGHT+HEIGHT-LARGE_OVERHANG));
			}
		else if(getDoorDirection() == DoorDirection.UP) {
			g.draw(new Line2D.Float(col*WIDTH+MEDIUM_OVERHANG, row*HEIGHT+SMALL_OVERHANG, WIDTH*col+WIDTH -LARGE_OVERHANG, row*HEIGHT+SMALL_OVERHANG));
		}
		else if(getDoorDirection() == DoorDirection.RIGHT) {
			g.draw(new Line2D.Float(col*WIDTH+WIDTH-MEDIUM_OVERHANG, row*HEIGHT, WIDTH*col+WIDTH-MEDIUM_OVERHANG, row*HEIGHT+HEIGHT));
		}
		else if(getDoorDirection() == DoorDirection.LEFT) {
			g.draw(new Line2D.Float(col*WIDTH+MEDIUM_OVERHANG, row*HEIGHT+SMALL_OVERHANG, WIDTH*col+MEDIUM_OVERHANG, row*HEIGHT+HEIGHT-SMALL_OVERHANG));
		}
	}
}
/*
 * method to color the possible target squares red
 */
public void colorTargets(Graphics2D g) {
	g.setColor(Color.RED);
	g.fillRect(col*WIDTH, row*HEIGHT, WIDTH, HEIGHT);
	g.setColor(Color.BLACK);
	g.drawRect(col*WIDTH, row*HEIGHT, WIDTH, HEIGHT);
	if (isDoorway()) {
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.BLUE);
		if(getDoorDirection() == DoorDirection.DOWN) {
		g.draw(new Line2D.Float(col*WIDTH+MEDIUM_OVERHANG, row*HEIGHT + HEIGHT-LARGE_OVERHANG, WIDTH*col+WIDTH -LARGE_OVERHANG, row*HEIGHT+HEIGHT-LARGE_OVERHANG));
			}
		else if(getDoorDirection() == DoorDirection.UP) {
			g.draw(new Line2D.Float(col*WIDTH+MEDIUM_OVERHANG, row*HEIGHT+SMALL_OVERHANG, WIDTH*col+WIDTH -LARGE_OVERHANG, row*HEIGHT+SMALL_OVERHANG));
		}
		else if(getDoorDirection() == DoorDirection.RIGHT) {
			g.draw(new Line2D.Float(col*WIDTH+WIDTH-MEDIUM_OVERHANG, row*HEIGHT, WIDTH*col+WIDTH-MEDIUM_OVERHANG, row*HEIGHT+HEIGHT));
		}
		else if(getDoorDirection() == DoorDirection.LEFT) {
			g.draw(new Line2D.Float(col*WIDTH+MEDIUM_OVERHANG, row*HEIGHT+SMALL_OVERHANG, WIDTH*col+MEDIUM_OVERHANG, row*HEIGHT+HEIGHT-SMALL_OVERHANG));
		}
	}
}
/**
 * set the door directions
 * @param d
 */
public void setDoorDirection(DoorDirection d) {
	this.d = d;
}

public int getRow() {
	return row;
}

public void setRow(int row) {
	this.row = row;
}

public int getCol() {
	return col;
}
public void setCol(int col) {
	this.col = col;
}
public boolean isWalkway() {
	if (Initial == 'W') {
		return true;
	}
	else {
	return false;
	}
}
public boolean isRoom() {
	if (Initial != 'W' && Initial != 'X') {
		return true;
	}
	else {
	return false;
	}
}
/**
 * determines if the boardcell is a doorway or not
 * @return
 */
public boolean isDoorway() {	
	if (d == DoorDirection.NONE) {
	return false;
	}
	else {
		return true;
	}
}

@Override
public String toString() {
	return "BoardCell [row=" + row + ", col=" + col + ", Initial=" + Initial + ", d=" + d + "]";
}
public Enum<DoorDirection> getDoorDirection() {
	return d;
}
public char getInitial() {
	return Initial;
}
public void setInitial(char initial) {
	Initial = initial;
}
}
