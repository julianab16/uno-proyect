package org.example.eiscuno.model.machine;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private volatile boolean execute = true;

    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, ThreadSingUNOMachineI listener) {
        this.cardsPlayer = cardsPlayer;
        this.listener = listener;
    }

    private ThreadSingUNOMachineI listener;

    @Override
    public void run() {
        while (execute) {
            try {
                Thread.sleep((long) (Math.random() * 5000));
                hasOneCardTheHumanPlayer();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void hasOneCardTheHumanPlayer() {
        if (cardsPlayer.size() == 1) {
            System.out.println(" UNO ");
            listener.onMachineSaysUno();
            execute = false;
        }
    }

    public void setCondition(boolean condition) {
        execute = condition;
    }

}
