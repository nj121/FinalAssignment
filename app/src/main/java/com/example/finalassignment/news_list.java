package com.example.finalassignment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class news_list implements Runnable{

    private Handler handler;
    private static final String TAG = "lll";

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException interruptedException) {
//            interruptedException.printStackTrace();
//        }
        Log.i(TAG, "线程启动: ");
        Document doc = null;
        Message msg;
        try {
            ArrayList<HashMap<String,String>> listItem = new ArrayList<HashMap<String,String>>();
            doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG, "title: "+doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element table=tables.get(1);
            //Log.i(TAG, "run: table="+table);
            Elements trs = table.getElementsByTag("tr");
            trs.remove(0);
            //Log.i(TAG, "size ="+trs.size());
            for(Element tr : trs){
                //Log.i(TAG, "run: tr="+tr);
                HashMap<String,String> map = new HashMap<String,String>();
                Elements tds = tr.getElementsByTag("td");
                Element td1 = tds.get(0);
                Element td2 = tds.get(4);
                map.put("ItemTitle",td1.text());
                map.put("ItemDetail",td2.text());
                listItem.add(map);
            }
            sendMessage(listItem,1);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
    public void sendMessage(ArrayList<HashMap<String,String>> td, int mid){
        Message msg;
        msg = handler.obtainMessage(mid);
        msg.obj = td;
        handler.sendMessage(msg);
    }


}
