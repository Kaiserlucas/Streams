package filepersistence;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class FileStorageTest {

    @Test
    public void storageTest() throws IOException {
        FileStorage storage = new SimpleIntegerStorage("Test.txt");
        storage.setValue(5);
        assert(storage.getValue() == 5);
        storage.appendValue(4);
        assert(storage.getValue(1) == 4);
        storage.clear();

        try {
            storage.getValue();
            Assert.fail();
        } catch(IOException e) {

        }

    }
}
