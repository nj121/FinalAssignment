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
    TextView mm,ss,tt;
    Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);

        Thread get = new Thread(this);
        get.start();
        mm = findViewById(R.id.moreinfo);
        ss = findViewById(R.id.sourceinfo);
        tt = findViewById(R.id.titleinfo);
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String mi[] = {"",""};
                Log.i(TAG, "handleMessage: ok");
                if(msg.what == 1){
                    mi = (String[])msg.obj;
                }
                super.handleMessage(msg);
//                Log.i(TAG, "onCreate: "+mi);
                mm.setText(mi[0]);
//                Log.i(TAG, "handleMessage: mi[1]="+mi[1]);
                ss.setText(mi[1]);
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
            Elements info= doc.select("div.rm_txt_con").select("p");
            String mo[] = {"",""};
            info.remove(info.size()-1);
            for(Element ds : info){
                mo[0] += "\t\t\t"+ds.text()+"\n";
            }
            mo[1] = doc.select("div.col-1-1").text();
//            Log.i(TAG, "run: mo[1]="+mo[1]);
            sendMessage(mo,1);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void sendMessage(String[] td,int mid){
        Message msg;
        msg = handler.obtainMessage(mid);
        msg.obj = td;
        handler.sendMessage(msg);
        Log.i(TAG, "run: ok"+mid);
    }
}