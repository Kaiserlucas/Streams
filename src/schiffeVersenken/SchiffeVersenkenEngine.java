package schiffeVersenken;

import java.io.IOException;

public class SchiffeVersenkenEngine implements SchiffeVersenkenReceiver, SchiffeVersenkenSender {
    public static final int UNDEFINED_DICE = -1;
    private SchiffeVersenkenStatus status;
    private int sentDice = UNDEFINED_DICE;
    private int anzahlVersenktGegner = 0;
    private int anzahlVersenktSelber = 0;
    private int lastShotX;
    private int lastShotY;
    private boolean gewonnen;

    private SchiffeVersenkenBoard ownBoard = new SchiffeVersenkenBoard();
    private SchiffeVersenkenBoard opponentBoard = new SchiffeVersenkenBoard();

    public SchiffeVersenkenEngine() {
        this.status = SchiffeVersenkenStatus.PLACE_SHIPS;
    }


    @Override
    public void receiveReihenfolgeWuerfeln(int random) throws IOException, StatusException  {
        if(this.status != SchiffeVersenkenStatus.DICE_SENT) {
            throw new StatusException();
        }

        if(random > sentDice) {
            this.status = SchiffeVersenkenStatus.VERSENKEN_E;
        } else if (random < sentDice) {
            this.status = SchiffeVersenkenStatus.VERSENKEN_S;
        } else {
            this.status = SchiffeVersenkenStatus.SPIELSTART;
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
                ownBoard.setSpace(lastShotX, lastShotY, BoardSpace.SHOT_SHIP);
                break;
            case 1:
                ownBoard.setSpace(lastShotX, lastShotY, BoardSpace.SHOT_WATER);
                break;
            case 2:
                anzahlVersenktGegner++;
                ownBoard.setSpace(lastShotX, lastShotY, BoardSpace.SHOT_SHIP);
                try {
                    ownBoard.setSunk(lastShotX, lastShotY);
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

    @Override
    public void sendReihenfolgeWuerfeln(int random) throws IOException, StatusException {
        if(this.status != SchiffeVersenkenStatus.SPIELSTART) {
            throw new StatusException();
        }

        //TODO: Sende Wert über TCP
        this.sentDice = random;

        this.status = SchiffeVersenkenStatus.DICE_SENT;
    }

    @Override
    public void sendKoordinate(int zeile, int spalte) throws IOException, StatusException {
        if(this.status != SchiffeVersenkenStatus.VERSENKEN_S) {
            throw new StatusException();
        }

        lastShotX = zeile;
        lastShotY = spalte;
        //TODO: Sende Werte über TCP

        this.status = SchiffeVersenkenStatus.BESTAETIGEN_E;

    }

    @Override
    public void sendKapitulation() throws IOException, StatusException {
        if(this.status != SchiffeVersenkenStatus.VERSENKEN_S) {
            throw new StatusException();
        }

        //TODO: Sende Kapitulation

        gewonnen = false;
        this.status = SchiffeVersenkenStatus.SPIELENDE;

    }

    @Override
    public void sendBestaetigen(int status) throws IOException, StatusException {
        if(this.status != SchiffeVersenkenStatus.BESTAETIGEN_S) {
            throw new StatusException();
        }

        //TODO: Sende Status
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
}
