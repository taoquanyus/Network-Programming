

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("all")
public class ChatServer {

    private ServerSocket serverSocket;
    private int port;
    private List<Socket> socketList = new ArrayList<>();//  用来存socket

    public ChatServer(int port){
        this.port=port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void start(){

//        ChatServer chatServer = new ChatServer(port);
        while (true){
            try {
                Socket socket = serverSocket.accept();
                socketList.add(socket);
                System.out.println("New Connect from port： " + socket.getPort());
                Thread receiveThread = new Thread(new ReceiveThread(socket,socketList));
                receiveThread.start();
                SendMessage sendMessage = new SendMessage(socketList);
                Thread thread = new Thread(sendMessage);
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class SendMessage implements Runnable{

        private List<Socket> socketList;

        public SendMessage(List<Socket> socketList){
            this.socketList = socketList;
        }

        @Override
        public void run() {
            while(true){
                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();
                for (Socket socket1 : socketList) {
                    Thread sendThread = new Thread(new SendThread(socket1, s));
                    sendThread.start();
                }
            }
        }
    }

    public class SendThread implements Runnable{

        private Socket socket;
        private String s;

        public SendThread(Socket socket){
            this.socket = socket;
        }

        public SendThread(Socket socket, String s){
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

    public class ReceiveThread implements Runnable{

        private Socket socket;
        private List<Socket> socketList;
        public ReceiveThread(Socket socket,List<Socket> socketList){
            this.socket = socket;
            this.socketList = socketList;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);
                while (true){
                    String s=br.readLine();
                    System.out.println(s);
                    for (Socket socket1 : socketList) {
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


