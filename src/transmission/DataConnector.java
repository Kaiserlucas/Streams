package transmission;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class DataConnector implements DataConnection, Runnable {

    Socket socket;
    private int port;

    /**
     * Create client side - open connection to address / port
     * @param address
     */
    public DataConnector(String address, int port) throws UnknownHostException, IOException {

            socket = new Socket(address, port);

    }

    /**
     * Create server side - open port on this port and wait for one client
     * @param port
     */
    public DataConnector(int port) throws InterruptedException {
        this.port = port;
        Thread acceptThread = new Thread(this);
        acceptThread.start();
        acceptThread.join();

    }

    @Override
    public DataInputStream getDataInputStream() throws IOException {
        InputStream is;
        is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        return dis;
    }

    @Override
    public DataOutputStream getDataOutputStream() throws IOException {
        OutputStream os;
        os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        return dos;
    }


    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(port);
            socket = server.accept();
        } catch(IOException ex) {

        }
    }
}
