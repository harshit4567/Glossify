package com.example.harshit.glossify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.http.RequestQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.os.SystemClock.sleep;
import static java.security.AccessController.getContext;


public class process extends AppCompatActivity {

    TextView taxtview;
    static String str;
    static String str2;
    String aJsonString="";
    String aJsonString2="";
    String[] arr = new String[0];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        setTitle("Extracted Text");

        // getActionBar().setTitle("Complete Text");
        taxtview = (TextView) findViewById(R.id.show_response);
        //taxtview.setText("text displayed\n");
        sendPost();
       /* try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        JSONObject jsonObject = null;

        if(str!=null) {
            try {
                jsonObject = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                aJsonString = jsonObject.getString("Text");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            aJsonString = aJsonString.replaceAll("[^\\x00-\\x7F]", "");
            sendPost2();
           /* try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            JSONObject jsonObject2 = null;
            try {
                jsonObject2 = new JSONObject(str2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONArray temp = jsonObject2.getJSONArray("documents");
                JSONObject temp2 = temp.getJSONObject(0);
                JSONArray temp3 = temp2.getJSONArray("keyPhrases");
                //aJsonString2 = temp3.toString();

                for (int i = 0; i < temp3.length(); i++) {
                    try {

                        //arr[i] = temp3.getString(i);
                        aJsonString2 += temp3.getString(i) + '\n';
                    } catch (JSONException e) {
                        // Oops
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // sleep(2000);
        //String t = Arrays.toString(arr);
        taxtview.setMovementMethod(new ScrollingMovementMethod());
        taxtview.setText(aJsonString);
        Intent intent =new Intent(process.this,glossy_list.class);
        intent.putExtra("array",aJsonString2);
        startActivity(intent);

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

    public void forward(View view){
        Intent i = new Intent(process.this,glossy_list.class);
        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("array",aJsonString2);
        startActivity(i);



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

    public void sendPost(){
        final String urlfile = "https://glossify.blob.core.windows.net/images/"+ImageManager.geturl();

        final String urlAddress = "https://southeastasia.api.cognitive.microsoft.com/contentmoderator/moderate/v1.0/ProcessImage/OCR?language=eng&CacheImage=false&enhanced=false";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAddress);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Ocp-Apim-Subscription-Key", "key here");

                    // conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    Log.i("URL", ""+urlfile);
                    jsonParam.put("DataRepresentation","URL");
                    jsonParam.put( "Value",urlfile);



                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

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
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // taxtview.setText(str);

    }



    public void sendPost2(){
        //final String urlfile = "https://glossify.blob.core.windows.net/images/"+ImageManager.geturl();

        final String urlAddress2 = "https://southeastasia.api.cognitive.microsoft.com/text/analytics/v2.0/keyPhrases";
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAddress2);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Ocp-Apim-Subscription-Key", "key here");
                    //conn.setRequestProperty("Host","southeastasia.api.cognitive.microsoft.com");
                    conn.setUseCaches(false);
                    // conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject documents = new JSONObject();
                    // Log.i("URL", ""+urlfile);
                    documents.put( "language","en");
                    documents.put("id","string");
                    documents.put("text",aJsonString);

                    //jsonParam.put("DataRepresentation","URL");
                    //jsonParam.put( "Value",urlfile);

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(documents);
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("documents", jsonArray);
                    Log.i("JSON2", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS2", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG2" , conn.getResponseMessage());



                    if(conn.getInputStream()!=null) {
                        DataInputStream is = new DataInputStream(conn.getInputStream());
                        if(is!=null)
                        {str2 = convertStreamToString(is);}
                    }



                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //  taxtview.setText(str);

        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // taxtview.setText(str);

    }


}