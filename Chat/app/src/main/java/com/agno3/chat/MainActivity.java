package com.agno3.chat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();
    private EditText editText;
    private MsgAdapter adapter;
    private RecyclerView recyclerView;
    private String apikey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.send);
        editText = findViewById(R.id.input_text);
        recyclerView = findViewById(R.id.recycle_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        Msg msg1 = new Msg(0, "hello!");
        msgList.add(msg1);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(adapter);
        button.setOnClickListener(v -> {
            msgList.add(new Msg(1, editText.getText().toString()));
            String string = editText.getText().toString();
            editText.setText("");
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            getContent(string);
        });

        SharedPreferences pref = getSharedPreferences("apiKey", MODE_PRIVATE);
       this.apikey = pref.getString("key", "");
    }


    //通过apikey
    private void getContent(String s) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("key", apikey)
                        .add("info", s)
                        .build();
                Log.d("s", s);
                Request request = new Request.Builder()
                        .url("http://www.tuling123.com/openapi/api")
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                String s1 = response.body().string();
                Log.d("s", s1);
                show(s1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ).start();
    }

    private void show(String s1) {
        runOnUiThread(() -> {
            String[] strArray = s1.split("\"");
            msgList.add(new Msg(0, strArray[5]));
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.apikey:
                ShowAdialog(MainActivity.this);
                break;
            case R.id.about:
                AlertDialog.Builder dialog = new AlertDialog.Builder (MainActivity.
                        this);
                dialog.setTitle("关于");
                dialog.setMessage("你需要在图灵网（http://www.tuling123.com/）中注册并获取机器人的apikey并填入apikey选项里面\n\n" +
                        "作者：gNO3\n\n获取源码请访问：https://github.com/hhhxiao/Chat");
                dialog.setCancelable(false);
                dialog.setPositiveButton("我知道了", (dialog12, which) -> {});
                dialog.setNegativeButton("我知道了", (dialog1, which) -> {});
                dialog.show();
                break;
            default:
        }
        return true;
    }

    private void ShowAdialog(Context context)
    {
        LayoutInflater inflater = LayoutInflater.from(context
        );
        View view = inflater.inflate(R.layout.apikey_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("write your own key");
        builder.setView(view);
        builder.setMessage("在下方填入自己的apikey方可和机器人交流");
        builder.create();

        EditText text = view.findViewById(R.id.dialog_edit);
        text.setText(apikey);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("好的", (dialog, which) -> {
            apikey = text.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences("apiKey", MODE_PRIVATE).edit();
            editor.putString("key", text.getText().toString());
            editor.apply();
        });
        builder.show();

    }
}
