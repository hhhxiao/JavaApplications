package com.example.agno3.digitalrecognition;

import android.annotation.SuppressLint;

import java.util.ArrayList;

public class MathUtil {

    @SuppressLint("DefaultLocale")
    static  double[][] conv(double[][] img, double[][] filter){
        double sum;
        int filter_width = filter.length;
        int result_width = img.length+1-filter_width;
        double[][] result = new double[result_width][result_width];
        for (int i = 0; i < result_width; i++)
            for (int j = 0; j < result_width; j++) {
                sum = 0.0;
                for (int x = 0; x < filter_width; x++) {
                    for (int y = 0; y < filter_width; y++) {
                        sum += filter[x][y] * img[x + i][y + j];
                    }
                }
                result[i][j] = sum>0?sum:0;
            }
        return result;
    }

    static double[][] pool(ArrayList<double[][]> cov_maps){
        int len = cov_maps.get(0).length;//len = 14
        int array_len = len*len/4*cov_maps.size();
        double[][] pool_array = new double[array_len][1];
        double num;
        int index = 0;
        for (double[][] cov_map:cov_maps){
            for (int i = 0; i < len; i+=2) {
                for (int j = 0; j < len; j+=2) {
                    num = (cov_map[j][i]+cov_map[j][i+1]+cov_map[j+1][i]+cov_map[j+1][i+1])/4;
                    pool_array[index][0] = num;
                    index++;
                }
            }
        }
     return pool_array;
    }
}


