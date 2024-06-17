package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.alertbox.AlertBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachineI;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController implements ThreadSingUNOMachineI {
    @FXML
    private GridPane gridPaneCardsPlayer;
    @FXML
    private ImageView tableImageView;
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonNext;
    @FXML
    private Button buttonDeckCards;
    @FXML
    private Button buttonOut;
    @FXML
    private Button buttonUno;
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;
    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;
    private boolean primeraCartaPuesta = false;
    private long machineTime;
    private long playerTime;
    private boolean machineSaidUno;
    private boolean playerSaidUno;
    public AlertBox alertBox = new AlertBox();
    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(),this);
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.deck, this.gameUno);
        threadPlayMachine.start();
        System.out.println(" Tus cartas: ");
        humanPlayer.printCardsPlayer();
        System.out.println(" ");
        System.out.println(" Cartas de la máquina: ");
        machinePlayer.printCardsPlayer();
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    public void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                if (!primeraCartaPuesta) {
                    tableImageView.setImage(card.getImage());
                    humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                    threadPlayMachine.setHasPlayerPlayed(true);
                    gameUno.playCard(card);
                    printCardsHumanPlayer();
                    primeraCartaPuesta = true;

                    System.out.println("\n Tus cartas: ");
                    humanPlayer.printCardsPlayer();

                } else if (gameUno.canPlayCard(card)) {
                    tableImageView.setImage(card.getImage());
                    humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                    gameUno.isWildCards(card,threadPlayMachine, machinePlayer);
                    gameUno.playCard(card);
                    printCardsHumanPlayer();
                    System.out.println("\n Tus cartas: ");
                    humanPlayer.printCardsPlayer();
                    deck.discardCard(card);
                    Card currentCard = card;
                } else {
                    alertBox.showMessageError("Error", "Carta invalida. Intenta otra vez. \uD83C\uDCCF");
                }
            });
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }
    @FXML
    void onHandleMouseEnteredBack(MouseEvent event) {
        Scale scale = new Scale(1.1,1.1);
        buttonBack.getTransforms().add(scale);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.DARKRED);
        dropShadow.setRadius(20);
        buttonBack.setEffect(dropShadow);
    }

    @FXML
    void onHandleMouseExitedBack(MouseEvent event) {
        buttonBack.getTransforms().clear();
        buttonBack.setEffect(null);
    }
    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {

        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }
    @FXML
    void onHandleMouseEnteredNext(MouseEvent event) {
        Scale scale = new Scale(1.1,1.1);
        buttonNext.getTransforms().add(scale);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.DARKRED);
        dropShadow.setRadius(20);
        buttonNext.setEffect(dropShadow);
    }
    @FXML
    void onHandleMouseExitedNext(MouseEvent event) {
        buttonNext.getTransforms().clear();
        buttonNext.setEffect(null);
    }
    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        if (!deck.isEmpty()) {
            Card newCard = deck.takeCard();
            humanPlayer.addCard(newCard);
            deck.discardCard(newCard);
            printCardsHumanPlayer();
            System.out.println("\n Tus cartas: ");
            humanPlayer.printCardsPlayer();
            threadPlayMachine.setHasPlayerPlayed(true);
            System.out.println("\nTurno de la maquina");
        } else {
            deck.refillDeckFromDiscardPile();
            alertBox.showMessage("Mazo","El mazo se acabo!\nPero fue llenado nuevamente. \uD83C\uDCCF");
            System.out.println("\nNo hay más cartas en el mazo.\n");
        }
    }
    @FXML
    void onHandleMouseEnteredDeckCards(MouseEvent event) {
        Scale scale = new Scale(1.1,1.1);
        buttonDeckCards.getTransforms().add(scale);
        Glow glow = new Glow(0.8);
        buttonDeckCards.setEffect(glow);
    }

    @FXML
    void onHandleMouseExitedDeckCards(MouseEvent event) {
        buttonDeckCards.getTransforms().clear();
        buttonDeckCards.setEffect(null);
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        if (humanPlayer.getCardsPlayer().size() == 1) {
            System.out.println("\nEl jugador dijo ¡UNO!\n");
            playerTime = System.currentTimeMillis();
            playerSaidUno = true;
            checkUno();
            // Aquí puedes añadir lógica adicional si hay reglas específicas para cuando se dice "UNO"
        } else {
            alertBox.showMessageError("Error", "No puedes decir 'UNO' porque no tienes exactamente una carta. Toma una carta como penitencia. \uD83D\uDE14");
            // Penalización: por ejemplo, el jugador debe tomar 2 cartas
            gameUno.eatCard(humanPlayer, 1);
            printCardsHumanPlayer();
            System.out.println(" Tus cartas: ");
            humanPlayer.printCardsPlayer();
        }
    }

    @FXML
    void onHandleMouseEnteredUno(MouseEvent event) {
        Scale scale = new Scale(1.1,1.1);
        buttonUno.getTransforms().add(scale);
        Glow glow = new Glow(0.8);
        buttonUno.setEffect(glow);
    }

    @FXML
    void onHandleMouseExitedUno(MouseEvent event) {
        buttonUno.getTransforms().clear();
        buttonUno.setEffect(null);
    }

    @Override
    public void onMachineSaysUno()  {
        machineTime = System.currentTimeMillis();
        machineSaidUno = true;
        checkUno();
    }

    @FXML
    void OnHnableExitButton(ActionEvent event) {
        GameUnoStage.deleteInstance();
    }

    @FXML
    void onHandleMouseEnteredOut(MouseEvent event) {
        Scale scale = new Scale(1.1,1.1);
        buttonOut.getTransforms().add(scale);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.WHITE);
        dropShadow.setRadius(20);
        buttonOut.setEffect(dropShadow);
    }

    @FXML
    void onHandleMouseExitedOut(MouseEvent event) {
        buttonOut.getTransforms().clear();
        buttonOut.setEffect(null);
    }

    private void checkUno() {
        System.out.println("\nMaquina lo dijo en "+machineTime);
        System.out.println("\njugador lo dijo en "+playerTime);
        if (machineSaidUno && playerSaidUno) {
            if (machineTime < playerTime || playerTime == 0) {
                alertBox.showMessage("UNO","¡Máquina dijo UNO más rápido! Toma una carta. \uD83C\uDCCF");
                System.out.println("\n¡Máquina dijo UNO más rápido! Jugador debe comer una carta.\n");
                String playerMachime = machinePlayer.getTypePlayer();
                gameUno.haveSingOne(playerMachime);
                printCardsHumanPlayer();
            } else {
                System.out.println("\n¡Jugador dijo UNO más rápido!\n");
                alertBox.showMessage("UNO","¡Has dicho UNO más rápido! \uD83D\uDE04");
            }
            machineTime = 0;
            playerTime = 0;
            machineSaidUno = false;
            playerSaidUno = false;
            threadSingUNOMachine.setCondition(true);
        }
    }
}
