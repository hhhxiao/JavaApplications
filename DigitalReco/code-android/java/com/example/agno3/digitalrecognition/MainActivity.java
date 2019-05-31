package com.example.agno3.digitalrecognition;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private ArrayList<double[][]> filters;
    private double[][] W_1;
    private double[][] W_2;


    private static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        this.filters = new ArrayList<>();
        loadData();
        textView = findViewById(R.id.text_view);


        //设置监听事件
        Button button = findViewById(R.id.clear);
        Button button1 = findViewById(R.id.rec);
        DrawView view = findViewById(R.id.can);
        view.clear();
        button.setOnClickListener(v->view.clear());
        button1.setOnClickListener((v)-> {
            try {
                view.reco(this, filters,W_1,W_2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void loadData(){
        for (int i = 1; i < 6; i++) {
            String fileName = "filters_"+i+".csv";
            double[][] filter = MyCSVReader.readMatrix(this,9,9,fileName);
            this.filters.add(filter);
        }
        this.W_1 = MyCSVReader.readMatrix(this,100,500,"w_1.csv");
        this.W_2 = MyCSVReader.readMatrix(this,10,100,"w_2.csv");
    }

    public TextView getTextView() {
        return textView;
    }
}
