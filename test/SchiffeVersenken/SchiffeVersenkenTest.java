package SchiffeVersenken;

import org.junit.Assert;
import org.junit.Test;
import schiffeVersenken.*;
import schiffeVersenken.SchiffeVersenkenException;
import schiffeVersenken.StatusException;
import schiffeVersenken.SchiffeVersenkenEngine;
import schiffeVersenken.SchiffeVersenkenUsage;
import schiffeVersenken.SchiffeVersenkenStatus;

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

}
