package com.example.kapil.customerservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.kapil.customerservice.Model.ContactDetailBeanClass;
import com.example.kapil.customerservice.Model.MessageBeanClass;
import com.example.kapil.customerservice.ProjectUrl.AllUrl;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.kapil.customerservice.CreatMessage.queue;

public class ContactListActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private String imei, message_str, id_chat, msgbody;
    private RecyclerView direct_chat_recycleview;
    private ArrayList<ContactDetailBeanClass> contatact_list;
    private ArrayList<ContactDetailBeanClass> message_list;
    private ProgressBar sign_in_progress_bar;
    private RequestQueue queue;
    private String idUserOfReader, message_id;
    private String ImeiId_str, SubjectId_str, chatBoxId_str, reqString, token;
    public  static  String deviceIMEI;
    private ProgressDialog progress;
    private CustmerDatabase custmerDatabase;
    private boolean flag;
    MessageBeanClass messageBeanClass;
    public DirectChatAdapter directChatAdapter;
    //   private ArrayList<String> product_id_arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

 /*       if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);

                if (key.equals("MessageActivity") && value.equals("True")) {
                    Intent intent = new Intent(this, MessageActivity.class);
                    intent.putExtra("value", value);
                    startActivity(intent);
                    finish();
                }

            }
        }*/
     //

        queue = Volley.newRequestQueue(ContactListActivity.this);



        custmerDatabase=new CustmerDatabase(ContactListActivity.this);
        //subscribeToPushService();

        TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        deviceIMEI = tManager.getDeviceId();
        Log.d("deviceIMEI", "" + deviceIMEI);




      // deviceIMEI="123456";
    // deviceIMEI="353627075615446";
/*


        reqString = Build.MANUFACTURER + "," + Build.MODEL + "," + Build.VERSION.RELEASE
                + "," + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();*/
        String modle = Build.MODEL;
        String modelWithoutSpace = modle.replaceAll("\\s", "");
        reqString = Build.MANUFACTURER + "," + modelWithoutSpace;

        Log.d("reqString", "" + reqString);

        subscribeToPushService();

        startService();


        init();
        imei = Utility.getStringPreferences(ContactListActivity.this, "imie");
        //   msgbody = Utility.getStringPreferences(MainActivity.this, "msgbody");

        if (imei == null) {
            imei = " ";
        } else {

        }

        if (CheckNet.IsInternet(ContactListActivity.this)) {
            //notificationApi(deviceIMEI, reqString, token);
            sign_in_server();

        } else {
            Toast.makeText(ContactListActivity.this, "Check Network Connectivity", Toast.LENGTH_SHORT).show();
        }


        try {

            //  call_direct_chat_adapter();
           /* new Handler().postDelayed(new Runnable() {
                //start the progress dialog
                @Override
                public void run() {
                    call_direct_chat_adapter();
                }
            }, 3000);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this, CreatMessage.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");

     //  Log.d("AndroidBash", "Subscribed");
       // Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();

        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(token);
        // Log and toast
        // Log.d("AndroidBash", token);
       // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
    }
    public void startService() {
        startService(new Intent(getBaseContext(), MyService.class));
    }



    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.


        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST,
                // AllUrl.BASE_URL_TRY,null,
                AllUrl.TOKEN_URL + "&Token=" + token + "&imei=" + deviceIMEI, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("Token_response", "" + response);
                            //  nameResponse(response);


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


    private void init() {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        direct_chat_recycleview = (RecyclerView) findViewById(R.id.direct_chat);
        // sign_in_progress_bar = (ProgressBar) findViewById(R.id.sign_in_progess_id);

    }

    private void call_direct_chat_adapter(ArrayList<ContactDetailBeanClass> detailBeanClasses, ArrayList<ContactDetailBeanClass> msg_list) {

        if (contatact_list != null) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            direct_chat_recycleview.setLayoutManager(mLayoutManager);
            direct_chat_recycleview.setItemAnimator(new DefaultItemAnimator());
             directChatAdapter=new DirectChatAdapter(this, detailBeanClasses, msg_list);
            direct_chat_recycleview.setAdapter(directChatAdapter);
            directChatAdapter.notifyDataSetChanged();
         //   direct_chat_recycleview.setAdapter(directChatAdapter);

        }
    }

    private void sign_in_server() {
        progressDialog();
        //Showing the progress dialog
        //  sign_in_progress_bar.setVisibility(View.VISIBLE);
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
                        progress.dismiss();
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
            contatact_list = new ArrayList<ContactDetailBeanClass>();
            message_list = new ArrayList<ContactDetailBeanClass>();

            JSONArray jsonArray = jsonObject.getJSONArray("chats");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String subject = jsonObject1.getString("subject");
                String imei = jsonObject1.getString("imei");
                String id = jsonObject1.getString("id");
                String id_chat = jsonObject1.getString("idChat");


                ContactDetailBeanClass contactDetailBeanClass = new ContactDetailBeanClass(subject, id, id_chat, imei);
                contatact_list.add(contactDetailBeanClass);

                JSONArray jsonArraymsg = jsonObject1.getJSONArray("Messages");
                for (int i2 = 0; i2 < jsonArraymsg.length(); i2++) {
                    JSONObject jsonObject2 = jsonArraymsg.getJSONObject(i2);

                    String msg_str = jsonObject2.getString("messageBody");
                    message_id = jsonObject2.getString("id");
                    idUserOfReader = jsonObject2.getString("idUserOfReader");
                   // String id_chat_other=jsonObject2.getString("idUserOfReader");
                    String date_created=(String) jsonObject2.getString("dateCreate");
                    String  id_chat2 = (String) jsonObject2.getString("idChat");
                    String  imei_str = (String) jsonObject2.getString("senderType");


                    ContactDetailBeanClass contactDetailBeanClassmsg_str = new ContactDetailBeanClass(msg_str, idUserOfReader, message_id);
                    message_list.add(contactDetailBeanClassmsg_str);

                    String[] date_parts = date_created.split(" ");
                    String date=date_parts[0];
                    String time=date_parts[1];
                    Log.d("date_created",date+" "+time);


                   /* flag=custmerDatabase.get_check_msg_id(message_id);

                    if(flag==false){
                        messageBeanClass = new MessageBeanClass(message_id,msg_str, id_chat2,date,time,true,imei_str);
                        custmerDatabase.insert_chat_data(messageBeanClass);
//                        Toast.makeText(this, "my_successful", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        messageBeanClass = new MessageBeanClass(message_id,msg_str, id_chat,date,time,true,imei_str);
                        custmerDatabase.update_MSG_Table(messageBeanClass);
//                        Toast.makeText(this, "my_successful_updated", Toast.LENGTH_SHORT).show();
                    }
*/
                }



            }
//            call_direct_chat_adapter(message_list);
            call_direct_chat_adapter(contatact_list, message_list);
            progress.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class DirectChatAdapter extends RecyclerView.Adapter<DirectChatAdapter.MyViewHolder> {

        ArrayList<ContactDetailBeanClass> msg_list;
        ArrayList<ContactDetailBeanClass> contact_list_adapter;
        Context mContext;


        public DirectChatAdapter(Context mContext, ArrayList<ContactDetailBeanClass> contact_list_adapter, ArrayList<ContactDetailBeanClass> msg_list) {
            this.msg_list = msg_list;
            this.contact_list_adapter = contact_list_adapter;
            this.mContext = mContext;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.direct_chat_row, parent, false);

            MyViewHolder holder = new MyViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            holder.subject.setText(contact_list_adapter.get(position).getSubject());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                 //   setStatus(msg_list.get(position).getMessage_id(), msg_list.get(position).getIdUserOfReader());


                    String message = msg_list.get(position).getMessagebody();
                    Intent intent = new Intent(ContactListActivity.this, MessageActivity.class);
           /*         intent.putExtra("msg_body", msg_list.get(position).getMessagebody());
                    intent.putExtra("chat_id", contact_list_adapter.get(position).getIdchat());
                    intent.putExtra("imei", contact_list_adapter.get(position).getImei());
                    intent.putExtra("subject", contact_list_adapter.get(position).getSubject());*/

                    Utility.setStringPreferences(ContactListActivity.this,"msg_body1",""+msg_list.get(position).getMessagebody());
                    Utility.setStringPreferences(ContactListActivity.this,"chat_id1",""+contact_list_adapter.get(position).getIdchat());
                    Utility.setStringPreferences(ContactListActivity.this,"imei1",""+contact_list_adapter.get(position).getImei());
                    Utility.setStringPreferences(ContactListActivity.this,"subject1",""+contact_list_adapter.get(position).getSubject());

                    Log.d("adaaaa", contact_list_adapter.get(position).getImei() + "," + contact_list_adapter.get(position).getIdchat()
                            + "," + msg_list.get(position).getMessagebody() + "," + contact_list_adapter.get(position).getSubject());
                    startActivity(intent);
                    finish();
                }
            });
        }


        @Override
        public int getItemCount() {
            return contact_list_adapter.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView subject, number;
            private LinearLayout linearLayout;

            public MyViewHolder(View view) {
                super(view);
                subject = (TextView) view.findViewById(R.id.direct_person_name);
             //   number = (TextView) view.findViewById(R.id.direct_chat_num);
                linearLayout = (LinearLayout) view.findViewById(R.id.direct_linear);

            }
        }
    }



    private void progressDialog() {
        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setMessage("Loading...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

}

