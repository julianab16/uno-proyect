package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * This class represents a thread that monitors the machine player's hand to check if it has only one card left (UNO condition).
 */
public class ThreadSingUNOMachine implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private Thread thread;
    private volatile boolean execute = true;

    /**
     * Constructor to initialize the thread with the player's cards and a listener for UNO events.
     *
     * @param cardsPlayer the list of cards in the machine player's hand
     * @param listener the listener interface for UNO events
     */
    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, ThreadSingUNOMachineI listener) {
        this.cardsPlayer = cardsPlayer;
        this.listener = listener;
        this.thread = new Thread(this);
    }

    private ThreadSingUNOMachineI listener;

    /**
     * Method implementing the thread's task.
     * Monitors the player's hand to check if it has one card left and triggers the UNO event.
     */
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

    /**
     * Checks if the machine player has only one card left and triggers the UNO event if true.
     */
    private void hasOneCardTheHumanPlayer() {
        if (cardsPlayer.size() == 1) {
            System.out.println(" UNO ");
            listener.onMachineSaysUno();
            execute = false;
        }
    }

    /**
     * Starts the thread if it is not already running.
     */
    public void startThread() {
        if (!thread.isAlive()) {
            thread = new Thread(this);
            execute = true;
            thread.start();
        }
    }

    /**
     * Stops the thread by setting the execute flag to false and interrupting the thread.
     */
    public void stopThread() {
        execute = false;
        if (thread != null) {
            thread.interrupt();
        }
    }
}
