package SchiffeVersenken;

import schiffeVersenken.SchiffeVersenkenException;
import schiffeVersenken.SchiffeVersenkenReceiver;
import schiffeVersenken.SchiffeVersenkenSender;
import schiffeVersenken.StatusException;

import java.io.IOException;

public class Shortcut implements SchiffeVersenkenSender {

    private SchiffeVersenkenReceiver receiver;


    public void setReceiver(SchiffeVersenkenReceiver receiver) {

        this.receiver = receiver;
    }

    @Override
    public void sendReihenfolgeWuerfeln(int random) throws IOException, StatusException {
        this.receiver.receiveReihenfolgeWuerfeln(random);
    }

    @Override
    public void sendKoordinate(int zeile, int spalte) throws IOException, StatusException, SchiffeVersenkenException {
        this.receiver.receiveKoordinate(zeile, spalte);
    }

    @Override
    public void sendKapitulation() throws IOException, StatusException {
        this.receiver.receiveKapitulation();
    }

    @Override
    public void sendBestaetigen(int status) throws IOException, StatusException, SchiffeVersenkenException {
        this.receiver.receiveBestaetigen(status);
    }
}
