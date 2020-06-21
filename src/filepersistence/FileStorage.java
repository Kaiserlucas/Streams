package filepersistence;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface FileStorage {

    void setValue(int value) throws IOException;

    void appendValue(int value) throws IOException;

    int getValue() throws IOException;

    int getValue(int index) throws IOException;

    void clear() throws IOException;

    DataInputStream getDataInputStream(String filename) throws IOException;

    DataOutputStream getDataOutputStream(String filename) throws IOException;

    DataOutputStream getDataOutputStreamAppend(String filename) throws IOException;


}
