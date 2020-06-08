package schiffeVersenken.protocolBinding;

import schiffeVersenken.SchiffeVersenkenSender;
import schiffeVersenken.StatusException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class StreamBindingSender implements SchiffeVersenkenSender {
    private final DataOutputStream dos;

    public StreamBindingSender(OutputStream os) {
        this.dos = new DataOutputStream(os);
    }

    @Override
    public void sendReihenfolgeWuerfeln(int random) throws IOException, StatusException {
        this.dos.writeInt(Commands.DICE);
        this.dos.writeInt(random);
    }

    @Override
    public void sendKoordinate(int zeile, int spalte) throws IOException, StatusException {
        this.dos.writeInt(Commands.COORDINATE);
        this.dos.writeInt(zeile);
        this.dos.writeInt(spalte);
    }

    @Override
    public void sendKapitulation() throws IOException, StatusException {
        this.dos.writeInt(Commands.GIVE_UP);
    }

    @Override
    public void sendBestaetigen(int status) throws IOException, StatusException {
        this.dos.writeInt(Commands.FEEDBACK);
        this.dos.writeInt(status);
    }
}
