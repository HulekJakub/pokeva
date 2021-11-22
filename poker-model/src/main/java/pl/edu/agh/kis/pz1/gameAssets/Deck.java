package pl.edu.agh.kis.pz1.gameAssets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class deck implements methods that make using a 52 card deck and easier task
 * @author jakub
 */

public class Deck {
    List<Card> cards = new ArrayList<>();

    /**
     * Deck class constructor
     */
    public Deck(){
        generateDeck();
    }

    /**
     * @param  fill should the constructor fill it
     * Deck class constructor
     */
    public Deck(boolean fill){
        if(fill){
            generateDeck();
        }
    }

    /**
     * Returns a Card from the top of the deck
     * If deck is empty returns null
     */
    public Card popFirst()  {
        if(cards.size() == 0){
            return null;
        }
        return cards.get(0);
    }

    /**
     * Returns list of cards
     * @param card Card to put at the end of the deck
     */
    public boolean putAtLast(Card card){
        if(card != null && !cards.contains(card)){
            cards.add(cards.size(), card);
            return true;
        }
        return false;
    }

    /**
     * Returns list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Shuffles the deck
     */
    public void shuffle(){
        Collections.shuffle(this.cards);
    }

    /**
     * Returns string of deck's cards in order in which they are in it
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        for (Card card:cards) {
            s.append(card.toString());
            s.append('\n');
        }
        return s.toString();
    }

    /**
     * Fills Deck class with 52 different Cards, ordered
     */
    public void generateDeck(){
        for (Card.Rank rank: Card.Rank.values()) {
            for(Card.Suit suit: Card.Suit.values()){
                Card a = new Card(suit,rank);
                cards.add(a);
            }
        }
    }

    /**
     * Resets a deck so it contains 52 ordered cards
     */
    public void resetDeck(){
        cards.clear();
        generateDeck();
    }
}
