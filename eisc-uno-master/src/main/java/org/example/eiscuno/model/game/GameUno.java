package org.example.eiscuno.model.game;

import javafx.scene.control.Alert;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;

/**
 * Represents a game of Uno.
 * This class manages the game logic and interactions between players, deck, and the table.
 */
public class GameUno implements IGameUno {
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;

    /**
     * Constructs a new GameUno instance.
     *
     * @param humanPlayer   The human player participating in the game.
     * @param machinePlayer The machine player participating in the game.
     * @param deck          The deck of cards used in the game.
     * @param table         The table where cards are placed during the game.
     */
    public GameUno(Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
    }

    /**
     * Starts the Uno game by distributing cards to players.
     * The human player and the machine player each receive 10 cards from the deck.
     */
    @Override
    public void startGame() {
        for (int i = 0; i < 5; i++) {
            humanPlayer.addCard(this.deck.takeCard());
            machinePlayer.addCard(this.deck.takeCard());
        }
    }

    /**
     * Allows a player to draw a specified number of cards from the deck.
     *
     * @param player        The player who will draw cards.
     * @param numberOfCards The number of cards to draw.
     */
    @Override
    public void eatCard(Player player, int numberOfCards) {
        for (int i = 0; i < 1; i++) {
            player.addCard(this.deck.takeCard());
        }
    }
    /**
     * Places a card on the table during the game.
     *
     * @param card The card to be placed on the table.
     */
    @Override
    public void playCard(Card card) {
        // Determinar el tipo de jugador que está jugando la carta
        String playerType = humanPlayer.getTypePlayer();
        String playerMachime = machinePlayer.getTypePlayer();

        // Agregar la carta a la mesa
        this.table.addCardOnTheTable(card);

        // Realizar acciones posteriores al movimiento
        postMoveActions(playerType);
        postMoveActions(playerMachime);

        // Imprimir las cartas del jugador máquina
        System.out.println(" Cartas de la máquina: ");
        machinePlayer.printCardsPlayer();

        // Imprimir las cartas del jugador humano
        System.out.println(" Tus cartas: ");
        humanPlayer.printCardsPlayer();
        System.out.println(" ");
    }

    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     *
     * @param playerWhoSang The player who shouted "Uno".
     */
    @Override
    public void haveSingOne(String playerWhoSang) {
        if (playerWhoSang.equals("HUMAN_PLAYER")) {
            machinePlayer.addCard(this.deck.takeCard());
            System.out.println("La maquina comio una carta");
        } else {
            humanPlayer.addCard(this.deck.takeCard());
            System.out.println("El jugador comio una carta");
            System.out.println(" Tus cartas: ");
            humanPlayer.printCardsPlayer();
        }
    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow The initial position of the cards to show.
     * @return An array of cards visible to the human player.
     */
    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        int totalCards = this.humanPlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.humanPlayer.getCard(posInitCardToShow + i);
        }
        return cards;
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the deck is empty, indicating the game is over; otherwise, false.
     */
    @Override
    public Boolean isGameOver() {
        GameUnoStage.deleteInstance();
        return null;
    }

    /**
     * Verifica si una carta puede ser jugada según las reglas del juego.
     *
     * @param card La carta que se quiere jugar.
     * @return true si la carta puede ser jugada, false de lo contrario.
     */
    public boolean canPlayCard(Card card) {
        Card topCard = table.getTopCard();
        return card.getColor().equals(topCard.getColor()) ||
                card.getValue().equals(topCard.getValue()) ||
                card.isWildCard();
    }

    /**
     * Verifica si un jugador ha ganado después de jugar una carta.
     *
     * @param player El jugador cuya mano se verificará.
     * @return true si el jugador ha ganado, false de lo contrario.
     */
    private boolean checkWin(Player player) {
        return player.getCardsPlayer().isEmpty();
    }

    /**
     * Verifica si un jugador ha ganado después de jugar una carta.
     *
     * @param playerType El tipo de jugador que realizó el movimiento.
     */
    private void postMoveActions(String playerType) {
        if (playerType.equals(humanPlayer.getTypePlayer())) {
            if (humanPlayer.getCardsPlayer().isEmpty()) {
                showAlert("GANADOR", "Ha ganado!");
                System.out.println("¡Has ganado!");
                isGameOver();
            }
        } else if (playerType.equals(machinePlayer.getTypePlayer())) {
            if (machinePlayer.getCardsPlayer().isEmpty()) {
                showAlert("GAME OVER", "La maquina ha ganado!");
                System.out.println("La máquina ha ganado!");
                isGameOver();
            }
        }
    }

    /**
     * Muestra una alerta con el título y el mensaje especificados.
     *
     * @param title   El título de la alerta.
     * @param message El mensaje de la alerta.
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
