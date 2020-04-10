package filepersistence;

import java.io.*;

public class WriteAndReadDataSet{
    public static void main(String[] args) {
        // three example data sets
        String sensorName = "MyGoodOldSensor"; // does not change

        long timeStamps[] = new long[3];
        timeStamps[0] = System.currentTimeMillis();
        timeStamps[1] = timeStamps[0] + 1; // milli sec later
        timeStamps[2] = timeStamps[1] + 1000; // second later

        float[][] values = new float[3][];
        // 1st measure .. just one value
        float[] valueSet = new float[1];
        values[0] = valueSet;
        valueSet[0] = (float) 1.5; // example value 1.5 degrees

        // 2nd measure .. just three values
        valueSet = new float[3];
        values[1] = valueSet;
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;
        valueSet[2] = (float) 2.1;

        // 3rd measure .. two values
        valueSet = new float[2];
        values[2] = valueSet;
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;

        WriteAndReadDataSet set = new WriteAndReadDataSet();

        // write three data set into a file
        OutputStream os = null;
        try {
            String filename = "data.txt";
            os = new FileOutputStream(filename);

        } catch (FileNotFoundException ex) {
            System.err.println("couldn’t open file - fatal");
            System.exit(0);

        }

        /*
         Writes all available data sets in the following format:

         int number of data sets

         then for each data set:

         int number of characters in sensor name
         string sensor name
         long timestamp
         int number of temperature values in the current data set
         float temperature values
         */
        set.writeInt(os, timeStamps.length);
        for(int i = 0; i < timeStamps.length; i++) {
            set.writeInt(os, sensorName.length());
            set.writeString(os, sensorName);
            set.writeLong(os,timeStamps[i]);
            float[] tempArray = values[i];
            set.writeInt(os, tempArray.length);
            for(int j = 0; j < tempArray.length; j++) {
                set.writeFloat(os, tempArray[j]);
            }
        }

        // read data from file and print to System.out
        InputStream is = null;
        try {
            String filename = "data.txt";
            is = new FileInputStream(filename);

        } catch (FileNotFoundException ex) {
            System.err.println("couldn’t open file - fatal");
            System.exit(0);

        }


        int dataSets = set.readInt(is);
        for(int i = 0; i < dataSets; i++) {
            System.out.println("Data set "+(i+1)+":");
            int nameLength = set.readInt(is);
            System.out.print("Sensor name: ");
            for (int j = 0; j < nameLength; j++) {
                System.out.print(set.readChar(is));
            }
            System.out.println("");

            System.out.println("Timestamp: "+set.readLong(is));

            int temperatureValues = set.readInt(is);
            for(int j = 0; j < temperatureValues; j++) {
               System.out.println("Value "+(j+1)+": "+set.readFloat(is));
            }

            System.out.println("");
        }

    }


    /*
    A couple of Methods that take inputs and write it to the file bound to the FileOutputStream.
    Takes an existing OutputStream and the variable you write to the file as parameters.
     */
    public void writeString(OutputStream pos, String text) {
        DataOutputStream dos = new DataOutputStream(pos);

        try {
            dos.writeChars(text);
        } catch (IOException ex) {
            System.err.println("couldn’t write data (fatal)");
            System.exit(0);
        }

    }

    public void writeInt(OutputStream pos, int value) {
        DataOutputStream dos = new DataOutputStream(pos);

        try {
            dos.writeInt(value);
        } catch (IOException ex) {
            System.err.println("couldn’t write data (fatal)");
            System.exit(0);
        }

    }

    public void writeLong(OutputStream pos, long value) {
        DataOutputStream dos = new DataOutputStream(pos);

        try {
            dos.writeLong(value);
        } catch (IOException ex) {
            System.err.println("couldn’t write data (fatal)");
            System.exit(0);
        }

    }

    public void writeFloat(OutputStream pos, float value) {
        DataOutputStream dos = new DataOutputStream(pos);

        try {
            dos.writeFloat(value);
        } catch (IOException ex) {
            System.err.println("couldn’t write data (fatal)");
            System.exit(0);
        }

    }

    /*
    Just as the write methods above, this is a set of read methods that read information from the file bound to
    the InputStream given as a parameter. The methods only read the next instance of the asked for data type.
    Takes and InputStream to read from as a parameter and returns the data type specified in the method name.
     */

    public float readFloat(InputStream pis) {
        DataInputStream dis = new DataInputStream(pis);
        float readValue = -1;
        try {
            readValue = dis.readFloat();
        } catch (IOException ex) {
            System.err.println("couldn’t read data (fatal). File seems to be corrupted.");
            System.exit(0);
        }

        return readValue;
    }

    public char readChar(InputStream pis) {
        DataInputStream dis = new DataInputStream(pis);
        char readValue = 'a';
        try {
            readValue = dis.readChar();
        } catch (IOException ex) {
            System.err.println("couldn’t read data (fatal). File seems to be corrupted.");
            System.exit(0);
        }

        return readValue;
    }

    public long readLong(InputStream pis) {
        DataInputStream dis = new DataInputStream(pis);
        long readValue = -1;
        try {
            readValue = dis.readLong();
        } catch (IOException ex) {
            System.err.println("couldn’t read data (fatal). File seems to be corrupted.");
            System.exit(0);
        }

        return readValue;
    }

    public int readInt(InputStream pis) {
        DataInputStream dis = new DataInputStream(pis);
        int readValue = -1;
        try {
            readValue = dis.readInt();
        } catch (IOException ex) {
            System.err.println("couldn’t read data (fatal). File seems to be corrupted.");
            System.exit(0);
        }

        return readValue;
    }

}

