package com.example.lab3;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {

    private String inputText = "You clicked on the overflow menu";
    private Toolbar testToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:

            case R.id.icon1:

                Toast.makeText(this, "This is the initial message", Toast.LENGTH_LONG).show();

                break;

            case R.id.icon2:
                             alert();

            case R.id.icon3:

               Snackbar sb = Snackbar.make(findViewById(android.R.id.content), "Going Back?", Snackbar.LENGTH_LONG)
                        .setAction("Go Back?", new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                sb.show();

                break;

            case R.id.itemText:

                Toast.makeText(this, inputText, Toast.LENGTH_LONG).show();

                break;
        }
        return true;
    }

    public void alert() {
        View middle = getLayoutInflater().inflate(R.layout.alert, null);

        EditText et = (EditText) middle.findViewById(R.id.alertEditText);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Positive", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                inputText = et.getText().toString();
            }
        })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }
}