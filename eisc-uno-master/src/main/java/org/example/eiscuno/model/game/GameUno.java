
package org.example.eiscuno.model.game;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.alertbox.AlertBox;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.ColorSelectionDialog;
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
    public AlertBox alertBox = new AlertBox();
    private ThreadPlayMachine threadPlayMachine;
    private GameUnoController gameUnoController;

    /**
     * Constructs a new GameUno instance.
     *
     * @param humanPlayer   The human player participating in the game.
     * @param machinePlayer The machine player participating in the game.
     * @param deck          The deck of cards used in the game.
     * @param table         The table where cards are placed during the game.
     */
    public GameUno(Player humanPlayer, Player machinePlayer, Deck deck, Table table, ThreadPlayMachine threadPlayMachine,GameUnoController gameUnoController) {
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
        this.threadPlayMachine = threadPlayMachine;
        this.gameUnoController = gameUnoController;
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
            deck.discardCard(this.deck.takeCard());
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
        for (int i = 0; i < numberOfCards; i++) {
            player.addCard(this.deck.takeCard());
            deck.discardCard(this.deck.takeCard());
        }
    }

    /**
     * Places a card on the table during the game.
     *
     * @param card The card to be placed on the table.
     */
    @Override
    public void playCard(Card card) {
        deck.discardCard(card);
        String playerType = humanPlayer.getTypePlayer();
        String playerMachine = machinePlayer.getTypePlayer();
        this.table.addCardOnTheTable(card);
        postMoveActions(playerType);
        postMoveActions(playerMachine);
    }

    /**
     * Opens a color selection dialog for the specified card.
     * When the dialog is closed, the selected color is set for the card.
     *
     * @param card the card for which to select a color
     */
    private void openColorSelectionDialog(Card card) {
        ColorSelectionDialog dialog = new ColorSelectionDialog();
        dialog.setOnCloseRequest(event -> {
            String selectedColor = dialog.getSelectedColor();
            System.out.println("Elegiste: " + selectedColor);
            card.setColor(selectedColor);
        });
        dialog.showAndWait();
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
            System.out.println("La maquina comio una carta \n");
        } else {
            humanPlayer.addCard(this.deck.takeCard());
            System.out.println("El jugador comio una carta \n");
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
    public void isGameOver() {
        threadPlayMachine.stopThread();
        System.out.println("\nFin de la partida!\n");
        Platform.runLater(() -> {
            GameUnoStage.deleteInstance();
        });
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
                (card.isSkipCard()  && (topCard.isSkipCard())) ||
                (card.isTwoWildCard()  && (topCard.isTwoWildCard())) ||
                (card.isReverseCard()  && (topCard.isReverseCard()))|| card.isWildCard();
    }

    /**
     * Handles special actions for wild cards in the game.
     * Depending on the card's value, performs specific actions such as skipping turns, reversing play,
     * making players draw cards, or opening a color selection dialog.
     *
     * @param card the card being played
     * @param threadPlayMachine the thread managing machine's gameplay
     * @param player the current player
     */
    public void isWildCards(Card card, ThreadPlayMachine threadPlayMachine, Player player){
        if (card.getValue() == "SKIP"){
            System.out.println("\nUtilizaste una carta de Skip.");
            threadPlayMachine.setHasPlayerPlayed(false);

        } else if (card.getValue() =="RESERVE") {
            System.out.println("\nUtilizaste una carta de Reverse.");
            threadPlayMachine.setHasPlayerPlayed(false);

        } else if (card.getValue() =="TWO_WILD_DRAW") {
            eatCard(player, 2);
            System.out.println("\nUtilizaste un TWO_WILD_DRAW, " +player.getTypePlayer()+ " comio 2 cartas");
            System.out.println("\nCartas de la máquina: ");
            machinePlayer.printCardsPlayer();
            threadPlayMachine.setHasPlayerPlayed(true);

        } else if (card.getValue() =="WILD") {
            openColorSelectionDialog(card);//Falta que el color elegido por el jugador se use para que el jugador tenga que poner el mismo
            System.out.println("\nUtilizaste un WILD, ");
            threadPlayMachine.setHasPlayerPlayed(true);

        }else if (card.getValue() == "FOUR_WILD_DRAW") {
            eatCard(player, 4);
            openColorSelectionDialog(card);//Falta que el color elegido por el jugador se use para que el jugador tenga que poner el mismo
            System.out.println("\nUtilizaste un FOUR_WILD_DRAW, " +player.getTypePlayer()+ " comio 2 cartas");
            System.out.println("\nCartas de la máquina: ");
            machinePlayer.printCardsPlayer();
            threadPlayMachine.setHasPlayerPlayed(true);
        }
        else {
            threadPlayMachine.setHasPlayerPlayed(true);
        }
    }

    /**
     * Verifica si un jugador ha ganado después de jugar una carta.
     *
     * @param playerType El tipo de jugador que realizó el movimiento.
     */
    public void postMoveActions(String playerType) {

        if (playerType.equals(humanPlayer.getTypePlayer())) {
            if (humanPlayer.getCardsPlayer().isEmpty()) {
                alertBox.showMessage("GANADOR", "Has ganado! \uD83C\uDFC6");
                isGameOver();
            }
        } else if (playerType.equals(machinePlayer.getTypePlayer())) {
            if (machinePlayer.getCardsPlayer().isEmpty()) {
                alertBox.showMessage("GAME OVER", "La maquina ha ganado! \uD83E\uDD16 ");
                isGameOver();
            }
        }
    }
}

