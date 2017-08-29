package com.example.kapil.customerservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.kapil.customerservice.DataBase.CustmerDatabase;
import com.example.kapil.customerservice.InternetCheck.CheckNet;
import com.example.kapil.customerservice.LocalStorage.Utility;
import com.example.kapil.customerservice.Model.MessageBeanClass;
import com.example.kapil.customerservice.ProjectUrl.AllUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreatMessage extends AppCompatActivity implements View.OnClickListener {
    private TextView submitId, chat_person_name;
    private EditText ImeiId, SubjectId, chatBoxId;
    private String ImeiId_str, SubjectId_str, chatBoxId_str;
    public static RequestQueue queue;
    private String deviceIMEI;
    private String reqString;
    private CustmerDatabase custmerDatabase;
    private boolean flag;
    MessageBeanClass messageBeanClass;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_message);
        queue = Volley.newRequestQueue(CreatMessage.this);
        custmerDatabase=new CustmerDatabase(CreatMessage.this);
        init();
      /*  final String deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("deviceId", "" + deviceId);

        Toast.makeText(this, "deviceId" + deviceId, Toast.LENGTH_SHORT).show();
*/

// GET IMEI NUMBER

        TelephonyManager tManager = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
         deviceIMEI = tManager.getDeviceId();

        //deviceIMEI="123456";
        //deviceIMEI="353627075615446";
        Log.d("deviceIMEI", "" + deviceIMEI);


        // Toast.makeText(this, "deviceIMEI" + deviceIMEI, Toast.LENGTH_SHORT).show();

//        String model = Build.MODEL;

        reqString = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

        Log.d("reqString", "" + reqString);

        setname_in_server();

    }

    private void init() {
//        ImeiId = (EditText) findViewById(R.id.ImeiId);
        SubjectId = (EditText) findViewById(R.id.SubjectId);
        chatBoxId = (EditText) findViewById(R.id.chatBoxId);
        submitId = (TextView) findViewById(R.id.submitId);
        chat_person_name = (TextView) findViewById(R.id.chat_person_);

        submitId.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {//
            case R.id.submitId:
                if(CheckNet.IsInternet(CreatMessage.this)) {
                    msg_body_vallidation();
                }
                else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    private void msg_body_vallidation() {
//        ImeiId_str = ImeiId.getText().toString().trim();
//        ImeiId.setText(deviceIMEI);
        SubjectId_str = SubjectId.getText().toString().trim();
        chatBoxId_str = chatBoxId.getText().toString().trim();

//        if (TextUtils.isEmpty(ImeiId_str)) {
//                Toast.makeText(this, "Please enter EMIE", Toast.LENGTH_SHORT).show();
//              }
        if (TextUtils.isEmpty(SubjectId_str)) {
            Toast.makeText(this, "Please enter subject", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(chatBoxId_str)) {
            Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
        } else {
            if (CheckNet.IsInternet(CreatMessage.this)) {
                sign_in_server();
             //   moveto_Activity();


            } else {
                Toast.makeText(this, "Check Network Connectivity", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setname_in_server() {
        //Showing the progress dialog
        //  sign_in_progress_bar.setVisibility(View.VISIBLE);

        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST,
                // AllUrl.BASE_URL_TRY,null,
                AllUrl.BASE_URL7 + "&imei=" + deviceIMEI + "&device_model=" + reqString, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("Name_Response", "" + response);
                            nameResponse(response);


////                                        response_punch_data_from_server(response);

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

    }


    private void nameResponse(JSONObject jsonObject) {
        try {
            String user_name = jsonObject.getString("name");
//            if(user_name!=null) {
            chat_person_name.setText(user_name);
            Log.d("user_name", "" + user_name);
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sign_in_server() {
        //Showing the progress dialog
        //sign_in_progress_bar.setVisibility(View.VISIBLE);
        String new_SubjectId_str = null;
        String new_chatBoxId_str = null;
        progressDialog();


        if(SubjectId_str.contains(" ")){
            new_SubjectId_str =SubjectId_str.replace(" ","%20");
        }
        if(chatBoxId_str.contains(" ")){
             new_chatBoxId_str =chatBoxId_str.replace(" ","%20");
        }

        Log.d("new_string",""+new_SubjectId_str+"/"+new_chatBoxId_str);


        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST,
                AllUrl.BASE_URL + "&imei=" + deviceIMEI + "&subject=" + new_SubjectId_str + "&messageBody=" + new_chatBoxId_str, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
////                                        dialog.dismiss();
                            Log.d("chat_response", "" + response);
                            signdingMessageResponse(response);
                            ///  if(response.)
////                                        response_punch_data_from_server(response);

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        progress.dismiss();
                    }
                }
        );

        queue.add(obreq);

    }
    private void signdingMessageResponse(JSONObject jsonObject) {

        try {

            String chat=jsonObject.getString("Chat");
            JSONObject jsonObject2=new JSONObject(chat);

            String sub_name=jsonObject2.getString("subject");
       ;


            Utility.setStringPreferences(CreatMessage.this,"create_subject",sub_name);
            JSONArray jsonArray = jsonObject2.getJSONArray("Messages");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            //    String msg_id=(String) jsonObject1.getString("id");
                String  id_chat = (String) jsonObject1.getString("idChat");
              //  String   mesg_body = (String) jsonObject1.getString("messageBody");
                //String date_created=(String) jsonObject1.getString("dateCreate");

                    Utility.setStringPreferences(CreatMessage.this,"create_my_chat",id_chat);
            }
            progress.dismiss();

            Intent intent = new Intent(CreatMessage.this, ContactListActivity.class);

            //    intent.putExtra("imi",ImeiId_str);
            startActivity(intent);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void progressDialog() {
        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setMessage("Loading...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    private void moveto_Activity() {
        Utility.setStringPreferences(CreatMessage.this, "imie", deviceIMEI);
        // Utility.setStringPreferences(CreatMessage.this,"msgbody",chatBoxId_str);
        Intent intent = new Intent(CreatMessage.this, ContactListActivity.class);

        //    intent.putExtra("imi",ImeiId_str);
        startActivity(intent);
        finish();
    }



}
