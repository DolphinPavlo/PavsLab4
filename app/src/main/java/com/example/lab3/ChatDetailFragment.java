package com.example.lab3;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ChatDetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long listID;
    private long dbID;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        listID = dataFromActivity.getLong(ChatRoomActivity.LIST_ITEM_ID );
        dbID = dataFromActivity.getLong(ChatRoomActivity.DB_ITEM_ID);

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_chat_detail, container, false);

        //show the message
        TextView message = (TextView)result.findViewById(R.id.message);
        message.setText("Message = " + dataFromActivity.getString(ChatRoomActivity.MESSAGE));

        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.idText);
        idView.setText("ID=" + dbID);

        TextView isSentView = (TextView)result.findViewById(R.id.isSent);
        isSentView.setText("Is sent = " + dataFromActivity.getString(ChatRoomActivity.IS_SENT));


        TextView isReceivedView = (TextView)result.findViewById(R.id.isReceived);
        isReceivedView.setText("Is received= " + dataFromActivity.getString(ChatRoomActivity.IS_RECEIVED));

        // get the delete button, and add a click listener:
        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                ChatRoomActivity parent = (ChatRoomActivity) getActivity();
                parent.deleteListMessageId((int)listID); //this deletes the item and updates the list
                parent.deleteDbMessageID((int)dbID);


                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                EmptyActivity parent = (EmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(ChatRoomActivity.LIST_ITEM_ID, dataFromActivity.getLong(ChatRoomActivity.LIST_ITEM_ID ));
                backToFragmentExample.putExtra(ChatRoomActivity.DB_ITEM_ID, dataFromActivity.getLong(ChatRoomActivity.DB_ITEM_ID));

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;
    }
}
