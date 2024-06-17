package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.alertbox.AlertBox;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private Deck deck;
    public AlertBox alertBox = new AlertBox();
    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, Deck deck) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.deck = deck;
    }

    public void run() {
        while (true){
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
        }
    }

    private void putCardOnTheTable(){
        Card topCard = table.getCurrentCardOnTheTable();
        String currentColor = topCard.getColor();
        String currentValue = topCard.getValue();

        Card playableCard = machinePlayer.findPlayableCard(currentColor, currentValue);

        if (playableCard != null) {
            table.addCardOnTheTable(playableCard);
            tableImageView.setImage(playableCard.getImage());
            machinePlayer.getCardsPlayer().remove(playableCard);
        } else {
            // Lógica para sacar una nueva carta no incluida aquí
            eatCardMachine();
        }
        unoMachine();
        System.out.println("\n Cartas de la máquina: ");
        machinePlayer.printCardsPlayer();
    }

    void eatCardMachine() {
        if (!deck.isEmpty()) {
            Card newCard = deck.takeCard();
            machinePlayer.addCard(newCard);
            alertBox.showMessage("Turno", "La maquina comio una carta, turno del jugador \uD83C\uDFC3");
        } else {
            System.out.println("No hay más cartas en el mazo.");
        }
    }

    public void unoMachine() {
        if (machinePlayer.getCardsPlayer().size() == 1) {
            alertBox.showMessage("Machina", "La maquina dijo ¡UNO! \uD83D\uDC40 ");
        }
    }
    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}
