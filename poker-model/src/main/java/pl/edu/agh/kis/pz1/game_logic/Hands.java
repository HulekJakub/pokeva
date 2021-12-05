package pl.edu.agh.kis.pz1.game_logic;


import pl.edu.agh.kis.pz1.game_assets.Card;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Hands {
    public enum Hand{
        ROYALFLUSH, STRAIGHTFLUSH, FOURSAMERANKS, FULLHOUSE, FLUSH, STRAIGHT, THREESAMERANKS, TWOPAIRS, PAIR, NOTHING}

    public static Hand handType(Player player){
        if (hasRoyalFlush(player))  return Hand.ROYALFLUSH;
        if (hasStraightFlush(player))  return Hand.STRAIGHTFLUSH;
        if (hasFourSameRanks(player))  return Hand.FOURSAMERANKS;
        if (hasFullHouse(player))  return Hand.FULLHOUSE;
        if (hasFlush(player))  return Hand.FLUSH;
        if (hasStraight(player))  return Hand.STRAIGHT;
        if (hasThreeSameRanks(player))  return Hand.THREESAMERANKS;
        if (hasTwoPairs(player))  return Hand.TWOPAIRS;
        if (hasPair(player))  return Hand.PAIR;
        return  Hand.NOTHING;
    }

    private static boolean hasRoyalFlush(Player player){
        return hasStraightFlush(player) && player.getHand().get(0).getSuit().equals(Card.Suit.HEARTS);
    }

    private static boolean hasStraightFlush(Player player){
        return hasFlush(player) && hasStraight(player);
    }

    private static boolean hasFourSameRanks(Player player){
        Map<Card.Rank, Integer> rankMap = getRankMap(player);
        for (Integer numberOfCards: rankMap.values()){
            if(numberOfCards == 4){
                return true;
            }
        }
        return false;
    }

    private static boolean hasFullHouse(Player player){
        Map<Card.Rank, Integer> rankMap = getRankMap(player);
        if(rankMap.values().size() != 2){
            return false;
        }
        for (Integer numberOfCards: rankMap.values()){
            if(numberOfCards == 3 || numberOfCards == 2){
                return true;
            }
        }
        return false;
    }

    private static boolean hasFlush(Player player){
        Map<Card.Suit, Integer> suitMap = getSuitMap(player);
        return suitMap.values().size() == 1;
    }

    private static boolean hasStraight(Player player){
        List<Card> hand = player.getHand();
        int maxOrdinal = hand.get(0).getRank().ordinal();
        int minOrdinal = maxOrdinal;
        for (int i = 1; i < hand.size(); i++) {
            int currentOrdinal = hand.get(i).getRank().ordinal();
            if(currentOrdinal > maxOrdinal){
                maxOrdinal = currentOrdinal;
            }
            if(currentOrdinal < minOrdinal){
                minOrdinal = currentOrdinal;
            }
        }
        return maxOrdinal-minOrdinal == 4 && getRankMap(player).values().size() == 5;
    }

    private static boolean hasThreeSameRanks(Player player){
        Map<Card.Rank, Integer> rankMap = getRankMap(player);
        for (Integer numberOfCards: rankMap.values()){
            if(numberOfCards == 3){
                return true;
            }
        }
        return false;
    }

    private static boolean hasTwoPairs(Player player){
        Map<Card.Rank, Integer> rankMap = getRankMap(player);
        int numberOfPairs = 0;
        for (Integer numberOfCards: rankMap.values()){
            if(numberOfCards == 2){
                numberOfPairs ++;
            }
        }
        return numberOfPairs == 2;

    }

    private static boolean hasPair(Player player){
        Map<Card.Rank, Integer> rankMap = getRankMap(player);
        for (Integer numberOfCards: rankMap.values()){
            if(numberOfCards == 2){
                return true;
            }
        }
        return false;
    }

    private static Map<Card.Rank, Integer> getRankMap(Player player){
        Map<Card.Rank, Integer> rankMap = new EnumMap<>(Card.Rank.class);
        for (Card card: player.getHand()){
            Card.Rank rank = card.getRank();
            rankMap.putIfAbsent(rank, 0);
            rankMap.put(rank, rankMap.get(rank) + 1);
        }
        return rankMap;
    }

    private static Map<Card.Suit, Integer> getSuitMap(Player player){
        Map<Card.Suit, Integer> suitMap = new EnumMap<>(Card.Suit.class);
        for (Card card: player.getHand()){
            Card.Suit suit = card.getSuit();
            suitMap.putIfAbsent(suit, 0);
            suitMap.put(suit, suitMap.get(suit) + 1);
        }
        return suitMap;
    }

    private Hands(){}

}
