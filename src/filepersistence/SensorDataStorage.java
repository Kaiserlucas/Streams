package filepersistence;

import java.io.*;

public interface SensorDataStorage {

    /**
     * This method can be called by a sensor to save a data set.
     * @param time UNIX time when measurement took place
     * @param values sensor data
     * @throws IOException if something unexpected happened. Insufficient right, medium broken, offline..
     */
    void saveData(long time, float[] values) throws IOException;

    /**
     * This method returns the timestamp of a data set specified by the parameter
     * @param dataset number of the data set you want the values of
     * @return timestamp of the specific data set
     * @throws IllegalArgumentException if the dataset parameter is negative or bigger than the number of saved sets
     */
    long getTimestamp(int dataset) throws IllegalArgumentException;

    /**
     * This method returns the temperature values of a data set specified by the parameter
     * @param dataset number of the data set you want the values of
     * @return array with all found values of the specific data set
     * @throws IllegalArgumentException if the dataset parameter is negative or bigger than the number of saved sets
     */
    float[] getValues(int dataset) throws IllegalArgumentException;

    /**
     * This method reads the current number of saved data sets from the size.txt file
     * @return current amount of data sets
     */
    int getSize();

    /**
     * This method writes the amount of data sets into the size.txt file
     * @param size amount of data sets currently saved
     * @throws IllegalArgumentException if a negative size is passed as an argument
     */
    void writeSize(int size) throws IllegalArgumentException;

    /**
     * This method prints all data in data.txt to System.out
     */
    void printAllData();

    /**
     * This method deletes all sets stored in data.txt and resets size.txt to 0
     */
    void deleteAllData();

}