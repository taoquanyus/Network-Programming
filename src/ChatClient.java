
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;

    public ChatClient(){

        try {
            socket = new Socket("127.0.0.1", 9806);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){

        Thread sendThread = new Thread(new SendThread());
        Thread receiveThread = new Thread(new ReceiceThread());
        sendThread.start();
        receiveThread.start();
    }

    public static void main(String[] args){

        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }


    public class SendThread implements Runnable{

        @Override
        public void run() {
            try {
                OutputStream outputStream = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(outputStream);
                PrintWriter pw = new PrintWriter(osw, true);
                Scanner scanner = new Scanner(System.in);

                while (true){
                    String text_temp="client1 says: "+scanner.nextLine();
                    pw.println(text_temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ReceiceThread implements Runnable{

        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);

                while (true){
                    System.out.println(br.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
