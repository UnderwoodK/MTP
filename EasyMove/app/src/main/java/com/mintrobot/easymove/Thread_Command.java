package com.mintrobot.easymove;

import android.os.Message;
import android.os.SystemClock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Thread_Command extends Thread {

    String ipAddr;
    int port1;
    byte returnValue;

    byte moveCommand;
    double data1, data2, data3, data4, data5, data6, data7, data8, data9;

    //Socket
    Socket socket;
    OutputStream os;
    DataOutputStream dos;
    InputStream is;
    DataInputStream dis;
    ByteBuffer commandBuffer, returnBuffer;

    public Thread_Command() {
        ipAddr = FragmentSetting.ipAddr;
        port1 = FragmentSetting.port1;
    }

    public void run() {
        try {
            socket = new Socket(ipAddr, port1);
            socket.setSoTimeout(10000);
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            commandBuffer = ByteBuffer.allocate(1024);
            returnBuffer = ByteBuffer.allocate(1);
            commandBuffer.order(ByteOrder.LITTLE_ENDIAN);
            returnBuffer.order(ByteOrder.LITTLE_ENDIAN);
            SetSensitivity();
            SendWeight();
        } catch (Exception e) {
            socket = null;
            dos = null;
            dis = null;
            e.printStackTrace();
        }
    }

    public byte SendWeight() {
        try {
            commandBuffer.put((byte)0x0D);
            commandBuffer.putDouble(0);
            commandBuffer.putDouble(0);
            commandBuffer.putDouble(0);
            commandBuffer.putDouble(0);
            commandBuffer.putDouble(0);
            commandBuffer.putDouble(0);
            commandBuffer.putDouble((double)FragmentSetting.weight);
            dos.write(commandBuffer.array());
            dos.flush();
            commandBuffer.clear();
            commandBuffer.rewind();
            returnValue = GetReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public byte SetSensitivity() {
        try {
            commandBuffer.put((byte)0x0F);
            commandBuffer.putDouble((double)FragmentSetting.sensitivity);
            dos.write(commandBuffer.array());
            dos.flush();
            commandBuffer.clear();
            commandBuffer.rewind();
            returnValue = GetReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public void SendIo() {

    }

    public byte SendStop() {
        try {
            commandBuffer.put((byte)0x00);
            dos.write(commandBuffer.array());
            dos.flush();
            commandBuffer.clear();
            commandBuffer.rewind();
            returnValue = GetReturn();
            System.out.println("stop return : " + returnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public void SendWayPoint() {

    }

    public byte SendJog() {
        try {
            commandBuffer.put((byte) 0x0B);
            commandBuffer.put(FragmentJog.sendJogIndex);
            dos.write(commandBuffer.array());
            dos.flush();
            commandBuffer.clear();
            commandBuffer.rewind();
            returnValue = GetReturn();
            System.out.println("SendJog : " + returnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public byte SendMoveLJ() {
        try{
            commandBuffer.put(moveCommand);  // 1, 0
            commandBuffer.put((byte) 0); // 1, 1
            commandBuffer.putDouble(data1);  // 8, 2 ~ 9
            commandBuffer.putDouble(data2);  // 8, 10 ~ 17
            commandBuffer.putDouble(data3);  // 8, 18 ~ 25
            commandBuffer.putDouble(data4);  // 8, 26 ~ 33
            commandBuffer.putDouble(data5);  // 8, 34 ~ 41
            commandBuffer.putDouble(data6);  // 8, 42 ~ 49
            commandBuffer.putDouble(data7);  // 8, 50 ~ 57
            commandBuffer.putDouble(data8);  // 8, 58 ~ 65
            commandBuffer.putDouble(data9);  // 8, 58 ~ 65
            dos.write(commandBuffer.array());
            dos.flush();
            commandBuffer.clear();
            commandBuffer.rewind();
            returnValue = GetReturn();
            System.out.println("SendMoveLJ : " + returnValue);
            if(returnValue == 0x00) {
                System.out.println("moving");
            } else if(returnValue == 0x01) {
                System.out.println("Ready");
            } else {
                System.out.println("Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    void InputJogData(byte command) {
        //if(CheckConnection() == 0) {
        moveCommand = command;
        switch(command) {
            case (byte) 0x02 : //0x02 : moveJointRel, command + int + double(6) + double(2) - 69
                data1 = FragmentJog.jog_j1 * 3.141592 / 180;
                data2 = FragmentJog.jog_j2 * 3.141592 / 180;
                data3 = FragmentJog.jog_j3 * 3.141592 / 180;
                data4 = FragmentJog.jog_j4 * 3.141592 / 180;
                data5 = FragmentJog.jog_j5 * 3.141592 / 180;
                data6 = FragmentJog.jog_j6 * 3.141592 / 180;
                data7 = FragmentJog.jog_jVel * 3.141592 / 180;
                data8 = FragmentJog.jog_jAcc * 3.141592 / 180;
                data9 = FragmentJog.jog_jAcc * 3.141592 / 180;
                FragmentJog.jog_j1 = 0; FragmentJog.jog_j2 = 0; FragmentJog.jog_j3 = 0; FragmentJog.jog_j4 = 0; FragmentJog.jog_j5 = 0; FragmentJog.jog_j6 = 0;
                break;
            case (byte) 0x04 : //0x04 : moveEndPosRel, command + int + double(6) + double(2) - 69
                if(FragmentJog.isChanged) {
                    data1 = FragmentJog.jog_x / 1000;
                    data2 = FragmentJog.jog_y / 1000;
                    data3 = FragmentJog.jog_z / 1000;
                    data4 = FragmentJog.jog_Rx * 3.141592 / 180;
                    data5 = FragmentJog.jog_Ry * 3.141592 / 180;
                    data6 = FragmentJog.jog_Rz * 3.141592 / 180;
                    data7 = FragmentJog.jog_jVel / 9 * 3.141592 / 180;
                    data8 = FragmentJog.jog_jAcc / 9 * 3.141592 / 180;
                    data9 = FragmentJog.jog_jAcc / 9 * 3.141592 / 180;
                } else {
                    data1 = FragmentJog.jog_x / 1000;
                    data2 = FragmentJog.jog_y / 1000;
                    data3 = FragmentJog.jog_z / 1000;
                    data4 = FragmentJog.jog_Rx * 3.141592 / 180;
                    data5 = FragmentJog.jog_Ry * 3.141592 / 180;
                    data6 = FragmentJog.jog_Rz * 3.141592 / 180;
                    data7 = FragmentJog.jog_lVel / 1000;
                    data8 = FragmentJog.jog_lAcc / 1000;
                    data9 = FragmentJog.jog_lAcc / 1000;
                }
                FragmentJog.jog_x = 0; FragmentJog.jog_y = 0; FragmentJog.jog_z = 0; FragmentJog.jog_Rx = 0; FragmentJog.jog_Ry = 0; FragmentJog.jog_Rz = 0;
                break;
            case (byte) 0x05 :
                data1 = FragmentJog.jog_x / 1000;
                data2 = FragmentJog.jog_y / 1000;
                data3 = FragmentJog.jog_z / 1000;
                data4 = FragmentJog.jog_Rx * 3.141592 / 180;
                data5 = FragmentJog.jog_Ry * 3.141592 / 180;
                data6 = FragmentJog.jog_Rz * 3.141592 / 180;
                data7 = FragmentJog.jog_lVel / 1000;
                data8 = FragmentJog.jog_lAcc / 1000;
                data9 = FragmentJog.jog_lAcc / 1000;
                FragmentJog.jog_x = 0.00; FragmentJog.jog_y = 0; FragmentJog.jog_z = 0; FragmentJog.jog_Rx = 0; FragmentJog.jog_Ry = 0; FragmentJog.jog_Rz = 0;
                break;
            case (byte) 0xFE : //트래킹 스타트

                break;
            case (byte) 0xFF : // 트래킹 엔드
                break;
        }
        SendMoveLJ();
    }

    public void InputMoveData(byte command, double a, double b, double c, double d, double e, double f, double g, double h, double i) {
        //if(CheckConnection() == 0) {
        moveCommand = command;
        switch(command) {
            case (byte) 0x01 : //0x02 : moveJointRel, command + int + double(6) + double(2) - 69
                data1 = a;
                data2 = b;
                data3 = c;
                data4 = d;
                data5 = e;
                data6 = f;
                data7 = g * 3.141592 / 180;
                data8 = h * 3.141592 / 180;
                data9 = i * 3.141592 / 180;
                break;
            case (byte) 0x03 : //0x04 : moveEndPosRel, command + int + double(6) + double(2) - 69
                data1 = a;
                data2 = b;
                data3 = c;
                data4 = d;
                data5 = e;
                data6 = f;
                data7 = g / 1000;
                data8 = h / 1000;
                data9 = i / 1000;
                break;
            case (byte) 0xFE : //트래킹 스타트

                break;
            case (byte) 0xFF : // 트래킹 엔드
                break;
        }
        SendMoveLJ();
    }


    public byte IsFinished() {
        try{
            commandBuffer.put((byte)0x0A);  // 1, 0
            dos.write(commandBuffer.array());
            dos.flush();
            commandBuffer.clear();
            commandBuffer.rewind();
            returnValue = GetReturn();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public byte GetReturn() {
        byte tempReturn = (byte) 0xFF;
        try{
            returnBuffer.clear();
            returnBuffer.rewind();
            dis.read(returnBuffer.array());
            tempReturn = returnBuffer.get(0);
            returnBuffer.clear();
            returnBuffer.rewind();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return tempReturn;
    }
}
