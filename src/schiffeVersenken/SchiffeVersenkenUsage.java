package schiffeVersenken;

import java.io.IOException;

public interface SchiffeVersenkenUsage {

    /**
     * Figure out who starts
     */
    void doDice() throws StatusException, IOException;

    /**
     *
     * @return true if it's currently the player's turn
     */
    boolean isMyTurn();

    /**
     * Shoots at position x,y
     * @param x 0-9
     * @param y 0-9
     */
    void doTurn(int x, int y) throws SchiffeVersenkenException, StatusException, IOException;

    /**
     * End the game prematurely
     */
    void giveUp() throws StatusException, IOException;

    SchiffeVersenkenStatus getStatus();

    void printOwnBoard();

    void printOpponentBoard();

    boolean setShip(int zeile1, int zeile2, int spalte1, int spalte2) throws StatusException, SchiffeVersenkenException;

    boolean checkWon();
}
