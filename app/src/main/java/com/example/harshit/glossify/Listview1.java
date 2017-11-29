package com.example.harshit.glossify;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.Thread.sleep;

public class Listview1 extends AppCompatActivity {

    String q;
    String str="";
    String str2="";
    // Button button = (Button) findViewById(R.id.button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview1);
        //Button button = (Button) findViewById(R.id.button);
        Intent myIntent = getIntent(); // gets the previously created intent
        q = myIntent.getStringExtra("name");
        setTitle(q);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        //TextView txtview = (TextView) findViewById(R.id.details);
        //sendGet();
        // txtview.setText(str);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGet();
                /*try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                settext(str);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGet2();
                /*try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                settext2(str2);
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



    public void settext(String str){
        TextView txtview = (TextView) findViewById(R.id.details);
        String aJsonString=" ";
        if(str!=null)
        {aJsonString = calc(str);}
        txtview.setMovementMethod(new ScrollingMovementMethod());
        txtview.setText(aJsonString);
    }

    public void settext2(String str){
        TextView txtview = (TextView) findViewById(R.id.details);
        String aJsonString = calc2(str);
        // txtview.setMovementMethod(new ScrollingMovementMethod());
        txtview.setMovementMethod(new ScrollingMovementMethod());
        txtview.setText(aJsonString);
    }

    public String calc(String str){

        String aJsonString="";
        JSONObject jsonObject = null;
        JSONObject temp = null;
        JSONObject temp2=null;
        JSONArray temp3 = null;
        if(str!=null)
        {try {
            jsonObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonObject!=null && jsonObject.has("entities"))
        { try {

            temp= jsonObject.getJSONObject("entities");
        } catch (JSONException e) {
            e.printStackTrace();
        }
            assert temp != null;
            if(temp!=null && temp.has("value"))
            { try {

                temp3= temp.getJSONArray("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }

                for(int i=0;i<temp3.length();i++)
                {
                    assert temp3 != null;
                    try {
                        temp2=temp3.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    assert  temp2!=null;
                    if(temp2!=null && temp2.has("description"))
                    {try {

                        aJsonString+=temp2.getString("description")+'\n';
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}


                }}}}
        return aJsonString;
    }

    public String calc2(String str) {

        String aJsonString="";
        JSONObject jsonObject = null;
        JSONArray temp = null;
        JSONObject temp2=null;
        JSONObject temp3 = null;
        JSONArray temp4 = null;
        JSONObject temp5 = null;
        JSONArray temp6 = null;
        JSONObject temp7 = null;
        JSONArray temp8 = null;
        JSONObject temp9=null;

        if(str!=null)
        {  try {
            jsonObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonObject!=null && jsonObject.has("results"))
        { try {

            temp= jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
            assert temp != null;
            if(temp!=null)
            { try {

                temp3= temp.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(temp3 !=null && temp3.has("lexicalEntries"))
            { try {
                    temp4=temp3.getJSONArray("lexicalEntries");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<temp4.length();i++) {
                    try {
                        temp5 = temp4.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(temp5!=null && temp5.has("entries"))
                    { try {
                        temp6= temp5.getJSONArray("entries");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        temp7 = temp6.getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(temp7!=null && temp7.has("senses"))
                    {try {
                        temp8 = temp7.getJSONArray("senses");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        temp9 = temp8.getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(temp9!=null && temp9.has("definitions"))
                    { try {
                        aJsonString+= temp9.getString("definitions");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }}}}}}
             /*   for(int i=0;i<temp3.length();i++)
                {
                    assert temp3 != null;
                    try {
                        temp2=temp3.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    assert  temp2!=null;
                    if(temp2!=null && temp2.has("description"))
                    {try {

                        aJsonString+=temp2.getString("description")+'\n';
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}


                }*/
            }}
        return aJsonString;
    }

    public String convertStreamToString(DataInputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void sendGet(){

        // Button button = (Button) findViewById(R.id.button);

        // final String urlfile = "https://glossify.blob.core.windows.net/images/"+ImageManager.geturl();
        q = q.replaceAll(" ","%20");
        final String request = "https://api.cognitive.microsoft.com/bing/v7.0/entities/?q="+q+"&mkt=en-us&count=10&offset=0&safesearch=Moderate";
        final String urlAddress = request;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = null;
                    URL url = new URL(urlAddress);
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Ocp-Apim-Subscription-Key", "a7f16447c3b1437a91cddc2ff39ea179");

                    Log.i("request", ""+request);

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());


                    DataInputStream is = new DataInputStream(conn.getInputStream());
                    str = convertStreamToString(is);

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //  taxtview.setText(str);

        thread.start();
        // taxtview.setText(str);
        // settext(str);
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void sendGet2(){

        // Button button = (Button) findViewById(R.id.button);

        // final String urlfile = "https://glossify.blob.core.windows.net/images/"+ImageManager.geturl();
        q = q.replaceAll(" ","%20");
        final String request = "https://od-api.oxforddictionaries.com:443/api/v1/entries/en/"+q;
        final String urlAddress = request;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = null;
                    URL url = new URL(urlAddress);
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setRequestProperty("app_id","18a41497");
                    conn.setRequestProperty( "app_key","key here");
                    Log.i("request", ""+request);

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    DataInputStream is = new DataInputStream(conn.getInputStream());
                    str2 = convertStreamToString(is);

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //  taxtview.setText(str);

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // taxtview.setText(str);
        // settext(str);

    }
}

