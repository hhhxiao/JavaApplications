package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DBHelper {

    private String databaseName;
    private String userName;
    private String password;
    private Connection connection;

    public DBHelper(){
        this.databaseName = Constant.DB_NAME;
        this.password = Constant.password;
        this.userName = Constant.UserName;

    }
    //检查数据库是否正常连接
    public boolean checkConnection(){
        String dbUrl = Constant.DBurlFront+databaseName+Constant.DBUrlEnd;
        /*debugger
        Constant.logger("checkConnection url:"+dbUrl);
        Constant.logger("checkConnection user:"+userName);
        Constant.logger("checkConnection password:"+password);*/
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//通过反射加载驱动
            connection = DriverManager.getConnection(dbUrl,userName,password);//获取连接
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
    }

    //创建表单（日消费和月消费）

    public void createTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(Constant.CREATE_DAILY_COST_TABLE);
    }

    public void insertCost(DailyCost cost) throws SQLException {
        /*
        * TODO: 向数据库中插入对象
        * */
        /*
        * "(id INTEGER not NULL, " +
            " description VARCHAR(255), " +
            " cost NUMERIC(9,2), " +
            " sort VARCHAR(255)"+
            " costDate VARCHAR(16), " +
            " PRIMARY KEY ( id ))";*/
        Statement statement = connection.createStatement();
        String insertSQL = "INSERT INTO DAILY_COST_TABLE (id, description,cost,sort,costDate) VALUES ('"
                +cost.getId()+"', '"
                +cost.getDescription()+"', '"
                +cost.getCost()+"', '"
                +cost.getSort()+"', '"
                +cost.getData()+"')";
        statement.execute(insertSQL);
    }



    public ArrayList<String[]> searchResult(String attr,String value) throws SQLException {
        ArrayList<String[]> arrayList = new ArrayList<>();
        String selectSQL =  "SELECT * FROM DAILY_COST_TABLE WHERE "+attr+" ='"+value+"'";
        //Constant.logger(selectSQL);
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(selectSQL);
        while (set.next()){
           String s[] = new String[]{
                   set.getString("description"),
                   set.getString("cost"),
                   set.getString("sort"),
                   set.getString("costDate")};
           arrayList.add(s);
        }

        return arrayList;
    }

    //为第一页的list提供数据
    public HashSet<String> getDataList() throws SQLException {
        HashSet<String> dataList = new HashSet<>();
        String selectSQL =  "SELECT * FROM DAILY_COST_TABLE";
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(selectSQL);
        while (set.next()){
            String s = set.getString( "costDate");
            dataList.add(s);
        }
        return dataList;
    }
    //返回条形图数据
    public ObservableList<XYChart.Series<String,Double>> getBarChartData() throws SQLException {
        ObservableList<XYChart.Series<String,Double>> list = FXCollections.observableArrayList();
        Map<String,Double> map = new HashMap<>();
        HashSet<String> dataSet = getDataList();
        for(String data:dataSet)
            map.put(data,0.0);

        String selectSQL =  "SELECT * FROM DAILY_COST_TABLE";
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(selectSQL);
        while (set.next()){
            String s = set.getString( "costDate");
            String costData = set.getString("cost");
            Double cost = Double.valueOf(costData);
            double total = cost + map.get(s);
            map.put(s,total);
        }

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            XYChart.Series<String,Double> stringSeries=  new XYChart.Series<>();
            XYChart.Data<String,Double> doubleData = new XYChart.Data<>(entry.getKey().toString(),
                    (-1)*Double.valueOf(entry.getValue().toString()));
            stringSeries.getData().add(doubleData);
            list.add(stringSeries);
     }
        return list;
    }



    //读取配置文件，返回数据库名，用户名和密码
    public static String[] getConfig() throws IOException {
        FileInputStream stream = new FileInputStream(Constant._CONFIG_FILE);
        StringBuilder builder = new StringBuilder();
        int tempByte;
        while ((tempByte = stream.read()) != -1) {
            builder.append((char) tempByte);
        }
        stream.close();
        try {
            String s[] = builder.toString().split(":");//配置文件为空的时候会有数组溢出
            return new String[]{s[1],s[3],s[5]};
        }catch (ArrayIndexOutOfBoundsException e){
            Constant.logger("未能读取到相关信息");
            return null;//此时返回空数组
        }
    }

    //修改配置文件，写入数据库名，用户名和密码
    public static void changeConfig(String dbName,String userName,String password) throws IOException {
        String s= "dbName:"+dbName+":userName:"+userName+":password:"+password;
        File file = new File(Constant._CONFIG_FILE); // 相对路径，如果没有则要建立一个新的output。txt文件
        boolean temp = file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(s);
        out.flush();
        out.close();
    }


    /*public static void main(String[] args) throws IOException, SQLException {
        DBHelper dbHelper = new DBHelper();
       // String s = dbHelper.configManger("_config.txt");
       // Constant.logger(s);
        boolean flag =  dbHelper.checkConnection();
        ArrayList<String> s = dbHelper.searchResult("description","吃饭");

    }*/
}

