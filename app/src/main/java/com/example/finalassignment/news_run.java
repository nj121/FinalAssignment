package com.example.finalassignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class news_run implements Runnable{

    private Handler handler;
    private static final String TAG = "lll";
    ListView first_list;
    MyAdapter myAdapter;
    ArrayList<HashMap<String,String>> list;

    public void setHandler(Handler handler) {
        this.handler = handler;
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
}
