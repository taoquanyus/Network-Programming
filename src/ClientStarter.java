import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientStarter {
    public static boolean chatFlag;
    public static void main(String[] args) {
        System.out.println("Hi, Are you new here? \n please sign up or sign in");
        while(true){
            System.out.println("1.sign up\n2.sign in");
            Scanner scanner=new Scanner(System.in);
            String input=scanner.nextLine();
            if(input.equals("1"))//注册
            {
                System.out.println("please input your user name");
                String usrname= scanner.nextLine();
                System.out.println("please input your user password");
                String password= scanner.nextLine();
                System.out.println("please input your user nickname");
                String nickname= scanner.nextLine();
                ClientRegistered(usrname,password,nickname);

                System.out.println("successfully sign up! please sign in.");
            }
            else if(input.equals("2"))//登陆
            {
                System.out.println("please input your user name");
                String usrname= scanner.nextLine();
                System.out.println("please input your user password");
                String password= scanner.nextLine();
                String nickname=ResultOfSignIn(usrname,password);
                if(nickname!=null){
                    System.out.println("Hi "+nickname+"!");
                    break;
                }
                else{
                    System.out.println("fail to sign in\n please check your username and password");
                }
            }
            else {
                System.out.println("please just input number 1 or 2!");
            }
        }


        //下面应该是验证过后的
        System.out.println("welcome to chat room!");
        while(true){
            chatFlag=true;
            System.out.println("please select function:");
            System.out.println("1.chat\n2.upload file\n3.download file\n");
            Scanner scanner=new Scanner(System.in);
            String input=scanner.nextLine();
            if(input.equals("1")){//聊天模式
                //加入聊天室
                JoinChatRoom();
            }
            else if(input.equals("2")){//上传文件
                uploadFiles();

            }
            else if(input.equals("3")){//下载文件
                downloadFiles();
            }
            else {
                System.out.println("please just input number 1 , 2 or 3\n\n");
            }

        }


//        String username="tom";
//        String password="123456";
//        String nickname="tommy";
////        UserRegister nuser=new UserRegister(username,password,nickname);
//        UserSignin user=new UserSignin(username,password);
    }

    private static void downloadFiles() {
        //下载文件，使用端口号9999
        //下载完一个文件应该自动结束，返回成功下载
    }

    private static void uploadFiles() {
        //上传文件，使用端口号8888
        //上传完一个文件应该自动结束，返回成功上传
        Socket socket;
        String ServerAddress="127.0.0.1";
        int port=8888;
        System.out.println("please input the file directory you want to upload");
        Scanner scanner=new Scanner(System.in);
        String fileDirectory=scanner.nextLine();
        try{
            socket=new Socket(ServerAddress,port);
            FileTransferClient uploadclient=new FileTransferClient(socket,fileDirectory);
            uploadclient.sendFile();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void JoinChatRoom() {
        //建立socket链接，这个端口号就定为9806
        //长时间保持在JoinChatRoom中，输出指令'\quit'即可退出
        Socket socket;
        String ServerAddress="127.0.0.1";
        int port=9806;
        try{
            socket=new Socket(ServerAddress,port);
            Thread sendThread = new Thread(new SendThread(socket));
            Thread receiveThread = new Thread(new ReceiceThread(socket));
            sendThread.start();
            receiveThread.start();
            while(chatFlag){
                Thread.sleep(100);
            }//死循环
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String ResultOfSignIn(String usrname, String password) {
        //建立socket链接，端口号为9611
        //登陆，根据账号密码返回nickname
        //如果登陆失败，则返回null
        Socket socket;
        String ServerAddress="127.0.0.1";
        String sendContextGenerate="2 "+usrname+" "+password;
        try{
            socket = new Socket(ServerAddress,9611);
            OutputStream outputStream=socket.getOutputStream();
            OutputStreamWriter osw=new OutputStreamWriter(outputStream);
            PrintWriter pw=new PrintWriter(osw,true);
            pw.println(sendContextGenerate);
            InputStream inputStream=socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String nickname=br.readLine();
            socket.close();
            if(nickname.equals("false")) return null;
            return nickname;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static void ClientRegistered(String username, String password, String nickname) {
        //建立socket链接，端口号同样定为9611
        //把三个信息发过去
        //认证用的端口
        Socket socket;
        String ServerAddress="127.0.0.1";
        String sendContextGenerate="1 "+username+" "+password+" "+nickname;
        try{
            socket = new Socket(ServerAddress,9611);
            OutputStream outputStream=socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            PrintWriter pw=new PrintWriter(osw,true);
            pw.println(sendContextGenerate);
            socket.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class SendThread implements Runnable {
        Socket socket;
        public SendThread(Socket socket) {
            this.socket=socket;
        }

        @Override
        public void run() {
            try {
                OutputStream outputStream=socket.getOutputStream();
                OutputStreamWriter osw=new OutputStreamWriter(outputStream);
                PrintWriter pw=new PrintWriter(osw,true);
                Scanner scanner=new Scanner(System.in);
                while(true){
                    String message=scanner.nextLine();
                    if(message.equals("\\quit")){//退出聊天程序
                        chatFlag=false;
                        socket.shutdownOutput();
                        break;
                    }
                    String text_temp="client1 says: "+message;
                    pw.println(text_temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ReceiceThread implements Runnable {
        Socket socket;

        public ReceiceThread(Socket socket) {
            this.socket=socket;
        }

        @Override
        public void run() {
            try{
                InputStream inputStream=socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(inputStream);
                BufferedReader br =new BufferedReader(isr);
                while(true){
//                    if(!chatFlag) break;
                    int read=br.read();
                    if(read==-1){
                        socket.shutdownInput();
                        socket.close();
                        break;
                    }
                    String receiveMessage=br.readLine();
                    System.out.println(receiveMessage);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
