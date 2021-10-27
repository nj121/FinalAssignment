package com.example.finalassignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FirstFragment extends Fragment implements Runnable,
        AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private static final String TAG = "fff";
    Handler handler;
    ListView first_list;
    MyListAdapter myListAdapter;
    ArrayList<HashMap<String,String>> list;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        first_list = (ListView) view.findViewById(R.id.first_news);
        Thread get = new Thread(this);
        get.start();
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: ok");
                if(msg.what == 1){
                    list = (ArrayList<HashMap<String,String>>)msg.obj;
                    myListAdapter = new MyListAdapter(FirstFragment.this.getActivity(),R.layout.list_item,list);
                    first_list.setAdapter(myListAdapter);
                    first_list.setEmptyView(view.findViewById(R.id.nodata));

                }
                super.handleMessage(msg);
            }
        };
        first_list.setOnItemLongClickListener(this::onItemLongClick);
        first_list.setOnItemClickListener(this::onItemClick);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
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
                Elements doc_select= doc.select("div.headingNews").get(1)
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
        Intent first = new Intent(FirstFragment.this.getContext(), MoreInfo.class);
        first.putExtra("attr",map.get("attr"));
        first.putExtra("title",map.get("title"));
        first.putExtra("attr1","finance");
        startActivityForResult(first,1);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: yes");
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstFragment.this.getContext());
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                list.remove(position);
                myListAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否",null);
        builder.create().show();

        return true;
    }
}