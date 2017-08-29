package com.example.kapil.customerservice.adapter;

/**
 * Created by AVYAKT on 18-05-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.kapil.customerservice.Model.MessageBeanClass;
import com.example.kapil.customerservice.R;

import java.util.ArrayList;

import static com.example.kapil.customerservice.ContactListActivity.deviceIMEI;


public class ChatAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<MessageBeanClass> chatMessageList;
    private Context context;


      public ChatAdapter(Context context, ArrayList<MessageBeanClass> chatMessageList) {
       this.chatMessageList = chatMessageList;
          this.context=context;
          Log.d("mylist",""+chatMessageList);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        try {

            MessageBeanClass message = (MessageBeanClass) chatMessageList.get(position);

            if (convertView == null)
                vi = inflater.inflate(R.layout.chatbubble, null);
            Log.d("mydata",""+message.msg_body);
            //Log.d("mylist",""+chatMessageList);
            LinearLayout linearLayout= (LinearLayout) vi.findViewById(R.id.bubble_layout);
            RelativeLayout parent_layout = (RelativeLayout) vi.findViewById(R.id.bubble_layout_parent);
            TextView msg = (TextView) vi.findViewById(R.id.message_text);
            TextView time = (TextView) vi.findViewById(R.id.chat_bubble_time);
            TextView date = (TextView) vi.findViewById(R.id.message_date);
            ImageView read_status=(ImageView) vi.findViewById(R.id.user_reply_status);

           // msg.setText(Html.fromHtml("</b> msg_body"));
            String mess,tikam;
            msg.setText(message.msg_body);


          /*  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 10);
            msg.setLayoutParams(lp);
            linearLayout.addView(msg);*/

           /* public void addMessageBox(String message, int type){
                TextView textView = new TextView(Chat.this);
                textView.setText(message);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 10);
                textView.setLayoutParams(lp);

                if(type == 1) {
                    textView.setBackgroundResource(R.drawable.rounded_corner1);
                }
                else{
                    textView.setBackgroundResource(R.drawable.rounded_corner2);
                }

                layout.addView(textView);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
*/
//            msg.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//            msg.setSingleLine(false);


            time.setText(message.Time);
            date.setText(message.Date);

            if(message.sender_type.equals("User")){
                Log.d("User_pls_senns", "" + message.readStatus + "-" + message.sendStatus + "_" + message.msg_body + "-" + message.sender_type);

                linearLayout.setBackgroundResource(R.drawable.bubble1);
                parent_layout.setGravity(Gravity.LEFT);
                read_status.setImageDrawable(context.getResources().getDrawable(R.drawable.my_back_round));

            }
            else {
                linearLayout.setBackgroundResource(R.drawable.bubble2);
                parent_layout.setGravity(Gravity.RIGHT);


                //    Log.d("status1",""+message.sendStatus);
                if (message.sendStatus.equals("0")  && message.readStatus.equals("0")) {
                    Log.d("pls_send", "" + message.readStatus + "-" + message.sendStatus + "_" + message.msg_body + "-" + message.sender_type);

                    read_status.setImageDrawable(context.getResources().getDrawable(R.drawable.message_got_receipt_from_server));

                } else if (message.sendStatus.equals("1")  && message.readStatus.equals("0")) {

                    Log.d("pls_send1", "" + message.readStatus + "-" + message.sendStatus + "_" + message.msg_body + "-" + message.sender_type);
                    read_status.setImageDrawable(context.getResources().getDrawable(R.drawable.message_got_receipt_from_target));
                } else if (message.readStatus.equals("1")) {
                   ;
                    Log.d("pls_read", "" + message.readStatus + "-" + message.sendStatus + "_" + message.msg_body + "-" + message.sender_type);
                    read_status.setImageDrawable(context.getResources().getDrawable(R.drawable.message_got_read_receipt_from_target_onmedia));
                }

            }

            msg.setTextColor(Color.BLACK);
        }catch (Exception e){

        }
        return vi;
    }

    public void add(MessageBeanClass object) {
        chatMessageList.add(object);
    }
    private class ViewHolder1 {
        LinearLayout linearLayout;
        RelativeLayout parent_layout;
        TextView msg;
        TextView time;
        TextView date;
        ImageView read_status;

    }

    private class ViewHolder2 {
        LinearLayout linearLayout;
        RelativeLayout parent_layout;
        TextView msg;
        TextView time;
        TextView date;
       // ImageView read_status;

    }
}
