package org.example.eiscuno.model.game;

import javafx.stage.Stage;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class GameUnoTest extends ApplicationTest {
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private ThreadPlayMachine threadPlayMachine;
    private GameUnoController gameUnoController;


    @Override
    public void start(Stage stage) {
        // This is required to start the JavaFX application thread.
    }

    @BeforeEach
    public void setUp() {
        humanPlayer = new Player("HUMAN_PLAYER");
        machinePlayer = new Player("MACHINE_PLAYER");
        deck = new Deck();
        table = new Table();
        gameUno = new GameUno(humanPlayer, machinePlayer, deck, table, threadPlayMachine,gameUnoController);
    }

    @Test
    void humanPlayerEatsCardWhenMachinePlayerSingsUnoTest() {
        int initialHumanPlayerCards = humanPlayer.getCardsPlayer().size();
        gameUno.haveSingOne("MACHINE_PLAYER");

        assertEquals(initialHumanPlayerCards + 1, humanPlayer.getCardsPlayer().size());
    }
    @Test
    void machinePlayerEatsCardWhenHumanPlayerSingsUnoTest() {
        int initialMachineCards = machinePlayer.getCardsPlayer().size();
        gameUno.haveSingOne("HUMAN_PLAYER");

        assertEquals(initialMachineCards + 1, machinePlayer.getCardsPlayer().size());
    }
}
