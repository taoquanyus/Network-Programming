import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CheckServer {
    private ServerSocket serverSocket;
    private int port;
    private List<Socket> socketList = new ArrayList<>();//  用来存socket
    public CheckServer(int port) {
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

    public void start() {
        //建立socket链接，接受获得的信息
        while(true){
            try{
                Socket socket=serverSocket.accept();
                socketList.add(socket);
                System.out.println("#1check Server New connect from port:"+socket.getPort());
                //用来建立socket链接，并返回注册结果或登陆结果
                Thread ChecksubThread=new Thread(new CheckThread(socket,socketList));
                ChecksubThread.start();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class CheckThread implements Runnable {
        private Socket socket;
        private List<Socket> socketList;
        public CheckThread(Socket socket,List<Socket> socketList){
            this.socket = socket;
            this.socketList = socketList;
        }


        @Override
        public void run() {
            //原理是发送功能+空格+用户名+空格+密码（+空格+昵称）
            //如果是注册则不返回
            //如果是登陆则返回字符串true/false；
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);
                    String s=br.readLine();
                    String[] parts=s.split(" ");
                    if(parts[0].equals("1")){//注册
                        String user_username=parts[1];
                        String user_password=parts[2];
                        String nickname=parts[3];
                        UserRegister newUser=new UserRegister(user_username,user_password,nickname);
                    }
                    if(parts[0].equals("2")){//登陆
                        String user_username=parts[1];
                        String user_password=parts[2];
                        UserSignin newClient=new UserSignin(user_username,user_password);

                        OutputStream outputStream=socket.getOutputStream();
                        OutputStreamWriter osw=new OutputStreamWriter(outputStream);
                        PrintWriter pw=new PrintWriter(osw,true);
                        String nickname=newClient.signin();
                        if(nickname==null){
                            //登陆成功
                            //返回昵称
                            pw.println("false");
                        }
                        else pw.println(nickname);
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
