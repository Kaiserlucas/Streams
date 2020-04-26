package filepersistence;

import java.io.*;

public class WriteAndReadDataSet implements SensorDataStorage{
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

        /*
         delete all previously saved data before adding the test data sets
         (Just for cleaner looking testing. It would work without deleting old data beforehand as well
         but the file would get very large very quickly by running this program repeatedly)
         */
        set.deleteAllData();

        // Then saves all three test data sets in data.txt and prints them out.
        try {
            set.saveData(timeStamps[0], values[0]);
            set.saveData(timeStamps[1], values[1]);
            set.saveData(timeStamps[2], values[2]);
        } catch (Exception ex) {
            System.out.print(ex.getMessage()+" Data set could not be saved.");
        }

        set.printAllData();


    }


    /*
    A couple of Methods that take inputs and write it to the file bound to the FileOutputStream.
    Takes an existing OutputStream and the variable you write to the file as parameters.
     */

    public static void writeInt(DataOutputStream pdos, int value) {

        try {
            pdos.writeInt(value);
        } catch (IOException ex) {
            System.err.println("couldn’t write data (fatal)");
            System.exit(0);
        }

    }

    public static void writeLong(DataOutputStream pdos, long value) {

        try {
            pdos.writeLong(value);
        } catch (IOException ex) {
            System.err.println("couldn’t write data (fatal)");
            System.exit(0);
        }

    }

    public static void writeFloat(DataOutputStream pdos, float value) {

        try {
            pdos.writeFloat(value);
        } catch (IOException ex) {
            System.err.println("couldn’t write data (fatal)");
            System.exit(0);
        }

    }

    public static void writeString(DataOutputStream pdos, String text) {

        try {
            pdos.writeUTF(text);
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

    public static float readFloat(DataInputStream pdis) {

        float readValue = -1;
        try {
            readValue = pdis.readFloat();
        } catch (IOException ex) {
            System.err.println("couldn’t read float (fatal). File seems to be corrupted.");
            System.exit(0);
        }

        return readValue;
    }

    public static long readLong(DataInputStream pdis) {

        long readValue = -1;
        try {
            readValue = pdis.readLong();
        } catch (IOException ex) {
            System.err.println("couldn’t read long (fatal). File seems to be corrupted.");
            System.exit(0);
        }

        return readValue;
    }

    public static int readInt(DataInputStream pdis) {

        int readValue = -1;
        try {
            readValue = pdis.readInt();
        } catch (IOException ex) {
            System.err.println("couldn’t read int (fatal). File seems to be corrupted.");
            System.exit(0);
        }

        return readValue;
    }

    public static String readString(DataInputStream pdis) {

        String readText = "a";
        try {
            readText = pdis.readUTF();
        } catch (IOException ex) {
            System.err.println("couldn’t read string (fatal). File seems to be corrupted.");
            System.exit(0);
        }

        return readText;
    }

    private DataInputStream getDataInputStream(String filename) {
        InputStream is = null;
        try {
            is = new FileInputStream(filename);

        } catch (FileNotFoundException ex) {
            System.err.println("couldn’t open file - fatal");
            System.exit(0);

        }

        DataInputStream dis = null;
        try {
            dis = new DataInputStream(is);

        } catch (Exception ex) {
            System.err.println("couldn’t open file - fatal");
            System.exit(0);

        }
        return dis;
    }

    private DataOutputStream getDataOutputStream(String filename) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(filename, true);

        } catch (FileNotFoundException ex) {
            System.err.println("couldn’t open file - fatal");
            System.exit(0);

        }

        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(os);

        } catch (Exception ex) {
            System.err.println("couldn’t open file - fatal");
            System.exit(0);

        }
        return dos;
    }

    private DataOutputStream getDataOutputStreamNoAppend(String filename) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(filename);

        } catch (FileNotFoundException ex) {
            System.err.println("couldn’t open file - fatal");
            System.exit(0);

        }

        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(os);

        } catch (Exception ex) {
            System.err.println("couldn’t open file - fatal");
            System.exit(0);

        }
        return dos;
    }

    @Override
    public void saveData(long time, float[] values){
        DataOutputStream dos = getDataOutputStream("data.txt");

        int valueNumber = 0; // Counts the number of valid values contained inside of values[]

        // This for loop tests if the entire values[] array is filled or not and counts the number of included values.
        for(int i = 0; i < values.length; i++) {
            try {
                float temp = values[i];
                valueNumber++;
            } catch (NullPointerException ex) {
                System.err.println("Array was not completely filled with values");
                break;
            }
        }

        /*
        Then write the data set to data.txt in the following format:
        1st: int -> number of temperature values in the set
        2nd: String -> Sensor Name
        3rd: long -> timestamp
        4th: float -> the temperature values themselves
         */
        this.writeInt(dos, valueNumber);
        this.writeString(dos, "MyGoodOldSensor");
        this.writeLong(dos, time);

        for(int i = 0; i < valueNumber; i++) {
            this.writeFloat(dos, values[i]);
        }

        // Increase data size in size.txt by 1 to signify that a data set was added
        int currentSize = this.getSize();
        this.writeSize((currentSize+1));
    }

    @Override
    public long getTimestamp(int dataset) throws IllegalArgumentException {

        // checks if the parameter is valid
        int currentSize = this.getSize();
        if(dataset < 1 || dataset > currentSize) {
            IllegalArgumentException ex = new IllegalArgumentException("Invalid data set exception");
            throw  ex;
        }

        DataInputStream dis = getDataInputStream("data.txt");

        // skips to the correct place in the file if the dataset parameter was bigger than 1
        for(int i = 1; i < dataset; i++) {
            int nextSetValues = readInt(dis);
            readString(dis);
            for(int j = 0; j < (nextSetValues+2);j++) {
                readFloat(dis);
            }
        }

        // reads the timestamp value from the desired set and returns it
        readInt(dis);
        readString(dis);
        return readLong(dis);
    }

    @Override
    public float[] getValues(int dataset) throws IllegalArgumentException {

        // checks if the parameter is valid
        int currentSize = this.getSize();
        if(dataset < 1 || dataset > currentSize) {
            IllegalArgumentException ex = new IllegalArgumentException("Invalid data set exception");
            throw  ex;
        }

        DataInputStream dis = getDataInputStream("data.txt");

        // skips to the correct place in the file if the dataset parameter was bigger than 1
        for(int i = 1; i < dataset; i++) {
            int nextSetValues = readInt(dis);
            readString(dis);
            for(int j = 0; j < (nextSetValues+2);j++) {
                readFloat(dis);
            }
        }

        // writes the temperature values in the set to an array and returns it
        int numberOfValues = readInt(dis);
        readString(dis);
        readLong(dis);
        float[] data = new float[numberOfValues];

        for(int i = 0; i < numberOfValues; i++) {
            data[i] = readFloat(dis);
        }

        return data;
    }

    @Override
    public String getName(int dataset) throws IllegalArgumentException {
        // checks if the parameter is valid
        int currentSize = this.getSize();
        if(dataset < 1 || dataset > currentSize) {
            IllegalArgumentException ex = new IllegalArgumentException("Invalid data set exception");
            throw  ex;
        }

        DataInputStream dis = getDataInputStream("data.txt");

        // skips to the correct place in the file if the dataset parameter was bigger than 1
        for(int i = 1; i < dataset; i++) {
            int nextSetValues = readInt(dis);
            readString(dis);
            for(int j = 0; j < (nextSetValues+2);j++) {
                readFloat(dis);
            }
        }

        // reads the sensor name from the desired set and returns it
        readInt(dis);
        return readString(dis);
    }

    @Override
    public int getSize() {
        DataInputStream dis = getDataInputStream("size.txt");
        // just returns the value written to size.txt
        return this.readInt(dis);
    }

    @Override
    public void writeSize(int size) throws IllegalArgumentException {

        // checks if the parameter is valid
        if(size < 0) {
            IllegalArgumentException ex = new IllegalArgumentException("Negative data set size error");
            throw ex;
        }

        DataOutputStream dos = getDataOutputStreamNoAppend("size.txt");
        // just writes the parameter to size.txt
        this.writeInt(dos,size);
    }

    @Override
    public void printAllData() {

        DataInputStream disSize = getDataInputStream("size.txt");

        int currentSize = readInt(disSize); // saves the amount of total data sets

        DataInputStream disData = getDataInputStream("data.txt");

        // goes through the entire file and prints all relevant information
        for(int i = 0; i < currentSize; i++) {
            System.out.println("Data set "+(i+1)+":");
            int nextSetValues = readInt(disData);
            System.out.println("Sensor Name: "+readString(disData));
            System.out.println("Timestamp: "+readLong(disData));
            for(int j = 0; j < nextSetValues; j++) {
                System.out.println("Value "+(j+1)+": "+readFloat(disData));
            }
            System.out.println("");
        }
    }

    @Override
    public void deleteAllData() {

        //Empties both data.txt and size.txt of all data / creates them if they do not exist yet
        getDataOutputStreamNoAppend("data.txt");
        DataOutputStream dos = getDataOutputStreamNoAppend("size.txt");

        // initializes size.txt with the value 0
        writeInt(dos,0);
    }
}

