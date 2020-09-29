package com.mintrobot.easymove;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentManage extends Fragment {

    View view;
    Context context;

    Handler uiHandler;

    int limitIndex = 0;

    Button btn_deleteAllJobs, btn_getReturn, btn_unlimited, btn_zero, btn_readyPosition;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manage, container, false);
        context = view.getContext();

        uiHandler = new Handler();

        btn_deleteAllJobs = view.findViewById(R.id.btn_deleteAllJobs);
        btn_getReturn = view.findViewById(R.id.btn_getReturn);
        btn_unlimited = view.findViewById(R.id.btn_unlimited);
        btn_zero = view.findViewById(R.id.btn_zero);
        btn_readyPosition = view.findViewById(R.id.btn_readyPosition);

        btn_deleteAllJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.db.dao_point().deleteAllDatas();
                        if(FragmentJog.adapter_job == null) {
                        } else {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    FragmentJog.adapter_job.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }).start();
            Toast.makeText(context, "All Jobs have been deleted", Toast.LENGTH_SHORT).show();
            }
        });

        btn_getReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println( FragmentJog.thread_command.IsFinished());
                    }
                }).start();
            }
        });

        btn_unlimited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(limitIndex == 0) {
                    limitIndex = 1;
                    FragmentJog.jointVel = 180;
                    FragmentJog.lineVel = 1800;
                    FragmentJog.maxJointAcc = 900;
                    FragmentJog.maxLineAcc = 5000;
                    FragmentJog.sb_cycleTime.setMax(100);
                    btn_unlimited.setBackgroundColor(Color.argb(255, 255, 0, 0));
                } else {
                    limitIndex = 0;
                    FragmentJog.jointVel = 50;
                    FragmentJog.lineVel = 500;
                    FragmentJog.maxJointAcc = 360;
                    FragmentJog.maxLineAcc = 1200;
                    FragmentJog.sb_cycleTime.setMax(10);
                    btn_unlimited.setBackgroundColor(Color.argb(255, 13, 168, 99));
                }
            }
        });

        btn_zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentJog.thread_command.InputMoveData((byte)0x01, 0, 0, 0, 0, 0, 0, 30, 60, 60);
                    }
                }).start();
            }
        });

        btn_readyPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentJog.thread_command.InputMoveData((byte)0x01, 0, 0, 90  * 3.141592 / 180, 0, 90  * 3.141592 / 180, 0, 20, 50, 50);
                    }
                }).start();
            }
        });

        return view;
    }
}
