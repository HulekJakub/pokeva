package pl.edu.agh.kis.pz1.gameAsstes;

import org.junit.Before;
import pl.edu.agh.kis.pz1.gameAssets.Card;
import pl.edu.agh.kis.pz1.gameAssets.Deck;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeckTest {
    private Deck deck;

    @Before
    public void init(){
        deck = new Deck(true);
    }

    @Test
    public void testGenerateDeck(){
        Set<Card> foundCards = new HashSet<>(deck.getCards());
        assertEquals(13*4, foundCards.size());
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

}
