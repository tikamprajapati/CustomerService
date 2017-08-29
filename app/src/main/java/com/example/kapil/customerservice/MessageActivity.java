package com.example.kapil.customerservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.kapil.customerservice.LocalStorage.CommonMethods;
import com.example.kapil.customerservice.LocalStorage.Utility;
import com.example.kapil.customerservice.Model.ContactDetailBeanClass;
import com.example.kapil.customerservice.Model.MessageBeanClass;
import com.example.kapil.customerservice.ProjectUrl.AllUrl;
import com.example.kapil.customerservice.adapter.ChatAdapter;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.kapil.customerservice.ContactListActivity.deviceIMEI;

/**
 * Created by kapil on 29-Jun-17.
 */

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView msg_thread_recycle;
    private String imei_str, message_str, id_chat_str, subject_str;
    private String id_chat, mesg_body, token;
    private ArrayList<MessageBeanClass> msg_list;
    private ImageButton sendMessageButton;
    private EditText messageEditText;
    public ChatAdapter chatAdapter;
    private ListView msgListView;
    private RequestQueue queue;
    private ProgressDialog progress;
    private TextView contact_name;
    private CustmerDatabase custmerDatabase;
    private boolean flag;
    MessageBeanClass messageBeanClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        queue = Volley.newRequestQueue(MessageActivity.this);
        custmerDatabase = new CustmerDatabase(MessageActivity.this);
        msg_list = new ArrayList<MessageBeanClass>();
        init();


        message_str = Utility.getStringPreferences(MessageActivity.this, "msg_body1");
        id_chat_str = Utility.getStringPreferences(MessageActivity.this, "chat_id1");
        imei_str = Utility.getStringPreferences(MessageActivity.this, "imei1");
        subject_str = Utility.getStringPreferences(MessageActivity.this, "subject1");
        Log.d("my_data", "" + message_str + id_chat_str + imei_str + subject_str);

        if (subject_str != null) {
            contact_name.setText(subject_str);
        }

        ;
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            id_chat_str = Utility.getStringPreferences(MessageActivity.this, "create_my_chat");
            subject_str = Utility.getStringPreferences(MessageActivity.this, "create_subject");

            contact_name.setText("" + subject_str);
        }
        sign_in_server();
        //subscribeToPushService();
        if (bundle != null) {
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
        //local_msg();

    }

    private void init() {
        //   msg_thread_recycle = (RecyclerView) findViewById(R.id.thread_message);
        sendMessageButton = (ImageButton) findViewById(R.id.sendMessageButton);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        contact_name = (TextView) findViewById(R.id.chat_person_name);
        msgListView = (ListView) findViewById(R.id.msgListView);


        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        //  msgListView = (ListView) findViewById(R.id.msgListView);
        sendMessageButton.setOnClickListener(this);
    }
  /*  private void local_msg(){
          msg_list=custmerDatabase.get_chat_data(id_chat_str);
        Log.d("listttt",""+msg_list);
       //   if(msg_list.size()!=0){
            chatAdapter=new ChatAdapter(MessageActivity.this,msg_list);
            msgListView.setAdapter(chatAdapter);
             chatAdapter.notifyDataSetChanged();
        //}
    }

    public void sendTextMessage() {

        message_str = messageEditText.getEditableText().toString();
        if(!message_str.isEmpty()) {
            messageBeanClass = new MessageBeanClass("", mesg_body, id_chat, CommonMethods.getCurrentDate(), CommonMethods.getCurrentTime(),false,"Customer");
            messageBeanClass.msg_body = message_str;
            messageEditText.setText("");
            chatAdapter.add(messageBeanClass);
            chatAdapter.notifyDataSetChanged();
            signdingMessage_in_server(message_str);

        }
        else {
            Toast.makeText(this, "No Message", Toast.LENGTH_SHORT).show();
        }

    }*/

    private void sign_in_server() {
        //  progressDialog();
        //Showing the progress dialog
        //  sign_in_progress_bar.setVisibility(View.VISIBLE);
        msg_list.clear();
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST,
                AllUrl.BASE_URL1 + "&imei=" + deviceIMEI, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            //progress.dismiss();
                            Log.d("punch_response", "" + response);
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
                        //   progress.dismiss();
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
            //contatact_list = new ArrayList<ContactDetailBeanClass>();
            //   ArrayList<MessageBeanClass> message_list = new ArrayList<MessageBeanClass>();

            JSONArray jsonArray = jsonObject.getJSONArray("chats");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String subject = jsonObject1.getString("subject");
                String imei = jsonObject1.getString("imei");
                String id = jsonObject1.getString("id");
                String id_chat = jsonObject1.getString("idChat");


                JSONArray jsonArraymsg = jsonObject1.getJSONArray("Messages");
                for (int i2 = 0; i2 < jsonArraymsg.length(); i2++) {
                    JSONObject jsonObject2 = jsonArraymsg.getJSONObject(i2);

                    String msg_str = jsonObject2.getString("messageBody");
                    String message_id = jsonObject2.getString("id");
                    String idUserOfReader = jsonObject2.getString("idUserOfReader");
                    // String id_chat_other=jsonObject2.getString("idUserOfReader");
                    String date_created = (String) jsonObject2.getString("dateCreate");
                    String id_chat2 = (String) jsonObject2.getString("idChat");
                    String imei_str = (String) jsonObject2.getString("senderType");
                    String read_staus = (String) jsonObject2.getString("readStatus");
                    String send_status = (String) jsonObject2.getString("sendStatus");
                    String sender_type = (String) jsonObject2.getString("senderType");

                    String[] date_parts = date_created.split(" ");
                    String date = date_parts[0];
                    String time = date_parts[1];
                    Log.d("date_created", date + " " + time);


                    if (read_staus.equals("0") && sender_type.equals("User")) {
                        setStatus(message_id);
                    }

                    if (send_status.equals("0") && sender_type.equals("User")) {
                        set_sent_Status(message_id);
                    }

                    MessageBeanClass messageBeanClass = new MessageBeanClass(msg_str, date, time, read_staus, send_status, sender_type);

                    if (id_chat2.equals(id_chat_str)) {
                        msg_list.add(messageBeanClass);
                    }
                }

            }
//            call_direct_chat_adapter(message_list);
            // progress.dismiss();
            if (msg_list.size() != 0)
                chatAdapter = new ChatAdapter(MessageActivity.this, msg_list);
            msgListView.setAdapter(chatAdapter);
            chatAdapter.notifyDataSetChanged();
            // progress.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sendMessageButton:
                message_str = messageEditText.getEditableText().toString();

                String mass;
                mass = message_str.replace("\n", " ");
                if (!mass.isEmpty()) {
                    signdingMessage_in_server(mass);
                }
                messageEditText.setText("");
                chatAdapter.notifyDataSetChanged();
                break;
        }
    }


    private void signdingMessage_in_server(String msg) {
        progressDialog();
        //Showing the progress dialog
        //  sign_in_progress_bar.setVisibility(View.VISIBLE);
        Log.d("parameters", "" + msg + "," + id_chat_str + "," + imei_str);

        String mgs2 = msg.replaceAll(" ", "%20");

        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST,
                AllUrl.BASE_URL3 + "&idChat=" + id_chat_str + "&messageBody=" + mgs2 + "&imei=" + imei_str, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("res_response1", "" + response);
                            signdingMessageResponse(response);
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
            msg_list.clear();
            JSONArray jsonArraymsg;
            JSONArray jsonArray = jsonObject.getJSONArray("Messages");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String msg_str = jsonObject1.getString("messageBody");
                String message_id = jsonObject1.getString("id");
                String idUserOfReader = jsonObject1.getString("idUserOfReader");
                // String id_chat_other=jsonObject2.getString("idUserOfReader");
                String date_created = (String) jsonObject1.getString("dateCreate");
                String id_chat2 = (String) jsonObject1.getString("idChat");
                String imei_str = (String) jsonObject1.getString("senderType");
                String read_staus = (String) jsonObject1.getString("readStatus");
                String send_status = (String) jsonObject1.getString("sendStatus");
                String sender_type = (String) jsonObject1.getString("senderType");


                if (read_staus.equals("0") && sender_type.equals("User")) {
                    setStatus(message_id);
                }

                if (send_status.equals("0") && sender_type.equals("User")) {
                    set_sent_Status(message_id);
                }

                String[] date_parts = date_created.split(" ");
                String date = date_parts[0];
                String time = date_parts[1];
                Log.d("date_created", date + " " + time);

                MessageBeanClass messageBeanClass = new MessageBeanClass(msg_str, date, time, read_staus, send_status, sender_type);

                if (id_chat2.equals(id_chat_str)) {
                    msg_list.add(messageBeanClass);

                }
            }
            progress.dismiss();
            if (msg_list.size() != 0) {
                chatAdapter = new ChatAdapter(MessageActivity.this, msg_list);
                msgListView.setAdapter(chatAdapter);

            }
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MessageActivity.this, ContactListActivity.class);
        startActivity(intent);
        finish();

    }

    private void setStatus(String message_id) {
        Log.d("idMessage", "" + message_id);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST,
                AllUrl.MESSAGER_READED_BASE_URL + "&idMessage=" + message_id, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
////                                        dialog.dismiss();
                            Log.d("Status_respomse", "" + response);
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
                            Log.d("Status_respomse_sent", "" + response);
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

    }

  /*  private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        Log.d("AndroidBash", "Subscribed");
        // Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();

        token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        // Log.d("AndroidBash", token);
        // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
    }*/
}