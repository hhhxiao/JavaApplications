package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

public class SortHelper {
    //读取分类列表文件，返回list
    public static ObservableList<String> getSortList() throws IOException {
        StringBuilder builder = new StringBuilder();
        FileInputStream fis = new FileInputStream(Constant.SORT_LIST);
        InputStreamReader reader = new InputStreamReader(fis); //最后的"GBK"根据文件属性而定，如果不行，改成"UTF-8"试试
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }
        br.close();
        reader.close();
        Constant.logger(builder.toString());
        String s[] = builder.toString().split(":");
        return FXCollections.observableArrayList(s);
    }

    //向文件中新增分类并更新
    public static void addSort(String s) throws IOException {
        ObservableList<String> list = getSortList();
        list.add(s);
        Constant.sortList = list;
        StringBuilder builder = new StringBuilder();
        for(String item: list){
            builder.append(item);
            builder.append(":");
        }
        File file = new File(Constant.SORT_LIST); // 相对路径，如果没有则要建立一个新的output。txt文件
        boolean temp = file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(builder.toString().substring(0,builder.length()-1));
        out.flush();
        out.close();

    }


    /*public static void main(String[] args) throws IOException {
        SortHelper.addSort("我们");


    }*/
}
