package sensorData;

import filepersistence.SensorDataStorage;
import filepersistence.WriteAndReadDataSet;
import org.junit.Assert;
import org.junit.Test;
import transmission.DataConnection;
import transmission.DataConnector;

import java.io.IOException;

public class SensorDataTransmissionTests {
    private static final int PORTNUMBER = 9876;

    @Test
    public void gutTransmissionTest() throws IOException {
        // create example data set
        String sensorName = "MyGoodOldSensor"; // does not change
        long timeStamp = System.currentTimeMillis();
        float[] valueSet = new float[3];
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;
        valueSet[2] = (float) 2.1;

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                              receiver side                                        //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        // create storage
        SensorDataStorage dataStorage = new WriteAndReadDataSet();

        // create connections
        DataConnection receiverConnection = new DataConnector(PORTNUMBER);

        // create receiver
        SensorDataReceiver sensorDataReceiver = new SensorDataReceiver(receiverConnection, dataStorage);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                              sender side                                          //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        // create connections
        DataConnection senderConnection = new DataConnector("localhost", PORTNUMBER);

        // create sender
        SensorDataSender sensorDataSender = new SensorDataSender(senderConnection);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //                               execute communication and test                                      //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        // send data with TCP
        sensorDataSender.sendData(sensorName, timeStamp, valueSet);

        // test if stored
        SensorDataStorage dataStorageReceived = sensorDataReceiver.getStorage();
        dataStorageReceived.deleteAllData();

        sensorDataReceiver.receiveAndWriteData();

        //Test is too fast and checks file before the receiver is done writing -> Pause this thread for a short time
        try {
            Thread.sleep(100);
        } catch(InterruptedException ex) {
            System.out.println("Something went wrong in the test thread");
        }

        // Get values from storage
        String sensorNameReceived = dataStorageReceived.getName(1);
        long timeStampReceived = dataStorageReceived.getTimestamp(1);
        float[] valueSetReceived = dataStorageReceived.getValues(1);

        // test
        Assert.assertEquals(sensorName, sensorNameReceived);
        Assert.assertEquals(timeStamp, timeStampReceived);
        Assert.assertArrayEquals(valueSet, valueSetReceived, 0);
    }
}
