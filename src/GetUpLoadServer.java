import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GetUpLoadServer {
    private ServerSocket serverSocket;
    public GetUpLoadServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while(true){
            try {
                Socket socket=serverSocket.accept();
                Thread getUploadThread=new Thread(new getUploadRunnable(socket));
                getUploadThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private class getUploadRunnable implements Runnable {
        Socket socket;
        private DataInputStream dis;
        private FileOutputStream fos;
        public getUploadRunnable(Socket socket) {
            this.socket=socket;
        }
        @Override
        public void run() {
            try{
                dis = new DataInputStream(socket.getInputStream());

                // 文件名和长度
                String fileName = dis.readUTF();
                File directory = new File("/Users/quanyu/Desktop/FileCollector");//存放上传文件的路径
                if(!directory.exists()) {
                    directory.mkdir();
                }

                File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                fos = new FileOutputStream(file);
                // 开始接收文件
                byte[] bytes = new byte[1024];
                int length = 0;
                while((length = dis.read(bytes, 0, bytes.length)) != -1) {
                    fos.write(bytes, 0, length);
                    fos.flush();
                }
                System.out.println("========"+fileName+"接收成功========");
            }
            catch (Exception e){

            }
        }
    }
}
