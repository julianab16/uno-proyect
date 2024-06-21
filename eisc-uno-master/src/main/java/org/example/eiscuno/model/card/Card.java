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

    /**
     * Gets the ImageView representation of the card.
     *
     * @return the ImageView of the card
     */
    public ImageView getCard() {
        return cardImageView;
    }

    /**
     * Gets the image of the card.
     *
     * @return the image of the card
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the value of the card.
     *
     * @return the value of the card
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the color of the card.
     *
     * @return the color of the card
     */
    public String getColor() {
        return color;
    }

    /**
     * Checks if the card is a reverse card.
     * A reverse card's value starts with "RESERVE".
     *
     * @return true if the card is a reverse card, false otherwise
     */
    public boolean isReverseCard() {
        return value.startsWith("RESERVE");
    }

    /**
     * Checks if the card is a skip card.
     * A skip card's value starts with "SKIP".
     *
     * @return true if the card is a skip card, false otherwise
     */
    public boolean isSkipCard() {
        return value.startsWith("SKIP");
    }


    /**
     * Checks if the card is a +2 wild card.
     * A +2 wild card's value starts with "TWO_WILD_DRAW".
     *
     * @return true if the card is a +2 wild card, false otherwise
     */
    public boolean isTwoWildCard() {
        return value.startsWith("TWO_WILD_DRAW");
    }

    /**
     * Checks if the card is a wild card.
     * A wild card's value is either "WILD" or "FOUR_WILD_DRAW".
     *
     * @return true if the card is a wild card, false otherwise
     */
    public boolean isWildCard() {
        return value.equals("WILD") || value.equals("FOUR_WILD_DRAW");
    }


    /**
     * Returns a string representation of the card.
     *
     * @return a string representation of the card, showing its color and value
     */
    @Override
    public String toString() {
        return "Color: " + this.color + ", Value: " + this.value;

    }

    /**
     * Sets the color of the card.
     *
     * @param color the color to set for the card
     */
    public void setColor(String color) {
        this.color = color;
    }

}



