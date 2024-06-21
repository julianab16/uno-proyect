package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a card in the Uno game.
 */
public class Card {
    private String url;
    private String value;
    private String color;
    private Image image;
    private ImageView cardImageView;

    /**
     * Constructs a Card with the specified image URL, value, and color.
     *
     <<<<<<< HEAD
     * @param url   the URL of the card image
     * @param value of the card
    =======
     * @param url the URL of the card image
     * @param value the value of the card
    >>>>>>> 9c100da4ee9edb264400c8aaa1f9292aad5a5b76
     * @param color the color of the card
     */
    public Card(String url, String value, String color) {
        if (value == null || color == null) {
            throw new IllegalArgumentException("Value and color cannot be null");
        }
        this.url = url;
        this.value = value;
        this.color = color;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
    }

    /**
     * Creates and configures the ImageView for the card.
     *
     * @return the configured ImageView of the card
     */
    private ImageView createCardImageView() {
        ImageView card = new ImageView(this.image);
        card.setY(16);
        card.setFitHeight(100);
        card.setFitWidth(70);
        return card;
    }

    public ImageView getCard() {
        return cardImageView;
    }

    public Image getImage() {
        return image;
    }

    public String getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }

    public boolean isReverseCard() {
        return value.startsWith("RESERVE");
    }

    public boolean isSkipCard() {
        return value.startsWith("SKIP");
    }

    public boolean isTwoWildCrad(){
        return value.startsWith("TWO_WILD_DRAW");
    }

    public boolean isWildCard() {
        return value.equals("WILD") || value.equals("FOUR_WILD_DRAW");
    }

    public String getWildCardName(Card card) {
        String value = card.getValue();
        if (value.startsWith("TWO_WILD_DRAW")) {
            return "TWO_WILD_DRAW";
        } else if(value.startsWith("SKIP")){
            return "SKIP";
        } else if (value.startsWith("RESERVE")) {
            return "RESERVE";
        } else {
            return "NOT_WILD";
        }
    }


    @Override
    public String toString() {
        return "Color: " + this.color + ", Valor: " + this.value;

    }

    public void setColor(String color) {
        this.color = color;
    }
}



