package com.viba.homeautomationfinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

public class NewComponents extends AppCompatActivity {

    //    Button show, showb, hsowc;
//    int state=0, s2;
    Button addcomp;
    LinearLayout layoutvisi;
    Button addswitch, addSwitch2;
    EditText switchnamea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_components);
        addcomp = findViewById(R.id.addcomp);


//        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        final String address = info.getMacAddress();
        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.filename), Context.MODE_PRIVATE);

        addcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(NewComponents.this).inflate(R.layout.switchedit, viewGroup, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(NewComponents.this);

                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();
                Window window = alertDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);

                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                alertDialog.show();
                final Button addswitch, addSwitch2;
                final EditText switchnamea;

                final String android_id = Secure.getString(getApplicationContext().getContentResolver(),
                        Secure.ANDROID_ID);
                addswitch = dialogView.findViewById(R.id.addSwitch);
                switchnamea = dialogView.findViewById(R.id.editswitch1);
                addswitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            RequestQueue requestQueue = Volley.newRequestQueue(NewComponents.this);
                            String URL = "http://192.168.1.10:5000";
                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("Uuid", android_id);
                            jsonBody.put("id", 1);
                            jsonBody.put("state", 0);
                            final String requestBody = jsonBody.toString();
                            Toast.makeText(NewComponents.this, android_id, Toast.LENGTH_SHORT).show();

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
                            addswitch.setText("Added");
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("switchname", switchnamea.getText().toString());
                            editor.putInt("id", 1);
                            editor.putInt("state",0);
                            editor.commit();
                            alertDialog.dismiss();

                            startActivity(new Intent(NewComponents.this, HomeActivity.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });



            }

            //  layoutvisi.setVisibility(View.VISIBLE);

        });


    }
}

//        addSwitch2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//        String name = getIntent().getStringExtra("name");
//
//        show = findViewById(R.id.compa);
//        show.setText(name);
//      //  showb = findViewById(R.id.compb);
//
//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //put();
//            }
//        });



//    private void resolveRequest() {
//        String url = "https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400&date=today";
//        StringRequest req;
//        req = new StringRequest(Request.Method.GET,url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response.toString());
//
//                            String ipaddress = String.valueOf(jsonObject.get("gpio"));
//                            String mac = String.valueOf(jsonObject.get("gpio"));
//                           // Toast.makeText(NewComponents.this,"gpio :"+jsonObject.getString("results"), Toast.LENGTH_SHORT).show();
//                            // Toast.makeText(MainActivity.this, "status : "+jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(NewComponents.this,error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(req);
//
//
//
//
 //   }

