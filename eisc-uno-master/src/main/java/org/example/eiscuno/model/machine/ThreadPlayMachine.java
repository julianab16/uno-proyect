package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.alertbox.AlertBox;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.game.GameUno;


import java.util.HashMap;

import java.util.Map;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private Player humanPlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private GameUno gameUno;
    private Deck deck;
    public AlertBox alertBox = new AlertBox();
    private volatile boolean running = true;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, Deck deck, GameUno gameUno, Player humanPlayer) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.deck = deck;
        this.gameUno = gameUno;
        this.humanPlayer = humanPlayer;
    }

    public void run() {
        while (running){
            if(hasPlayerPlayed){
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta
                putCardOnTheTable();
                hasPlayerPlayed = false;
            }
            if (!running) {
                break;
            }
        }
    }

    private void putCardOnTheTable(){
        try {
            Card topCard = table.getCurrentCardOnTheTable();
            String currentColor = topCard.getColor();
            String currentValue = topCard.getValue();

            Card playableCard = machinePlayer.findPlayableCard(currentColor, currentValue);

            Card fourCard = machinePlayer.findPlayableCard("NONE", "FOUR_WILD_DRAW");
            Card wildCard = machinePlayer.findPlayableCard("NONE", "WILD");

            if (fourCard != null || wildCard != null) {
                table.addCardOnTheTable(fourCard);
                tableImageView.setImage(fourCard.getImage());
                machinePlayer.getCardsPlayer().remove(fourCard);

                table.addCardOnTheTable(wildCard);
                tableImageView.setImage(wildCard.getImage());
                machinePlayer.getCardsPlayer().remove(wildCard);
                deck.discardCard(fourCard);
                isWildCards(fourCard, humanPlayer);
            }
            else {
                if (playableCard != null) {
                    table.addCardOnTheTable(playableCard);
                    tableImageView.setImage(playableCard.getImage());
                    machinePlayer.getCardsPlayer().remove(playableCard);
                    isWildCards(playableCard, humanPlayer);
                } else {
                    gameUno.eatCard(machinePlayer, 1);
                    alertBox.showMessage("Turno", "La máquina comió una carta, turno del jugador \uD83C\uDFC3");
                }
            }
            unoMachine();
            gameUno.postMoveActions(machinePlayer.getTypePlayer());
            deck.discardCard(playableCard);
            System.out.println("\nCartas de la máquina: ");
            machinePlayer.printCardsPlayer();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No hay cartas en la mesa.");
            deck.refillDeckFromDiscardPile();
        }
    }

    private String chooseBestColor() {
        // Map to count cards per color
        Map<String, Integer> colorCount = new HashMap<>();
        colorCount.put("BLUE", 0);
        colorCount.put("GREEN", 0);
        colorCount.put("RED", 0);
        colorCount.put("YELLOW", 0);

        // Count cards of each color
        for (Card card : machinePlayer.getCardsPlayer()) {
            String color = card.getColor();
            if (colorCount.containsKey(color)) {
                colorCount.put(color, colorCount.get(color) + 1);
            }
        }

        // Find color with the most cards
        int maxCount = -1;
        String bestColor = "";
        for (Map.Entry<String, Integer> entry : colorCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                bestColor = entry.getKey();
            } else if (entry.getValue() == maxCount) {
                // If tie, choose randomly between the tied colors
                if (Math.random() < 0.5) {
                    bestColor = entry.getKey();
                }
            }
        }
        return bestColor;
    }

    public void unoMachine() {
        if (machinePlayer.getCardsPlayer().size() == 1) {
            alertBox.showMessage("Machina", "La maquina dijo ¡UNO! \uD83D\uDC40 ");
        }
    }

    public void isWildCards(Card card, Player player){
        if (card.getValue().equals("SKIP")) {
            System.out.println("\nLa máquina utilizó una carta de Skip.");
            setHasPlayerPlayed(true);
            run();

        } else if (card.getValue().equals("RESERVE")) {
            System.out.println("\nLa máquina utilizó una carta de Reverse.");
            setHasPlayerPlayed(true);
            run();

        } else if (card.getValue().equals("TWO_WILD_DRAW")) {
            gameUno.eatCard(player, 2);
            System.out.println("\nLa máquina utilizó un TWO_WILD_DRAW, " + player.getTypePlayer() + " comió 2 cartas.");
            System.out.println("\n Tus cartas: ");
            humanPlayer.printCardsPlayer();
            setHasPlayerPlayed(true);

        } else if (card.getValue().equals("WILD")) {
            // Select color based on most cards of that color
            String chosenColor = chooseBestColor();
            alertBox.showMessage("Color elegido", "La máquina ha elegido el color: " + chosenColor);
            card.setColor(chosenColor);
            setHasPlayerPlayed(false);

        } else if (card.getValue().equals("FOUR_WILD_DRAW")) {
            // Select color based on most cards of that color
            String chosenColor = chooseBestColor();
            alertBox.showMessage("Color elegido", "La máquina ha elegido el color: " + chosenColor);
            card.setColor(chosenColor);
            gameUno.eatCard(player, 4); // Hace que el jugador humano coma 4 cartas
            System.out.println("\nLa máquina utilizó un FOUR_WILD_DRAW, " + player.getTypePlayer() + " comió 4 cartas.");
            System.out.println("\n Tus cartas: ");
            humanPlayer.printCardsPlayer();
            setHasPlayerPlayed(false);

        } else {
            setHasPlayerPlayed(false);
        }
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    public boolean isRunning(boolean running) {
        return running;
    }
}
