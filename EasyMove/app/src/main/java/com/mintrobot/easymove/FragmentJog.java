package com.mintrobot.easymove;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.mintrobot.easymove.Adapter.Adapter_job;
import com.mintrobot.easymove.DB.Object_job;

import java.util.ArrayList;

import static com.mintrobot.easymove.MainActivity.db;
import static com.mintrobot.easymove.MainActivity.isPushed;

public class FragmentJog extends Fragment {

    View view;
    static Context context;

    // 전체공통 ==================================================
    View currentView = null;

    public static Adapter_job adapter_job;

    public static EditText et_search;
    ListView listView;
    Button btn_jobDown, btn_jobUp, btn_addIo, btn_deleteJob; //OK

    TextView tv_textX, tv_textY, tv_textZ, tv_textRx, tv_textRy, tv_textRz,
            tv_textJ1, tv_textJ2, tv_textJ3, tv_textJ4, tv_textJ5, tv_textJ6;

    static TextView tv_j1, tv_j2, tv_j3, tv_j4, tv_j5, tv_j6,
            tv_x, tv_y, tv_z, tv_Rx, tv_Ry, tv_Rz;

    Button btn_j1, btn_j2, btn_j3, btn_j4, btn_j5, btn_j6;

    Button btn_jogShow;

    // cl_posSetting part ==================================================
    LinearLayout cl_posSetting; //OK

    Button btn_startJobNo, btn_endJobNo;
    TextView tv_startJobName, tv_endJobName;
    SeekBar sb_vel, sb_acc;
    public static SeekBar sb_cycleTime;  //OK

    TextView tv_vel, tv_acc, tv_cycleTime;  //OK

    TextView tv_startJobNo, tv_endJobNo;  //OK

    public static int startJobNo = 99999;
    public static int endJobNo = 99999;


    // cl_playCenter part ==================================================================
    ConstraintLayout cl_playCenter; //OK

    Button btn_tracking, btn_trackingStop, btn_back, btn_play, btn_pause, btn_next, btn_stop1;  //OK
    ProgressBar pb_tracking, pb_move, pb_play;


    // Jog 부분
    LinearLayout ll_speed;
    ConstraintLayout cl_jog, cl_joystick, cl_servo, cl_save;

    Button btn_jointPlus, btn_jointMinus;

    static TextView tv_unitSize;

    Button btn_unitMinus, btn_unitPlus;

    static Button btn_status, btn_xPlus, btn_xMinus, btn_yPlus, btn_yMinus, btn_zPlus, btn_zMinus;
    TextView tv_xPlus, tv_xMinus, tv_yPlus, tv_yMinus, tv_zPlus, tv_zMinus;

    SeekBar sb_rotation;

    TextView tv_servoOn, tv_servoOff, tv_base, tv_tool, tv_position, tv_rotation;

    Switch switch1, switch2, switch3;

    Button btn_moveL, btn_moveJ, btn_stop2;

    Dialog_save dialog_save;
    Dialog_io dialog_io;

    EditText et_pointName, et_ioPort;

    // Index
    public static int selectedJobNo = 99999;
    static double x, y, z, Rx, Ry, Rz, j1, j2, j3, j4, j5, j6;
    static double jog_x, jog_y, jog_z, jog_Rx, jog_Ry, jog_Rz, jog_j1, jog_j2, jog_j3, jog_j4, jog_j5, jog_j6;
    int jog_j1_index = 0, jog_j2_index = 0, jog_j3_index = 0, jog_j4_index = 0, jog_j5_index = 0, jog_j6_index = 0;
    static byte sendJogIndex = 0x00;

    Object_job tempDbJob, tempJob;
    static double moveLsize, moveJsize;
    static double jog_lVel = moveLsize * 8, jog_lAcc = 150000, jog_jVel = moveJsize * 10, jog_jAcc = 35000;
    static double jointVel = 60, jointAcc = 0, lineVel = 500, lineAcc = 0;
    public static double maxJointAcc = 360, maxLineAcc = 1200; // deg/mm
    static boolean isJogOpened = false;
    public static boolean isChanged = false;

    byte baseModeIndex = 0x04;
    int jointIndex = 1;
    byte moveMethodIndex = 0x00;
    int initialIndex = 1000, normalIndex = 100;
    int initialIndexSpeed = 1000, normalIndexSpeed = 60;

    double playAcc = 0;
    int cycleTime = 1;

    int startJobNoIndex = 0;
    int endJobNoIndex = 0;

    public static ArrayList<Integer> searchItems = new ArrayList<>();

    // Thread
    static Thread_Command thread_command;
    static Thread_Status thread_status;
    static Thread myThread, newThread, playThread;
    Handler uiHandler;
    int tempI, tempJ;
    boolean isTracking = false;
    public static boolean isInterrupted = false;
    int trackingStartIndex = 99999;

    // 애니메이션
    Animation warningShow;
    Animation warningHide;

    Display display;
    Window window;
    public static int width;
    public static int height;


    Button btn_warning;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_jog, container, false);
        context = view.getContext();

        display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        width = size.x;
        height = size.y;

        // 뷰 잡기 ===============================================
        // 뷰 잡기 -- 공통 ===========================
        et_search = view.findViewById(R.id.et_search);
        listView = view.findViewById(R.id.listView);
        btn_jobDown = view.findViewById(R.id.btn_jobDown);
        btn_jobUp = view.findViewById(R.id.btn_jobUp);
        btn_addIo = view.findViewById(R.id.btn_addIo);
        btn_deleteJob = view.findViewById(R.id.btn_deleteJob);

        btn_warning = view.findViewById(R.id.btn_warning);

        tv_textX = view.findViewById(R.id.tv_textX);
        tv_textY = view.findViewById(R.id.tv_textY);
        tv_textZ = view.findViewById(R.id.tv_textZ);
        tv_textRx = view.findViewById(R.id.tv_textRx);
        tv_textRy = view.findViewById(R.id.tv_textRy);
        tv_textRz = view.findViewById(R.id.tv_textRz);

        tv_textJ1 = view.findViewById(R.id.tv_textJ1);
        tv_textJ2 = view.findViewById(R.id.tv_textJ2);
        tv_textJ3 = view.findViewById(R.id.tv_textJ3);
        tv_textJ4 = view.findViewById(R.id.tv_textJ4);
        tv_textJ5 = view.findViewById(R.id.tv_textJ5);
        tv_textJ6 = view.findViewById(R.id.tv_textJ6);

        btn_j1 = view.findViewById(R.id.tv_textJ1);
        btn_j2 = view.findViewById(R.id.tv_textJ2);
        btn_j3 = view.findViewById(R.id.tv_textJ3);
        btn_j4 = view.findViewById(R.id.tv_textJ4);
        btn_j5 = view.findViewById(R.id.tv_textJ5);
        btn_j6 = view.findViewById(R.id.tv_textJ6);

        tv_j1 = view.findViewById(R.id.tv_j1);
        tv_j2 = view.findViewById(R.id.tv_j2);
        tv_j3 = view.findViewById(R.id.tv_j3);
        tv_j4 = view.findViewById(R.id.tv_j4);
        tv_j5 = view.findViewById(R.id.tv_j5);
        tv_j6 = view.findViewById(R.id.tv_j6);
        tv_x = view.findViewById(R.id.tv_x);
        tv_y = view.findViewById(R.id.tv_y);
        tv_z = view.findViewById(R.id.tv_z);
        tv_Rx = view.findViewById(R.id.tv_Rx);
        tv_Ry = view.findViewById(R.id.tv_Ry);
        tv_Rz = view.findViewById(R.id.tv_Rz);

        btn_jogShow = view.findViewById(R.id.btn_jogShow);

        btn_jointPlus = view.findViewById(R.id.btn_jointPlus);
        btn_jointMinus = view.findViewById(R.id.btn_jointMinus);

        // 뷰 잡기 -- posSetting ===============================
        cl_posSetting = view.findViewById(R.id.cl_posSetting);
        cl_playCenter = view.findViewById(R.id.cl_playCenter);

        btn_startJobNo = view.findViewById(R.id.btn_startJobNo);
        btn_endJobNo = view.findViewById(R.id.btn_endJobNo);

        tv_startJobName = view.findViewById(R.id.tv_startJobName);
        tv_endJobName = view.findViewById(R.id.tv_endJobName);

        //sb_vel = view.findViewById(R.id.sb_vel);
        sb_acc = view.findViewById(R.id.sb_acc);
        sb_cycleTime = view.findViewById(R.id.sb_cycleTime);

        //tv_vel = view.findViewById(R.id.tv_vel);
        tv_acc = view.findViewById(R.id.tv_acc);
        tv_cycleTime = view.findViewById(R.id.tv_cycleTime);

        tv_startJobNo = view.findViewById(R.id.tv_startJobNo);
        tv_endJobNo = view.findViewById(R.id.tv_endJobNo);

        btn_tracking = view.findViewById(R.id.btn_tracking);
        btn_trackingStop = view.findViewById(R.id.btn_trackingStop);
        btn_back = view.findViewById(R.id.btn_back);
        btn_play = view.findViewById(R.id.btn_play);
        btn_pause = view.findViewById(R.id.btn_pause);
        btn_next = view.findViewById(R.id.btn_next);

        pb_tracking = view.findViewById(R.id.pb_tracking);
        pb_play = view.findViewById(R.id.pb_play);

        btn_stop1 = view.findViewById(R.id.btn_stop1);

        // 뷰 잡기 -- 조그

        ll_speed = view.findViewById(R.id.ll_speed);
        cl_jog = view.findViewById(R.id.cl_jog);
        cl_joystick = view.findViewById(R.id.cl_joystick);
        cl_servo = view.findViewById(R.id.cl_servo);
        cl_save = view.findViewById(R.id.cl_save);

        btn_jointPlus = view.findViewById(R.id.btn_jointPlus);
        btn_jointMinus = view.findViewById(R.id.btn_jointMinus);

        tv_unitSize = view.findViewById(R.id.tv_unitSize);

        btn_unitMinus = view.findViewById(R.id.btn_unitMinus);
        btn_unitPlus = view.findViewById(R.id.btn_unitPlus);
        btn_status = view.findViewById(R.id.btn_status);
        btn_xPlus = view.findViewById(R.id.btn_xPlus);
        btn_xMinus = view.findViewById(R.id.btn_xMinus);
        btn_yPlus = view.findViewById(R.id.btn_yPlus);
        btn_yMinus = view.findViewById(R.id.btn_yMinus);
        btn_zPlus = view.findViewById(R.id.btn_zPlus);
        btn_zMinus = view.findViewById(R.id.btn_zMinus);

        tv_xPlus = view.findViewById(R.id.tv_xPlus);
        tv_xMinus = view.findViewById(R.id.tv_xMinus);
        tv_yPlus = view.findViewById(R.id.tv_yPlus);
        tv_yMinus = view.findViewById(R.id.tv_yMinus);
        tv_zPlus = view.findViewById(R.id.tv_zPlus);
        tv_zMinus = view.findViewById(R.id.tv_zMinus);

        sb_rotation = view.findViewById(R.id.sb_rotation);

        tv_servoOn = view.findViewById(R.id.tv_servoOn);
        tv_servoOff = view.findViewById(R.id.tv_servoOff);
        tv_base = view.findViewById(R.id.tv_base);
        tv_tool = view.findViewById(R.id.tv_tool);
        tv_position = view.findViewById(R.id.tv_position);
        tv_rotation = view.findViewById(R.id.tv_rotation);

        switch1 = view.findViewById(R.id.switch1);
        switch2 = view.findViewById(R.id.switch2);
        switch3 = view.findViewById(R.id.switch3);

        btn_moveL = view.findViewById(R.id.btn_moveL);
        btn_moveJ = view.findViewById(R.id.btn_moveJ);
        btn_stop2 = view.findViewById(R.id.btn_stop2);

        dialog_save = new Dialog_save(context, savePointListener, cancelPointListener);
        dialog_io = new Dialog_io(context, saveIoListener, cancelIoListener);

        // 초기 셋팅 =============================================
        adapter_job = new Adapter_job(context);
        listView.setAdapter(adapter_job);
        adapter_job.notifyDataSetChanged();

        sb_cycleTime.setProgress(cycleTime);
        tv_cycleTime.setText(String.valueOf(cycleTime));
        sb_acc.setProgress((int)playAcc);
        tv_acc.setText(playAcc + "%");

        // 초기 실행시 조그화면 닫기 ==============================
        isJogOpened = false;
        isTracking = false;

        jointIndex = 0;
        ChangeJointButton();

        startJobNo = 99999;
        endJobNo = 99999;
        startJobNoIndex = 0;
        tv_startJobName.setText("Start job name");
        tv_endJobName.setText("End job name");
        tv_startJobNo.setText("0");
        tv_endJobNo.setText("0");
        btn_startJobNo.setBackgroundResource(R.drawable.savebutton);
        btn_endJobNo.setBackgroundResource(R.drawable.savebutton);

        cl_posSetting.setVisibility(View.VISIBLE);
        cl_playCenter.setVisibility(View.VISIBLE);
        btn_jogShow.setText("Jog Open");
        btn_jointMinus.setVisibility(View.GONE);
        btn_jointPlus.setVisibility(View.GONE);

        ll_speed.setVisibility(View.GONE);
        cl_jog.setVisibility(View.GONE);
        cl_servo.setVisibility(View.GONE);
        cl_save.setVisibility(View.GONE);

        warningShow = AnimationUtils.loadAnimation(context, R.anim.warningshow);
        warningHide = AnimationUtils.loadAnimation(context, R.anim.warninghide);

        if(thread_status == null) {
            thread_status = new Thread_Status();
            try {
                thread_status.start();
                thread_status.setPriority(3);
            } catch (Exception e) {
                thread_status = null;
                e.printStackTrace();
            }
        }

        if(thread_command == null) {
            thread_command = new Thread_Command();
            try {
                thread_command.start();
            } catch (Exception e) {
                thread_command = null;
                e.printStackTrace();
            }
        }

        thread_command.setPriority(8);

        if(uiHandler == null) {
            uiHandler = new Handler();
        }

        tv_x.setText("-");
        tv_y.setText("-");
        tv_z.setText("-");
        tv_Rx.setText("-");
        tv_Ry.setText("-");
        tv_Rz.setText("-");

        tv_j1.setText("-");
        tv_j2.setText("-");
        tv_j3.setText("-");
        tv_j4.setText("-");
        tv_j5.setText("-");
        tv_j6.setText("-");


        // 뷰 동작 ===============================================
        // 뷰 동작 -- 공통
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItems.clear();
                selectedJobNo = 99999;
                if(FragmentSetting.jobs.size() > 0) {
                    for(int i = 0; i < FragmentSetting.jobs.size(); i++) {
                        getViewByPosition(i, listView).setBackgroundColor(Color.argb(255, 255, 255, 255));
                    }
                }
                if(s.length() != 0) {
                    for(int i = 0; i < FragmentSetting.jobs.size(); i++) {
                        if(FragmentSetting.jobs.get(i).getJobName().contains(s)) {
                            listView.setSelection(i);
                            getViewByPosition(i, listView).setBackgroundColor(Color.argb(130, 76, 218, 100));
                            searchItems.add(i);
                        }
                    }
                } else {
                    for(int i = 0; i < FragmentSetting.jobs.size(); i++) {
                        getViewByPosition(i, listView).setBackgroundColor(Color.argb(0, 0, 0, 0));
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_jogShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JogOpen();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startJobNo = 99999;
                        endJobNo = 99999;
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                btn_startJobNo.setBackgroundResource(R.drawable.savebutton);
                                btn_endJobNo.setBackgroundResource(R.drawable.savebutton);
                                tv_startJobName.setText("Start job name");
                                tv_startJobNo.setText("");
                                tv_endJobName.setText("End job name");
                                tv_endJobNo.setText("");
                            }
                        });
                        // I/O 선택시
                        if(FragmentSetting.jobs.get(position).getMoveMethod() == (byte)0x13) {

                        }
                        // 처음 선택하거나 똑같은거 선택했을 때
                        else  if(selectedJobNo == 99999 || selectedJobNo == position) {
                            selectedJobNo = position;
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getViewByPosition(position, listView).setBackgroundColor(Color.argb(130, 76, 218, 100));
                                    adapter_job.notifyDataSetChanged();
                                }
                            });
                            if (isJogOpened) {
                                thread_command.InputMoveData((byte) 0x01,
                                        FragmentSetting.jobs.get(position).getJ1(), FragmentSetting.jobs.get(position).getJ2(), FragmentSetting.jobs.get(position).getJ3(),
                                        FragmentSetting.jobs.get(position).getJ4(), FragmentSetting.jobs.get(position).getJ5(), FragmentSetting.jobs.get(position).getJ6(),
                                        jog_jVel, jog_jAcc, jog_jAcc);
                            } else {
                                thread_command.InputMoveData((byte) 0x01,
                                        FragmentSetting.jobs.get(position).getJ1(), FragmentSetting.jobs.get(position).getJ2(), FragmentSetting.jobs.get(position).getJ3(),
                                        FragmentSetting.jobs.get(position).getJ4(), FragmentSetting.jobs.get(position).getJ5(), FragmentSetting.jobs.get(position).getJ6(),
                                        jointVel, jointAcc, jointAcc);
                                tempJob = FragmentSetting.jobs.get(selectedJobNo);
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_j1.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ1() * (180 / 3.141592)));
                                        tv_j2.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ2() * (180 / 3.141592)));
                                        tv_j3.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ3() * (180 / 3.141592)));
                                        tv_j4.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ4() * (180 / 3.141592)));
                                        tv_j5.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ5() * (180 / 3.141592)));
                                        tv_j6.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ6() * (180 / 3.141592)));
                                        tv_x.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getX() * 1000));
                                        tv_y.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getY() * 1000));
                                        tv_z.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getZ() * 1000));
                                        tv_Rx.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRz() * (180 / 3.141592)));
                                        tv_Ry.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRy() * (180 / 3.141592)));
                                        tv_Rz.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRz() * (180 / 3.141592)));
                                    }
                                });
                            }
                            // 일반선택
                        } else {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getViewByPosition(selectedJobNo, listView).setBackgroundColor(Color.argb(0, 0, 0, 0));
                                    getViewByPosition(position, listView).setBackgroundColor(Color.argb(130, 76, 218, 100));
                                    adapter_job.notifyDataSetChanged();
                                }
                            });
                            selectedJobNo = position;
                            if(isJogOpened) {
                                thread_command.InputMoveData((byte)0x01,
                                        FragmentSetting.jobs.get(position).getJ1(),FragmentSetting.jobs.get(position).getJ2(), FragmentSetting.jobs.get(position).getJ3(),
                                        FragmentSetting.jobs.get(position).getJ4(), FragmentSetting.jobs.get(position).getJ5(), FragmentSetting.jobs.get(position).getJ6(),
                                        jog_jVel, jog_jAcc, jog_jAcc);
                            } else {
                                thread_command.InputMoveData((byte)0x01,
                                        FragmentSetting.jobs.get(position).getJ1(),FragmentSetting.jobs.get(position).getJ2(), FragmentSetting.jobs.get(position).getJ3(),
                                        FragmentSetting.jobs.get(position).getJ4(), FragmentSetting.jobs.get(position).getJ5(), FragmentSetting.jobs.get(position).getJ6(),
                                        jointVel, jointAcc, jointAcc);
                                tempJob = FragmentSetting.jobs.get(selectedJobNo);
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_j1.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ1() * (180 / 3.141592)));
                                        tv_j2.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ2() * (180 / 3.141592)));
                                        tv_j3.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ3() * (180 / 3.141592)));
                                        tv_j4.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ4() * (180 / 3.141592)));
                                        tv_j5.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ5() * (180 / 3.141592)));
                                        tv_j6.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ6() * (180 / 3.141592)));
                                        tv_x.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getX() * 1000));
                                        tv_y.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getY() * 1000));
                                        tv_z.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getZ() * 1000));
                                        tv_Rx.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRz() * (180 / 3.141592)));
                                        tv_Ry.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRy() * (180 / 3.141592)));
                                        tv_Rz.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRz() * (180 / 3.141592)));
                                    }
                                });
                            }
                        }
                        currentView = getViewByPosition(position, listView);
                    }
                }).start();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 일반 포인트 선택
                if(startJobNoIndex == 0 && endJobNoIndex == 0) {
                    startJobNo = 99999;
                    endJobNo = 99999;
                    btn_startJobNo.setBackgroundResource(R.drawable.savebutton);
                    btn_endJobNo.setBackgroundResource(R.drawable.savebutton);
                    tv_startJobName.setText("Start job name");
                    tv_startJobNo.setText("");
                    tv_endJobName.setText("End job name");
                    tv_endJobNo.setText("");
                    // I/O 선택시
                    if(FragmentSetting.jobs.get(position).getMoveMethod() == (byte)0x13) {
                        if(selectedJobNo == 99999) {
                            selectedJobNo = position;
                            getViewByPosition(position, listView).setBackgroundColor(Color.argb(130, 76, 218, 100));
                            tv_j1.setText("-");
                            tv_j2.setText("-");
                            tv_j3.setText("-");
                            tv_j4.setText("-");
                            tv_j5.setText("-");
                            tv_j6.setText("-");
                            tv_x.setText("-");
                            tv_y.setText("-");
                            tv_z.setText("-");
                            tv_Rx.setText("-");
                            tv_Ry.setText("-");
                            tv_Rz.setText("-");
                        } else if(selectedJobNo == position) {
                            selectedJobNo = 99999;
                            getViewByPosition(position, listView).setBackgroundColor(Color.argb(0, 0, 0, 0));
                            tv_j1.setText("-");
                            tv_j2.setText("-");
                            tv_j3.setText("-");
                            tv_j4.setText("-");
                            tv_j5.setText("-");
                            tv_j6.setText("-");
                            tv_x.setText("-");
                            tv_y.setText("-");
                            tv_z.setText("-");
                            tv_Rx.setText("-");
                            tv_Ry.setText("-");
                            tv_Rz.setText("-");
                        } else {
                            getViewByPosition(selectedJobNo, listView).setBackgroundColor(Color.argb(0, 0, 0, 0));
                            getViewByPosition(position, listView).setBackgroundColor(Color.argb(130, 76, 218, 100));
                            selectedJobNo = position;
                            if(isJogOpened == false) {
                                tv_j1.setText("-");
                                tv_j2.setText("-");
                                tv_j3.setText("-");
                                tv_j4.setText("-");
                                tv_j5.setText("-");
                                tv_j6.setText("-");
                                tv_x.setText("-");
                                tv_y.setText("-");
                                tv_z.setText("-");
                                tv_Rx.setText("-");
                                tv_Ry.setText("-");
                                tv_Rz.setText("-");
                            }
                        }
                    }
                    else if(selectedJobNo == 99999) {
                    // 처음 선택할 때
                        et_search.setText("");
                        selectedJobNo = position;
                        getViewByPosition(position, listView).setBackgroundColor(Color.argb(130, 76, 218, 100));
                        if(isJogOpened == false) {
                            tempJob = FragmentSetting.jobs.get(selectedJobNo);
                            tv_j1.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ1() * (180 / 3.141592)));
                            tv_j2.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ2() * (180 / 3.141592)));
                            tv_j3.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ3() * (180 / 3.141592)));
                            tv_j4.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ4() * (180 / 3.141592)));
                            tv_j5.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ5() * (180 / 3.141592)));
                            tv_j6.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ6() * (180 / 3.141592)));
                            tv_x.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getX() * 1000));
                            tv_y.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getY() * 1000));
                            tv_z.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getZ() * 1000));
                            tv_Rx.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRz() * (180 / 3.141592)));
                            tv_Ry.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRy() * (180 / 3.141592)));
                            tv_Rz.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRz() * (180 / 3.141592)));
                        }
                    }
                    // 똑같은거 선택할 때
                    else if(selectedJobNo == position) {
                        selectedJobNo = 99999;
                        getViewByPosition(position, listView).setBackgroundColor(Color.argb(0, 0, 0, 0));
                        tv_j1.setText("-");
                        tv_j2.setText("-");
                        tv_j3.setText("-");
                        tv_j4.setText("-");
                        tv_j5.setText("-");
                        tv_j6.setText("-");
                        tv_x.setText("-");
                        tv_y.setText("-");
                        tv_z.setText("-");
                        tv_Rx.setText("-");
                        tv_Ry.setText("-");
                        tv_Rz.setText("-");
                    }
                    // 그 외(일반선택)
                    else {
                        getViewByPosition(selectedJobNo, listView).setBackgroundColor(Color.argb(0, 0, 0, 0));
                        getViewByPosition(position, listView).setBackgroundColor(Color.argb(130, 76, 218, 100));
                        selectedJobNo = position;
                        if(isJogOpened == false) {
                            tempJob = FragmentSetting.jobs.get(selectedJobNo);
                            tv_j1.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ1() * (180 / 3.141592)));
                            tv_j2.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ2() * (180 / 3.141592)));
                            tv_j3.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ3() * (180 / 3.141592)));
                            tv_j4.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ4() * (180 / 3.141592)));
                            tv_j5.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ5() * (180 / 3.141592)));
                            tv_j6.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getJ6() * (180 / 3.141592)));
                            tv_x.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getX() * 1000));
                            tv_y.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getY() * 1000));
                            tv_z.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getZ() * 1000));
                            tv_Rx.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRz() * (180 / 3.141592)));
                            tv_Ry.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRy() * (180 / 3.141592)));
                            tv_Rz.setText(String.format("%.2f", FragmentSetting.jobs.get(selectedJobNo).getRz() * (180 / 3.141592)));
                        }
                    }
                    currentView = getViewByPosition(position, listView);
                }
                // 스타트 포인트 선택(playStartJob 선택 되었을 때)
                else if(startJobNoIndex == 1 && endJobNoIndex == 0) {
                    btn_startJobNo.setBackgroundResource(R.drawable.savebutton);
                    tv_startJobName.setText(FragmentSetting.jobs.get(position).getJobName());
                    tv_startJobNo.setText(String.valueOf(FragmentSetting.jobs.get(position).getJobNo()));
                    // startJobNo 가 endJobNo 보다 클 때
                    // 똑같은 startJobNo 선택했을 때
                    // startJobNo 랑 endJobNo 가 같을 때
                    // 그 외(정상선택)
                    if(position >= endJobNo) {
                        startJobNo = 99999;
                        tv_startJobName.setText("Start job name");
                        tv_startJobNo.setText("");
                        Toast.makeText(context, "Start Job No >= End Job No", Toast.LENGTH_SHORT).show();
                    } else if(startJobNo == position) {
                        tv_startJobName.setText("Start job name");
                        tv_startJobNo.setText("");
                        startJobNo = 99999;
                        Toast.makeText(context, "Selected same no", Toast.LENGTH_SHORT).show();
                    } else {
                        startJobNo = position;
                        btn_startJobNo.setBackgroundResource(R.drawable.addbuttoncon);
                        getViewByPosition(startJobNo, listView).setBackgroundColor(Color.argb(130, 0, 122, 255));
                    }

                }
                // 엔드 포인트 선택
                else if(startJobNoIndex == 0 && endJobNoIndex == 1) {
                    btn_endJobNo.setBackgroundResource(R.drawable.savebutton);
                    tv_endJobName.setText(FragmentSetting.jobs.get(position).getJobName());
                    tv_endJobNo.setText(String.valueOf(FragmentSetting.jobs.get(position).getJobNo()));
                    // endJobNo 가 startJobNo 보다 작을 때
                    // 똑같은 endJobNo 선택했을 때
                    // endJobNo 랑 startJobNo 가 같을 때
                    // 그 외(정상선택)
                    if(startJobNo == 99999) {
                        endJobNo = 99999;
                        tv_endJobName.setText("Start job name");
                        tv_endJobNo.setText("");
                        Toast.makeText(context, "Select start job no first", Toast.LENGTH_SHORT).show();
                    } else if(position <= startJobNo) {
                        endJobNo = 99999;
                        tv_endJobName.setText("Start job name");
                        tv_endJobNo.setText("");
                        Toast.makeText(context, "End Job No <= Start Job No", Toast.LENGTH_SHORT).show();
                    } else if(endJobNo == position) {
                        endJobNo = 99999;
                        tv_endJobName.setText("Start job name");
                        tv_endJobNo.setText("");
                        Toast.makeText(context, "Selected Same No", Toast.LENGTH_SHORT).show();
                    } else {
                        endJobNo = position;
                        btn_endJobNo.setBackgroundResource(R.drawable.addbuttoncon);
                        if(FragmentSetting.jobs.size() > 0) {
                            for(int i = 0; i < FragmentSetting.jobs.size(); i++) {
                                getViewByPosition(i, listView).setBackgroundColor(Color.argb(0, 0, 0, 0));
                            }
                        }
                        for(int i = startJobNo; i <= endJobNo; i++) {
                            getViewByPosition(i, listView).setBackgroundColor(Color.argb(130, 0, 122, 255));
                        }
                    }
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
                startJobNoIndex = 0;
                endJobNoIndex = 0;
            }
        });

        btn_jobDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedJobNo == 99999) {
                    Toast.makeText(context, "Select job", Toast.LENGTH_SHORT).show();
                } else if(selectedJobNo == FragmentSetting.jobs.size() - 1) {
                    Toast.makeText(context, "Last of list", Toast.LENGTH_SHORT).show();
                } else {
                    Object_job tempJob1 = FragmentSetting.jobs.get(selectedJobNo);
                    Object_job tempJob2 = FragmentSetting.jobs.get(selectedJobNo + 1);
                    FragmentSetting.jobs.set(selectedJobNo, tempJob2);
                    FragmentSetting.jobs.set(selectedJobNo + 1, tempJob1);
                    FragmentSetting.jobs.get(selectedJobNo).setJobNo(selectedJobNo);
                    FragmentSetting.jobs.get(selectedJobNo + 1).setJobNo(selectedJobNo + 1);
                    selectedJobNo = selectedJobNo + 1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db.dao_point().updateData(FragmentSetting.jobs);
                        }
                    }).start();
                    adapter_job.notifyDataSetChanged();
                    listView.setSelection(selectedJobNo - 2);
                }
            }
        });

        btn_jobUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedJobNo == 99999) {
                    Toast.makeText(context, "Select job", Toast.LENGTH_SHORT).show();
                } else if(selectedJobNo == 0) {
                    Toast.makeText(context, "First of list", Toast.LENGTH_SHORT).show();
                } else {
                    Object_job tempJob1 = FragmentSetting.jobs.get(selectedJobNo - 1);
                    Object_job tempJob2 = FragmentSetting.jobs.get(selectedJobNo);
                    FragmentSetting.jobs.set(selectedJobNo, tempJob1);
                    FragmentSetting.jobs.set(selectedJobNo - 1, tempJob2);
                    FragmentSetting.jobs.get(selectedJobNo -1).setJobNo(selectedJobNo - 1);
                    FragmentSetting.jobs.get(selectedJobNo).setJobNo(selectedJobNo);
                    selectedJobNo = selectedJobNo - 1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db.dao_point().updateData(FragmentSetting.jobs);
                        }
                    }).start();
                    adapter_job.notifyDataSetChanged();
                    listView.setSelection(selectedJobNo - 2);
                }
            }
        });

        btn_addIo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_io.ioIndex = 0;
                dialog_io.show();
                window = dialog_io.getWindow();
                window.setLayout((int) (width * 0.85f), (int) (height * 0.8f));
            }
        });


        btn_deleteJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedJobNo == 99999) { // 선택X 또는 1개도 없을 때
                    Toast.makeText(context, "Select job", Toast.LENGTH_SHORT).show();
                } else if(FragmentSetting.jobs.size() == 1) { // 1개만 있을 때
                    getViewByPosition(selectedJobNo, listView).setBackgroundColor(Color.argb(0, 0, 0, 0));
                    FragmentSetting.jobs.remove(selectedJobNo);
                } else {
                    FragmentSetting.jobs.remove(selectedJobNo);
                    for(int i = selectedJobNo; i < FragmentSetting.jobs.size(); i++) {
                        FragmentSetting.jobs.get(i).setJobNo(i);
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.clearAllTables();
                        for(int i = 0; i < FragmentSetting.jobs.size(); i++) {
                            db.dao_point().insertData(FragmentSetting.jobs.get(i));
                        }
                    }
                }).start();
                selectedJobNo = 99999;
                adapter_job.notifyDataSetChanged();
            }
        });

        btn_startJobNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJobNo = 99999;
                if(FragmentSetting.jobs.size() > 0) {
                    for(int i = 0; i < FragmentSetting.jobs.size(); i++) {
                        getViewByPosition(i, listView).setBackgroundColor(Color.argb(0, 0, 0, 0));
                    }
                }
                if(endJobNoIndex == 1) {
                    Toast.makeText(context, "Select end job no", Toast.LENGTH_SHORT).show();
                } else if(startJobNoIndex == 0) {
                    startJobNoIndex = 1;
                    btn_startJobNo.setBackgroundResource(R.drawable.addbuttonsel);
                } else {
                    startJobNoIndex = 0;
                    btn_startJobNo.setBackgroundResource(R.drawable.savebutton);
                }
                endJobNoIndex = 0;
            }
        });

        btn_endJobNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJobNo = 99999;
                if(currentView != null) {
                    currentView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                }
                if(startJobNoIndex == 1) {
                    Toast.makeText(context, "Select start job no", Toast.LENGTH_SHORT).show();
                } else if(endJobNoIndex == 0) {
                    endJobNoIndex = 1;
                    btn_endJobNo.setBackgroundResource(R.drawable.addbuttonsel);
                } else {
                    endJobNoIndex = 0;
                    btn_endJobNo.setBackgroundResource(R.drawable.savebutton);
                }
                startJobNoIndex = 0;
            }
        });

/*        sb_vel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0) {
                    playVel = 0;
                } else if(progress == 100) {
                    playVel = 1;
                    jointVel = maxJointVel * playVel / 100;
                    lineVel = maxLineVel * playVel / 100;
                    tv_vel.setText(String.valueOf(progress) + "%");
                    tv_vel.setTextColor(Color.argb(255, 33, 150, 243));
                } else {
                    playVel = 100 - progress;
                    jointVel = maxJointVel * playVel / 100;
                    lineVel = maxLineVel * playVel / 100;
                    tv_vel.setText(String.valueOf(progress) + "%");
                    tv_vel.setTextColor(Color.argb(255, 33, 150, 243));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_vel.setTextColor(Color.argb(255, 143, 143, 143));
            }
        });*/

        sb_acc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                playAcc = progress;
                jointAcc = maxJointAcc * playAcc / 100;
                lineAcc = maxLineAcc * playAcc / 100;
                tv_acc.setText(String.valueOf(progress) + "%");
                tv_acc.setTextColor(Color.argb(255, 33, 150, 243));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_acc.setTextColor(Color.argb(255, 143, 143, 143));
            }
        });
        sb_cycleTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cycleTime = progress;
                tv_cycleTime.setText(String.valueOf(progress));
                tv_cycleTime.setTextColor(Color.argb(255, 33, 150, 243));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_cycleTime.setTextColor(Color.argb(255, 143, 143, 143));
            }
        });

        // 플레이센터
        btn_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Not Today", Toast.LENGTH_SHORT).show();
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(thread_status.threadSocket != null) {
                            isTracking = true;
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    JogOpen();
                                    btn_moveL.setVisibility(View.GONE);
                                    btn_moveJ.setVisibility(View.GONE);
                                    btn_trackingStop.setVisibility(View.VISIBLE);
                                    pb_tracking.setVisibility(View.VISIBLE);
                                }
                            });
                            while(true) {
                                if(isTracking == true) {
                                    if(FragmentSetting.jobs.size() == 0) {
                                        tempDbJob = new Object_job(FragmentSetting.jobs.size(), "Tracking " + FragmentSetting.jobs.size(), (byte)0xFA,
                                                j1, j2, j3, j4, j5, j6, x, y, z, Rx, Ry, Rz, 5, 150000, 150000, 0);
                                        db.dao_point().insertData(tempDbJob);
                                        FragmentSetting.jobs = db.dao_point().getAllData();
                                    } else {
                                        int tempSize = FragmentSetting.jobs.size() - 1;
                                        if(Math.abs(FragmentSetting.jobs.get(tempSize).getJ1() - j1) >= 0.1 ||
                                            Math.abs(FragmentSetting.jobs.get(tempSize).getJ2() - j2) >= 0.1 ||
                                            Math.abs(FragmentSetting.jobs.get(tempSize).getJ3() - j3) >= 0.1 ||
                                            Math.abs(FragmentSetting.jobs.get(tempSize).getJ4() - j4) >= 0.1 ||
                                            Math.abs(FragmentSetting.jobs.get(tempSize).getJ5() - j5) >= 0.1 ||
                                            Math.abs(FragmentSetting.jobs.get(tempSize).getJ6() - j6) >= 0.1)
                                        {
                                            tempDbJob = new Object_job(FragmentSetting.jobs.size(), "Track " + FragmentSetting.jobs.size(), (byte)0xFA,
                                                    j1, j2, j3, j4, j5, j6, x, y, z, Rx, Ry, Rz, 5, 150000, 150000, 0);
                                            db.dao_point().insertData(tempDbJob);
                                            FragmentSetting.jobs = db.dao_point().getAllData();
                                        }
                                    }
                                    uiHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter_job.notifyDataSetChanged();
                                        }
                                    });
                                    uiHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listView.setSelection(FragmentSetting.jobs.size());
                                        }
                                    });
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(context, "Status server is not connected.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
                 */
            }
        });

        btn_trackingStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*isTracking = false;
                JogOpen();
                isJogOpened = false;
                btn_moveL.setVisibility(View.VISIBLE);
                btn_moveJ.setVisibility(View.VISIBLE);
                btn_trackingStop.setVisibility(View.GONE);
                pb_tracking.setVisibility(View.GONE);*/
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if(selectedJobNo == 99999) {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Select job", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if(selectedJobNo == 0) {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "First job", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                selectedJobNo = selectedJobNo - 1;
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentView.setBackgroundColor(Color.argb(255, 255, 255, 255));
                                        getViewByPosition(selectedJobNo, listView).setBackgroundColor(Color.argb(130, 76, 218, 100));
                                        listView.setSelection(selectedJobNo - 2);
                                        currentView = getViewByPosition(selectedJobNo, listView);
                                    }
                                });
                                if(thread_command.socket != null) {
                                    if(FragmentSetting.jobs.get(selectedJobNo).getMoveMethod() == (byte)0x01) { // moveJointAbs
                                        thread_command.InputMoveData(
                                                FragmentSetting.jobs.get(selectedJobNo).getMoveMethod(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ1(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ2(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ3(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ4(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ5(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ6(),
                                                jointVel, jointAcc, jointAcc);
                                    } else if(FragmentSetting.jobs.get(selectedJobNo).getMoveMethod() == (byte)0x03) { // moveJointAbs
                                        thread_command.InputMoveData(
                                                FragmentSetting.jobs.get(selectedJobNo).getMoveMethod(),
                                                FragmentSetting.jobs.get(selectedJobNo).getX(),
                                                FragmentSetting.jobs.get(selectedJobNo).getY(),
                                                FragmentSetting.jobs.get(selectedJobNo).getZ(),
                                                FragmentSetting.jobs.get(selectedJobNo).getRx(),
                                                FragmentSetting.jobs.get(selectedJobNo).getRy(),
                                                FragmentSetting.jobs.get(selectedJobNo).getRz(),
                                                lineVel, lineAcc, lineAcc);
                                    }
                                }
                            }
                        } else {
                            Warning();
                        }
                    }
                }).start();
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if (startJobNo == 99999) {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Select start job no", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (endJobNo == 99999) {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Select end job no", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (jointVel == 0) {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Set speed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (jointAcc == 0) {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Set acceleration", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (cycleTime == 0) {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Set play time", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (thread_command.socket != null) {
                                isInterrupted = false;
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb_play.setVisibility(View.VISIBLE);
                                        btn_jogShow.setVisibility(View.GONE);
                                        btn_tracking.setVisibility(View.INVISIBLE);
                                        btn_back.setVisibility(View.INVISIBLE);
                                        btn_play.setVisibility(View.GONE);
                                        btn_next.setVisibility(View.INVISIBLE);
                                        btn_pause.setVisibility(View.VISIBLE);
                                    }
                                });
                                for (int i = 0; i < cycleTime; cycleTime--) {
                                    tempI = i;
                                    if(isInterrupted == true) {
                                        thread_command.SendStop();
                                        break;
                                    }
                                    for (int j = startJobNo; j <= endJobNo; j++) {
                                        if(isInterrupted == true) {
                                            thread_command.SendStop();
                                            break;
                                        }
                                        tempJ = j;
                                        while (!isInterrupted) {
                                            byte tempReturn = thread_command.IsFinished();
                                            if (tempReturn == 0x01) {
                                                break;
                                            } else {
                                            }
                                        }
                                        uiHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                listView.setSelection(tempJ);
                                                for (int l = startJobNo; l <= endJobNo; l++) {
                                                    getViewByPosition(l, listView).setBackgroundColor(Color.argb(130, 0, 122, 255));
                                                }
                                                if (tempJ == startJobNo) {
                                                    getViewByPosition(endJobNo, listView).setBackgroundColor(Color.argb(130, 0, 122, 255));
                                                } else {
                                                    getViewByPosition(tempJ - 1, listView).setBackgroundColor(Color.argb(130, 0, 122, 255));
                                                }
                                                getViewByPosition(tempJ, listView).setBackgroundColor(Color.argb(240, 0, 122, 255));
                                                isJogOpened = true;
                                            }
                                        });
                                        if (tempJ == startJobNo) {
                                            thread_command.InputMoveData(
                                                    (byte) 0x01,
                                                    FragmentSetting.jobs.get(tempJ).getJ1(),
                                                    FragmentSetting.jobs.get(tempJ).getJ2(),
                                                    FragmentSetting.jobs.get(tempJ).getJ3(),
                                                    FragmentSetting.jobs.get(tempJ).getJ4(),
                                                    FragmentSetting.jobs.get(tempJ).getJ5(),
                                                    FragmentSetting.jobs.get(tempJ).getJ6(),
                                                    20, 50, 50);
                                        } else if (FragmentSetting.jobs.get(tempJ).getMoveMethod() == (byte) 0x01) { // moveJointAbs
                                            thread_command.InputMoveData(
                                                    FragmentSetting.jobs.get(tempJ).getMoveMethod(),
                                                    FragmentSetting.jobs.get(tempJ).getJ1(),
                                                    FragmentSetting.jobs.get(tempJ).getJ2(),
                                                    FragmentSetting.jobs.get(tempJ).getJ3(),
                                                    FragmentSetting.jobs.get(tempJ).getJ4(),
                                                    FragmentSetting.jobs.get(tempJ).getJ5(),
                                                    FragmentSetting.jobs.get(tempJ).getJ6(),
                                                    jointVel, jointAcc, jointAcc);
                                        } else if (FragmentSetting.jobs.get(tempJ).getMoveMethod() == (byte) 0x03) { // moveLineAbs
                                            thread_command.InputMoveData(
                                                    FragmentSetting.jobs.get(tempJ).getMoveMethod(),
                                                    FragmentSetting.jobs.get(tempJ).getX(),
                                                    FragmentSetting.jobs.get(tempJ).getY(),
                                                    FragmentSetting.jobs.get(tempJ).getZ(),
                                                    FragmentSetting.jobs.get(tempJ).getRx(),
                                                    FragmentSetting.jobs.get(tempJ).getRy(),
                                                    FragmentSetting.jobs.get(tempJ).getRz(),
                                                    lineVel, lineAcc, lineAcc);
                                            System.out.println(FragmentSetting.jobs.get(tempJ).getMoveMethod() + "/" +
                                                    FragmentSetting.jobs.get(tempJ).getX() + "/" +
                                                    FragmentSetting.jobs.get(tempJ).getY() + "/" +
                                                    FragmentSetting.jobs.get(tempJ).getZ() + "/" +
                                                    FragmentSetting.jobs.get(tempJ).getRx() + "/" +
                                                    FragmentSetting.jobs.get(tempJ).getRy() + "/" +
                                                    FragmentSetting.jobs.get(tempJ).getRz() + "/" +
                                                    lineVel + "/" + lineAcc + "/" + lineAcc);
                                        } else if (FragmentSetting.jobs.get(tempJ).getMoveMethod() == (byte)0xFA) {
                                            thread_command.InputMoveData(
                                                    FragmentSetting.jobs.get(tempJ).getMoveMethod(),
                                                    FragmentSetting.jobs.get(tempJ).getX(),
                                                    FragmentSetting.jobs.get(tempJ).getY(),
                                                    FragmentSetting.jobs.get(tempJ).getZ(),
                                                    FragmentSetting.jobs.get(tempJ).getRx(),
                                                    FragmentSetting.jobs.get(tempJ).getRy(),
                                                    FragmentSetting.jobs.get(tempJ).getRz(),
                                                    FragmentSetting.jobs.get(tempJ).getVel(),
                                                    FragmentSetting.jobs.get(tempJ).getAcc1(),
                                                    FragmentSetting.jobs.get(tempJ).getAcc2());
                                        } else {
                                            System.out.println("둘다 아님");
                                        }
                                        while (!isInterrupted) {
                                            if (thread_command.IsFinished() == 0x01) {
                                                break;
                                            }
                                        }
                                    }
                                    uiHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            sb_cycleTime.setProgress(cycleTime);
                                            tv_cycleTime.setText(String.valueOf(cycleTime));
                                        }
                                    });
                                }
                                isInterrupted = true;
                                isJogOpened = true;
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb_play.setVisibility(View.INVISIBLE);

                                        btn_jogShow.setVisibility(View.VISIBLE);
                                        btn_tracking.setVisibility(View.VISIBLE);
                                        btn_back.setVisibility(View.VISIBLE);
                                        btn_play.setVisibility(View.VISIBLE);
                                        btn_next.setVisibility(View.VISIBLE);

                                        btn_pause.setVisibility(View.GONE);

                                        Toast.makeText(context, "Play is done", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                myThread = new Thread(runnable);
                                newThread = new Thread(newRunnable);
                                myThread.start();
                                newThread.start();
                            }
                        } else {
                            Warning();
                        }
                    }
                }).start();
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isInterrupted = true;
                        playThread = null;
                        if(thread_command.SendStop() == 0x01) {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Robot has been stopped", Toast.LENGTH_LONG).show();
                                    pb_play.setVisibility(View.INVISIBLE);

                                    btn_jogShow.setVisibility(View.VISIBLE);
                                    btn_tracking.setVisibility(View.VISIBLE);
                                    btn_back.setVisibility(View.VISIBLE);
                                    btn_play.setVisibility(View.VISIBLE);
                                    btn_next.setVisibility(View.VISIBLE);

                                    btn_pause.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "return Error", Toast.LENGTH_LONG).show();
                                }
                            });
                        };
                    }
                }).start();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if(selectedJobNo == 99999) {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Select job", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if(selectedJobNo == FragmentSetting.jobs.size() - 1) {
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Last job", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                selectedJobNo = selectedJobNo + 1;
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentView.setBackgroundColor(Color.argb(255, 255, 255, 255));
                                        getViewByPosition(selectedJobNo, listView).setBackgroundColor(Color.argb(130, 76, 218, 100));
                                        listView.setSelection(selectedJobNo - 2);
                                        currentView = getViewByPosition(selectedJobNo, listView);
                                    }
                                });

                                if(thread_command.socket != null) {
                                    if(FragmentSetting.jobs.get(selectedJobNo).getMoveMethod() == (byte)0x01) { // moveJointAbs
                                        thread_command.InputMoveData(
                                                FragmentSetting.jobs.get(selectedJobNo).getMoveMethod(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ1(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ2(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ3(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ4(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ5(),
                                                FragmentSetting.jobs.get(selectedJobNo).getJ6(),
                                                jointVel, jointAcc, jointAcc);
                                    } else if(FragmentSetting.jobs.get(selectedJobNo).getMoveMethod() == (byte)0x03) { // moveJointAbs
                                        thread_command.InputMoveData(
                                                FragmentSetting.jobs.get(selectedJobNo).getMoveMethod(),
                                                FragmentSetting.jobs.get(selectedJobNo).getX(),
                                                FragmentSetting.jobs.get(selectedJobNo).getY(),
                                                FragmentSetting.jobs.get(selectedJobNo).getZ(),
                                                FragmentSetting.jobs.get(selectedJobNo).getRx(),
                                                FragmentSetting.jobs.get(selectedJobNo).getRy(),
                                                FragmentSetting.jobs.get(selectedJobNo).getRz(),
                                                lineVel, lineAcc, lineAcc);
                                    }
                                }
                            }
                        } else {
                            Warning();
                        }
                    }
                }).start();
            }
        });

        btn_stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        thread_command.SendStop();
                    }
                }).start();
            }
        });


        // 뷰 동작 -- 조그
        btn_stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread_command.SendStop();
            }
        });

        btn_j1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jog_j1_index = 1;
                jointIndex = 1;
                ChangeJointButton();
            }
        });
        btn_j2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jog_j2_index = 1;
                jointIndex = 2;
                ChangeJointButton();
            }
        });
        btn_j3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jog_j3_index = 1;
                jointIndex = 3;
                ChangeJointButton();
            }
        });
        btn_j4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jog_j4_index = 1;
                jointIndex = 4;
                ChangeJointButton();
            }
        });
        btn_j5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jog_j5_index = 1;
                jointIndex = 5;
                ChangeJointButton();
            }
        });
        btn_j6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jog_j6_index = 1;
                jointIndex = 6;
                ChangeJointButton();
            }
        });
        btn_jointMinus.setOnTouchListener(new LongPressRepeatListener(initialIndex, normalIndex, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if(jog_j1_index + jog_j2_index + jog_j3_index + jog_j4_index + jog_j5_index + jog_j6_index > 0) {
                                jog_j1_index = jog_j1_index * -1; jog_j2_index = jog_j2_index * -1; jog_j3_index = jog_j3_index * -1;
                                jog_j4_index = jog_j4_index * -1; jog_j5_index = jog_j5_index * -1; jog_j6_index = jog_j6_index * -1;
                            }
                            jog_j1 = moveJsize * jog_j1_index;jog_j2 = moveJsize * jog_j2_index;jog_j3 = moveJsize * jog_j3_index;
                            jog_j4 = moveJsize * jog_j4_index;jog_j5 = moveJsize * jog_j5_index;jog_j6 = moveJsize * jog_j6_index;
                            thread_command.InputJogData((byte)0x02);
                        } else {
                            Warning();
                        }
                    }
                }).start();
            }
        }));
        btn_jointPlus.setOnTouchListener(new LongPressRepeatListener(initialIndex, normalIndex, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if(jog_j1_index + jog_j2_index + jog_j3_index + jog_j4_index + jog_j5_index + jog_j6_index < 0) {
                                jog_j1_index = jog_j1_index * -1; jog_j2_index = jog_j2_index * -1; jog_j3_index = jog_j3_index * -1;
                                jog_j4_index = jog_j4_index * -1; jog_j5_index = jog_j5_index * -1; jog_j6_index = jog_j6_index * -1;
                            }
                            jog_j1 = moveJsize * jog_j1_index;jog_j2 = moveJsize * jog_j2_index;jog_j3 = moveJsize * jog_j3_index;
                            jog_j4 = moveJsize * jog_j4_index;jog_j5 = moveJsize * jog_j5_index;jog_j6 = moveJsize * jog_j6_index;
                            thread_command.InputJogData((byte)0x02);
                        } else {
                            Warning();
                        }
                    }
                }).start();
                //thread_command.InputJogData((byte)0x02);
            }
        }));

        btn_unitPlus.setOnTouchListener(new LongPressRepeatListener(initialIndexSpeed, normalIndexSpeed, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moveLsize <= 9.9) {
                    moveLsize = moveLsize + 0.1; moveJsize = moveJsize + 0.02;
                    jog_lVel = moveLsize * 6; jog_jVel = moveJsize * 7;
                    tv_unitSize.setText(String.format("%.1f", moveLsize) + "mm / " + String.format("%.2f", moveJsize) + "˚");
                } else {
                    Toast.makeText(context, "Maximum unit size", Toast.LENGTH_SHORT).show();
                }
            }
        }));
        btn_unitMinus.setOnTouchListener(new LongPressRepeatListener(initialIndexSpeed, normalIndexSpeed, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moveLsize >= 0.1) {
                    moveLsize = moveLsize - 0.1; moveJsize = moveJsize - 0.02;
                    jog_lVel = moveLsize * 6; jog_jVel = moveJsize * 7;
                    tv_unitSize.setText(String.format("%.1f", moveLsize) + "mm / "  + String.format("%.2f", moveJsize) + "˚");
                } else {
                    Toast.makeText(context, "Minimum unit size", Toast.LENGTH_SHORT).show();
                }
            }
        }));

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) { // Servo Off
                    tv_servoOff.setTextColor(Color.argb(255, 255, 193, 7));
                    tv_servoOn.setTextColor(Color.argb(255, 206, 206, 206));
                    sendJogIndex = 0x01;
                    thread_command.SendJog();
                } else { // Servo On
                    tv_servoOff.setTextColor(Color.argb(255, 206, 206, 206));
                    tv_servoOn.setTextColor(Color.argb(255, 0, 122, 255));
                    sendJogIndex = 0x00;
                    thread_command.SendJog();

                }
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    baseModeIndex = 0X05;
                    tv_tool.setTextColor(Color.argb(255, 255, 193, 7));
                    tv_base.setTextColor(Color.argb(255, 206, 206, 206));
                } else {
                    baseModeIndex = 0X04;
                    tv_tool.setTextColor(Color.argb(255, 206, 206, 206));
                    tv_base.setTextColor(Color.argb(255, 0, 122, 255));
                }
            }
        });
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    isChanged = true;
                    ChangeRotation();
                    tv_rotation.setTextColor(Color.argb(255, 255, 193, 7));
                    tv_position.setTextColor(Color.argb(255, 206, 206, 206));
                } else {
                    isChanged = false;
                    ChangeRotation();
                    tv_rotation.setTextColor(Color.argb(255, 206, 206, 206));
                    tv_position.setTextColor(Color.argb(255, 0, 122, 255));
                }
            }
        });

        sb_rotation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rotateImage(cl_joystick, progress);
                rotateText(tv_xPlus, progress);
                rotateText(tv_xMinus, progress);
                rotateText(tv_yPlus, progress);
                rotateText(tv_yMinus, progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        btn_xPlus.setOnTouchListener(new LongPressRepeatListener(initialIndex, normalIndex, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if(isChanged) {
                                jog_Rx = moveJsize;
                            } else {
                                jog_x = moveLsize;
                            }
                            thread_command.InputJogData(baseModeIndex);
                        } else {
                            Warning();
                        }
                    }
                }).start();

            }
        }));
        btn_xMinus.setOnTouchListener(new LongPressRepeatListener(initialIndex, normalIndex, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if(isChanged) {
                                jog_Rx = -moveJsize;
                            } else {
                                jog_x = -moveLsize;
                            }
                            thread_command.InputJogData(baseModeIndex);
                        } else {
                            Warning();
                        }
                    }
                }).start();
            }
        }));
        btn_yPlus.setOnTouchListener(new LongPressRepeatListener(initialIndex, normalIndex, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if(isChanged) {
                                jog_Ry = moveJsize;
                            } else {
                                jog_y = moveLsize;
                            }
                            thread_command.InputJogData(baseModeIndex);
                        } else {
                            Warning();
                        }
                    }
                }).start();
            }
        }));
        btn_yMinus.setOnTouchListener(new LongPressRepeatListener(initialIndex, normalIndex, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if(isChanged) {
                                jog_Ry = -moveJsize;
                            } else {
                                jog_y = -moveLsize;
                            }
                            thread_command.InputJogData(baseModeIndex);
                        } else {
                            Warning();
                        }
                    }
                }).start();
            }
        }));
        btn_zPlus.setOnTouchListener(new LongPressRepeatListener(initialIndex, normalIndex, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if(isChanged) {
                                jog_Rz = moveJsize;
                            } else {
                                jog_z = moveLsize;
                            }
                            thread_command.InputJogData(baseModeIndex);
                        } else {
                            Warning();
                        }
                    }
                }).start();
            }
        }));
        btn_zMinus.setOnTouchListener(new LongPressRepeatListener(initialIndex, normalIndex, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(isPushed == true) {
                            if(isChanged) {
                                jog_Rz = -moveJsize;
                            } else {
                                jog_z = -moveLsize;
                            }
                            thread_command.InputJogData(baseModeIndex);
                        } else {
                            Warning();
                        }
                    }
                }).start();
            }
        }));

        btn_moveL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_moveL.setBackgroundResource(R.drawable.savebuttonsel);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_moveL.setBackgroundResource(R.drawable.savebutton);
                        moveMethodIndex = 0x03;
                        dialog_save.show();
                        break;
                }
                return false;
            }
        });

        btn_moveJ.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_moveJ.setBackgroundResource(R.drawable.savebuttonsel);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_moveJ.setBackgroundResource(R.drawable.savebutton);
                        moveMethodIndex = 0x01;
                        dialog_save.show();
                        break;
                }
                return false;
            }
        });

        return view;
    }

    public View getViewByPosition(int pos, ListView listView){
        int firstListItemPosition = listView.getFirstVisiblePosition();
        int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        int childIndex = pos + listView.getHeaderViewsCount() - firstListItemPosition;
        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public void JogOpen() {
        if(isJogOpened) {
            jointIndex = 0;
            ChangeJointButton();
            cl_posSetting.setVisibility(View.VISIBLE);
            cl_playCenter.setVisibility(View.VISIBLE);
            btn_jogShow.setText("Jog Open");
            btn_jointMinus.setVisibility(View.GONE);
            btn_jointPlus.setVisibility(View.GONE);

            ll_speed.setVisibility(View.GONE);
            cl_jog.setVisibility(View.GONE);
            cl_servo.setVisibility(View.GONE);
            cl_save.setVisibility(View.GONE);
            tv_x.setText("-");
            tv_y.setText("-");
            tv_z.setText("-");
            tv_Rx.setText("-");
            tv_Ry.setText("-");
            tv_Rz.setText("-");

            tv_j1.setText("-");
            tv_j2.setText("-");
            tv_j3.setText("-");
            tv_j4.setText("-");
            tv_j5.setText("-");
            tv_j6.setText("-");
            isJogOpened = false;
        } else {
            startJobNo = 99999;
            endJobNo = 99999;
            moveJsize = 0;
            moveLsize = 0;
            selectedJobNo = 99999;
            jointIndex = 1;
            ChangeJointButton();
            cl_posSetting.setVisibility(View.GONE);
            cl_playCenter.setVisibility(View.GONE);
            btn_jogShow.setText("Jog Close");
            btn_jointMinus.setVisibility(View.VISIBLE);
            btn_jointPlus.setVisibility(View.VISIBLE);

            ll_speed.setVisibility(View.VISIBLE);
            cl_jog.setVisibility(View.VISIBLE);
            cl_servo.setVisibility(View.VISIBLE);
            cl_save.setVisibility(View.VISIBLE);
            isJogOpened = true;
        }

        if(FragmentSetting.jobs.size() > 0) {
            for(int i = 0; i < FragmentSetting.jobs.size(); i++) {
                getViewByPosition(i, listView).setBackgroundColor(Color.argb(255, 255, 255, 255));
            }
        }

        startJobNo = 99999;
        endJobNo = 99999;
        tv_startJobName.setText("Start job name");
        tv_startJobNo.setText("");
        tv_endJobName.setText("End job name");
        tv_endJobNo.setText("");
    }

    public void ChangeJobBack() {
        startJobNo = 99999;
        endJobNo = 99999;
        btn_startJobNo.setBackgroundResource(R.drawable.savebutton);
        btn_endJobNo.setBackgroundResource(R.drawable.savebutton);
        tv_startJobName.setText("");
        tv_startJobNo.setText("");
        tv_endJobName.setText("");
        tv_endJobNo.setText("");
    }

    Handler showPb = new Handler() {
        public void handleMessage(Message msg) {
            pb_play.setVisibility(View.VISIBLE);

            btn_jogShow.setVisibility(View.GONE);
            btn_tracking.setVisibility(View.INVISIBLE);
            btn_back.setVisibility(View.INVISIBLE);
            btn_play.setVisibility(View.GONE);
            btn_next.setVisibility(View.INVISIBLE);

            btn_pause.setVisibility(View.VISIBLE);
        }
    };

    Handler hidePb = new Handler() {
        public void handleMessage(Message msg) {
            thread_command.SendStop();

            pb_play.setVisibility(View.INVISIBLE);

            btn_jogShow.setVisibility(View.VISIBLE);
            btn_tracking.setVisibility(View.VISIBLE);
            btn_back.setVisibility(View.VISIBLE);
            btn_play.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.VISIBLE);

            btn_pause.setVisibility(View.GONE);
        }
    };

    Runnable runnable = new Runnable() {
        public void run() {
            Message msg = showPb.obtainMessage();
            showPb.sendMessage(msg);
            for (int i = 5; i >= 0; i--) {
                tempI = i;
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Robot is moving in " + tempI + "\nBe careful.", Toast.LENGTH_SHORT).show();
                    }
                });
                SystemClock.sleep(1000);
            }
        }
    };

    Runnable newRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                myThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SystemClock.sleep(3000);
            for (int i = 0; i < cycleTime; i++) {
                tempI = 0;
                for (int j = startJobNo; j <= endJobNo; j++) {
                    tempI = j;
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (tempI - 2 <= 0) {
                                listView.setSelection(0);
                            }
                            listView.setSelection(tempI - 2);
                            if (tempI == startJobNo) {
                                getViewByPosition(startJobNo, listView).setBackgroundColor(Color.argb(240, 0, 122, 255));
                                getViewByPosition(endJobNo, listView).setBackgroundColor(Color.argb(130, 0, 122, 255));
                            } else {
                                getViewByPosition(tempI, listView).setBackgroundColor(Color.argb(240, 0, 122, 255));
                                getViewByPosition(tempI - 1, listView).setBackgroundColor(Color.argb(130, 0, 122, 255));
                            }
                            tv_j1.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getJ1() * (180 / 3.141592)));
                            tv_j2.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getJ2() * (180 / 3.141592)));
                            tv_j3.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getJ3() * (180 / 3.141592)));
                            tv_j4.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getJ4() * (180 / 3.141592)));
                            tv_j5.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getJ5() * (180 / 3.141592)));
                            tv_j6.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getJ6() * (180 / 3.141592)));
                            tv_x.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getX() * 1000));
                            tv_y.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getY() * 1000));
                            tv_z.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getZ() * 1000));
                            tv_Rx.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getRx() * (180 / 3.141592)));
                            tv_Ry.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getRy() * (180 / 3.141592)));
                            tv_Rz.setText(String.format("%.2f", FragmentSetting.jobs.get(tempJ).getRz() * (180 / 3.141592)));
                        }
                    });
                    SystemClock.sleep(2000);
                }
            }
            Message msg = hidePb.obtainMessage();
            hidePb.sendMessage(msg);
        }
    };
        public void ChangeJointButton() {
        if(isJogOpened) {
            switch(jointIndex) {
                case 1:
                    btn_j1.setBackgroundColor(Color.argb(255, 76, 218, 100));
                    btn_j2.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j3.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j4.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j5.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j6.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j1.setTextColor(Color.argb(255, 255, 255, 255));
                    btn_j2.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j3.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j4.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j5.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j6.setTextColor(Color.argb(255, 0, 0, 0));
                    break;
                case 2:
                    btn_j1.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j2.setBackgroundColor(Color.argb(255, 76, 218, 100));
                    btn_j3.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j4.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j5.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j6.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j1.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j2.setTextColor(Color.argb(255, 255, 255, 255));
                    btn_j3.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j4.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j5.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j6.setTextColor(Color.argb(255, 0, 0, 0));
                    break;
                case 3:
                    btn_j1.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j2.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j3.setBackgroundColor(Color.argb(255, 76, 218, 100));
                    btn_j4.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j5.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j6.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j1.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j2.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j3.setTextColor(Color.argb(255, 255, 255, 255));
                    btn_j4.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j5.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j6.setTextColor(Color.argb(255, 0, 0, 0));
                    break;
                case 4:
                    btn_j1.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j2.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j3.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j4.setBackgroundColor(Color.argb(255, 76, 218, 100));
                    btn_j5.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j6.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j1.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j2.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j3.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j4.setTextColor(Color.argb(255, 255, 255, 255));
                    btn_j5.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j6.setTextColor(Color.argb(255, 0, 0, 0));
                    break;
                case 5:
                    btn_j1.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j2.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j3.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j4.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j5.setBackgroundColor(Color.argb(255, 76, 218, 100));
                    btn_j6.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j1.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j2.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j3.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j4.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j5.setTextColor(Color.argb(255, 255, 255, 255));
                    btn_j6.setTextColor(Color.argb(255, 0, 0, 0));
                    break;
                case 6:
                    btn_j1.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j2.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j3.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j4.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j5.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    btn_j6.setBackgroundColor(Color.argb(255, 76, 218, 100));
                    btn_j1.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j2.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j3.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j4.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j5.setTextColor(Color.argb(255, 0, 0, 0));
                    btn_j6.setTextColor(Color.argb(255, 255, 255, 255));
                    break;
            }
        } else {
            jointIndex = 0;
            btn_j1.setBackgroundColor(Color.argb(255, 255, 255, 255));
            btn_j2.setBackgroundColor(Color.argb(255, 255, 255, 255));
            btn_j3.setBackgroundColor(Color.argb(255, 255, 255, 255));
            btn_j4.setBackgroundColor(Color.argb(255, 255, 255, 255));
            btn_j5.setBackgroundColor(Color.argb(255, 255, 255, 255));
            btn_j6.setBackgroundColor(Color.argb(255, 255, 255, 255));
            btn_j1.setTextColor(Color.argb(255, 0, 0, 0));
            btn_j2.setTextColor(Color.argb(255, 0, 0, 0));
            btn_j3.setTextColor(Color.argb(255, 0, 0, 0));
            btn_j4.setTextColor(Color.argb(255, 0, 0, 0));
            btn_j5.setTextColor(Color.argb(255, 0, 0, 0));
            btn_j6.setTextColor(Color.argb(255, 0, 0, 0));
        }
        ChangeJointSpeed();
    }

    public void ChangeJointSpeed() {
        switch(jointIndex) {
            case 0:
                jog_j1_index = 0; jog_j2_index = 0; jog_j3_index = 0; jog_j4_index = 0; jog_j5_index = 0; jog_j6_index = 0;
                break;
            case 1:
                jog_j1_index = 1; jog_j2_index = 0; jog_j3_index = 0; jog_j4_index = 0; jog_j5_index = 0; jog_j6_index = 0;
                break;
            case 2:
                jog_j1_index = 0; jog_j2_index = 1; jog_j3_index = 0; jog_j4_index = 0; jog_j5_index = 0; jog_j6_index = 0;
                break;
            case 3:
                jog_j1_index = 0; jog_j2_index = 0; jog_j3_index = 1; jog_j4_index = 0; jog_j5_index = 0; jog_j6_index = 0;
                break;
            case 4:
                jog_j1_index = 0; jog_j2_index = 0; jog_j3_index = 0; jog_j4_index = 1; jog_j5_index = 0; jog_j6_index = 0;
                break;
            case 5:
                jog_j1_index = 0; jog_j2_index = 0; jog_j3_index = 0; jog_j4_index = 0; jog_j5_index = 1; jog_j6_index = 0;
                break;
            case 6:
                jog_j1_index = 0; jog_j2_index = 0; jog_j3_index = 0; jog_j4_index = 0; jog_j5_index = 0; jog_j6_index = 1;
                break;
        }
    }

    int beforeImage = 0;
    public void rotateImage(View v, int toDegrees) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", beforeImage, toDegrees - 180);
        anim.start();
        beforeImage = toDegrees - 180;
    }

    int beforeText = 0;
    public void rotateText(View view, int toDegrees) {
        RotateAnimation rotate = new RotateAnimation(
                beforeText, -(toDegrees - 180),
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        rotate.setFillAfter(true);
        view.startAnimation(rotate);
        beforeText = toDegrees - 180;
    }

    public void ChangeRotation() {
        if(isChanged) {
            tv_xPlus.setText("+Rx");
            tv_xMinus.setText("-Rx");
            tv_yPlus.setText("+Ry");
            tv_yMinus.setText("-Ry");
            tv_zPlus.setText("+Rz");
            tv_zMinus.setText("-Rz");
        } else {
            tv_xPlus.setText("+X");
            tv_xMinus.setText("-X");
            tv_yPlus.setText("+Y");
            tv_yMinus.setText("-Y");
            tv_zPlus.setText("+Z");
            tv_zMinus.setText("-Z");
        }
    }

    private View.OnClickListener savePointListener = new View.OnClickListener() {
        public void onClick(View v) {
            et_pointName = dialog_save.findViewById(R.id.et_pointName);
            tempDbJob = new Object_job(FragmentSetting.jobs.size(), et_pointName.getText().toString(), moveMethodIndex,
                    j1, j2, j3, j4, j5, j6, x, y, z, Rx, Ry, Rz, 0, 0, 0, 0);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.dao_point().insertData(tempDbJob);
                    FragmentSetting.jobs = db.dao_point().getAllData();
                }
            }).start();

            et_pointName.setText("");
            adapter_job.notifyDataSetChanged();

            if(FragmentSetting.jobs.size() != 0) {
                listView.setSelection(FragmentSetting.jobs.size());
            }

            dialog_save.dismiss();
            moveMethodIndex = 0x00;
            selectedJobNo = 99999;
        }
    };

    private View.OnClickListener cancelPointListener = new View.OnClickListener() {
        public void onClick(View v) {
            moveMethodIndex = 0;
            dialog_save.dismiss();
        }
    };

    private View.OnClickListener saveIoListener = new View.OnClickListener() {
        public void onClick(View v) {
            et_ioPort = dialog_io.findViewById(R.id.et_ioPort);
            tempJob = new Object_job(FragmentSetting.jobs.size(), et_ioPort.getText().toString(), (byte) 0x13,
                    dialog_io.io1, dialog_io.io2, dialog_io.io3, dialog_io.io4, dialog_io.io5, dialog_io.io6, dialog_io.io7, dialog_io.io8,
                    dialog_io.io9, dialog_io.io10, dialog_io.io11, dialog_io.io12, dialog_io.io13, dialog_io.io14, dialog_io.io15, dialog_io.io16);
            System.out.println(et_ioPort.getText().toString());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.dao_point().insertData(tempJob);
                    FragmentSetting.jobs = db.dao_point().getAllData();
                }
            }).start();

            adapter_job.notifyDataSetChanged();
            if(FragmentSetting.jobs.size() != 0) {
                listView.setSelection(FragmentSetting.jobs.size());
            }
            dialog_io.Initialize();
            dialog_io.dismiss();

            moveMethodIndex = 0x00;
            selectedJobNo = 99999;
        }
    };

    private View.OnClickListener cancelIoListener = new View.OnClickListener() {
        public void onClick(View v) {
            //moveMethodIndex = 0;
            dialog_io.dismiss();
        }
    };

    public void showInfoByRobotType() {
        switch(FragmentSetting.robotIndex) {
            case 0: // 스카라
                tv_textX.setVisibility(View.VISIBLE);
                tv_textY.setVisibility(View.VISIBLE);
                tv_textZ.setVisibility(View.VISIBLE);
                tv_textRx.setVisibility(View.INVISIBLE);
                tv_textRy.setVisibility(View.INVISIBLE);
                tv_textRz.setVisibility(View.VISIBLE);
                tv_textJ1.setVisibility(View.VISIBLE);
                tv_textJ2.setVisibility(View.VISIBLE);
                tv_textJ3.setVisibility(View.VISIBLE);
                tv_textJ4.setVisibility(View.VISIBLE);
                tv_textJ5.setVisibility(View.INVISIBLE);
                tv_textJ6.setVisibility(View.INVISIBLE);

                tv_x.setVisibility(View.VISIBLE);
                tv_y.setVisibility(View.VISIBLE);
                tv_z.setVisibility(View.VISIBLE);
                tv_Rx.setVisibility(View.INVISIBLE);
                tv_Ry.setVisibility(View.INVISIBLE);
                tv_Rz.setVisibility(View.VISIBLE);
                tv_j1.setVisibility(View.VISIBLE);
                tv_j2.setVisibility(View.VISIBLE);
                tv_j3.setVisibility(View.VISIBLE);
                tv_j4.setVisibility(View.VISIBLE);
                tv_j5.setVisibility(View.INVISIBLE);
                tv_j6.setVisibility(View.INVISIBLE);
                break;
            case 1: // 3축
                tv_textX.setVisibility(View.VISIBLE);
                tv_textY.setVisibility(View.VISIBLE);
                tv_textZ.setVisibility(View.INVISIBLE);
                tv_textRx.setVisibility(View.INVISIBLE);
                tv_textRy.setVisibility(View.INVISIBLE);
                tv_textRz.setVisibility(View.VISIBLE);
                tv_textJ1.setVisibility(View.VISIBLE);
                tv_textJ2.setVisibility(View.VISIBLE);
                tv_textJ3.setVisibility(View.VISIBLE);
                tv_textJ4.setVisibility(View.INVISIBLE);
                tv_textJ5.setVisibility(View.INVISIBLE);
                tv_textJ6.setVisibility(View.INVISIBLE);

                tv_x.setVisibility(View.VISIBLE);
                tv_y.setVisibility(View.VISIBLE);
                tv_z.setVisibility(View.INVISIBLE);
                tv_Rx.setVisibility(View.INVISIBLE);
                tv_Ry.setVisibility(View.INVISIBLE);
                tv_Rz.setVisibility(View.VISIBLE);
                tv_j1.setVisibility(View.VISIBLE);
                tv_j2.setVisibility(View.VISIBLE);
                tv_j3.setVisibility(View.VISIBLE);
                tv_j4.setVisibility(View.INVISIBLE);
                tv_j5.setVisibility(View.INVISIBLE);
                tv_j6.setVisibility(View.INVISIBLE);
                break;
            case 2: // 6축
                tv_textX.setVisibility(View.VISIBLE);
                tv_textY.setVisibility(View.VISIBLE);
                tv_textZ.setVisibility(View.VISIBLE);
                tv_textRx.setVisibility(View.VISIBLE);
                tv_textRy.setVisibility(View.VISIBLE);
                tv_textRz.setVisibility(View.VISIBLE);
                tv_textJ1.setVisibility(View.VISIBLE);
                tv_textJ2.setVisibility(View.VISIBLE);
                tv_textJ3.setVisibility(View.VISIBLE);
                tv_textJ4.setVisibility(View.VISIBLE);
                tv_textJ5.setVisibility(View.VISIBLE);
                tv_textJ6.setVisibility(View.VISIBLE);

                tv_x.setVisibility(View.VISIBLE);
                tv_y.setVisibility(View.VISIBLE);
                tv_z.setVisibility(View.VISIBLE);
                tv_Rx.setVisibility(View.VISIBLE);
                tv_Ry.setVisibility(View.VISIBLE);
                tv_Rz.setVisibility(View.VISIBLE);
                tv_j1.setVisibility(View.VISIBLE);
                tv_j2.setVisibility(View.VISIBLE);
                tv_j3.setVisibility(View.VISIBLE);
                tv_j4.setVisibility(View.VISIBLE);
                tv_j5.setVisibility(View.VISIBLE);
                tv_j6.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void Warning() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Hold down the ready button while operate", Toast.LENGTH_LONG).show();
                btn_warning.setVisibility(View.VISIBLE);
                btn_warning.startAnimation(warningShow);
            }
        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                btn_warning.startAnimation(warningHide);
                btn_warning.setVisibility(View.INVISIBLE);
            }
        });
    }


}