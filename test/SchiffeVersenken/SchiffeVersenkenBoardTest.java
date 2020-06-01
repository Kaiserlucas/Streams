package SchiffeVersenken;

import org.junit.Assert;
import org.junit.Test;
import schiffeVersenken.BoardSpace;
import schiffeVersenken.SchiffeVersenkenBoard;
import schiffeVersenken.SchiffeVersenkenException;

public class SchiffeVersenkenBoardTest {

    @Test
    public void setTest() throws SchiffeVersenkenException {
        SchiffeVersenkenBoard board = new SchiffeVersenkenBoard();
        board.setSpace(1, 2, BoardSpace.SHOT_WATER);
        assert(board.getSpace(1,2) == BoardSpace.SHOT_WATER);
    }

    @Test
    public void printTest() throws SchiffeVersenkenException {
        SchiffeVersenkenBoard board = new SchiffeVersenkenBoard();
        board.setSpace(1, 2, BoardSpace.SHOT_WATER);
        board.setSpace(1, 3, BoardSpace.SHIP);
        board.setSpace(1, 4, BoardSpace.SHIP);
        board.setSpace(1, 5, BoardSpace.SHIP);
        board.setSpace(1, 6, BoardSpace.SHOT_SHIP);

        board.setSpace(3, 6, BoardSpace.SUNK_SHIP);
        board.setSpace(4, 6, BoardSpace.SUNK_SHIP);
        board.setSpace(5, 6, BoardSpace.SUNK_SHIP);
        board.printBoard();

    }

    @Test
    public void sunkCheckTest() throws SchiffeVersenkenException {
        SchiffeVersenkenBoard board = new SchiffeVersenkenBoard();
        board.setSpace(1, 2, BoardSpace.SHOT_WATER);
        board.setSpace(1, 3, BoardSpace.SHIP);
        board.setSpace(1, 4, BoardSpace.SHIP);
        board.setSpace(1, 5, BoardSpace.SHIP);
        board.setSpace(1, 6, BoardSpace.SHOT_SHIP);

        board.setSpace(3, 6, BoardSpace.SHOT_SHIP);
        board.setSpace(4, 6, BoardSpace.SHOT_SHIP);
        board.setSpace(5, 6, BoardSpace.SHOT_SHIP);

        assert(board.checkSunk(4, 6));
        assert(board.checkSunk(3, 6));
        assert(board.checkSunk(5, 6));
        assert(!(board.checkSunk(1, 6)));
        assert(!(board.checkSunk(1, 5)));
        assert(!(board.checkSunk(7, 6)));
    }

    @Test
    public void setSunkTest() throws SchiffeVersenkenException {
        SchiffeVersenkenBoard board = new SchiffeVersenkenBoard();

        board.setSpace(3, 6, BoardSpace.SHOT_SHIP);
        board.setSpace(4, 6, BoardSpace.SHOT_SHIP);
        board.setSpace(5, 6, BoardSpace.SHOT_SHIP);

        try {
            board.setSunk(4, 6);
        } catch (Exception ex) {
            Assert.fail();
        }

        assert(board.getSpace(3, 6) == BoardSpace.SUNK_SHIP);
        assert(board.getSpace(4, 6) == BoardSpace.SUNK_SHIP);
        assert(board.getSpace(5, 6) == BoardSpace.SUNK_SHIP);
    }

}
