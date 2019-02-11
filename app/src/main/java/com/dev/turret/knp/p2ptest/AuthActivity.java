package com.dev.turret.knp.p2ptest;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity implements LanAuth.LanAuthCallBack {

    FloatingActionButton fabAuth;
    TextInputLayout tilUUID;
    AppCompatEditText etUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);
        initViews();

        //TODO temp
        final String fuuid = "HL00026286";

        fabAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String uuid = etUUID.getText().toString();
                new LanAuth(AuthActivity.this).getRemoteDeviceAddress(fuuid,AuthActivity.this);
            }
        });
    }

    private void initViews(){
        fabAuth = findViewById(R.id.fabAuth);
        tilUUID = findViewById(R.id.tilUUID);
        etUUID = findViewById(R.id.etUUID);
    }

    @Override
    public void onLanConnected(String deviceAddress) {
        Toast.makeText(this, deviceAddress, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLanException(Exception e) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLanTimeOut() {
        Toast.makeText(this, "Time out!", Toast.LENGTH_SHORT).show();
    }

//    public boolean LanConnect(String strUUID)
//    {
//
//        int ip=0;
//        String strip=null;
//        Message mes1=new Message();
//        mes1.what=7;
//        mes1.obj="abc";
//        Log.i("T2u", "LanConnect: lan search");
//        //handler.sendMessage(mes1);
//
////				addr = InetAddress.getLocalHost();
////				ip=addr.getHostAddress().toString(); //��ȡ����ip
////				ip=ip.substring(0,ip.lastIndexOf(".")+1)+"255";
//
//        ip=mWifiAdmin.getIpAddress();
////				 mWifiAdmin.getIpAddress();
//        if(ip != 0){strip = ((ip & 0xff) + "." + (ip >> 8 & 0xff) + "." + (ip >> 16 & 0xff) + "." + "255");}
//        //System.out.println("����ip="+ip);
//        Log.i("T2u", "LanConnect: ip="+ip);
//
//
//
//        try {
//            address = InetAddress.getByName(strip);
//        } catch (UnknownHostException e2) {
//            // TODO Auto-generated catch block
//            e2.printStackTrace();
//        }
//        byte[] out= "HLK".getBytes();
//        datapacket = new DatagramPacket(out,out.length,address,988);
//        pack=new DatagramPacket(receivebuf,receivebuf.length);
//
//
//        if(ms!=null)
//        {
//            ms.close();
//            ms=null;
//        }
//        if(ms==null)
//        {
//            try {
//                ms=new MulticastSocket();
//                ms.send(datapacket);
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//
//            intWaitTime=0;
//            while(true)
//            {
//                try {
//                    ms.setSoTimeout(1000);
//                    ms.receive(pack);
//                    udpresult = new String(pack.getData()).trim();
//                    if(udpresult.contains(strUUID))
//                    {
//                        lanControlFlag=true;
//                        strip=pack.getAddress().toString();
//                        System.out.println("�豸��ip��ַΪ��"+strip);
//                        System.out.println("�ھ��������ҵ��豸");
//
//                        if(strip.indexOf("/")!=-1){	strip=strip.substring(strip.indexOf("/")+1);	}
//                        Message mes11=new Message();
//                        mes11.what=8;
//                        mes11.obj=strip;
//                        //handler.sendMessage(mes11);
//                        Log.i("T2u", "LanConnect: Connected");
//                        return true;
//                    }
//
//
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                intWaitTime += 1;
//                System.out.println("���ڽ��о�����������");
//                if (intWaitTime > 3)
//                {
//                    System.out.println("��������δ�������豸");
//                    break;
//                }
//            }
//        }
//        return false;
//    }



}
