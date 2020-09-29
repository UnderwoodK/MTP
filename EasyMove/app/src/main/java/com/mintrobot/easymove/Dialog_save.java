package com.mintrobot.easymove;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

public class Dialog_save extends Dialog {

    Button btn_savePoint, btn_cancelPoint;
    View.OnClickListener savePointListener;
    View.OnClickListener cancelPointListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.98f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.dialog_save);

        btn_savePoint = findViewById(R.id.btn_savePoint);
        btn_cancelPoint = findViewById(R.id.btn_cancelPoint);

        btn_savePoint.setOnClickListener(savePointListener);
        btn_cancelPoint.setOnClickListener(cancelPointListener);
    }

    public Dialog_save(@NonNull Context context, View.OnClickListener savePointListener, View.OnClickListener cancelPointListener) {
        super(context);
        this.savePointListener = savePointListener;
        this.cancelPointListener = cancelPointListener;

    }
}

