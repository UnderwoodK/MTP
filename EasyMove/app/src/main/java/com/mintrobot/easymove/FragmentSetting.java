package com.mintrobot.easymove;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mintrobot.easymove.DB.Object_job;

import java.util.ArrayList;
import java.util.List;

public class FragmentSetting extends Fragment {

    View view;
    Context context;

    Button btn_psa, btn_ppa, btn_paa;
    SeekBar sb_weight, sb_sensitivity;
    TextView tv_weight, tv_sensitivity;
    EditText et_ip, et_port1, et_port2;

    public static List<Object_job> jobs;

    static int robotIndex = 999;
    static float weight, sensitivity;

    static String ipAddr = "10.0.0.4";
    static int port1 = 9009, port2 = 9010;

    SharedPreferences cache;
    SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        context = view.getContext();

        cache = context.getSharedPreferences("cache", Activity.MODE_PRIVATE);
        editor = cache.edit();

        btn_psa = view.findViewById(R.id.btn_psa);
        btn_ppa = view.findViewById(R.id.btn_ppa);
        btn_paa = view.findViewById(R.id.btn_paa);

        sb_weight = view.findViewById(R.id.sb_weight);
        sb_sensitivity = view.findViewById(R.id.sb_sensitivity);

        tv_weight = view.findViewById(R.id.tv_weight);
        tv_sensitivity = view.findViewById(R.id.tv_sensitivity);

        et_ip = view.findViewById(R.id.et_ip);
        et_port1 = view.findViewById(R.id.et_port1);
        et_port2 = view.findViewById(R.id.et_port2);

        //초기화
        robotIndex = cache.getInt("robotIndex", 0);
        weight = cache.getFloat("weight", 0.0f);
        sensitivity = cache.getFloat("sensitivity", 0.0f);
        ipAddr = cache.getString("ipAddr", "10.0.0.4");
        port1 = cache.getInt("port1", 9009);
        port2 = cache.getInt("port2", 9010);
        tv_weight.setText(String.format("%.1f", weight) + "kg");
        sb_weight.setProgress((int)(weight * 10));
        tv_sensitivity.setText(String.valueOf(sensitivity));
        sb_sensitivity.setProgress((int)(sensitivity * 10));
        et_ip.setText(ipAddr);
        et_port1.setText(String.valueOf(port1));
        et_port2.setText(String.valueOf(port2));
        ChangeButtonColor();

        jobs = new ArrayList<Object_job>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                jobs = MainActivity.db.dao_point().getAllData();
            }
        }).start();

        btn_psa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotIndex = 4;
                editor.putInt("robotIndex", 4);
                editor.commit();
                ChangeButtonColor();
            }
        });

        btn_ppa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotIndex = 3;
                editor.putInt("robotIndex", 3);
                editor.commit();
                ChangeButtonColor();
            }
        });

        btn_paa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotIndex = 6;
                editor.putInt("robotIndex", 6);
                editor.commit();
                ChangeButtonColor();
            }
        });

        sb_weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weight = (float)progress / 10;
                tv_weight.setText(String.format("%.1f", weight) + "kg");
                tv_weight.setTextColor(Color.argb(255, 33, 150, 243));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putFloat("weight", weight);
                editor.commit();
                tv_weight.setTextColor(Color.argb(255, 143, 143, 143));
            }
        });

        sb_sensitivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sensitivity = (float)progress / 10;
                tv_sensitivity.setText(String.format("%.1f", sensitivity));
                tv_sensitivity.setTextColor(Color.argb(255, 33, 150, 243));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putFloat("sensitivity", sensitivity);
                editor.commit();
                tv_sensitivity.setTextColor(Color.argb(255, 143, 143, 143));
            }
        });

        et_ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != "") {
                    ipAddr = String.valueOf(s).trim();
                } else {
                    Toast.makeText(context, "Check ip address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString("ipAddr", ipAddr);
                editor.commit();
            }
        });

        et_port1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                port1 = Integer.parseInt(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putInt("port1", port1);
                editor.commit();
            }
        });

        et_port2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                port2 = Integer.parseInt(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putInt("port2", port2);
                editor.commit();
            }
        });
        return view;
    }

    public void ChangeButtonColor() {
        switch(robotIndex) {
            case 4:
                btn_psa.setBackgroundResource(R.drawable.mainbuttonsel);
                btn_psa.setTextColor(Color.argb(255, 255, 255, 255));

                btn_ppa.setBackgroundResource(R.drawable.mainbuttonnor);
                btn_ppa.setTextColor(Color.argb(255, 0, 0, 0));

                btn_paa.setBackgroundResource(R.drawable.mainbuttonnor);
                btn_paa.setTextColor(Color.argb(255, 0, 0, 0));
                break;
            case 3:
                btn_psa.setBackgroundResource(R.drawable.mainbuttonnor);
                btn_psa.setTextColor(Color.argb(255, 0, 0, 0));

                btn_ppa.setBackgroundResource(R.drawable.mainbuttonsel);
                btn_ppa.setTextColor(Color.argb(255, 255, 255, 255));

                btn_paa.setBackgroundResource(R.drawable.mainbuttonnor);
                btn_paa.setTextColor(Color.argb(255, 0, 0, 0));
                break;
            case 6:
                btn_psa.setBackgroundResource(R.drawable.mainbuttonnor);
                btn_psa.setTextColor(Color.argb(255, 0, 0, 0));

                btn_ppa.setBackgroundResource(R.drawable.mainbuttonnor);
                btn_ppa.setTextColor(Color.argb(255, 0, 0, 0));

                btn_paa.setBackgroundResource(R.drawable.mainbuttonsel);
                btn_paa.setTextColor(Color.argb(255, 255, 255, 255));
                break;
            default:
                btn_psa.setBackgroundResource(R.drawable.mainbuttonnor);
                btn_psa.setTextColor(Color.argb(255, 0, 0, 0));

                btn_ppa.setBackgroundResource(R.drawable.mainbuttonnor);
                btn_ppa.setTextColor(Color.argb(255, 0, 0, 0));

                btn_paa.setBackgroundResource(R.drawable.mainbuttonnor);
                btn_paa.setTextColor(Color.argb(255, 0, 0, 0));
                break;
        }
    }
}
