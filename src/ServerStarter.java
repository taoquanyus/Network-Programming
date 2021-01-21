public class ServerStarter {
    public static void main(String[] args) {
        //ServerStarter要做那些事？
        //1.验证账号密码或注册账号 端口号9611
//        CheckServer checkServer=new CheckServer(9611);
        //2.开启聊天室 端口号9806
//        ChatServer chatServer=new ChatServer(9806);
        //3.开启接收文件上传功能 端口号8888
//        GetUpLoadServer upLoadServer=new GetUpLoadServer(8888);
        //4.开启提供文件下载功能 端口号9999
//        DownLoadServer downLoadServer=new DownLoadServer(9999);
        //以上四个socket连接分别要使用不同的线程
        Thread check=new Thread(new CheckRunnable());
        check.start();
        Thread chat=new Thread(new ChatRunnable());
        chat.start();
        Thread upload = new Thread(new UploadRunnable());
        upload.start();
        Thread Download=new Thread(new DownloadRunnable());
        Download.start();
    }

    private static class CheckRunnable implements Runnable {
        @Override
        public void run() {
            CheckServer checkServer=new CheckServer(9611);
            checkServer.start();
        }
    }

    private static class ChatRunnable implements Runnable {
        @Override
        public void run() {
            ChatServer chatServer=new ChatServer(9806);
            chatServer.start();
        }
    }

    private static class UploadRunnable implements Runnable {
        //3.开启接收文件上传功能 端口号8888
//        GetUpLoadServer upLoadServer=new GetUpLoadServer(8888);
        @Override
        public void run() {
            GetUpLoadServer upLoadServer=new GetUpLoadServer(8888);
            upLoadServer.start();
        }
    }

    private static class DownloadRunnable implements Runnable {
        @Override
        public void run() {
            DownLoadServer downLoadServer=new DownLoadServer(9999);
            downLoadServer.start();
        }
    }
}
