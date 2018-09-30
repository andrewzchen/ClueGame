package clueGame;

import java.util.Set;

public class HumanPlayer extends Player{
	public boolean isHuman() {
		return true;
	}

	@Override
	public void makeMove(Set<BoardCell> targets, BoardCell startCell) {
		for(BoardCell b : targets) {
			
		}
	}
	@Override
	public Card createSuggestion(Board board, String r, int index) {
		Card c = new Card();
		return c;
		// do nothing, Human Player shouldn't be using createSuggestion
	}
	
	@Override
	public void lastSuggestion(String p, String r, String w) {
		
	}
	public Solution getGuess() {
		Solution s = new Solution();
		return s;
	}

	@Override
	public void fillLists(Board board) {
		// do nothing, Human Player shouldn't be using fillLists
		
	}

}
