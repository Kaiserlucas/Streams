package filepersistence;

import java.io.*;

public class SimpleIntegerStorage implements FileStorage {

    private String filename;


    public SimpleIntegerStorage(String filename) throws IOException {
        this.filename = filename;
    }


    @Override
    public void setValue(int value) throws IOException {
        DataOutputStream dos = getDataOutputStream(filename);
        dos.writeInt(value);
    }

    @Override
    public void appendValue(int value) throws IOException {
        DataOutputStream dos = getDataOutputStreamAppend(filename);
        dos.writeInt(value);
    }

    @Override
    public int getValue() throws IOException {
        DataInputStream dis = getDataInputStream(filename);
        return dis.readInt();
    }

    @Override
    public int getValue(int index) throws IOException {
        DataInputStream dis = getDataInputStream(filename);
        for(int i = 0; i < index; i++) {
            dis.readInt();
        }
        return dis.readInt();
    }

    @Override
    public void clear() throws IOException {
        getDataOutputStream(filename);
    }

    @Override
    public DataInputStream getDataInputStream(String filename) throws IOException {

        InputStream is = new FileInputStream(filename);
        return new DataInputStream(is);

    }

    @Override
    public DataOutputStream getDataOutputStream(String filename) throws IOException {

        OutputStream os = new FileOutputStream(filename);
        return new DataOutputStream(os);

    }

    @Override
    public DataOutputStream getDataOutputStreamAppend(String filename) throws IOException {

        OutputStream os = new FileOutputStream(filename, true);
        return new DataOutputStream(os);

    }


}
