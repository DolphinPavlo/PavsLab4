package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<Message> list = new ArrayList<>();
    BaseAdapter myAdapter;
    Button sendButton;
    Button recieveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView theList = findViewById(R.id.listView);
        theList.setAdapter(myAdapter = new MyListAdapter());
        myAdapter.notifyDataSetChanged();

        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.textBox);
                Message message = new Message(textView.getText().toString(), true);
                list.add(0, message);
                myAdapter.notifyDataSetChanged();
                textView.setText("");
            }
        });

        recieveButton = findViewById(R.id.receiveButton);
        recieveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.textBox);
                Message message = new Message(textView.getText().toString(), false);
                list.add(0, message);
                myAdapter.notifyDataSetChanged();
                textView.setText("");
            }
        });
    }

    private class MyListAdapter extends BaseAdapter {


        public int getCount() {
            return list.size();
        }

        public Message getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Message message = getItem(position);
            int layout;
            if (message.isSend) {
                layout = R.layout.sendlayout;
            } else {
                layout = R.layout.receivelayout;
            }
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(layout, parent, false);
                TextView textView = convertView.findViewById(R.id.message);
                textView.setText(message.message);
            }
            return convertView;
        }
    }
}
