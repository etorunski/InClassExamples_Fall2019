package com.example.inclassexamples_fall2019;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class ViewContact extends AppCompatActivity {

    public static final int PUSHED_DELETE = 35;
    public static final int PUSHED_UPDATE = 5338;

    protected SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        //get the database:
        MyDatabaseOpenHelper opener = new MyDatabaseOpenHelper(this);
        db =  opener.getWritableDatabase();

        //Get the information from the previous page:
        Intent fromPreviousPage = getIntent();
        String name = fromPreviousPage.getStringExtra("Name");
        String email = fromPreviousPage.getStringExtra("Email");
        long id = fromPreviousPage.getLongExtra("Id", -1);

        //Find the Edit text for name:
        EditText nameEdit = (EditText)findViewById(R.id.Name);
        nameEdit.setText(name);


        //Find the edit text for email:
        EditText emailEdit = (EditText)findViewById(R.id.Email);
        emailEdit.setText(email);

        //Find the text view for ID:
        TextView idView = (TextView)findViewById(R.id.ID);
        idView.setText("ID="+id);


        //When you click on the delete button:
        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(b -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //This is the builder pattern, just call many functions on the same object:
            AlertDialog dialog = builder.setTitle("Alert!")
                    .setMessage("Do you want to delete?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //If you click the "Delete" button
                            int numDeleted = db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "=?", new String[] {Long.toString(id)});

                            Log.i("ViewContact", "Deleted " + numDeleted + " rows");

                            //set result to PUSHED_DELETE to show clicked the delete button
                            setResult(PUSHED_DELETE);
                            //go back to previous page:
                            finish();
                        }
                    })
                    //If you click the "Cancel" button:
                    .setNegativeButton("Cancel", (d,w) -> {  /* nothing */})
                    .create();

            //then show the dialog
            dialog.show();
        });
        //When you click on the update button:
        Button updateButton = (Button)findViewById(R.id.updateButton);
        updateButton.setOnClickListener(btn -> {

            //the new email:
            String newEmail = emailEdit.getText().toString();
            String newName = nameEdit.getText().toString();

            //Create a ContentValues object for the new values:
            ContentValues newValues = new ContentValues();
            newValues.put(MyDatabaseOpenHelper.COL_EMAIL, newEmail);
            newValues.put(MyDatabaseOpenHelper.COL_NAME, newName);

            //then update the row where the id is equal, and store the number of rows affected:
            int numUpdated = db.update(MyDatabaseOpenHelper.TABLE_NAME, newValues, MyDatabaseOpenHelper.COL_ID + "=?", new String[] { Long.toString(id)} );

            //Send the information back when you go back
            Intent newInfo = new Intent();
            newInfo.putExtra("Email", newEmail);
            newInfo.putExtra("Name", newName);

            //set the result to PUSHED_UPDATE to show the update button was pushed.
            setResult(PUSHED_UPDATE, newInfo);

            //show a notice window to say how many were updated.
            Toast.makeText(this, "Updated " + numUpdated + " rows", Toast.LENGTH_LONG).show();
        });
    }
}