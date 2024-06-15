package org.example.eiscuno.model.machine;

import javafx.concurrent.Task;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;
    boolean ejecutar = true;


    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer){
        this.cardsPlayer = cardsPlayer;
    }

    @Override
    public void run(){
        while (ejecutar){
            try {
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheHumanPlayer();
        }
    }

    private void hasOneCardTheHumanPlayer () {
        if (cardsPlayer.size() == 1) {
            System.out.println(" UNO ");
            ejecutar = false;
        }
    }
}
