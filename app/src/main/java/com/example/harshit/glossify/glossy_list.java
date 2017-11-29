package com.example.harshit.glossify;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class glossy_list extends AppCompatActivity {

    public ListView lv1;

   // Bundle bundle = getIntent().getExtras();
 //  String t = bundle.getString("array");


   // public String lv_arr[] = {"SuperLeague 2010-2011", "Olympiakos on YouTube", "Olympiakos Web Site", "Find Karaiskaki on map", "Reserve Tickets"};
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_glossy_list);
        setTitle("Keywords");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        // getActionBar().setTitle("Keywords");
        Intent myintent = getIntent();
        String t = myintent.getStringExtra("array");
        final String lv_arr[] = t.split("\n");
        lv1 = (ListView) findViewById(R.id.details);

        // By using setAdpater method in listview we an add string array in list.

        lv1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lv_arr));


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent myIntent = new Intent(glossy_list.this,Listview1.class);
                //(myIntent, 0);
                myIntent.putExtra("name",lv_arr[position]);
                startActivity(myIntent);



            }
        });
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
