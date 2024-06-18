package org.example.eiscuno.model.game;

import org.example.eiscuno.model.alertbox.AlertBox;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;

import java.util.Scanner;

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
            Card currentCard = deck.takeCard();
            deck.discardCard(currentCard);
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
    public Boolean isGameOver() {
        GameUnoStage.deleteInstance();
        ThreadPlayMachine.currentThread().interrupt();
        return true;
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

    private void changeColor(){//gente se supone que aca usa una funcion que permite escribir po rmedio del teclado el color que a que lo quiere cambiar
        Scanner scanner = new Scanner(System.in);
        System.out.println("Elige un nuevo color (RED, BLUE, GREEN, YELLOW): ");
        String newColor = scanner.nextLine().toUpperCase();

        while (!newColor.equals("RED") && !newColor.equals("BLUE") && !newColor.equals("GREEN") && !newColor.equals("YELLOW")) {
            System.out.println("Color no válido. Elige un color válido (RED, BLUE, GREEN, YELLOW): ");
            newColor = scanner.nextLine().toUpperCase();
        }
        String currentColor = newColor;//no estoy segura de eso porque current color ya se habia usado en otra clase ;(
        System.out.println("El nuevo color es " + currentColor);

    }

    public void isWildCards(Card card, ThreadPlayMachine threadPlayMachine, Player player){
        String cardValue = card.getValue();

        switch (cardValue) {
            case "SKIP":
                threadPlayMachine.setHasPlayerPlayed(false);
                System.out.println("\nUtilizaste una carta de Skip.\n");
                break;
            case "REVERSE":
                threadPlayMachine.setHasPlayerPlayed(false);
                System.out.println("\nUtilizaste una carta de Reverse.\n");
                break;
            case "TWO_WILD_DRAW":
                eatCard(player, 2);
                System.out.println("\nUtilizaste un TWO_WILD_DRAW, " + player.getTypePlayer() + " comió 2 cartas.\n");
                threadPlayMachine.setHasPlayerPlayed(true);
                break;
            case "WILD":
                alertBox.showMessage("Cambio de color","Escribe a que color lo quieres cambiar");
                changeColor();
                threadPlayMachine.setHasPlayerPlayed(true);
                break;
            case "FOUR_WILD_DRAW":
                eatCard(player, 4);
                System.out.println("\nUtilizaste un FOUR_WILD_DRAW, " + player.getTypePlayer() + " comió 4 cartas.\n");
                threadPlayMachine.setHasPlayerPlayed(true);
                break;
            default:
                threadPlayMachine.setHasPlayerPlayed(true);
                break;
        }
    }

    /**
     * Verifica si un jugador ha ganado después de jugar una carta.
     *
     * @param playerType El tipo de jugador que realizó el movimiento.
     */
    private void postMoveActions(String playerType) {
        if (playerType.equals(humanPlayer.getTypePlayer())) {
            if (humanPlayer.getCardsPlayer().isEmpty()) {
                alertBox.showMessage("GANADOR", "Has ganado! \uD83C\uDFC6");
                System.out.println("\nFin de la partida!\n");
                isGameOver();
            }
        } else if (playerType.equals(machinePlayer.getTypePlayer())) {
            if (machinePlayer.getCardsPlayer().isEmpty()) {
                alertBox.showMessage("GAME OVER", "La maquina ha ganado! \uD83E\uDD16 ");
                System.out.println("\nFin de la partida!\n");
                isGameOver();
            }
        }
    }
}
