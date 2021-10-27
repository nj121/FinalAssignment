package com.example.finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MoreInfo extends AppCompatActivity implements Runnable{

    private static final String TAG = "mmm";
    Handler handler;
    TextView mm,tt;
    Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);

        Thread get = new Thread(this);
        get.start();
        mm = findViewById(R.id.moreinfo);
        tt = findViewById(R.id.titleinfo);
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String mi = "";
                Log.i(TAG, "handleMessage: ok");
                if(msg.what == 1){
                    mi = (String)msg.obj;
                }
                super.handleMessage(msg);
                Log.i(TAG, "onCreate: "+mi);
                mm.setText(mi);
            }
        };

        in = getIntent();
        String title = in.getStringExtra("title");
        Log.i(TAG, "onCreate: "+title);
        tt.setText(title);
    }

    @Override
    public void run() {
        Document doc = null;
        try {
            in = getIntent();
            String attr = in.getStringExtra("attr");
            String attr1 = in.getStringExtra("attr1");
            ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
            doc = Jsoup.connect("http://"+attr1+".people.com.cn/"+attr).get();
            Log.i(TAG, "title: "+doc.title());
            Elements doc_select= doc.select("div.rm_txt_con").select("p");
            String mo = "";
            doc_select.remove(doc_select.size()-1);
            for(Element ds : doc_select){
                mo += "\t\t\t"+ds.text()+"\n";
            }
            sendMessage(mo,1);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void sendMessage(String td,int mid){
        Message msg;
        msg = handler.obtainMessage(mid);
        msg.obj = td;
        handler.sendMessage(msg);
        Log.i(TAG, "run: ok"+mid);
    }
}