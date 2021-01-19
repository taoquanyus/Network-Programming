import java.sql.*;

public class UserInformationShow {
    public static void main(String[] args) {
        //声明Connection对象
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

        //遍历查询结果集
        try {

            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url, user, password);

            if (!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();
            //要执行的SQL语句
            String sql = "select * from user_information";

            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("-----------------");
            System.out.println("执行结果如下所示:");
            System.out.println("-----------------");
            System.out.println("username" + "\t" + "password");
            System.out.println("-----------------");

            String user_password = null;
            String name = null;
            String nickname =null;

            rs.next();
            nickname=rs.getString("nickname");
            System.out.println(nickname);


            while (rs.next()) {
                //获取sname这列数据
                name = rs.getString("username");
                //获取address这列数据
                user_password = rs.getString("password");

                //输出结果
                System.out.println(name + "\t" + user_password);

            }
            rs.close();
            con.close();


        } catch (ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            System.out.println("successfully get the users information!");

        }

    }
}
