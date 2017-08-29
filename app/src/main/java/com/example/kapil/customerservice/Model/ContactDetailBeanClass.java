package com.example.kapil.customerservice.Model;

/**
 * Created by kapil on 29-Jun-17.
 */

public  class ContactDetailBeanClass {

    public String u_id;
    public String subject;
    public String idUserOfReader;
    public String sendStatus;
    public String message_id;
    public String messagebody;
    public String idchat;
    public String imei;


    public ContactDetailBeanClass(String subject, String u_id , String idchat, String imei) {
        this.subject = subject;
        this.u_id = u_id;
        this.idchat = idchat;
        this.imei = imei;

    }

    public ContactDetailBeanClass(String messagebody, String idUserOfReader, String message_id) {
        this.messagebody = messagebody;
        this.idUserOfReader = idUserOfReader;
        this.message_id = message_id;
    }


    public String getIdUserOfReader() {
        return idUserOfReader;
    }

    public void setIdUserOfReader(String idUserOfReader) {
        this.idUserOfReader = idUserOfReader;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessagebody() {

        return messagebody;
    }

    public void setMessagebody(String messagebody) {
        this.messagebody = messagebody;
    }


    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIdchat() {
        return idchat;
    }

    public void setIdchat(String idchat) {
        this.idchat = idchat;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }




}

