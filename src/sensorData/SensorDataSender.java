package sensorData;

import filepersistence.SensorDataStorage;
import filepersistence.WriteAndReadDataSet;
import transmission.DataConnection;

import java.io.DataOutputStream;
import java.io.IOException;

public class SensorDataSender {
    private final DataConnection connection;

    public SensorDataSender(DataConnection connection) {
        this.connection = connection;
    }

    public void sendData(String name, long time, float[] values) throws IOException {

        DataOutputStream tcpOutputStream = connection.getDataOutputStream();
        WriteAndReadDataSet.writeInt(tcpOutputStream, values.length);
        WriteAndReadDataSet.writeString(tcpOutputStream, name);
        WriteAndReadDataSet.writeLong(tcpOutputStream, time);
        for(int i = 0; i < values.length; i++) {
            WriteAndReadDataSet.writeFloat(tcpOutputStream, values[i]);
        }
    }

}
