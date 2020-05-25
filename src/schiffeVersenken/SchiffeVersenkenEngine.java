package schiffeVersenken;

import java.io.IOException;

public class SchiffeVersenkenEngine implements SchiffeVersenkenReceiver, SchiffeVersenkenSender {
    public static final int UNDEFINED_DICE = -1;
    private SchiffeVersenkenStatus status;
    private int sentDice = UNDEFINED_DICE;
    private int anzahlVersenktGegner = 0;
    private int anzahlVersenktSelber = 0;

    public SchiffeVersenkenEngine() {
        this.status = SchiffeVersenkenStatus.SPIELSTART;
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
    public void receiveKoordinate(int zeile, int spalte) throws IOException, StatusException  {
        if(this.status != SchiffeVersenkenStatus.VERSENKEN_E) {
            throw new StatusException();
        }

        this.status = SchiffeVersenkenStatus.BESTAETIGEN_S;
    }

    @Override
    public void receiveKapitulation() throws IOException, StatusException  {
        if(this.status != SchiffeVersenkenStatus.VERSENKEN_E) {
            throw new StatusException();
        }

        this.status = SchiffeVersenkenStatus.SPIELENDE;
    }

    @Override
    public void receiveBestaetigen(int status) throws IOException, StatusException  {
        if(this.status != SchiffeVersenkenStatus.BESTAETIGEN_E) {
            throw new StatusException();
        }

        if(status == 2) {
            anzahlVersenktGegner++;
        }


        if(anzahlVersenktGegner < 10) {
            this.status = SchiffeVersenkenStatus.VERSENKEN_E;
        } else {
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

        //TODO: Sende Werte über TCP

        this.status = SchiffeVersenkenStatus.BESTAETIGEN_E;

    }

    @Override
    public void sendKapitulation() throws IOException, StatusException {
        if(this.status != SchiffeVersenkenStatus.VERSENKEN_S) {
            throw new StatusException();
        }

        //TODO: Sende Kapitulation

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
            this.status = SchiffeVersenkenStatus.SPIELENDE;
        }

    }
}
