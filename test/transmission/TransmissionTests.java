package transmission;

import org.junit.Assert;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TransmissionTests {
    private static final int PORTNUMBER = 9876;
    private static final int TEST_INT = 42;

    @Test
    public void gutConnectionTest1() throws IOException {
        // open server side
        DataConnection serverSide = new DataConnector(PORTNUMBER);

        // open client side
        DataConnection clientSide = new DataConnector("localhost", PORTNUMBER);

        DataOutputStream dataOutputStream = clientSide.getDataOutputStream();
        dataOutputStream.writeInt(TEST_INT);

        DataInputStream dataInputStream = serverSide.getDataInputStream();
        int readValue = dataInputStream.readInt();

        Assert.assertEquals(TEST_INT, readValue);
    }


}
