package schiffeVersenken.ui;

import schiffeVersenken.SchiffeVersenkenEngine;
import schiffeVersenken.SchiffeVersenkenException;
import schiffeVersenken.SchiffeVersenkenStatus;
import schiffeVersenken.StatusException;
import schiffeVersenken.protocolBinding.StreamBindingReceiver;
import schiffeVersenken.protocolBinding.StreamBindingSender;
import transmission.DataConnection;
import transmission.DataConnector;

import java.io.IOException;
import java.net.UnknownHostException;

public class SchiffeVersenkenUI {

    static Console console = new Console();

    public static void main(String[] args) {

        //////////////////////////////////////////////////////////////////////////////////////////
        ////                             Initialize Objects                                   ////
        //////////////////////////////////////////////////////////////////////////////////////////

        DataConnection connection = null;
        SchiffeVersenkenEngine engine = null;
        StreamBindingReceiver receiver = null;
        boolean again = true;

        System.out.println("-Schiffe Versenken-");
        do {
            try {
                connection = establishConnection();
                again = false;
            } catch (Exception e) {
                System.err.println("Es konnte keine Verbindung aufgebaut werden. Versuche es noch einmal.");
            }
        } while(again);

        try {
            engine = new SchiffeVersenkenEngine(new StreamBindingSender(connection.getDataOutputStream()));
        } catch (IOException e) {
            System.err.println("Verbindung zum Gegenspieler verloren. Das Spiel muss abgebrochen werden.");
            System.exit(-1);
        }

        try {
            receiver = new StreamBindingReceiver(connection.getDataInputStream(), engine);
            receiver.start();
        } catch (IOException e) {
            System.err.println("Verbindung zum Gegenspieler verloren. Das Spiel muss abgebrochen werden.");
            System.exit(-1);
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        ////                                   Game logic                                     ////
        //////////////////////////////////////////////////////////////////////////////////////////

        setShips(engine);

        try {
            engine.doDice();
        } catch (StatusException e) {
            System.err.println("Status Exception while trying to do dice. Game was in the "+engine.getStatus()+" state.");
            System.err.println("Spiel wird abgebrochen.");
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Verbindung zum Gegenspieler verloren. Das Spiel muss abgebrochen werden.");
            System.exit(-1);
        }

        if(engine.getStatus() == SchiffeVersenkenStatus.DICE_SENT ) {
            System.out.println("Gegenspieler setzt noch seine Schiffe....");
            while(engine.getStatus() == SchiffeVersenkenStatus.DICE_SENT ) {
                //Do nothing. Just wait for your opponent
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if(engine.getStatus() == SchiffeVersenkenStatus.VERSENKEN_S ) {
            System.out.println("Du machst den ersten Zug.");
        } else {
            System.out.println("Der Gegner macht den ersten Zug.");
        }

        while(engine.getStatus() != SchiffeVersenkenStatus.SPIELENDE) {
            System.out.print(" ");
            if(engine.getStatus() == SchiffeVersenkenStatus.VERSENKEN_E) {
                engine.printOwnBoard();
                engine.printOpponentBoard();
                System.out.println("Gegner macht seinen Zug...");
                while(engine.getStatus() == SchiffeVersenkenStatus.VERSENKEN_E) {
                    //Do Nothing. Just wait.
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(engine.getStatus() == SchiffeVersenkenStatus.VERSENKEN_S) {
                engine.printOwnBoard();
                engine.printOpponentBoard();
                System.out.println("Du bist am Zug.");

                while(engine.getStatus() == SchiffeVersenkenStatus.VERSENKEN_S) {
                    int zeileZug = 0;
                    int spalteZug = 0;
                    do {
                        zeileZug = console.readIntegerFromStdin("Gib die Zeile des Feldes an welches du beschießen möchtest: ") - 1;
                        spalteZug = console.readIntegerFromStdin("Gib die Spalte des Feldes an welches du beschießen möchtest: ") - 1;

                        if(spalteZug < 0 || spalteZug > 9 || zeileZug < 0 || zeileZug > 9) {
                            System.out.println("Ungültige Eingabe. Zug wird wiederholt.");
                        }
                    } while(spalteZug < 0 || spalteZug > 9 || zeileZug < 0 || zeileZug > 9);

                    try {
                        engine.doTurn(zeileZug, spalteZug);
                    } catch (SchiffeVersenkenException e) {
                        System.err.println("Ungültige Koordinaten. Zug wird wiederholt.");
                    } catch (StatusException e) {
                        System.err.println("Status Exception while trying to do turn. Game was in the "+engine.getStatus()+" state.");
                        System.err.println("Spiel wird abgebrochen.");
                        System.exit(-1);
                    } catch (IOException e) {
                        System.err.println("Verbindung zum Gegenspieler verloren. Das Spiel muss abgebrochen werden.");
                        System.exit(-1);
                    }
                }
            }
            System.out.print(" ");
        }

        System.out.println("");
        if(engine.checkWon()) {
            System.out.println("Herzlichen Glückwunsch! Alle feindlichen Schiffe wurden erfolgreich versenkt.");
        } else {
            System.out.println("Taktischer Rückzug! ...zumindest würde ich das sagen wenn du noch Schiffe übrig hättest. Deine Flotte wurde restlos versenkt.");
        }

    }

    private static DataConnection establishConnection() throws IllegalArgumentException, InterruptedException, UnknownHostException, IOException {
        int input;
        boolean again = true;
        int port;
        String address;
        DataConnection connection = null;

        System.out.printf("Soll ein Spiel gehostet werden oder möchtest du einem offenen Spiel beitreten? \n \n [1] Hosten \n [2] Beitreten \n");

        do {
            input = console.readIntegerFromStdin("\nBitte Auswahl eingeben: ");

            switch(input) {
                case 1:
                    //Server
                    port = console.readIntegerFromStdin("Bitte Portnummer eingeben: ");
                    System.out.println("Warten auf Beitritt durch Gegenspieler...");
                    connection = new DataConnector(port);
                    System.out.println("Gegenspieler gefunden!");
                    again = false;
                    break;
                case 2:
                    //Client
                    address = console.readStringFromStdin("Bitte IP-Addresse des Gegenspielers eingeben: ");
                    port = console.readIntegerFromStdin("Bitte Portnummer eingeben: ");
                    connection = new DataConnector(address, port);
                    again = false;
                    break;
                default:
                    System.out.println("Ungültiger Wert. Eingabe wird wiederholt.");
                    break;
            }
        } while(again);

        return connection;
    }

    private static void setShips(SchiffeVersenkenEngine engine) {
        int fiveTileShips = 1;
        int fourTileShips = 2;
        int threeTileShips = 3;
        int twoTileShips = 4;

        while(fiveTileShips+fourTileShips+threeTileShips+twoTileShips > 0) {
            engine.printOwnBoard();
            System.out.println("Übrige Schiffe:");
            System.out.println("5 Felder Schiffe x" + fiveTileShips);
            System.out.println("4 Felder Schiffe x" + fourTileShips);
            System.out.println("3 Felder Schiffe x" + threeTileShips);
            System.out.println("2 Felder Schiffe x" + twoTileShips);

            int zeile1 = console.readIntegerFromStdin("Bitte gib die Zeile des einen Endstücks eines Schiffes ein: ")-1;
            int zeile2 = console.readIntegerFromStdin("Bitte gib die Spalte des einen Endstücks eines Schiffes ein: ")-1;
            int spalte1 = console.readIntegerFromStdin("Bitte gib die Zeile des anderen Endstücks eines Schiffes ein: ")-1;
            int spalte2 = console.readIntegerFromStdin("Bitte gib die Spalte des anderen Endstücks eines Schiffes ein: ")-1;
            int length = getShipLength(zeile1, zeile2, spalte1, spalte2);

            try {
                switch (length) {
                    case -1:
                        System.out.println("Die angegebenen Koordinaten ergeben kein horizontales oder vertikales Schiff. Erneute Eingabe.");
                        break;
                    case 2:
                        if (twoTileShips == 0) {
                            System.out.println("Du hast kein Schiff der Länge 2 mehr übrig. Bitte setze ein anderes.");
                            break;
                        }
                        if (engine.setShip(zeile1, zeile2, spalte1, spalte2)) {
                            twoTileShips--;
                        } else {
                            System.out.println("Schiffe dürfen nicht aneinander angrenzen. Bitte wähle eine andere Position.");
                        }
                        break;
                    case 3:
                        if (threeTileShips == 0) {
                            System.out.println("Du hast kein Schiff der Länge 3 mehr übrig. Bitte setze ein anderes.");
                            break;
                        }
                        if (engine.setShip(zeile1, zeile2, spalte1, spalte2)) {
                            threeTileShips--;
                        } else {
                            System.out.println("Schiffe dürfen nicht aneinander angrenzen. Bitte wähle eine andere Position.");
                        }
                        break;
                    case 4:
                        if (fourTileShips == 0) {
                            System.out.println("Du hast kein Schiff der Länge 4 mehr übrig. Bitte setze ein anderes.");
                            break;
                        }
                        if (engine.setShip(zeile1, zeile2, spalte1, spalte2)) {
                            fourTileShips--;
                        } else {
                            System.out.println("Schiffe dürfen nicht aneinander angrenzen. Bitte wähle eine andere Position.");
                        }
                        break;
                    case 5:
                        if (fiveTileShips == 0) {
                            System.out.println("Du hast kein Schiff der Länge 5 mehr übrig. Bitte setze ein anderes.");
                            break;
                        }
                        if (engine.setShip(zeile1, zeile2, spalte1, spalte2)) {
                            fiveTileShips--;
                        } else {
                            System.out.println("Schiffe dürfen nicht aneinander angrenzen. Bitte wähle eine andere Position.");
                        }
                        break;
                    case 100:
                        engine.setDebugBoard();
                        fiveTileShips = 0;
                        fourTileShips = 0;
                        threeTileShips = 0;
                        twoTileShips = 0;
                        break;
                    default:
                        System.out.print("Es gibt kein Schiff der Länge " + length + ". Bitte setze ein anderes.");
                        break;
                }
            } catch (StatusException e) {
                e.printStackTrace();
            } catch (SchiffeVersenkenException e) {
                e.printStackTrace();
                System.out.println("Ungültiges Schiff. Eingabe Wiederholen.");
            }

        }

    }

    private static int getShipLength(int zeile1, int spalte1, int zeile2, int spalte2) {
        int counter = 1;
        if(zeile1 == zeile2) {

            int momentaneSpalte = Math.min(spalte1, spalte2);
            while(momentaneSpalte < Math.max(spalte1, spalte2)) {
                momentaneSpalte++;
                counter++;
            }

        } else if (spalte1 == spalte2) {

            int momentaneZeile = Math.min(zeile1, zeile2);
            while(momentaneZeile < Math.max(zeile1, zeile2)) {
                momentaneZeile++;
                counter++;
            }

        } else {
            return -1;
        }

        return counter;
    }

}
