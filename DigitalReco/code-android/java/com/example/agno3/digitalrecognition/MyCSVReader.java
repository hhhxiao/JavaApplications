package com.example.agno3.digitalrecognition;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

class MyCSVReader {

    static double[][] readMatrix(int x ,int y ,String fileName)   {
        double[][] filter = new double[x][y];
        try {
            InputStreamReader inputReader = new InputStreamReader(/*这里自己改一下 */);
            BufferedReader reader = new BufferedReader(inputReader);
            String line;
            int j = 0;
            while((line=reader.readLine())!=null){//一次读取一行
                String items[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号
                double[] values = new double[items.length];
               // Log.d("CSVReader",values.length+"");
                for (int i = 0; i < items.length; i++) {
                    values[i] = Double.valueOf(items[i]);
                }
                filter[j] = values;
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }
}

