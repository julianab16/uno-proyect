package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
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

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
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
            System.out.println("No playable card found, drawing a new card...");
            // Lógica para sacar una nueva carta no incluida aquí
        }
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}
