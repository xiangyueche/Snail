package com.example.westsnow.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SendMoment extends ActionBarActivity {

    private String username;
    private EditText context;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String URL = Constant.serverDNS + "/sendMoment.php";
    //    private static final String URL =  "http://ec2-52-24-240-104.us-west-2.compute.amazonaws.com/register.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public void send(View view){
        new SendMomentAttemp().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_moment);

        context=(EditText)findViewById(R.id.context);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_moment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SendMomentAttemp extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog *
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SendMoment.this);
            pDialog.setMessage("Attempting for send moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", username));
                params.add(new BasicNameValuePair("context", context.getText().toString()));
                params.add(new BasicNameValuePair("latitude", "0.0"));
                params.add(new BasicNameValuePair("longtitude", "0.0"));

                Log.d("request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest(URL, "GET", params);
                // checking log for json response
                Log.d("send moment attempt", json.toString());
                // success tag for json
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Successfully registed!", json.toString());
                    Intent ii = new Intent(SendMoment.this, PersonalPage.class);
//                    finish();
                    // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                    ii.putExtra("username", username);
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE) + "~";
                } else {
                    return json.getString(TAG_MESSAGE) + "~";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Once the background process is done we need to Dismiss the progress dialog asap * *
         */
        protected void onPostExecute(String message) {
            pDialog.dismiss();
            if (message != null) {
                Toast.makeText(SendMoment.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
