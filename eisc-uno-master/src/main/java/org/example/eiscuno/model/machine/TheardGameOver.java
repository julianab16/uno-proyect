package org.example.eiscuno.model.machine;

import javafx.scene.control.Alert;
import org.example.eiscuno.model.player.Player;

public class TheardGameOver extends  Thread{

    private Player player;
    @Override
    public void run (){
        player.getTypePlayer();
        if (player.getCardsPlayer().isEmpty()){
            showAlert("GANADOR", player.getTypePlayer()+" Haz ganado");

        }
        else {
            System.out.println("No ha gando nadie todavia");
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
