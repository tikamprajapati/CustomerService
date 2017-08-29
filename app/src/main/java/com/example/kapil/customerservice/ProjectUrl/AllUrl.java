package com.example.kapil.customerservice.ProjectUrl;

/**
 * Created by dell on 23-05-2017.
 */

public class AllUrl {

    public static final String BASE_URL3 = "http://mutham.co.il/api/?action=CreateMessageToChat";
    public static String BASE_URL="http://mutham.co.il/api/?action=CreateChat";
    public static String BASE_URL1= "http://mutham.co.il/api/?action=GetChatsWithMessagesByIMEI&imei=";

  //  public static String BASE_URL6= "http://mutham.co.il/api/?action=UpdateMessageStatus&idChat=5&idStatus=1";

    public static String BASE_URL8="http://mutham.co.il/api/?action=updateMessageReaded&idMessage=8&idUserOfReader=5";

    public static String BASE_URL7 =  "http://mutham.co.il/market/fcm/muthamcontrol/index.php?type=getname";

    public static String BASE_URL_TRY ="http://mutham.co.il/market/fcm/muthamcontrol/index.php?type=getname&imei=353118081014394&device_model=Samsung%20SAMSUNG-SM-J120A";
    public static String BASE_URL_TOKEN ="http://mutham.co.il/market/fcm/muthamcontrol/register.php?";

    public static String  MESSAGER_RECIVED_BASE_URL ="Http://mutham.co.il/api/?action=updateMessageRecieved";


    public static String MESSAGER_READED_BASE_URL="http://mutham.co.il/api/?action=updateMessageReaded";

    public static String TOKEN_URL="http://mutham.co.il/market/fcm/muthamcontrol/register.php?";





/*
    Create New MEssage
    http://mutham.co.il/api/?action=CreateChat&imei=1&subject=New1&messageBody=test2
    Message List
    http://mutham.co.il/api/?action=GetChatsWithMessagesByIMEI&imei=1
    http://mutham.co.il/api/?action=updateMessageRecieved&idMessage=5
    http://mutham.co.il/api/?action=updateMessageReaded&idMessage=7
    Do Reply
    http://mutham.co.il/api/?action=CreateMessageToChat&idChat=5&messageBody=testing&imei=1
    http://mutham.co.il/api/?action=UpdateMessageStatus&idChat=5&idStatus=1
    http://mutham.co.il/api/?action=updateMessageReaded&idMessage=8&idUserOfReader=5
*/


}
