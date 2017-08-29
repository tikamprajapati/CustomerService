package com.example.kapil.customerservice.Model;

/**
 * Created by kapil on 29-Jun-17.
 */

public class MessageBeanClass {


    //  public   String emie ;
    public String msg_body;
    public String idchat;
    public String Date;
    public String Time;
    public String msg_id;
    public String readStatus;
    public String sendStatus;
    public String sender_type;

    public MessageBeanClass(String msg_body, String date, String time, String readStatus, String sendStatus, String sender_type) {
        this.msg_body = msg_body;
        this.idchat = idchat;
        Date = date;
        Time = time;
        this.readStatus = readStatus;
        this.sendStatus = sendStatus;
        this.sender_type = sender_type;
    }

    public String imei_Sender;
    public boolean read_Status;


    public MessageBeanClass(String msg_body, String idchat,String date,String time) {
        // this.emie = emie;
        this.msg_body = msg_body;
        this.idchat = idchat;
        this.Date = date;
        this.Time = time;
    }

    public MessageBeanClass(String msg_id,String msg_body,String idchat, String date, String time,boolean read_Status,String imei_sender ) {
        this.msg_body = msg_body;
        this.idchat = idchat;
        this.Date = date;
        this.Time = time;
        this.msg_id = msg_id;
        this.read_Status = read_Status;
        this.imei_Sender = imei_sender;


    }

    public String getMsg_body() {
        return msg_body;
    }

    public void setMsg_body(String msg_body) {
        this.msg_body = msg_body;
    }

    public String getIdchat() {
        return idchat;
    }

    public void setIdchat(String idchat) {
        this.idchat = idchat;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public MessageBeanClass(String msg_body) {
        this.msg_body = msg_body;
    }




}
