package pl.edu.agh.kis.pz1.game_assets;

import java.util.Objects;

/**
 * Class implementing a standard card
 * @author jakub
 */

public class Card {
    /**
     * Enum data class to make card handling easier and faster
     */
    public enum Suit{HEARTS, DIAMONDS, SPADES, CLUBS}

    /**
     * Enum data class to make card handling easier and faster
     */
    public enum Rank {ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO}

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
        return  suit.toString() + " " + rank.toString();
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

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    public pl.edu.agh.kis.pz1.game_assets.Card.Rank getRank() {
        return rank;
    }

    public pl.edu.agh.kis.pz1.game_assets.Card.Suit getSuit() {
        return suit;
    }
}
