package clueGame;

public class Card {
	private String cardName;
	private CardType c;
	public boolean equals() {
		return false;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public void setCardType(CardType cardT) {
		c = cardT;
	}
	public CardType getCardType() {
		return c;
	}
	@Override
	public String toString() {
		return "Card [cardName=" + cardName + ", c=" + c + "]";
	}
}
