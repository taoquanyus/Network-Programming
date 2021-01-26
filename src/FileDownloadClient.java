import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FileDownloadClient {
    private Socket socket;
    private String filename;
    private DataInputStream dis;
    private FileOutputStream fos;

    public FileDownloadClient(Socket socket) {
        this.socket = socket;
    }
    public void download() {
        try {
            InputStream inputStream=socket.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            System.out.println(bufferedReader.readLine());

            System.out.println("please input the file name");
            Scanner scanner=new Scanner(System.in);
            String filename=scanner.nextLine();

            OutputStream outputStream=socket.getOutputStream();
            OutputStreamWriter osw=new OutputStreamWriter(outputStream);
            PrintWriter pw=new PrintWriter(osw,true);
            pw.println(filename);

            dis=new DataInputStream(socket.getInputStream());

            String fileName=dis.readUTF();//注意，此filename 和上面的那个不同，下面的是大写
            File directory=new File("/Users/quanyu/Desktop/clientDownload");
            if(!directory.exists()){
                directory.mkdir();
            }
            File file=new File(directory.getAbsolutePath()+File.separatorChar+fileName);
            fos=new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int length = 0;
            while((length = dis.read(bytes, 0, bytes.length)) != -1) {
                fos.write(bytes, 0, length);
                fos.flush();
            }
            System.out.println("Successfully download!");
            socket.close();
            //这里是发送了文件名给服务器
            //下一步是服务器收到文件名再处理请求，把文件传回来

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
