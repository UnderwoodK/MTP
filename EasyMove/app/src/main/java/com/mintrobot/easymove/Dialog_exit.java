package com.mintrobot.easymove;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

public class Dialog_exit extends Dialog {

    Button btn_exit, btn_cancel;
    View.OnClickListener exitListener;
    View.OnClickListener cancelListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.98f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.dialog_exit);

        btn_exit = findViewById(R.id.btn_exit);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_exit.setOnClickListener(exitListener);
        btn_cancel.setOnClickListener(cancelListener);

    }

    public Dialog_exit(@NonNull Context context, View.OnClickListener exitListener, View.OnClickListener cancelListener) {
        super(context);
        this.exitListener = exitListener;
        this.cancelListener = cancelListener;

    }
}