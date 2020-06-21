package schiffeVersenken;

import schiffeVersenken.protocolBinding.StreamBindingSender;
import transmission.DataConnector;

import java.io.IOException;
import java.util.Random;

public class SchiffeVersenkenEngine implements SchiffeVersenkenReceiver, SchiffeVersenkenUsage {
    public static final int UNDEFINED_DICE = -1;
    private SchiffeVersenkenStatus status;
    private int sentDice = UNDEFINED_DICE;
    private int receivedDice = UNDEFINED_DICE;
    private int anzahlVersenktGegner = 0;
    private int anzahlVersenktSelber = 0;
    private int lastShotX;
    private int lastShotY;
    private boolean gewonnen;
    private SchiffeVersenkenSender sender;

    private SchiffeVersenkenBoard ownBoard = new SchiffeVersenkenBoard();
    private SchiffeVersenkenBoard opponentBoard = new SchiffeVersenkenBoard();

    public SchiffeVersenkenEngine(SchiffeVersenkenSender sender) {
        this.sender = sender;
        this.status = SchiffeVersenkenStatus.SPIELSTART;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ////                              Receive Methods                                     ////
    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void receiveReihenfolgeWuerfeln(int random) throws IOException, StatusException  {

        switch(status) {
            case SPIELSTART:
                receivedDice = random;
                this.status = SchiffeVersenkenStatus.DICE_RECEIVED;
                System.out.println("Dice Received");
                break;
            case DICE_SENT:
                if(random > sentDice) {
                    this.status = SchiffeVersenkenStatus.VERSENKEN_E;
                    System.out.println("Versenken E");
                } else if (random < sentDice) {
                    this.status = SchiffeVersenkenStatus.VERSENKEN_S;
                    System.out.println("Versenken S");
                } else {
                    this.status = SchiffeVersenkenStatus.SPIELSTART;
                }
                break;

            default:
                throw new StatusException();
        }



    }

    @Override
    public void receiveKoordinate(int zeile, int spalte) throws IOException, StatusException, SchiffeVersenkenException  {
        if(this.status != SchiffeVersenkenStatus.VERSENKEN_E) {
            throw new StatusException();
        }

        FeedbackStatus feedback;
        BoardSpace space = ownBoard.getSpace(zeile, spalte);
        if(space == BoardSpace.WATER) {
            feedback = FeedbackStatus.NO_HIT;
            ownBoard.setSpace(zeile, spalte, BoardSpace.SHOT_WATER);
        } else if(space == BoardSpace.SHIP) {
            ownBoard.setSpace(zeile, spalte, BoardSpace.SHOT_SHIP);
            if(ownBoard.checkSunk(zeile, spalte)) {
                feedback = FeedbackStatus.HIT_AND_SUNK;
                try {
                    ownBoard.setSunk(zeile, spalte);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else {
                feedback = FeedbackStatus.HIT;
            }
        } else {
            feedback = FeedbackStatus.NO_HIT;
        }

        System.out.println("Ich bin "+status+" und bestätige.");
        this.status = SchiffeVersenkenStatus.BESTAETIGEN_S;
        switch(feedback) {
            case HIT:
                sendBestaetigen(0);
                break;
            case NO_HIT:
                sendBestaetigen(1);
                break;
            case HIT_AND_SUNK:
                sendBestaetigen(2);
                break;
        }

    }

    @Override
    public void receiveKapitulation() throws IOException, StatusException  {
        if(this.status != SchiffeVersenkenStatus.VERSENKEN_E) {
            throw new StatusException();
        }

        gewonnen = true;
        this.status = SchiffeVersenkenStatus.SPIELENDE;
    }

    @Override
    public void receiveBestaetigen(int status) throws IOException, StatusException, SchiffeVersenkenException {
        if(this.status != SchiffeVersenkenStatus.BESTAETIGEN_E) {
            throw new StatusException();
        }

        switch(status) {
            case 0:
                opponentBoard.setSpace(lastShotX, lastShotY, BoardSpace.SHOT_SHIP);
                break;
            case 1:
                opponentBoard.setSpace(lastShotX, lastShotY, BoardSpace.SHOT_WATER);
                break;
            case 2:
                anzahlVersenktGegner++;
                opponentBoard.setSpace(lastShotX, lastShotY, BoardSpace.SHOT_SHIP);
                try {
                    opponentBoard.setSunk(lastShotX, lastShotY);
                } catch (Exception e) {
                    System.err.print(e.getMessage());
                }
                break;
            default:
                System.err.print("Critical Error. Garbage feedback received.");
                break;
        }


        if(anzahlVersenktGegner < 10) {
            this.status = SchiffeVersenkenStatus.VERSENKEN_E;
        } else {
            gewonnen = true;
            this.status = SchiffeVersenkenStatus.SPIELENDE;
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////
    ////                                 Send Methods                                     ////
    //////////////////////////////////////////////////////////////////////////////////////////

    public void sendBestaetigen(int status) throws IOException, StatusException, SchiffeVersenkenException {
        if(this.status != SchiffeVersenkenStatus.BESTAETIGEN_S) {
            throw new StatusException();
        }

        sender.sendBestaetigen(status);
        if(status == 2) {
            anzahlVersenktSelber++;
        }

        if(anzahlVersenktSelber < 10) {
            this.status = SchiffeVersenkenStatus.VERSENKEN_S;
        } else {
            gewonnen = false;
            this.status = SchiffeVersenkenStatus.SPIELENDE;
        }

    }


    //////////////////////////////////////////////////////////////////////////////////////////
    ////                                Usage Methods                                     ////
    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void doDice() throws StatusException, IOException {
        Random randomGenerator = new Random();
        int random = randomGenerator.nextInt();
        sender.sendReihenfolgeWuerfeln(random);

        switch(status) {
            case SPIELSTART:
                this.sentDice = random;
                this.status = SchiffeVersenkenStatus.DICE_SENT;
                System.out.println("Dice Sent");
                break;
            case DICE_RECEIVED:

                if(random > receivedDice) {
                    this.status = SchiffeVersenkenStatus.VERSENKEN_S;
                    System.out.println("Versenken S");
                } else if (random < receivedDice) {
                    this.status = SchiffeVersenkenStatus.VERSENKEN_E;
                    System.out.println("Versenken E");
                } else {
                    this.status = SchiffeVersenkenStatus.SPIELSTART;
                    System.out.println("Spielstart");
                }

                break;
            default:
                throw new StatusException();
        }

    }

    @Override
    public boolean isMyTurn() {
        return this.status == SchiffeVersenkenStatus.VERSENKEN_S;
    }

    @Override
    public void doTurn(int x, int y) throws SchiffeVersenkenException, StatusException, IOException {

        if(this.status != SchiffeVersenkenStatus.VERSENKEN_S) {
            throw new StatusException();
        }

        lastShotX = x;
        lastShotY = y;
        this.status = SchiffeVersenkenStatus.BESTAETIGEN_E;
        sender.sendKoordinate(x, y);

    }

    @Override
    public void giveUp() throws StatusException, IOException {

        if(this.status != SchiffeVersenkenStatus.VERSENKEN_S) {
            throw new StatusException();
        }

        sender.sendKapitulation();

        gewonnen = false;
        this.status = SchiffeVersenkenStatus.SPIELENDE;

    }

    @Override
    public SchiffeVersenkenStatus getStatus() {
        return this.status;
    }

    @Override
    public void printOwnBoard() {
        System.out.println("Eigenes Spielfeld: ");
        ownBoard.printBoard();
        System.out.println();
    }

    @Override
    public void printOpponentBoard() {
        System.out.println("Gegnerisches Spielfeld: ");
        opponentBoard.printBoard();
        System.out.println();
    }

    @Override
    public boolean setShip(int zeile1, int zeile2, int spalte1, int spalte2) throws StatusException, SchiffeVersenkenException {
        if(this.status != SchiffeVersenkenStatus.DICE_RECEIVED && this.status != SchiffeVersenkenStatus.SPIELSTART) {
            throw new StatusException();
        }

        if(!ownBoard.checkValidShipPosition(zeile1, zeile2, spalte1, spalte2)) {
            return false;
        } else {
            ownBoard.setShip(zeile1, zeile2, spalte1, spalte2);
        }

        return true;
    }

    @Override
    public boolean checkWon() {
        return gewonnen;
    }


    public void setDebugBoard() throws SchiffeVersenkenException {
        ownBoard.setSpace(0,0, BoardSpace.SHIP);
        ownBoard.setSpace(0,1, BoardSpace.SHIP);
        ownBoard.setSpace(0,2, BoardSpace.SHIP);

        ownBoard.setSpace(2,2, BoardSpace.SHIP);
        ownBoard.setSpace(3,3, BoardSpace.SHIP);
        ownBoard.setSpace(4,4, BoardSpace.SHIP);
        ownBoard.setSpace(5,5, BoardSpace.SHIP);
        ownBoard.setSpace(6,6, BoardSpace.SHIP);
        ownBoard.setSpace(7,7, BoardSpace.SHIP);
        ownBoard.setSpace(8,8, BoardSpace.SHIP);
        ownBoard.setSpace(9,9, BoardSpace.SHIP);
        ownBoard.setSpace(0,5, BoardSpace.SHIP);
    }
}
