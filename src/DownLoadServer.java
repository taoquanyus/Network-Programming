import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DownLoadServer {
    private ServerSocket serverSocket;
    private int port;
    private List<Socket> socketList = new ArrayList<>();//  用来存socket
    public DownLoadServer(int port) {
        this.port=port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
    }
}
