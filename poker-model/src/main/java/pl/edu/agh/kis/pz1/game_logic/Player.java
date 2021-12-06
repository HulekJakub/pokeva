package pl.edu.agh.kis.pz1.game_logic;

import pl.edu.agh.kis.pz1.game_assets.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold and manage information about players cards and money
 */

public class Player {
    private final List<Card> hand = new ArrayList<>();
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


    public void betMoney(int bet){
        if(bet == playersMoney){
            allIn = true;
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
     * @param cardIndex Card to remove
     * @return true if card existed in the hand prior to its removal
     */
    public boolean removeCard(int cardIndex)
    {
        return hand.remove(cardIndex) != null;
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

    /**
     * Checks players hands to check who wins
     * @param otherPlayer other player
     * @return true if player wins with otherPlayer, otherwise false
     */
    public boolean winsWith(Player otherPlayer){
        int handComparison = Hands.handType(this).compareTo(Hands.handType(otherPlayer));
        if(handComparison < 0){
            return true;
        } else if(handComparison > 0){
            return false;
        }

        for (Card.Rank rank: Card.Rank.values()){
            if(hasCardWithRank(rank) != otherPlayer.hasCardWithRank(rank)){
                return hasCardWithRank(rank);
            }
        }

        for (Card.Rank rank: Card.Rank.values()){
            for(Card.Suit suit: Card.Suit.values()){
                Card card = new Card(suit, rank);
                if(hand.contains(card) != otherPlayer.getHand().contains(card)){
                    return hand.contains(card);
                }
            }
        }
        return false;

    }

    private boolean hasCardWithRank(Card.Rank rank){
        for(Card card: hand){
            if(card.getRank().equals(rank)){
                return true;
            }
        }
        return false;
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

    /**
     * Passes player
     * @return Info to print
     */
    public String pass(){
        this.passed = true;
        return playerId + " has passed.";
    }

    public void setEliminated(boolean eliminated) {
        isEliminated = eliminated;
    }

    public boolean isEliminated() {
        return isEliminated;
    }

    public List<Card> getHand() {
        return hand;
    }
}
