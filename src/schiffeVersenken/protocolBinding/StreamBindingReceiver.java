package schiffeVersenken.protocolBinding;

import schiffeVersenken.SchiffeVersenkenException;
import schiffeVersenken.SchiffeVersenkenReceiver;
import schiffeVersenken.StatusException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamBindingReceiver extends Thread {
    public final DataInputStream dis;
    private final SchiffeVersenkenReceiver receiver;

    public StreamBindingReceiver(InputStream is, SchiffeVersenkenReceiver receiever) {
        this.dis = new DataInputStream(is);
        this.receiver = receiever;
    }


    public void readReihenfolgeWuerfeln() throws IOException, StatusException {
        int random = dis.readInt();
        this.receiver.receiveReihenfolgeWuerfeln(random);
    }

    public void readKoordinate() throws IOException, StatusException, SchiffeVersenkenException  {
        int x = dis.readInt();
        int y = dis.readInt();
        this.receiver.receiveKoordinate(x,y);
    }

    public void readKapitulation() throws IOException, StatusException  {
        this.receiver.receiveKapitulation();
    }

    public void readBestaetigen() throws IOException, StatusException, SchiffeVersenkenException  {
        int status = dis.readInt();
        this.receiver.receiveBestaetigen(status);
    }

    public void run() {
        boolean again = true;
        while(again) {

            try {
                int command = this.dis.readInt();

                switch(command) {
                    case Commands.DICE:
                        this.readReihenfolgeWuerfeln();
                        break;
                    case Commands.COORDINATE:
                        this.readKoordinate();
                        break;
                    case Commands.GIVE_UP:
                        this.readKapitulation();
                        break;
                    case Commands.FEEDBACK:
                        this.readBestaetigen();
                        break;
                    default:
                        again = false;
                        System.err.println("Unknown command received: "+ command);

                }
            } catch (IOException e) {
                System.err.println("IOException: "+e.getLocalizedMessage());
                again = false;
            } catch (SchiffeVersenkenException e) {
                System.err.println("Forbidden Values: "+e.getLocalizedMessage());
                again = false;
            } catch (StatusException e) {
                System.err.println("StatusException: "+e.getLocalizedMessage());
                again = false;
            }
        }

    }

}
