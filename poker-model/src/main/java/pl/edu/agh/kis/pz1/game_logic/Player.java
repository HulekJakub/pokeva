package pl.edu.agh.kis.pz1.game_logic;

import pl.edu.agh.kis.pz1.game_assets.Card;
import pl.edu.agh.kis.pz1.game_exceptions.NotEnoughMoneyException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold and manage information about players cards and money
 */

public class Player {
    private List<Card> hand = new ArrayList<>();
    private final String playerId;
    private int playersMoney = 0;
    private int betMoney = 0;
    private boolean isEliminated = false;
    private boolean allIn = false;
    private boolean passed = false;

    public Player(String id){
        playerId = id;
    }



    /**
     * Sets player money to given value
     * @param playersMoney new playersMoney value
     */
    public void setPlayersMoney(int playersMoney) {
        this.playersMoney = playersMoney;
    }

    /**
     * Changes player money by given value
     * @param bet int
     */
    public void betMoney(int bet) throws NotEnoughMoneyException {
        if(bet >= playersMoney){
            throw new NotEnoughMoneyException("You cannot bet more than you own.\nUse ALLIN or PASS move instead.");
        }
        betMoney += bet;
        playersMoney -= bet;
    }

    public void takeMoney(int toTake){
        this.playersMoney += toTake;
    }

    /**
     * Adds card to players hand
     * @param card new Card
     */
    public void addCard(Card card){
        hand.add(card);
    }

    /**
     * Removes card from players hand if it exists there
     * @param card Card to remove
     * @return true if card existed in the hand prior to its removal
     */
    public boolean removeCard(Card card)
    {
        if(hand.contains(card)){
            hand.remove(card);
            return true;
        }
        return false;
    }

    /**
     * Clears player's hand
     */
    public void reset(){
        clearHand();
        allIn = false;
        passed = false;
        betMoney = 0;
    }

    private void clearHand(){
        hand.clear();
    }

    public String getPlayerId() {
        return playerId;
    }

    public int getPlayersMoney() {
        return playersMoney;
    }

    public int getBetMoney() {
        return betMoney;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public boolean isPassed() {
        return passed;
    }

    public void pass(){
        this.passed = true;
    }

    public void setEliminated(boolean eliminated) {
        isEliminated = eliminated;
    }

    public boolean isEliminated() {
        return isEliminated;
    }
}
