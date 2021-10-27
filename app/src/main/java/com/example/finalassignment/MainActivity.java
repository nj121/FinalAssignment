package com.example.finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Runnable,
        AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private static final String TAG = "rrr";
    Handler handler;
    ListView first_list;
    MyAdapter myAdapter;
    ArrayList<HashMap<String,String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        first_list = findViewById(R.id.first_news);
        Thread get = new Thread(this);
        get.start();
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: ok");
                if(msg.what == 1){
                    list = (ArrayList<HashMap<String,String>>)msg.obj;
                    myAdapter = new MyAdapter(MainActivity.this,R.layout.list_item,list);
                    first_list.setAdapter(myAdapter);
                    first_list.setEmptyView(findViewById(R.id.nodata));

                }
                super.handleMessage(msg);
            }
        };
        first_list.setOnItemLongClickListener(this::onItemLongClick);
        first_list.setOnItemClickListener(this::onItemClick);
    }

    @Override
    public void run() {
            Document doc = null;
            Message msg;
            try {
                ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
                for(int i = 1;i<9;i++){
                    doc = Jsoup.connect("http://finance.people.com.cn/index"+i+".html#fy01").get();
                    Log.i(TAG, "title: "+doc.title());
                    Elements doc_select= doc.select("div.headingNews").first()
                            .select("div.on");
                    Elements news_title= doc_select.select("h5");
                    Elements news_content= doc_select.select("em");
                    for(Element ds : doc_select){
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("title",ds.select("h5").text());
                        map.put("content",ds.select("em").text());
                        map.put("attr",ds.select("h5").select("a").attr("href"));
                        listItem.add(map);
                    }
                }
                sendMessage(listItem,1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
    }

    public void sendMessage(ArrayList<HashMap<String,String>> td,int mid){
        Message msg;
        msg = handler.obtainMessage(mid);
        msg.obj = td;
        handler.sendMessage(msg);
        Log.i(TAG, "run: ok"+mid);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition = first_list.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String,String>) itemAtPosition;
        String attr = map.get("attr");
        Log.i(TAG, "attr = "+attr);
        Intent first = new Intent(this,moreinfo.class);
        first.putExtra("attr",attr);
        startActivityForResult(first,1);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: yes");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                list.remove(position);
                myAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否",null);
        builder.create().show();

        return true;
    }
}