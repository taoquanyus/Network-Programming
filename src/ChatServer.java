import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("all")
public class ChatServer {

    private SSLServerSocket serverSocket;
    private int port;
    private List<SSLSocket> socketList = new ArrayList<>();//  用来存socket
    

    public ChatServer(int port) throws IOException {
        this.port = port;
        start();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void start() throws IOException {

//        ChatServer chatServer = new ChatServer(port);
        System.setProperty("javax.net.ssl.keyStore", "/Users/quanyu/Desktop/Files/project/socket编程project/ChatRoom/Server/secure/kserver.keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "227195");
        System.setProperty("javax.net.ssl.trustStore", "/Users/quanyu/Desktop/Files/project/socket编程project/ChatRoom/Server/secure/tserver.keystore");
        System.setProperty("javax.net.ssl.trustStorePassword", "227195");
//            serverSocket = new ServerSocket(port);
        SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory
                .getDefault();
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory
                .createServerSocket(port);
        // 要求客户端身份验证
        serverSocket.setNeedClientAuth(true);
        while (true) {
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            socketList.add(socket);
            System.out.println("#2 chat Server New Connect from port： " + socket.getPort());
            Thread receiveThread = new Thread(new ReceiveThread(socket, socketList));
            receiveThread.start();
            SendMessage sendMessage = new SendMessage(socketList);
            Thread thread = new Thread(sendMessage);
            thread.start();
        }
    }


    public class SendMessage implements Runnable {

        private List<SSLSocket> socketList;

        public SendMessage(List<SSLSocket> socketList) {
            this.socketList = socketList;
        }

        @Override
        public void run() {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();
                for (Socket socket1 : socketList) {
                    Thread sendThread = new Thread(new SendThread(socket1, s));
                    sendThread.start();
                }
            }
        }
    }

    public class SendThread implements Runnable {

        private Socket socket;
        private String s;

        public SendThread(Socket socket) {
            this.socket = socket;
        }

        public SendThread(Socket socket, String s) {
            this.socket = socket;
            this.s = s;
        }

        @Override
        public void run() {
            try {
                OutputStream outputStream = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(outputStream);
                PrintWriter pw = new PrintWriter(osw, true);
                pw.println(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ReceiveThread implements Runnable {

        private Socket socket;
        private List<SSLSocket> socketList;

        public ReceiveThread(Socket socket, List<SSLSocket> socketList) {
            this.socket = socket;
            this.socketList = socketList;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);
                while (true) {
//                    int read=br.read();
                    /*if(read==-1){
                        //这两行应该是告诉其他用户，socket已经断开了连接
                        int port=socket.getPort();
                        System.out.println("Socket from port "+port+" is disconnected");
                        socketList.remove(socket);
                        socket.shutdownOutput();
                        socket.shutdownInput();
                        socket.close();
                        break;
                    }*/
                    String s = br.readLine();
                    if (s == null) {
                        //这两行应该是告诉其他用户，socket已经断开了连接
                        int port = socket.getPort();
                        System.out.println("Socket from port " + port + " is disconnected");
                        socketList.remove(socket);
                        socket.shutdownOutput();
                        socket.shutdownInput();
                        socket.close();
                        break;
                    }
                    System.out.println(s);
                    for (int i = 0; i < socketList.size(); ++i) {
                        Socket socket1 = socketList.get(i);
                        /*if(socket1.isClosed()){
                            socketList.remove(i);
                            continue;
                        }*/
                        Thread sendThread = new Thread(new SendThread(socket1, s));
                        sendThread.start();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


