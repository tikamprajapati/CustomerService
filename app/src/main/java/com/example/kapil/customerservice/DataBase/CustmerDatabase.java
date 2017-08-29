package com.example.kapil.customerservice.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kapil.customerservice.Model.MessageBeanClass;

import java.util.ArrayList;

/**
 * Created by dell on 05-07-2017.
 */

public class CustmerDatabase {
    Cart mcart;

    public CustmerDatabase(Context context) {
        mcart = new Cart(context);
    }


    public long insert_chat_data(MessageBeanClass messageBeanClass) {
        SQLiteDatabase db = mcart.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Cart.MSG_ID, messageBeanClass.msg_id);
        contentValues.put(Cart.DATE,  messageBeanClass.Date);
        contentValues.put(Cart.TIME,  messageBeanClass.Time);
        contentValues.put(Cart.MSG_BODY,  messageBeanClass.msg_body);
        contentValues.put(Cart.CHAT_ID,  messageBeanClass.idchat);
        contentValues.put(Cart.READ_STATUS,  messageBeanClass.read_Status);
        contentValues.put(Cart.IMEI,  messageBeanClass.imei_Sender);
        long id = db.insert(Cart.MSG_TABLE, null, contentValues);
        db.close();
        return id;
    }
    public ArrayList get_chat_data(String chat_id) {
        SQLiteDatabase sqLiteDatabase = mcart.getReadableDatabase();
        Cursor cr = sqLiteDatabase.rawQuery("SELECT * FROM " + Cart.MSG_TABLE + " where chat_id=" + chat_id, null);

        ArrayList<MessageBeanClass> chat_list = new ArrayList<MessageBeanClass>();
        if (cr != null && cr.moveToFirst()) {
            do {
              //  String u_id = cr.getString(cr.getColumnIndex(Cart.U_ID));
                String msg_body = cr.getString(cr.getColumnIndex(Cart.MSG_BODY));
                String msg_id = cr.getString(cr.getColumnIndex(Cart.MSG_ID));
                chat_id = cr.getString(cr.getColumnIndex(Cart.CHAT_ID));
                String date = cr.getString(cr.getColumnIndex(Cart.DATE));
                String time = cr.getString(cr.getColumnIndex(Cart.TIME));
                String imei = cr.getString(cr.getColumnIndex(Cart.IMEI));
                boolean is_mine = (cr.getString(cr.getColumnIndex(Cart.READ_STATUS))).equals("1");

                MessageBeanClass chatMessage = new MessageBeanClass(chat_id,msg_body, msg_id, date, time,is_mine,imei);
                chat_list.add(chatMessage);
            } while (cr.moveToNext());
            cr.close();
        }
        return chat_list;
    }

    public void update_MSG_Table(MessageBeanClass messageBeanClass) {
        SQLiteDatabase sqldb = mcart.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Cart.MSG_ID, messageBeanClass.msg_id);
        contentValues.put(Cart.DATE,  messageBeanClass.Date);
        contentValues.put(Cart.TIME,  messageBeanClass.Time);
        contentValues.put(Cart.MSG_BODY,  messageBeanClass.msg_body);
        contentValues.put(Cart.CHAT_ID,  messageBeanClass.idchat);
        contentValues.put(Cart.READ_STATUS,  messageBeanClass.read_Status);
        contentValues.put(Cart.IMEI,  messageBeanClass.imei_Sender);

        sqldb.update(Cart.MSG_TABLE, contentValues, Cart.MSG_ID + " =?",
                new String[]{messageBeanClass.msg_id});
    }

    public boolean get_check_msg_id(String msg_id) {

        // Select All Query
        SQLiteDatabase sqldb = mcart.getWritableDatabase();
        String query = "select * from " + Cart.MSG_TABLE + " where msg_id=" + msg_id;
        Cursor c = sqldb.rawQuery(query, null);
        if (c.getCount() != 0) {
            c.requery();
            // sqldb.close();
            return true;
        } else {
            c.requery();
            sqldb.close();
            return false;
        }
    }


    public static class Cart extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "CART";
        private static final int DATABASE_VERSION = 1;

        private static final String MSG_TABLE = "send_msg_table";
        private static final String MSG_ID = "msg_id";
        private static final String U_ID = "u_id";
        private static final String DATE = "date";
        private static final String TIME = "time";
        private static final String MSG_BODY = "msg_body";
        private static final String CHAT_ID = "chat_id";
        private static final String READ_STATUS = "read_status";
        private static final String IMEI = "imeiSender";

        private Context mContext;

        public Cart(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.mContext = context;
        }
        private static final String CREATE_CHAT_TABLE = "CREATE TABLE " + MSG_TABLE + "( " + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MSG_BODY + " VARCHAR, "  + CHAT_ID + " VARCHAR, "  + DATE + " VARCHAR, "
                + TIME + " VARCHAR, " + READ_STATUS + " VARCHAR, " + IMEI + " VARCHAR, " + MSG_ID + " VARCHAR);";

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CHAT_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
