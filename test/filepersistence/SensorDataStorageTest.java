package filepersistence;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SensorDataStorageTest {

    @Test
    public void gutTestSize() {
        SensorDataStorage SDS = new WriteAndReadDataSet();

        SDS.writeSize(4);
        Assert.assertEquals(4,SDS.getSize());
    }

    @Test
    public void randTestSize() {
        SensorDataStorage SDS = new WriteAndReadDataSet();

        SDS.writeSize(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE,SDS.getSize());
    }

    @Test
    public void schlechtTestWriteSize() {
        SensorDataStorage SDS = new WriteAndReadDataSet();
        try {
            SDS.writeSize(-5);
            Assert.fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void gutTestSaveAndReadLong() throws IOException {
        SensorDataStorage SDS = new WriteAndReadDataSet();
        SDS.deleteAllData();
        long testValue = 55;
        float[] testArray = {1, -7, 2};
        SDS.saveData(testValue, testArray);
        testValue++;
        SDS.saveData(testValue, testArray);
        Assert.assertEquals(56, SDS.getTimestamp(2));
    }

    @Test
    public void gutTestSaveAndReadValues() throws IOException{
        SensorDataStorage SDS = new WriteAndReadDataSet();
        SDS.deleteAllData();
        long testValue = 55;
        float[] testArray = {1, -7, 2};
        SDS.saveData(testValue, testArray);
        testArray[1] = 3;
        SDS.saveData(testValue, testArray);
        Assert.assertArrayEquals(testArray, SDS.getValues(2), 0);
    }

    @Test
    public void gutTestSaveAndReadName() throws IOException{
        SensorDataStorage SDS = new WriteAndReadDataSet();
        SDS.deleteAllData();
        long testValue = 55;
        float[] testArray = {1, -7, 2};
        SDS.saveData(testValue, testArray);
        SDS.saveData(testValue, testArray);
        String name = "MyGoodOldSensor";
        Assert.assertEquals(name,SDS.getName(2));
    }

    @Test
    public void schlechtTestSaveAndReadLong() throws IllegalArgumentException, IOException {
        SensorDataStorage SDS = new WriteAndReadDataSet();
        SDS.deleteAllData();
        long testValue = 55;
        float[] testArray = {1, -7, 2};
        SDS.saveData(testValue, testArray);
        testValue++;
        SDS.saveData(testValue, testArray);
        try {
            SDS.getTimestamp(3);
            Assert.fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void schlechtTestSaveAndReadValues() throws IllegalArgumentException, IOException {
        SensorDataStorage SDS = new WriteAndReadDataSet();
        SDS.deleteAllData();
        long testValue = 55;
        float[] testArray = {1, -7, 2};
        SDS.saveData(testValue, testArray);
        testArray[1] = 3;
        SDS.saveData(testValue, testArray);
        try {
            SDS.getValues(3);
            Assert.fail();
        } catch (Exception e) {

        }
    }

    @Test
    public void gutTestSaveEmptyArray() throws IOException {
        SensorDataStorage SDS = new WriteAndReadDataSet();
        SDS.deleteAllData();
        long testValue = 55;
        float[] testArray = new float[3];
        SDS.saveData(testValue, testArray);
        Assert.assertArrayEquals(testArray, SDS.getValues(1),0);

    }

    @Test
    public void deleteTest() {
        SensorDataStorage SDS = new WriteAndReadDataSet();
        SDS.deleteAllData();
    }

    @Test
    public void printTest() {
        SensorDataStorage SDS = new WriteAndReadDataSet();
        SDS.printAllData();
    }

}
