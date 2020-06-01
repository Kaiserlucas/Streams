package schiffeVersenken;

import java.io.IOException;

public interface SchiffeVersenkenReceiver {

    /**
     * Erlaubt im Zustand "Dice Sent", führt zu "Versenken (Senden)" oder "Versenken (Empfangen)"
     * @param random Zufallszahl
     * @throws IOException
     * @throws StatusException Wenn nicht im Zustand "Dice Sent"
     */
    void receiveReihenfolgeWuerfeln(int random) throws IOException, StatusException;

    /**
     * Erlaubt im Zustand "Versenken (Empfangen)", führt zu "Bestätigen (Senden)"
     * @param zeile
     * @param spalte
     * @throws IOException
     * @throws StatusException Wenn nicht im Zustand "Versenken (Empfangen)"
     */
    void receiveKoordinate(int zeile, int spalte) throws IOException, StatusException, SchiffeVersenkenException;

    /**
     * Erlaubt im Zustand "Versenken (Empfangen)", führt zu "Spiel beenden"
     * @throws IOException
     * @throws StatusException Wenn nicht im Zustand "Versenken (Empfangen)"
     */
    void receiveKapitulation() throws IOException, StatusException;

    /**
     * Erlaubt im Zustand "Bestätigen (Empfangen)", führt zu "Versenken (Empfangen)" oder "Spielende"
     * @param status Gelesener Wert: 0 = Treffer; 1 = Verfehlt; 2 = Versenkt
     * @throws IOException
     * @throws StatusException Wenn nicht im Zustand "Bestätigen (Empfangen)"
     */
    void receiveBestaetigen(int status) throws IOException, StatusException, SchiffeVersenkenException;

}
