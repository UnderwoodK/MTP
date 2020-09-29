package com.mintrobot.easymove;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Thread_Status extends Thread {

    String ipAddr;
    int port2;

    Socket threadSocket;
    OutputStream os;
    DataOutputStream dos;
    InputStream is;
    DataInputStream dis;

    boolean testIndex;

    ByteBuffer byteBufferOne;
    ByteBuffer byteBuffer12;

    StatusHandler statusHandler;

    public Thread_Status() {
        this.ipAddr = FragmentSetting.ipAddr;
        this.port2 = FragmentSetting.port2;
        statusHandler = new StatusHandler();
    }

    public void run() {
        try {
            threadSocket = new Socket(ipAddr, port2);
            threadSocket.setSoTimeout(10000);

            if(threadSocket.isConnected()) {
                os = threadSocket.getOutputStream();
                dos = new DataOutputStream(os);
                is = threadSocket.getInputStream();
                dis = new DataInputStream(is);
                byteBufferOne = ByteBuffer.allocate(1);
                byteBufferOne.order(ByteOrder.LITTLE_ENDIAN);
                byteBuffer12 = ByteBuffer.allocate(96);
                byteBuffer12.order(ByteOrder.LITTLE_ENDIAN);
                while(true) {
                    if(FragmentJog.isJogOpened) {
                        try {
                            byteBufferOne.put((byte)0x09);
                            dos.write(byteBufferOne.array());
                            byteBufferOne.clear();
                            byteBufferOne.rewind();
                            dos.flush();
                            dis.read(byteBuffer12.array());
                            FragmentJog.j1 = byteBuffer12.getDouble(); // 0 ~ 7
                            FragmentJog.j2= byteBuffer12.getDouble(); // 8 ~ 15
                            FragmentJog.j3 = byteBuffer12.getDouble(); // 16 ~ 23
                            FragmentJog.j4 = byteBuffer12.getDouble(); // 24 ~ 31
                            FragmentJog.j5 = byteBuffer12.getDouble(); // 32 ~ 39
                            FragmentJog.j6 = byteBuffer12.getDouble(); // 40 ~ 47
                            FragmentJog.x = byteBuffer12.getDouble(); // 48 ~ 55
                            FragmentJog.y = byteBuffer12.getDouble(); // 56 ~ 63
                            FragmentJog.z = byteBuffer12.getDouble(); // 64 ~ 71
                            FragmentJog.Rx = byteBuffer12.getDouble(); // 72 ~ 79
                            FragmentJog.Ry = byteBuffer12.getDouble(); // 80 ~ 87
                            FragmentJog.Rz = byteBuffer12.getDouble(); // 88 ~ 95, (96)
                            byteBuffer12.clear();
                            byteBuffer12.rewind();
                            Message msg = statusHandler.obtainMessage();
                            statusHandler.sendMessage(msg);
                            SystemClock.sleep(60);
                        } catch (Exception e) {
                            e.printStackTrace();
                            byteBufferOne.clear();
                            byteBufferOne.rewind();
                        }
                    } else{

                    }
                }
            } else {
                FragmentJog.j1 = 0.00;
                FragmentJog.j2 = 0.00;
                FragmentJog.j3 = 0.00;
                FragmentJog.j4 = 0.00;
                FragmentJog.j5 = 0.00;
                FragmentJog.j6 = 0.00;
                FragmentJog.x = 0.00;
                FragmentJog.y = 0.00;
                FragmentJog.z = 0.00;
                FragmentJog.Rx = 0.00;
                FragmentJog.Ry = 0.00;
                FragmentJog.Rz = 0.00;
                Message msg = statusHandler.obtainMessage();
                statusHandler.sendMessage(msg);
            }
        } catch (Exception e) {
            threadSocket = null;
            e.printStackTrace();
        }
    }

    class StatusHandler extends Handler {
        public void handleMessage(Message msg) {
            try {
                FragmentJog.tv_j1.setText(String.format("%.2f", FragmentJog.j1 * (180 / 3.141592)));
                FragmentJog.tv_j2.setText(String.format("%.2f", FragmentJog.j2 * (180 / 3.141592)));
                FragmentJog.tv_j3.setText(String.format("%.2f", FragmentJog.j3 * (180 / 3.141592)));
                FragmentJog.tv_j4.setText(String.format("%.2f", FragmentJog.j4 * (180 / 3.141592)));
                FragmentJog.tv_j5.setText(String.format("%.2f", FragmentJog.j5 * (180 / 3.141592)));
                FragmentJog.tv_j6.setText(String.format("%.2f", FragmentJog.j6 * (180 / 3.141592)));
                FragmentJog.tv_x.setText(String.format("%.2f", FragmentJog.x * 1000));
                FragmentJog.tv_y.setText(String.format("%.2f", FragmentJog.y * 1000));
                FragmentJog.tv_z.setText(String.format("%.2f", FragmentJog.z * 1000));
                FragmentJog.tv_Rx.setText(String.format("%.2f", FragmentJog.Rx * (180 / 3.141592)));
                FragmentJog.tv_Ry.setText(String.format("%.2f", FragmentJog.Ry * (180 / 3.141592)));
                FragmentJog.tv_Rz.setText(String.format("%.2f", FragmentJog.Rz * (180 / 3.141592)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}