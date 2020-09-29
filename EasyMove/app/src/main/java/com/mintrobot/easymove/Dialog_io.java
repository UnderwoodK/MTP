package com.mintrobot.easymove;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class Dialog_io extends Dialog {

    public static int ioIndex = 0;

    EditText et_ioPort;

    double ioNumber = 0;

    Button btn_io1, btn_io2, btn_io3, btn_io4, btn_io5, btn_io6, btn_io7, btn_io8,
            btn_io9, btn_io10, btn_io11, btn_io12, btn_io13, btn_io14, btn_io15, btn_io16;

    int io1 = 0, io2 = 0, io3 = 0, io4 = 0, io5 = 0, io6 = 0, io7 = 0, io8 = 0,
            io9 = 0, io10 = 0, io11 = 0, io12 = 0, io13 = 0, io14 = 0, io15 = 0, io16 = 0;

    Button btn_save, btn_cancel;
    View.OnClickListener saveIoListener;
    View.OnClickListener cancelIoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.98f;
        layoutParams.height = (int) (FragmentJog.height * 0.8);
        layoutParams.width = (int) (FragmentJog.width * 0.8);
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.dialog_io);

        et_ioPort = findViewById(R.id.et_ioPort);

        btn_io1 = findViewById(R.id.btn_io1);
        btn_io2 = findViewById(R.id.btn_io2);
        btn_io3 = findViewById(R.id.btn_io3);
        btn_io4 = findViewById(R.id.btn_io4);
        btn_io5 = findViewById(R.id.btn_io5);
        btn_io6 = findViewById(R.id.btn_io6);
        btn_io7 = findViewById(R.id.btn_io7);
        btn_io8 = findViewById(R.id.btn_io8);
        btn_io9 = findViewById(R.id.btn_io9);
        btn_io10 = findViewById(R.id.btn_io10);
        btn_io11 = findViewById(R.id.btn_io11);
        btn_io12 = findViewById(R.id.btn_io12);
        btn_io13 = findViewById(R.id.btn_io13);
        btn_io14 = findViewById(R.id.btn_io14);
        btn_io15 = findViewById(R.id.btn_io15);
        btn_io16 = findViewById(R.id.btn_io16);

        Initialize();

        et_ioPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_io1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });

        btn_io2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });

        btn_io6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });

        btn_io10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });

        btn_io14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });
        btn_io16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIoStatus(v);
                IoCalc();
            }
        });

        btn_save = findViewById(R.id.btn_saveIo);
        btn_cancel = findViewById(R.id.btn_cancelIo);

        btn_save.setOnClickListener(saveIoListener);
        btn_cancel.setOnClickListener(cancelIoListener);

    }

    public Dialog_io(@NonNull Context context, View.OnClickListener saveIoListener, View.OnClickListener cancelIoListener) {
        super(context);
        this.saveIoListener = saveIoListener;
        this.cancelIoListener = cancelIoListener;
    }

    public void ChangeIoStatus(View v) {
        switch(v.getId()) {
            case R.id.btn_io1 :
                if(io1 == 0) {
                    io1 = 1;
                    btn_io1.setBackgroundResource(R.drawable.ioon);
                } else {
                    io1 = 0;
                    btn_io1.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io2 :
                if(io2 == 0) {
                    io2 = 1;
                    btn_io2.setBackgroundResource(R.drawable.ioon);
                } else {
                    io2 = 0;
                    btn_io2.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io3 :
                if(io3 == 0) {
                    io3 = 1;
                    btn_io3.setBackgroundResource(R.drawable.ioon);
                } else {
                    io3 = 0;
                    btn_io3.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io4 :
                if(io4 == 0) {
                    io4 = 1;
                    btn_io4.setBackgroundResource(R.drawable.ioon);
                } else {
                    io4 = 0;
                    btn_io4.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io5 :
                if(io5 == 0) {
                    io5 = 1;
                    btn_io5.setBackgroundResource(R.drawable.ioon);
                } else {
                    io5 = 0;
                    btn_io5.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io6 :
                if(io6 == 0) {
                    io6 = 1;
                    btn_io6.setBackgroundResource(R.drawable.ioon);
                } else {
                    io6 = 0;
                    btn_io6.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io7 :
                if(io7 == 0) {
                    io7 = 1;
                    btn_io7.setBackgroundResource(R.drawable.ioon);
                } else {
                    io7 = 0;
                    btn_io7.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io8 :
                if(io8 == 0) {
                    io8 = 1;
                    btn_io8.setBackgroundResource(R.drawable.ioon);
                } else {
                    io8 = 0;
                    btn_io8.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io9 :
                if(io9 == 0) {
                    io9 = 1;
                    btn_io9.setBackgroundResource(R.drawable.ioon);
                } else {
                    io9 = 0;
                    btn_io9.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io10 :
                if(io10 == 0) {
                    io10 = 1;
                    btn_io10.setBackgroundResource(R.drawable.ioon);
                } else {
                    io10 = 0;
                    btn_io10.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io11 :
                if(io11 == 0) {
                    io11 = 1;
                    btn_io11.setBackgroundResource(R.drawable.ioon);
                } else {
                    io11 = 0;
                    btn_io11.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io12 :
                if(io12 == 0) {
                    io12 = 1;
                    btn_io12.setBackgroundResource(R.drawable.ioon);
                } else {
                    io12 = 0;
                    btn_io12.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io13 :
                if(io13 == 0) {
                    io13 = 1;
                    btn_io13.setBackgroundResource(R.drawable.ioon);
                } else {
                    io13 = 0;
                    btn_io13.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io14 :
                if(io14 == 0) {
                    io14 = 1;
                    btn_io14.setBackgroundResource(R.drawable.ioon);
                } else {
                    io14 = 0;
                    btn_io14.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io15 :
                if(io15 == 0) {
                    io15 = 1;
                    btn_io15.setBackgroundResource(R.drawable.ioon);
                } else {
                    io15 = 0;
                    btn_io15.setBackgroundResource(R.drawable.iooff);
                }
                break;
            case R.id.btn_io16 :
                if(io16 == 0) {
                    io16 = 1;
                    btn_io16.setBackgroundResource(R.drawable.ioon);
                } else {
                    io16 = 0;
                    btn_io16.setBackgroundResource(R.drawable.iooff);
                }
                break;
        }
    }

    public double IoCalc() {
        ioNumber = ((1) * io1) + ((2) * io2) + ((4) * io3) + ((8) * io4) + ((16) * io5) + ((32) * io6) + ((64) * io7) + ((128) * io8) +
                ((256) * io9) + ((512) * io10) + ((1024) * io11) + ((2048) * io12) + ((4096) * io13) + ((8192) * io14) + ((16384) * io15) + ((32768) * io16);
        et_ioPort.setText(String.valueOf((int)ioNumber));
        return ioNumber;
    }

    public void Initialize() {
        et_ioPort.setText("0");
        io1 = 0; io2 = 0; io3 = 0; io4 = 0; io5 = 0; io6 = 0; io7 = 0; io8 = 0;
        io9 = 0; io10 = 0; io11 = 0; io12 = 0; io13 = 0; io14 = 0; io15 = 0; io16 = 0;
        btn_io1.setBackgroundResource(R.drawable.iooff);
        btn_io2.setBackgroundResource(R.drawable.iooff);
        btn_io3.setBackgroundResource(R.drawable.iooff);
        btn_io4.setBackgroundResource(R.drawable.iooff);
        btn_io5.setBackgroundResource(R.drawable.iooff);
        btn_io6.setBackgroundResource(R.drawable.iooff);
        btn_io7.setBackgroundResource(R.drawable.iooff);
        btn_io8.setBackgroundResource(R.drawable.iooff);
        btn_io9.setBackgroundResource(R.drawable.iooff);
        btn_io10.setBackgroundResource(R.drawable.iooff);
        btn_io11.setBackgroundResource(R.drawable.iooff);
        btn_io12.setBackgroundResource(R.drawable.iooff);
        btn_io13.setBackgroundResource(R.drawable.iooff);
        btn_io14.setBackgroundResource(R.drawable.iooff);
        btn_io15.setBackgroundResource(R.drawable.iooff);
        btn_io16.setBackgroundResource(R.drawable.iooff);
    }
}
