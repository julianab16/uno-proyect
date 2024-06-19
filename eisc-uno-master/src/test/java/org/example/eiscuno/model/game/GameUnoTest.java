package org.example.eiscuno.model.game;

import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameUnoTest {
    @Test
    void tes1(){//cambiar el nombre de la prueba a uno mas especifico
        var humanPlayer = new Player("HUMAN_PLAYER");
        var machinePlayer = new Player("MACHINE_PLAYER");
        var deck = new Deck();
        var table =new Table();
        var gameUno = new GameUno(humanPlayer, machinePlayer, deck, table);



    }


}