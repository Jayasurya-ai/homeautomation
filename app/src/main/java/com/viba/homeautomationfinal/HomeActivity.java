package com.viba.homeautomationfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {
    TextView switchname;
    String switchnameeeeee;
    RelativeLayout onof;
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        switchname=findViewById(R.id.switchnameaa);
        onof=findViewById(R.id.onof);
        final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.filename), Context.MODE_PRIVATE);
        final String highScore = sharedPref.getString("switchname",switchnameeeeee);
       final int highSc = sharedPref.getInt("state",temp);
        Toast.makeText(this, highScore, Toast.LENGTH_SHORT).show();
        switchname.setText(highScore);
        final SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt("state",0);
        editor.commit();
        onof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "hiee", Toast.LENGTH_SHORT).show();


                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
                    String URL = "http://192.168.1.10:5000";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("id", 1);
                    jsonBody.put("Uuid",android_id);
                        if(Integer.parseInt(String.valueOf(highSc))==0){
                            jsonBody.put("state",1);
                            editor.putInt("state",1);

                        }
                        else
                        if(Integer.parseInt(String.valueOf(highSc))==1){
                            jsonBody.put("state",0);
                            editor.putInt("state",0);
                        }
                    Toast.makeText(HomeActivity.this, android_id, Toast.LENGTH_SHORT).show();

                    final String requestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            String responseString = "";
                            if (response != null) {
                                responseString = String.valueOf(response.statusCode);
                                // can get more details such as response.headers
                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}