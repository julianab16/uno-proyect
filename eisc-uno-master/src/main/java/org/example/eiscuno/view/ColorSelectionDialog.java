package org.example.eiscuno.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;

public class ColorSelectionDialog extends Alert {

    private String selectedColor;

    public ColorSelectionDialog() {
        super(AlertType.CONFIRMATION);
        setTitle("Selecciona un color");
        double canvasSize = 200;
        Canvas canvas = new Canvas(canvasSize, canvasSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawColorWheel(gc, canvasSize / 2);

        StackPane stackPane = new StackPane(canvas);
        stackPane.setStyle("-fx-background-color: black;");

        DialogPane dialogPane = getDialogPane();
        dialogPane.setContent(stackPane);
        dialogPane.setStyle("-fx-background-color: black;");

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            double x = event.getX() - canvasSize / 2;
            double y = event.getY() - canvasSize / 2;
            double angle = Math.toDegrees(Math.atan2(y, x)) + 180;

            if (angle >= 0 && angle < 90) {
                selectedColor = "GREEN";
                setResult(ButtonType.OK);
                close();
            } else if (angle >= 90 && angle < 180) {
                selectedColor = "BLUE";
                setResult(ButtonType.OK);
                close();
            } else if (angle >= 180 && angle < 270) {
                selectedColor = "YELLOW";
                setResult(ButtonType.OK);
                close();
            } else if (angle >= 270 && angle < 360) {
                selectedColor = "RED";
                setResult(ButtonType.OK);
                close();
            }
        });
    }

    private void drawColorWheel(GraphicsContext gc, double radius) {
        double[] angles = {0, 90, 180, 270};
        Color[] colors = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};

        for (int i = 0; i < angles.length; i++) {
            gc.setFill(colors[i]);
            gc.fillArc(0, 0, 2 * radius, 2 * radius, angles[i], 90, javafx.scene.shape.ArcType.ROUND);
        }
    }

    public String getSelectedColor() {
        return selectedColor;
    }
}