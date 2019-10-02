package com.example.inclassexamples_fall2019;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseExample extends AppCompatActivity {

    ArrayList<Contact> contactsList = new ArrayList<>();
    private static int ACTIVITY_VIEW_CONTACT = 33;
    int positionClicked = 0;
    MyOwnAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_layout);

        //Get the fields from the screen:
        EditText nameEdit = (EditText)findViewById(R.id.nameInput);
        EditText emailEdit = (EditText) findViewById(R.id.emailInput);
        Button insertButton = (Button)findViewById(R.id.insert);
        ListView theList = (ListView)findViewById(R.id.the_list);


        //get a database:
        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_EMAIL, MyDatabaseOpenHelper.COL_NAME};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int emailColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_EMAIL);
        int nameColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_NAME);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String name = results.getString(nameColIndex);
            String email = results.getString(emailColumnIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            contactsList.add(new Contact(name, email, id));
        }

        //create an adapter object and send it to the listVIew
        myAdapter = new MyOwnAdapter();
        theList.setAdapter(myAdapter);


        //This listens for items being clicked in the list view
        theList.setOnItemClickListener(( parent,  view,  position,  id) -> {
            Log.e("you clicked on :" , "item "+ position);
            //save the position in case this object gets deleted or updated
            positionClicked = position;

            //When you click on a row, open selected contact on a new page (ViewContact)
            Contact chosenOne = contactsList.get(position);
            Intent nextPage = new Intent(DatabaseExample.this, ViewContact.class);
            nextPage.putExtra("Name", chosenOne.getName());
            nextPage.putExtra("Email", chosenOne.getEmail());
            nextPage.putExtra("Id", id);
            startActivityForResult(nextPage, ACTIVITY_VIEW_CONTACT);
        });


        //Listen for an insert button click event:
        insertButton.setOnClickListener( click ->
        {
            //get the email and name that were typed
            String name = nameEdit.getText().toString();
            String email = emailEdit.getText().toString();


            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_NAME, name);
            //put string email in the EMAIL column:
            newRowValues.put(MyDatabaseOpenHelper.COL_EMAIL, email);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

            //now you have the newId, you can create the Contact object
            Contact newContact = new Contact(name, email, newId);

            //add the new contact to the list:
            contactsList.add(newContact);
            //update the listView:
            myAdapter.notifyDataSetChanged();

            //clear the EditText fields:
            nameEdit.setText("");
            emailEdit.setText("");

            //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            Snackbar.make(insertButton, "Inserted item id:"+newId, Snackbar.LENGTH_LONG).show();

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If you're coming back from the view contact activity
        if(requestCode == ACTIVITY_VIEW_CONTACT)
        {
            switch(resultCode) {
                //if you clicked delete, remove the item you clicked from the array list and update the listview:
                case ViewContact.PUSHED_DELETE:
                    contactsList.remove(positionClicked);
                    myAdapter.notifyDataSetChanged();
                    break;

                //if you clicked update, then the other activity should have sent back the new name and email in the Intent object:
                //update the selected object and update the listView:
                case ViewContact.PUSHED_UPDATE:
                    Contact oldContact = contactsList.get(positionClicked);
                    oldContact.update(data.getStringExtra("Name"), data.getStringExtra("Email"));
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    //This class needs 4 functions to work properly:
    protected class MyOwnAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return contactsList.size();
        }

        public Contact getItem(int position){
            return contactsList.get(position);
        }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.contact_row, parent, false );

            Contact thisRow = getItem(position);
            TextView rowName = (TextView)newView.findViewById(R.id.row_name);
            TextView rowEmail = (TextView)newView.findViewById(R.id.row_email);

            TextView rowId = (TextView)newView.findViewById(R.id.row_id);

            rowName.setText("Name:" + thisRow.getName());
            rowEmail.setText("Email:" + thisRow.getEmail());
            rowId.setText("id:" + thisRow.getId());
            //return the row:
            return newView;
        }

        public long getItemId(int position)
        {
            return getItem(position).getId();
        }
    }

}
