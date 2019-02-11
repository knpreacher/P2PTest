package com.dev.turret.knp.p2ptest;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class LanAuth {

    private static final String OUT_MESSAGE = "HLK";
    private static final int PORT = 988;
    private static final int WAIT_LIMIT = 4;

    interface LanAuthCallBack{
        void onLanConnected(String deviceAddress);
        void onLanException(Exception e);
        //void onLanAuthFailed();
        void onLanTimeOut();
    }

    private Context context;

    private MulticastSocket ms;
    private WifiAdmin mWifiAdmin;

    public LanAuth(Context context) {
        this.context = context;
        mWifiAdmin = new WifiAdmin(context);
    }

    //Получение мультикаст адреса локальной сети
    private String getMulticastAddress(){
        int ip=mWifiAdmin.getIpAddress();
        if(ip != 0) return  ((ip & 0xff) + "." + (ip >> 8 & 0xff) + "." + (ip >> 16 & 0xff) + "." + "255");
        return "";
    }

    //Поиск адреса устройства в локальной сети
    private void getDeviceAddress(String UUID, final LanAuthCallBack callBack){
        try {
            byte[] out= OUT_MESSAGE.getBytes();
            byte[] receiveBuffer = new byte[512];

            DatagramPacket datapacket = new DatagramPacket(out,out.length,InetAddress.getByName(getMulticastAddress()),PORT);
            DatagramPacket pack = new DatagramPacket(receiveBuffer,receiveBuffer.length);

            if(ms!=null) ms.close();

            ms=new MulticastSocket();
            ms.send(datapacket);

            int waitTime = 0;
            String strip;

            //Проверка получения ответа
            while (waitTime < WAIT_LIMIT){
                ms.setSoTimeout(1000);
                ms.receive(pack);
                 if(new String(pack.getData()).contains(UUID)){
                      strip = pack.getAddress().toString();
                      if(strip.contains("/")) {
                          strip=strip.substring(strip.indexOf("/")+1);
                          final String finalStrip = strip;
                          ((Activity)context).runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  callBack.onLanConnected(finalStrip);
                              }
                          });
                          return;
                      }
                 }
                waitTime++;
            }

            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onLanTimeOut();
                }
            });

        } catch (final UnknownHostException e) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onLanException(e);
                }
            });
        } catch (final IOException e) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onLanException(e);
                }
            });
        }

    }

    //Выполнение поиска адреса в отдельном потоке
    public void getRemoteDeviceAddress(final String UUID, final LanAuthCallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDeviceAddress(UUID,callBack);
            }
        }).start();
    }
}
