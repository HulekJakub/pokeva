package pl.edu.agh.kis.pz1.game_assets;

import org.junit.Before;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Test class
 */
public class DeckTest {
    private Deck deck;

    @Before
    public void init(){
        deck = new Deck(true);
    }

    @Test
    public void testPrintDeck(){
        String toTest = deck.toString();
        System.out.println(toTest);
        assertTrue(toTest.matches("([A-Z]* [A-Z]*\n){52}"));
    }

    @Test
    public void testShuffle(){
        Deck deck = new Deck(true);
        Deck shuffledDeck = new Deck(true);
        shuffledDeck.shuffle();
        int samePosition = 0;
        for (int i = 0; i < deck.getCards().size(); i++){
            if(deck.getCards().get(i) == shuffledDeck.getCards().get(i)){
                samePosition++;
            }
        }
        assertTrue(samePosition <=20);
    }

    @Test
    public void popFirst() {
        //TODO
        assertTrue(true);
    }

    @Test
    public void putAtLast() {
        //TODO
        assertTrue(true);
    }


    @Test
    public void generateDeck() {
        Set<Card> foundCards = new HashSet<>(deck.getCards());
        assertEquals(13*4, foundCards.size());
    }

    @Test
    public void resetDeck() {
        //TODO
        assertTrue(true);
    }
}
