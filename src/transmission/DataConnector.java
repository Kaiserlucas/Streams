package transmission;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DataConnector implements DataConnection {

    Socket socket;

    /**
     * Create client side - open connection to address / port
     * @param address
     */
    public DataConnector(String address, int port) {
        try {
            socket = new Socket(address, port);
        } catch(Exception ex) {

        }
    }

    /**
     * Create server side - open port on this port and wait for one client
     * @param port
     */
    public DataConnector(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            socket = server.accept();
        } catch(IOException ex) {

        }
    }

    @Override
    public DataInputStream getDataInputStream() throws IOException {
        InputStream is;
        try {
            is = socket.getInputStream();
        } catch(IOException ex) {
            throw ex;
        }
        DataInputStream dis = new DataInputStream(is);
        return dis;
    }

    @Override
    public DataOutputStream getDataOutputStream() throws IOException {
        OutputStream os;
        try {
            os = socket.getOutputStream();
        } catch(IOException ex) {
            throw ex;
        }
        DataOutputStream dos = new DataOutputStream(os);
        return dos;
    }
}
