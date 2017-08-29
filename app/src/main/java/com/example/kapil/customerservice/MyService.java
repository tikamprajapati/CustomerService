package com.example.kapil.customerservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kapil.customerservice.Model.ContactDetailBeanClass;
import com.example.kapil.customerservice.ProjectUrl.AllUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dell on 19-07-2017.
 */

public class MyService extends Service {

    private ConnectivityManager cnnxManager;
    public    String deviceIMEI;
    private RequestQueue queue;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(MyService.this);
        cnnxManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        deviceIMEI = tManager.getDeviceId();

        final Handler mHandler1 = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(3000);
                        mHandler1.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                // Write your code here to update the UI.
                                sign_in_server();
                                //   set_recycle_adapter();
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
      //  Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
    private void sign_in_server() {
        //progressDialog();
        //Showing the progress dialog
        //  sign_in_progress_bar.setVisibility(View.VISIBLE);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST,
                AllUrl.BASE_URL1 + "&imei=" + deviceIMEI, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            //progress.dismiss();
                            Log.d("punch_response_service", "" + response);
////                                        response_punch_data_from_server(response);
                            getsResponse(response);

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
          //              progress.dismiss();
                        Log.e("Volley", "Error");
                        // dialog.dismiss();
                    }
                }
        );

        queue.add(obreq);

    }


    private void getsResponse(JSONObject jsonObject) {
        try {
         /*   String error = jsonObject.getString("STATUS");
            if (error.equals("ERROR")) {

            }*/
          //  contatact_list = new ArrayList<ContactDetailBeanClass>();
           // message_list = new ArrayList<ContactDetailBeanClass>();

            JSONArray jsonArray = jsonObject.getJSONArray("chats");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String subject = jsonObject1.getString("subject");
                String imei = jsonObject1.getString("imei");
                String id = jsonObject1.getString("id");
                String id_chat = jsonObject1.getString("idChat");


                ContactDetailBeanClass contactDetailBeanClass = new ContactDetailBeanClass(subject, id, id_chat, imei);
             //   contatact_list.add(contactDetailBeanClass);

                JSONArray jsonArraymsg = jsonObject1.getJSONArray("Messages");
                for (int i2 = 0; i2 < jsonArraymsg.length(); i2++) {
                    JSONObject jsonObject2 = jsonArraymsg.getJSONObject(i2);

                    String msg_str = jsonObject2.getString("messageBody");
                  String  message_id = jsonObject2.getString("id");
                    String  idUserOfReader = jsonObject2.getString("idUserOfReader");
                    // String id_chat_other=jsonObject2.getString("idUserOfReader");
                    String date_created=(String) jsonObject2.getString("dateCreate");
                    String  id_chat2 = (String) jsonObject2.getString("idChat");
                    String  imei_str = (String) jsonObject2.getString("senderType");
                    String  sender_type = (String) jsonObject2.getString("senderType");

                    String  send_status = (String) jsonObject2.getString("sendStatus");
                    if(send_status.equals("0") && sender_type.equals("User")){
                        set_sent_Status(message_id);
                    }



                }



            }
//

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void set_sent_Status(String message_id) {
        Log.d("idMessage", "" + message_id);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST,
                AllUrl.MESSAGER_RECIVED_BASE_URL + "&idMessage=" + message_id, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
////                                        dialog.dismiss();
                            Log.d("Status_ser", "" + response);
////                                        response_punch_data_from_server(response);
                            //statusResponse(response);

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        // dialog.dismiss();
                    }
                }
        );

        queue.add(obreq);

    }}
