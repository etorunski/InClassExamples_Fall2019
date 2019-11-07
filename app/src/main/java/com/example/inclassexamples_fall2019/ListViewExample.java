package com.example.inclassexamples_fall2019;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.Arrays;


public class ListViewExample extends AppCompatActivity {
    //items to display
    ArrayList<String> objects = new ArrayList<>(Arrays.asList("Item 1", "Item 2", "Item 3" ) );

    BaseAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        //You only need 2 lines in onCreate to actually display data:
        ListView theList = findViewById(R.id.theList);
        theList.setAdapter( myAdapter = new MyListAdapter() );
        theList.setOnItemClickListener( ( lv, vw, pos, id) ->{

            Toast.makeText( ListViewExample.this,
                    "You clicked on:" + pos, Toast.LENGTH_SHORT).show();

        } );

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener( clik ->
        {
            objects.add("Item " + (1+objects.size()) );
            myAdapter.notifyDataSetChanged(); //update yourself
        });

        SwipeRefreshLayout refresher = findViewById(R.id.refresher);
        refresher.setOnRefreshListener(() -> {

            objects.add("Item " + (1+objects.size()) );
            myAdapter.notifyDataSetChanged(); //update yourself
            refresher.setRefreshing(false);  //get rid of spinning wheel;
        });
    }



    //Need to add 4 functions here:
    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return objects.size();  } //This function tells how many objects to show

        public String getItem(int position) {
            return objects.get(position);  }  //This returns the string at position p

        public long getItemId(int p) {
            return p; } //This returns the database id of the item at position p

        public View getView(int p, View recycled, ViewGroup parent)
        {
            View thisRow = recycled;

            if(recycled == null)
                thisRow = getLayoutInflater().inflate(R.layout.table_row_layout, null);

            TextView itemText = thisRow.findViewById(R.id.itemField  );
            itemText.setText( "Array at:" + p + " is " + getItem(p) );

            TextView numberText = thisRow.findViewById(R.id.numberField);
            numberText.setText("Your number is " + p);
            return thisRow;
        }
    }
}
