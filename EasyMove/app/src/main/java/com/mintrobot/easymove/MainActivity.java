package com.mintrobot.easymove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.pm.ActivityInfo;
import android.net.Network;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.mintrobot.easymove.DB.Database_db;
import com.mintrobot.easymove.DB.Object_job;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btn_setting, btn_jog, btn_manage, btn_exit;
    static Button btn_switch, btn_warning;

    FragmentSetting fragmentSetting;
    FragmentJog fragmentJog;
    FragmentManage fragmentManage;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    public static Database_db db;

    static boolean isPushed = false;

    Dialog_exit dialog_exit;


    DataOutputStream dos;

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Press the exit button to finish the app.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        try {
            //Runtime.getRuntime().exec("adb shell");
//            Runtime.getRuntime().exec("mount -o remount,rw /system");
//            Runtime.getRuntime().exec("chmod 4755  /system/bin/sh");
            System.out.println("ifconfig");
            Process process = Runtime.getRuntime().exec("su" + "\n");
            DataOutputStream dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes("ifconfig eth0 10.0.0.255 netmask 255.255.255.0 up" + "\n");
            dos.flush();
            //process2.waitFor();
//            Runtime.getRuntime().exec("mount -o remount,rw /system");
//            Runtime.getRuntime().exec("chmod 755  /system/bin/sh");
        } catch (IOException e) {
            e.printStackTrace();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db = Room.databaseBuilder(getApplicationContext(), Database_db.class, "MintDB")
                .fallbackToDestructiveMigration()
                .build();

        btn_setting = findViewById(R.id.btn_setting);
        btn_jog = findViewById(R.id.btn_jog);
        btn_manage = findViewById(R.id.btn_manage);
        btn_switch = findViewById(R.id.btn_switch);
        btn_exit = findViewById(R.id.btn_exit);
        btn_warning = findViewById(R.id.btn_warning);

        CreateFragment(0);

        dialog_exit = new Dialog_exit(this, exitListener, cancelListener);

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateFragment(0);
                ChangeStatusBack();
            }
        });
        btn_jog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateFragment(1);
                ChangeStatusBack();
            }
        });
        btn_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateFragment(2);
                ChangeStatusBack();
            }
        });
        btn_switch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_switch.setBackgroundResource(R.drawable.switchsel);
                        FragmentJog.btn_status.setBackgroundResource(R.drawable.usable);
                        isPushed = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_switch.setBackgroundResource(R.drawable.switchnor);
                        FragmentJog.btn_status.setBackgroundResource(R.drawable.unusable);
                        isPushed = false;
                        break;
                }
            return true;
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_exit.show();
            }
        });
    }

    public void CreateFragment(int i) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        switch(i) {
            case 0:
                if (fragmentSetting == null) {
                    fragmentSetting = new FragmentSetting();
                }
                transaction.replace(R.id.fl_container, fragmentSetting).commitAllowingStateLoss();
                btn_setting.setBackgroundResource(R.drawable.settingsel);
                btn_jog.setBackgroundResource(R.drawable.jognor);
                btn_manage.setBackgroundResource(R.drawable.managenor);
                btn_switch.setVisibility(View.INVISIBLE);

                break;
            case 1:
                if (fragmentJog == null) {
                    fragmentJog = new FragmentJog();
                }
                transaction.replace(R.id.fl_container, fragmentJog).commitAllowingStateLoss();
                btn_setting.setBackgroundResource(R.drawable.settingnor);
                btn_jog.setBackgroundResource(R.drawable.jogsel);
                btn_manage.setBackgroundResource(R.drawable.managenor);
                btn_switch.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (fragmentManage == null) {
                    fragmentManage = new FragmentManage();

                }
                transaction.replace(R.id.fl_container, fragmentManage).commitAllowingStateLoss();
                btn_setting.setBackgroundResource(R.drawable.settingnor);
                btn_jog.setBackgroundResource(R.drawable.jognor);
                btn_manage.setBackgroundResource(R.drawable.managesel);
                btn_switch.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void ChangeStatusBack() {
        FragmentJog.isJogOpened = true;
    }

    private View.OnClickListener exitListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentJog.isInterrupted = true;
            FragmentJog.isJogOpened = false;
            if(FragmentJog.thread_command != null) {
                if(FragmentJog.thread_command.socket != null) {
                    try {
                        FragmentJog.thread_command.socket.close();
                        FragmentJog.thread_command.interrupt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
            if(FragmentJog.thread_status != null) {
                if(FragmentJog.thread_status.threadSocket != null) {
                    try {
                        FragmentJog.thread_status.threadSocket.close();
                        FragmentJog.thread_status.interrupt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
            finish();
        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_exit.dismiss();
        }
    };
}

