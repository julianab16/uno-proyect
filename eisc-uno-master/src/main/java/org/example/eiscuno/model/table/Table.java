package org.example.eiscuno.model.table;

import org.example.eiscuno.model.alertbox.AlertBox;
import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table {
    private ArrayList<Card> cardsTable;
    private String currentColor;
    public AlertBox alertBox = new AlertBox();

    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table(){
        this.cardsTable = new ArrayList<>();
        this.currentColor = null; // Inicialmente no hay color seleccionado
    }

    /**
     * Adds a card to the table.
     *
     * @param card The card to be added to the table.
     */
    public void addCardOnTheTable(Card card){
        this.cardsTable.add(card);
        if (!card.isWildCard()) {
            this.currentColor = card.getColor(); // Actualizar el color actual si no es Wild
        }
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


    public Card getTopCard() {
        if (!cardsTable.isEmpty()) {
            return cardsTable.get(cardsTable.size() - 1);
        } else {
            return null;
        }
    }
}
