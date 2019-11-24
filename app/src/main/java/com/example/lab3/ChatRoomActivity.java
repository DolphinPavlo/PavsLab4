package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;



public class ChatRoomActivity extends AppCompatActivity {

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String LIST_ITEM_ID = "LIST_ID";
    public static final String DB_ITEM_ID = "DB_ID";
    public static final String MESSAGE = "MESSAGE";
    public static final String IS_SENT = "IS_SENT";
    public static final String IS_RECEIVED = "IS_RECEIVED";
    public static final int EMPTY_ACTIVITY = 345;

    ArrayList<Message> objects = new ArrayList<>();

    BaseAdapter myAdapter;

    EditText mess;

    Message message;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        mess = findViewById(R.id.textBox);


        //get a database:
        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_TEXT, MyDatabaseOpenHelper.COL_SENT, MyDatabaseOpenHelper.COL_RECEIVED};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);


        //find the column indices:
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);
        int textColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_TEXT);
        int isSentColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_SENT);
        int isReceivedColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_RECEIVED);


        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {


            long id = results.getLong(idColIndex);
            String text = results.getString(textColIndex);
            String sent = results.getString(isSentColIndex);
            String received = results.getString(isReceivedColIndex);

            if (sent.equals("1")) {
                objects.add(new Message(id, text, true, false));
            } else if (received.equals("1")) {
                objects.add(new Message(id, text, false, true));
            } else {
            }

        }

        //You only need 2 lines in onCreate to actually display data:
        ListView theList = findViewById(R.id.listView);
        theList.setAdapter(myAdapter = new MyListAdapter());

        theList.setOnItemClickListener((lv, vw, pos, id) -> {

       //     Toast.makeText(ChatRoomActivity.this,
         //           "You clicked on:" + pos, Toast.LENGTH_SHORT).show();
            Bundle dataToPass = new Bundle();
            dataToPass.putString(MESSAGE, objects.get(pos).getText());
            dataToPass.putLong(LIST_ITEM_ID, id);
            dataToPass.putLong(DB_ITEM_ID, objects.get(pos).getId());
            dataToPass.putString(IS_SENT, objects.get(pos).getSent().toString());
            dataToPass.putString(IS_RECEIVED, objects.get(pos).getReceived().toString());

            if (isTablet)
            {
                ChatDetailFragment dFragment = new ChatDetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }else{
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });

        Button addButton = findViewById(R.id.sendButton);
        addButton.setOnClickListener(clik ->
        {

            String text = mess.getText().toString();
            String isSent = "1";
            String isReceived = "0";

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_TEXT, text);
            newRowValues.put(MyDatabaseOpenHelper.COL_SENT, isSent);
            newRowValues.put(MyDatabaseOpenHelper.COL_RECEIVED, isReceived);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);


            objects.add(new Message(newId, text, true, false));


            myAdapter.notifyDataSetChanged(); //update yourself
            mess.getText().clear();
        });

        Button receiveButton = findViewById(R.id.receiveButton);
        receiveButton.setOnClickListener(clik ->
        {
            String text = mess.getText().toString();
            String isSent = "0";
            String isReceived = "1";

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_TEXT, text);
            newRowValues.put(MyDatabaseOpenHelper.COL_SENT, isSent);
            newRowValues.put(MyDatabaseOpenHelper.COL_RECEIVED, isReceived);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);


            objects.add(new Message(newId, text, false, true));

            myAdapter.notifyDataSetChanged();
            mess.getText().clear();
        });

        printCursor(results);
    }



    public void printCursor(Cursor c) {
        //database version number
        int v = MyDatabaseOpenHelper.VERSION_NUM;
        Log.i("Database version #:", String.valueOf(v));
        Log.i("Numb of colu in curs:", String.valueOf(c.getColumnCount()));
        Log.i("Columns names in curs:", Arrays.toString(c.getColumnNames()));
        Log.i("Numb results in curs:", String.valueOf(c.getCount()));


        if(c.moveToFirst()){
            do{
                String data = c.getString(c.getColumnIndex("TEXT"));
                Log.i("Row result:", data);
            }while (c.moveToNext());
        }

    }
    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long listID = data.getLongExtra(LIST_ITEM_ID, 0);
                deleteListMessageId((int)listID);
                long dbID = data.getLongExtra(DB_ITEM_ID, 0);
                deleteDbMessageID((int)dbID);
            }
        }
    }

    public void deleteDbMessageID(int id){
        db.delete(MyDatabaseOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(id)});
    }

    public void deleteListMessageId(int id) {
        Log.i("Delete this message:", " id=" + id);
        objects.remove(id);
        myAdapter.notifyDataSetChanged();
    }
    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return objects.size();
        } //This function tells how many objects to show

        public Message getItem(int position) {
            return objects.get(position);
        }  //This returns the string at position p

        public long getItemId(int p) {
            return p;
        } //This returns the database id of the item at position p

        public View getView(int p, View thisRow, ViewGroup parent) {
            //View thisRow = recycled;

            Message msg = getItem(p);


            if (msg.getSent()) {
                thisRow = getLayoutInflater().inflate(R.layout.sendlayout, null);

                TextView itemText = thisRow.findViewById(R.id.message);
                itemText.setText(msg.getText());
            } else if (msg.getReceived()) {
                thisRow = getLayoutInflater().inflate(R.layout.receivelayout, null);

                TextView itemText = thisRow.findViewById(R.id.message);
                itemText.setText(msg.getText());
            }
            return thisRow;

        }

    }
}