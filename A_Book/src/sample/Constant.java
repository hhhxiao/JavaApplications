package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Constant {

    //创建连接的URL
    public static final String DBurlFront = "jdbc:mysql://127.0.0.1:3306/";

    public static String _CONFIG_FILE = "_config.txt";

    public static String SORT_LIST = "sort.txt";

    public static ObservableList<String> sortList;



    public static final String DBUrlEnd = "?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT";

    //数据库名以及用户名和密码（临时变量）
    public static String DB_NAME;

    public static String UserName;//数据库用户名

    public static String password;//数据库密码

    public static boolean connectionFlag = false;//是否连接成功



    //建配置表的语句
    public final static  String  CREATE_CONFIG_TABLE = "CREATE TABLE IF NOT EXISTS CONFIG_TABLE " +
            "(name VARCHAR(255) not NULL, " +
            " value VARCHAR(255), " +
            " PRIMARY KEY ( name ))";

    /**
     *     private int id;
     *     private String description;
     *     private double cost;
     *     private String sort;
     *     private String data;
     *
     */
    //建日花销表的语句
    public static final String CREATE_DAILY_COST_TABLE = "CREATE TABLE IF NOT EXISTS DAILY_COST_TABLE " +
            "(id INTEGER not NULL, " +
            " description VARCHAR(255), " +
            " cost NUMERIC(9,2), " +
            " sort VARCHAR(255),"+
            " costDate VARCHAR(16), " +
            " PRIMARY KEY ( id ))";
    //建月花销表的语句
    public final static String CREATE_MONTH_COST_TABLE  = "CREATE TABLE IF NOT EXISTS MONTH_COST_TABLE " +
            "(id INTEGER not NULL, " +
            " data VARCHAR(16), " +
            " cost NUMERIC(9,2), " +
            " gain NUMERIC(9,2),"+
            " total NUMERIC(9,2),"+
            " PRIMARY KEY ( id ))";

    public static void dataInit(String dbName,String username,String password){
        Constant.DB_NAME = dbName;
        Constant.password = password;
        Constant.UserName = username;
    }

    //debug
    public static void logger(Object o){ System.out.println(o.toString()); }

    public static String getCurrentData(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dt);
    }

    public static void main(String[] args){
        logger(getCurrentData());

    }
}
