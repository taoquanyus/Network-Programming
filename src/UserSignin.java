import java.awt.*;
import java.sql.*;

public class UserSignin {
    private String username;
    private String user_password;

    public UserSignin(String username, String user_password) {
        this.username = username;
        this.user_password = user_password;
    }

    public String signin() {
        Connection con;
        //驱动程序名
        String driver = "com.mysql.cj.jdbc.Driver";
        //URL指向要访问的数据库名studb
        String url = "jdbc:mysql://localhost:3306/userchat?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        //包名叫做userchat
        //表名叫做user_information
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "quanyu112";

        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url, user, password);

            if (con.isClosed())
                System.out.println("unsuccessfully connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();
            //要执行的SQL语句
//            String sql = "insert ignore user_information (username,password,nickname) values ('"+username+"','"+user_password+"')";
            String sql="select nickname from user_information where username='"+username+"' and password='"+user_password+"'";

            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs=statement.executeQuery(sql);

            rs.next();
            String nickname=null;
            try {
                nickname = rs.getString("nickname");
                rs.close();
                con.close();
                return nickname;
            }
            catch (Exception e){
                System.out.println("fail to sign in");
                rs.close();
                con.close();
                return null;
            }
//            System.out.println("successfully sign in!");
//            System.out.println("hello "+nickname+"!");

        } catch (ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return user_password;
    }

    public void setPassword(String password) {
        this.user_password = user_password;
    }
}
