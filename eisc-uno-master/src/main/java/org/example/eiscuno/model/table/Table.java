package org.example.eiscuno.model.table;

import org.example.eiscuno.model.alertbox.AlertBox;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;

import java.util.ArrayList;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table {
    private ArrayList<Card> cardsTable;
    public AlertBox alertBox = new AlertBox();
    private Deck deck;

    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table(){
        this.cardsTable = new ArrayList<>();
        this.deck = new Deck();
    }

    /**
     * Adds a card to the table.
     *
     * @param card The card to be added to the table.
     */
    public void addCardOnTheTable(Card card){
        this.cardsTable.add(card);
    }

    /**
     * Retrieves the current card on the table.
     *
     * @return The card currently on the table.
     * @throws IndexOutOfBoundsException if there are no cards on the table.
     */
    public Card getCurrentCardOnTheTable() throws IndexOutOfBoundsException {
        if (cardsTable.isEmpty()) {
            alertBox.showMessageError("Error", "No puedes tomar una carta si no has empezado el juego ❌");
            throw new IndexOutOfBoundsException("There are no cards on the table.");
        }
        return this.cardsTable.get(this.cardsTable.size()-1);
    }

    /**
     * Retrieves the current card on the table.
     *
     * @return The card currently on the table, or null if the table is empty.
     */
    public Card getTopCard() {
        if (!cardsTable.isEmpty()) {
            return cardsTable.get(cardsTable.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * Adds a card to the end of the cardsTable.
     *
     * @param card The card to be added to the table.
     */
    public void setStartCard(Card card) {
        this.cardsTable.add(card);
    }

    /**
     * Draws cards from the deck until it finds a valid card (not a special card)
     * and places it on the table.
     *
     * @return The first valid card drawn and placed on the table.
     */
    public Card firstCard() {
        while (true) {
            Card card = deck.takeCard();
            if (!card.isWildCard() && !card.isReverseCard() && !card.isSkipCard() && !card.isTwoWildCard()) {
                setStartCard(card);
                return card;
            }
        }
    }
}

