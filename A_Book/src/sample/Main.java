package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("oneBook");
        Scene scene = new Scene(root, 960, 720);
        scene.getStylesheets().add(getClass().getResource("sample.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException, SQLException {
        dataInit();
        launch(args);
    }

    //初始化数据并检查数据库连接
    public static void dataInit() throws IOException {

        Constant.sortList = SortHelper.getSortList();//初始化分类列表临时变量
        String s[] = DBHelper.getConfig();
        if(s == null){
            Constant.logger("请在设置中完善相关信息");
        }else {
            Constant.dataInit(s[0],s[1],s[2]);
            DBHelper helper = new DBHelper();//初始化临时数据
            Constant.connectionFlag = helper.checkConnection();
            if(!Constant.connectionFlag){
                Constant.logger("请检查你的配置是否正确");
            }else {
                Constant.logger("数据库连接正常，可以正常使用");
            }
        }
    }



}
