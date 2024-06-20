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

        // Crear el Canvas para dibujar la ruleta
        double canvasSize = 200;
        Canvas canvas = new Canvas(canvasSize, canvasSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Dibujar las secciones de la ruleta
        drawColorWheel(gc, canvasSize / 2);

        // Crear un StackPane para contener el Canvas
        StackPane stackPane = new StackPane(canvas);
        stackPane.setStyle("-fx-background-color: black;");

        // Estilizar el panel de diálogo
        DialogPane dialogPane = getDialogPane();
        dialogPane.setContent(stackPane);
        dialogPane.setStyle("-fx-background-color: black;");

        // Añadir evento de click al Canvas
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            double x = event.getX() - canvasSize / 2;
            double y = event.getY() - canvasSize / 2;
            double angle = Math.toDegrees(Math.atan2(y, x)) + 180;

            if (angle >= 0 && angle < 90) {
                selectedColor = "Verde";
                setResult(ButtonType.OK);
                close();
            } else if (angle >= 90 && angle < 180) {
                selectedColor = "Azul";
                setResult(ButtonType.OK);
                close();
            } else if (angle >= 180 && angle < 270) {
                selectedColor = "Amarillo";
                setResult(ButtonType.OK);
                close();
            } else if (angle >= 270 && angle < 360) {
                selectedColor = "Rojo";
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