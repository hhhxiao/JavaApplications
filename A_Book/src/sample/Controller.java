package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sun.security.pkcs11.Secmod;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class Controller implements Initializable {

    /*-----------page 1--------------*/
    @FXML
    private Label cuData;
    @FXML
    private ListView<String> dataList;
    @FXML
    private PieChart pieChart;
    @FXML
    private Label dailyCost;
    @FXML
    private Label dailyGain;
    @FXML
    private Label total;
    /*-----------page 1--------------*/

    /*----------addCost Page2---------*/
    @FXML
    private TextField costDescription;
    @FXML
    private DatePicker costDate;
    @FXML
    private TextField cost;
    @FXML
    private ComboBox<String> sortItem;//分类框
    @FXML
    private Button addNewSort;
    @FXML
    private Button addNewCost;

    /*----------addCost Page2---------*/
    /*---------- Page4---------*/
    @FXML
    private BarChart<String ,Double> barChart;

    /*----------Page4---------*/



    /*----------settings Page5-----------*/
    @FXML
    private TextField dbNameTextFiled;
    @FXML
    private TextField userNameTextFiled;
    @FXML
    private PasswordField passwordTextFiled;
    @FXML
    private Label dbStatus;
    @FXML
    private Button check;
/*----------settings Page5-----------*/

    /*------------初始化------------*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {



        sortItem.setItems(Constant.sortList);//给分类框更新分类列表
        cuData.setText(Constant.getCurrentData());
        //填入数据库初始信息
        try {
                String s[] = DBHelper.getConfig();
                if(s!=null){//如果不为空的话
                    dbNameTextFiled.setText(s[0]);
                    userNameTextFiled.setText(s[1]);
                    passwordTextFiled.setText(s[2]);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            updatePage1Chart(Constant.getCurrentData());
            updateList();
            updateBarChat();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /*------------初始化------------*/
    /*----------Page1---------*/

    private void updatePage1Chart(String date) throws SQLException {
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        DBHelper helper = new DBHelper();
        boolean flag = helper.checkConnection();
        if(flag){
            Map<String,Double> map = new HashMap<>();
            for(String s: Constant.sortList)
                map.put(s,0.00);
            ArrayList<String[]> arrayList =helper.searchResult("costDate",date);
            double cost = 0;
            double gain = 0;
            for(String[] s:arrayList){

                double c = Double.valueOf(s[1]);
                double v  = map.get(s[2]);
                v -= c;
                map.put(s[2],v);
                if(c>0) gain += c;
                else cost += c;
            }
            dailyCost.setText("今日支出:"+cost);
            dailyGain.setText("今日得到:"+gain);
            total.setText("今日的总花销"+(gain+cost));
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                list.add(new PieChart.Data(entry.getKey().toString(),
                        Double.valueOf(entry.getValue().toString())));
            }
            pieChart.setData(list);


        }
    }

    //刷新第一页的列表
    public void updateList() throws SQLException {
        //刷新列表
        DBHelper helper = new DBHelper();
        if(helper.checkConnection()){
            HashSet<String> s = helper.getDataList();
            ObservableList<String> dateListData = FXCollections.observableArrayList(s);
            dataList.setItems(dateListData);
        }
    }

    public void listViewListener() throws SQLException {
        String s= dataList.getSelectionModel().getSelectedItem();
        if(s!= null){
            cuData.setText(s);
            updatePage1Chart(s);
        }

    }
    /*----------Page1---------*/

    /*----------addCost Page2---------*/
    public void addNewSort(){
        new AlertBox().display("添加分类","在下方填入自定义的分类后点击确定");
        sortItem.setItems(Constant.sortList);
    }
    public void addNewCost() throws SQLException {
        String costDescriptionString = costDescription.getText();
        double costValue = Double.parseDouble(cost.getText());
        String date = costDate.getValue().toString();
        String sortString = sortItem.getValue().toString();
        int id = (int)System.currentTimeMillis();
        DBHelper helper = new DBHelper();
        helper.checkConnection();
        helper.insertCost(new DailyCost(costDescriptionString,costValue,sortString,date,id));
        //刷新首页数据
        updateList();
        updatePage1Chart(Constant.getCurrentData());
        updateBarChat();
    }
    /*----------addCost Page2---------*/
    /*----------addCost Page4---------*/
    public void updateBarChat() throws SQLException {
        DBHelper helper = new DBHelper();
        if(helper.checkConnection()){
            ObservableList<XYChart.Series<String,Double>> list = helper.getBarChartData();
            barChart.setData(list);
        }
    }

    /*----------addCost Page4---------*/

    /*-----------settings Page5-----------*/
    public void ButtonCheckConnection(ActionEvent event) throws SQLException, IOException {
        //获取数据
        String userName = userNameTextFiled.getText();
        String password = passwordTextFiled.getText();
        String dbName = dbNameTextFiled.getText();
        DBHelper.changeConfig(dbName,userName,password);//修改并存入数据
        Constant.dataInit(dbName,userName,password);//修改临时数据
        DBHelper helper = new DBHelper();
        boolean flag = helper.checkConnection();//检查连接
        if(!flag){
            //无法连接成功
            dbStatus.setText("无法连接请重新检查数据库名等参数");
        }else{
            //连接成功后立即建表
            dbStatus.setText("连接成功");
            helper.createTable();
            //向配置表中写入配置信息
            //helper.changeConfig();
        }

    }
    /*-----------settings Page5-----------*/
}
