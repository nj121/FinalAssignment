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

public class moreinfo extends AppCompatActivity implements Runnable{

    private static final String TAG = "mmm";
    Handler handler;
    TextView tt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);

        Thread get = new Thread(this);
        get.start();
        tt = findViewById(R.id.moreinfo);
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
                tt.setText(mi);
            }
        };
    }

    @Override
    public void run() {
        Document doc = null;
        try {
            Intent in = getIntent();
            String attr = in.getStringExtra("attr");
            ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
            doc = Jsoup.connect("http://finance.people.com.cn/"+attr).get();
            Log.i(TAG, "title: "+doc.title());
            Elements doc_select= doc.select("div.rm_txt_con").select("p");
            String mo = "";
            for(Element ds : doc_select){
                mo += "\t\t\t"+ds.text()+"\n";
            }
            Message msg;
            msg = handler.obtainMessage(1);
            msg.obj = mo;
            handler.sendMessage(msg);
            Log.i(TAG, "run: ok"+1);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}