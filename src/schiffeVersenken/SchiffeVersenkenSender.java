package schiffeVersenken;

import java.io.IOException;

public interface SchiffeVersenkenSender {

    /**
     * Erlaubt im Zustand "Spielstart", führt zu "Dice Sent"
     * @param random zufällige Integer Zahl
     */
    void sendReihenfolgeWuerfeln(int random) throws IOException, StatusException;

    /**
     * Erlaubt im Zustand "Versenken (Senden)", führt zu "Bestätigen (Empfangen)"
     * @param zeile
     * @param spalte
     */
    void sendKoordinate(int zeile, int spalte) throws IOException, StatusException, SchiffeVersenkenException;

    /**
     * Erlaubt im Zustand "Versenken (Senden)", führt zu "Spiel beenden"
     */
    void sendKapitulation() throws IOException, StatusException;

    /**
     * Erlaubt im Zustand "Bestätigen (Senden)", führt zu "Versenken (Senden)" oder "Spielende"
     * @param status 0 = Treffer; 1 = Verfehlt; 2 = Versenkt
     */
    void sendBestaetigen(int status) throws IOException, StatusException, SchiffeVersenkenException;

}
