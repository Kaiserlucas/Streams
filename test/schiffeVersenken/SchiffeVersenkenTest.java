package schiffeVersenken;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SchiffeVersenkenTest {

    @Test
    public void usageTest() throws SchiffeVersenkenException, StatusException, IOException {

        Shortcut sender1 = new Shortcut();
        SchiffeVersenkenEngine game1 = new SchiffeVersenkenEngine(sender1);

        Shortcut sender2 = new Shortcut();
        SchiffeVersenkenEngine game2 = new SchiffeVersenkenEngine(sender2);

        // Connect both games
        sender1.setReceiver(game2);
        sender2.setReceiver(game1);

        game1.doDice();
        game2.doDice();

        SchiffeVersenkenEngine activeGame = game1.isMyTurn() ? game1 : game2;
        SchiffeVersenkenEngine otherGame = game1.isMyTurn() ? game2 : game1;

        activeGame.doTurn(0,0);
        Assert.assertFalse(activeGame.isMyTurn());
        otherGame.doTurn(9,9);
        Assert.assertFalse(otherGame.isMyTurn());
        assert(activeGame.isMyTurn());

        activeGame.giveUp();

        assert(activeGame.getStatus() == SchiffeVersenkenStatus.SPIELENDE);
        assert(otherGame.getStatus() == SchiffeVersenkenStatus.SPIELENDE);

    }

    @Test
    public void usageSchlechtTest() throws SchiffeVersenkenException, IOException, StatusException {

        Shortcut sender1 = new Shortcut();
        SchiffeVersenkenEngine game1 = new SchiffeVersenkenEngine(sender1);

        Shortcut sender2 = new Shortcut();
        SchiffeVersenkenEngine game2 = new SchiffeVersenkenEngine(sender2);

        // Connect both games
        sender1.setReceiver(game2);
        sender2.setReceiver(game1);

        // Attempting to do turn before dice
        try {
            game1.doTurn(0, 0);
        } catch(StatusException e) {

        }


        // Dice twice in a row
        game1.doDice();
        try {
            game1.doDice();
        } catch (StatusException e) {

        }
        game2.doDice();

        SchiffeVersenkenUsage activeGame = game1.isMyTurn() ? game1 : game2;
        SchiffeVersenkenUsage otherGame = game1.isMyTurn() ? game2 : game1;

        // Wrong game makes a turn
        try {
            otherGame.doTurn(0, 0);
            Assert.fail();
        } catch (StatusException e) {

        }

        // Game tries to give up outside its turn
        try {
            otherGame.giveUp();
            Assert.fail();
        } catch (StatusException e) {

        }

        // Active game makes a turn out of board boundaries
        try {
            activeGame.doTurn(1, 10);
            Assert.fail();
        } catch (SchiffeVersenkenException e) {

        }


    }

    @Test
    public void Spieltest() throws IOException, StatusException, SchiffeVersenkenException {
        Shortcut sender1 = new Shortcut();
        SchiffeVersenkenEngine game1 = new SchiffeVersenkenEngine(sender1);

        Shortcut sender2 = new Shortcut();
        SchiffeVersenkenEngine game2 = new SchiffeVersenkenEngine(sender2);

        // Connect both games
        sender1.setReceiver(game2);
        sender2.setReceiver(game1);

        game1.doDice();
        game2.doDice();

        SchiffeVersenkenEngine activeGame = game1.isMyTurn() ? game1 : game2;
        SchiffeVersenkenEngine otherGame = game1.isMyTurn() ? game2 : game1;

        otherGame.setDebugBoard();

        activeGame.doTurn(0,0);
        otherGame.doTurn(0,0);
        activeGame.doTurn(0,1);
        otherGame.doTurn(0,0);
        activeGame.doTurn(0,2);
        otherGame.doTurn(0,0);
        activeGame.doTurn(2,2);
        otherGame.doTurn(0,0);
        activeGame.doTurn(3,3);
        otherGame.doTurn(0,0);
        activeGame.doTurn(4,4);
        otherGame.doTurn(0,0);
        activeGame.doTurn(5,5);
        otherGame.doTurn(0,0);
        activeGame.doTurn(6,6);
        otherGame.doTurn(0,0);
        activeGame.doTurn(7,7);
        otherGame.doTurn(0,0);
        activeGame.doTurn(8,8);
        otherGame.doTurn(0,0);
        activeGame.doTurn(9,9);
        otherGame.doTurn(0,0);
        activeGame.doTurn(0,5);

        assert(activeGame.getStatus() == SchiffeVersenkenStatus.SPIELENDE);
        assert(otherGame.getStatus() == SchiffeVersenkenStatus.SPIELENDE);

    }


}
