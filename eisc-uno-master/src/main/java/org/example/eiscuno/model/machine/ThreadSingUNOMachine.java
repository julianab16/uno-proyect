package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private Thread thread;
    private volatile boolean execute = true;

    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, ThreadSingUNOMachineI listener) {
        this.cardsPlayer = cardsPlayer;
        this.listener = listener;
        this.thread = new Thread(this);
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
            //setCondition(false);
        }
    }
    public void startThread() {
        if (!thread.isAlive()) {
            thread = new Thread(this);
            execute = true;
            thread.start();
        }
    }

    public void stopThread() {
        execute = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    public void setCondition(boolean execute) {
        this.execute = execute;
    }
}
