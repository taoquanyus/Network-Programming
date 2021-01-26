import java.io.*;
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
        while(true) {
            try {
                String default_path="/Users/quanyu/Desktop/Server/files/";
                Socket socket = serverSocket.accept();
                OutputStream outputStream=socket.getOutputStream();
                OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream);
                PrintWriter printWriter=new PrintWriter(outputStream,true);

                StringBuilder sb=new StringBuilder();
                File file=new File(default_path);
                File[]fs=file.listFiles();
                String temp;
                for(File f:fs){
                    if(!f.isDirectory()) sb.append(f.getName());
                    sb.append(" ");
                }

                printWriter.println(sb.toString());
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String filename=br.readLine();
                String FileDirectory=default_path+filename;
                FileTransferClient ServerSend=new FileTransferClient(socket,FileDirectory);
                ServerSend.sendFile();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
