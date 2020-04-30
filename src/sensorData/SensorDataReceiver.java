package sensorData;

import filepersistence.SensorDataStorage;
import filepersistence.WriteAndReadDataSet;
import transmission.DataConnection;

import java.io.DataInputStream;
import java.io.IOException;

public class SensorDataReceiver implements Runnable {
    private final DataConnection connection;
    private final SensorDataStorage storage;

    public SensorDataReceiver(DataConnection connection, SensorDataStorage storage) {
        this.connection = connection;
        this.storage = storage;
    }

    SensorDataStorage getStorage() {
        return storage;
    }

    private void receiveAndWriteDataInternal() throws IOException {
        DataInputStream tcpDataInputStream;
        try {
            tcpDataInputStream = connection.getDataInputStream();
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
        int valueNumber = WriteAndReadDataSet.readInt(tcpDataInputStream);
        WriteAndReadDataSet.readString(tcpDataInputStream);
        long time = WriteAndReadDataSet.readLong(tcpDataInputStream);
        float[] values = new float[valueNumber];
        for(int i = 0; i < valueNumber; i++) {
            values[i] = WriteAndReadDataSet.readFloat(tcpDataInputStream);
        }
        storage.saveData(time, values);
    }

    public void receiveAndWriteData() throws IOException {
        Thread writeThread = new Thread(this);
        writeThread.start();
    }

    @Override
    public void run() {
        try {
            receiveAndWriteDataInternal();
        } catch(IOException ex) {
            System.err.println(ex.getMessage());
            System.err.println("Couldn't read data");
        }
    }
}
