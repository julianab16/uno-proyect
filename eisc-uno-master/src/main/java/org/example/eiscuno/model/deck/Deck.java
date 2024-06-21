package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.alertbox.AlertBox;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.model.card.Card;

import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of Uno cards.
 */
public class Deck {
    private Stack<Card> deckOfCards;
    private Stack<Card> discardPile;
    public AlertBox alertBox = new AlertBox();
    /**
     * Constructs a new deck of Uno cards and initializes it.
     */
    public Deck() {
        deckOfCards = new Stack<>();
        discardPile = new Stack<>();
        initializeDeck();
    }

    /**
     * Initializes the deck with cards based on the EISCUnoEnum values.
     */
    private void initializeDeck() {
        for (EISCUnoEnum cardEnum : EISCUnoEnum.values()) {
            if (cardEnum.name().startsWith("GREEN_") ||
                    cardEnum.name().startsWith("YELLOW_") ||
                    cardEnum.name().startsWith("BLUE_") ||
                    cardEnum.name().startsWith("RED_") ||
                    cardEnum.name().startsWith("SKIP_") ||
                    cardEnum.name().startsWith("RESERVE_") ||
                    cardEnum.name().startsWith("TWO_WILD_DRAW_") ||
                    cardEnum.name().equals("FOUR_WILD_DRAW") ||
                    cardEnum.name().equals("WILD")) {

                String value = getCardValue(cardEnum.name());
                String color = getCardColor(cardEnum.name());

                if (value != null && color != null) {
                    Card card = new Card(cardEnum.getFilePath(), value, color);
                    deckOfCards.push(card);
                } else {
                    System.err.println("Invalid card configuration: " + cardEnum.name());
                }
            }
        }
        Collections.shuffle(deckOfCards);
    }

    /**
     * Gets the card value based on its name.
     *
     * @param name the name of the card
     * @return the value of the card as a string
     */
    private String getCardValue(String name) {
        if (name.matches(".*_[0-9]$")) {
            return name.substring(name.length() - 1);
        } else if(name.startsWith("SKIP")){
                return "SKIP";
        } else if (name.startsWith("RESERVE")) {
                return "RESERVE";
        } else if (name.startsWith("TWO_WILD_DRAW")) {
                return "TWO_WILD_DRAW";
        } else if (name.equals("FOUR_WILD_DRAW")) {
                return "FOUR_WILD_DRAW";
        } else if (name.equals("WILD")) {
                return "WILD";
        } else {
            return null;
        }
    }

    /**
     * Gets the card color based on its name.
     *
     * @param name the name of the card
     * @return the color of the card as a string
     */
    private String getCardColor(String name){
        if(name.startsWith("GREEN")) {
            return "GREEN";
        } else if (name.startsWith("YELLOW")) {
            return "YELLOW";
        } else if (name.startsWith("BLUE")) {
            return "BLUE";
        } else if (name.startsWith("RED")) {
            return "RED";
        } else if (name.endsWith("GREEN")){
            return "GREEN";
        } else if(name.endsWith("YELLOW")){
            return "YELLOW";
        } else if(name.endsWith("BLUE")){
            return "BLUE";
        } else if(name.endsWith("RED")){
            return "RED";
        } else if (name.equals("FOUR_WILD_DRAW") || name.equals("WILD")) {
            return "NONE";
        } else {
            return null;
        }
    }

    /**
     * Takes a card from the top of the deck.
     *
     * @return the card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card takeCard() {
        if (deckOfCards.isEmpty()) {
            alertBox.showMessage("Mazo","El mazo fue llenado nuevamente");
            if (discardPile.isEmpty()) {
                throw new IllegalStateException("No hay más cartas en el mazo.");
            } else {
                    refillDeckFromDiscardPile();
            }
        }
        return deckOfCards.pop();
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }

    /**
     * Refill the deck from the discard pile and shuffle it.
     */
    public void refillDeckFromDiscardPile() {
        if (discardPile.isEmpty()) {
            throw new IllegalStateException("No hay más cartas en el mazo ni en la pila de descarte.");
        }
        Card lastDiscardedCard = discardPile.pop();
        while (!discardPile.isEmpty()) {
            deckOfCards.push(discardPile.pop());
        }
        discardPile.push(lastDiscardedCard);
        Collections.shuffle(deckOfCards);
    }

    /**
     * Adds a card to the discard pile.
     *
     * @param card the card to be added to the discard pile
     */
    public void discardCard(Card card) {
        discardPile.push(card);
    }
}
