package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.alertbox.AlertBox;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private Player humanPlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private GameUno gameUno;
    private ThreadPlayMachine threadPlayMachine;
    private Deck deck;
    public AlertBox alertBox = new AlertBox();
    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, Deck deck, GameUno gameUno, ThreadPlayMachine threadPlayMachine, Player humanPlayer) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.deck = deck;
        this.gameUno = gameUno;
        this.threadPlayMachine = threadPlayMachine;
        this.humanPlayer = humanPlayer;
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

        String cardColor = "NONE";
        String cardValue = "FOUR_WILD_DRAW";
        String cardWValue = "WILD";
        Card fourCard = machinePlayer.findPlayableCard(cardColor, cardValue);
        Card wildCard = machinePlayer.findPlayableCard(cardColor, cardWValue);

        if (fourCard != null || wildCard != null) {
            table.addCardOnTheTable(fourCard);
            tableImageView.setImage(fourCard.getImage());
            machinePlayer.getCardsPlayer().remove(fourCard);

            table.addCardOnTheTable(wildCard);
            tableImageView.setImage(wildCard.getImage());
            machinePlayer.getCardsPlayer().remove(wildCard);
            System.out.println("\nLa maquina utilizo un "+wildCard.getValue());
        }
        else {
            if (playableCard != null) {
                table.addCardOnTheTable(playableCard);
                tableImageView.setImage(playableCard.getImage());
                machinePlayer.getCardsPlayer().remove(playableCard);
                isWildCards(playableCard, humanPlayer);
            } else {
                gameUno.eatCard(machinePlayer, 1);
                alertBox.showMessage("Turno", "La maquina comio una carta, turno del jugador \uD83C\uDFC3");

            }
            unoMachine();
            System.out.println("\n Cartas de la máquina: ");
            machinePlayer.printCardsPlayer();
        }
        gameUno.postMoveActions(machinePlayer.getTypePlayer());
    }

    public void unoMachine() {
        if (machinePlayer.getCardsPlayer().size() == 1) {
            alertBox.showMessage("Machina", "La maquina dijo ¡UNO! \uD83D\uDC40 ");
        }
    }
    public void isWildCards(Card card, Player player){
        if (card.getValue() == "SKIP"){
            System.out.println("\nLa maquina utilizo una carta de Skip.");
            run();
        } else if (card.getValue() =="RESERVE") {
            System.out.println("\nLa maquina utilizo una carta de Reverse.");
            run();
        } else if (card.getValue() =="TWO_WILD_DRAW") {
            gameUno.eatCard(player, 2);
            System.out.println("\nLa maquina utilizo  un TWO_WILD_DRAW, " +player.getTypePlayer()+ " comio 2 cartas.");
            System.out.println("\n Tus cartas: ");
            humanPlayer.printCardsPlayer();
        } else if (card.getValue() =="WILD") {

        }else if (card.getValue() == "FOUR_WILD_DRAW" || card.getValue() =="WILD") {
        }
        else {
            setHasPlayerPlayed(true);
        }
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}
