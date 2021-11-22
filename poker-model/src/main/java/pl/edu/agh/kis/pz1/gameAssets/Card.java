package pl.edu.agh.kis.pz1.gameAssets;

/**
 * Class implementing a standard card
 * @author jakub
 */

public class Card {
    public enum Suit{HEARTS, DIAMONDS, SPADES, CLUBS}
    public enum Rank {ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING}

    private final Suit suit;
    private final Rank rank;

    /**
     * Generates a card from given characteristics
     * @param suit one of 4 suits
     * @param rank one of 13 ranks
     */
    public Card(Suit suit, Rank rank){
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Returns card info as string
     */
    public String toString(){
        return rank.toString() + " " + suit.toString();
    }

    /**
     * Standard equals method
     * @param anotherCard card to compare to
     * @return boolean
     */
    @Override
    public boolean equals(Object anotherCard) {
        if (this == anotherCard) return true;
        if (anotherCard == null || getClass() != anotherCard.getClass()) return false;
        Card card = (Card) anotherCard;
        return suit == card.suit && rank == card.rank;
    }

    public pl.edu.agh.kis.pz1.gameAssets.Card.Rank getRank() {
        return rank;
    }

    public pl.edu.agh.kis.pz1.gameAssets.Card.Suit getSuit() {
        return suit;
    }
}
