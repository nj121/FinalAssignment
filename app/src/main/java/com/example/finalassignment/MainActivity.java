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
import android.view.Menu;
import android.view.MenuItem;
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
    MyListAdapter myListAdapter;
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
                if(msg.what == 0){
                    list = (ArrayList<HashMap<String,String>>)msg.obj;
                    myListAdapter = new MyListAdapter(MainActivity.this,R.layout.list_item,list);
                    first_list.setAdapter(myListAdapter);
                    first_list.setEmptyView(findViewById(R.id.nodata));

                }
                super.handleMessage(msg);
            }
        };
        first_list.setOnItemLongClickListener(this::onItemLongClick);
        first_list.setOnItemClickListener(this::onItemClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.jump){
            Log.i(TAG, "menu: ");
            Intent jump = new Intent(this,GrossActivity.class);
            Log.i(TAG, "menu:ok");

            startActivityForResult(jump,10);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {
            Document doc = null;
            Message msg;
            try {
                ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
                for(int i = 1;i<9;i++){
                    doc = Jsoup.connect("http://finance.people.com.cn/index"+i+".html#fy01").get();
//                    Log.i(TAG, "title: "+doc.title());
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
                sendMessage(listItem,0);
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
        Intent first = new Intent(this, MoreInfo.class);
        first.putExtra("attr",map.get("attr"));
        first.putExtra("title",map.get("title"));
        first.putExtra("attr1","finance");
        startActivityForResult(first,1);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: yes");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????").setMessage("?????????????????????????????????").setPositiveButton("???",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: ?????????????????????");
                list.remove(position);
                myListAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("???",null);
        builder.create().show();
        return true;
    }
}