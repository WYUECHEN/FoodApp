package com.example.project183.Helper;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;

import com.example.project183.Domain.Foods;

import java.util.ArrayList;


public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB=new TinyDB(context);
    }

    public void insertFood(Foods item) {
        ArrayList<Foods> listpop = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if(existAlready){
            listpop.get(n).setNumberInCart(item.getNumberInCart());
        }else{
            listpop.add(item);
            String notificationMessage = item.getTitle() + " has been added to the cart (" + item.getNumberInCart() + " pcs)";
            addNotification(notificationMessage);
        }
        tinyDB.putListObject("CartList",listpop);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();

        ArrayList<String> notifications = tinyDB.getListString("NotificationList");
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        //String notificationMessage = item.getTitle() + " has been added to the cart";
        //notifications.add(notificationMessage);

        // 存储通知到 TinyDB
        tinyDB.putListString("NotificationList", notifications);

        // Debug：打印通知列表
        Log.d("ManagmentCart", "NotificationList: " + notifications.toString());



    }

    private void addNotification(String message) {
        ArrayList<String> notifications = tinyDB.getListString("NotificationList");
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(message);
        tinyDB.putListString("NotificationList", notifications);

        // 调试输出
        Log.d("ManagmentCart", "NotificationList: " + notifications.toString());

        // 设置未读状态为 true
        tinyDB.putBoolean("hasUnreadNotifications", true);

        // 发送广播通知 MainActivity 更新状态
        Intent intent = new Intent("UPDATE_NOTIFICATION_SIGN");
        context.sendBroadcast(intent);

    }





    public ArrayList<Foods> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public Double getTotalFee(){
        ArrayList<Foods> listItem=getListCart();
        double fee=0;
        for (int i = 0; i < listItem.size(); i++) {
            fee=fee+(listItem.get(i).getPrice()*listItem.get(i).getNumberInCart());
        }
        return fee;
    }
    public void minusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        if(listItem.get(position).getNumberInCart()==1){
            listItem.remove(position);
        }else{
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()-1);
        }
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    public  void plusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()+1);
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    public  void removeItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){

        // 获取要移除的商品名称
        String removedItemTitle = listItem.get(position).getTitle();
        int removedItemQuantity = listItem.get(position).getNumberInCart();

        listItem.remove(position);
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();


        // 添加通知
        ArrayList<String> notifications = tinyDB.getListString("NotificationList");
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        String notificationMessage = removedItemTitle + " (" + removedItemQuantity + " pcs) has been removed from the cart";
        notifications.add(notificationMessage);
        tinyDB.putListString("NotificationList", notifications);

        // 调试输出
        Log.d("ManagmentCart", "Removed Item Notification: " + notificationMessage);



    }
}
