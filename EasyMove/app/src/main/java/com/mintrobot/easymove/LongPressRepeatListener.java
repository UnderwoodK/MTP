package com.mintrobot.easymove;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class LongPressRepeatListener implements View.OnTouchListener {

    private Handler handler = new Handler();

    private int initialInterval;
    private final int normalInterval;
    private final View.OnClickListener clickListener;
    private View touchedView;

    Drawable current;

    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            if (touchedView.isEnabled()) {
                handler.postDelayed(this, normalInterval);
                clickListener.onClick(touchedView);
            } else {
                handler.removeCallbacks(handlerRunnable);
                touchedView.setPressed(false);
                touchedView = null;
            }

        }
    };

    public LongPressRepeatListener(int initialInterval, int normalInterval, View.OnClickListener clickListener) {
        if (clickListener == null) {
            throw new IllegalArgumentException("listener null");
        }
        if (initialInterval < 0 || normalInterval < 0) {
            throw new IllegalArgumentException("interval error");
        }
        this.initialInterval = initialInterval;
        this.normalInterval = normalInterval;
        this.clickListener = clickListener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(handlerRunnable);
                handler.postDelayed(handlerRunnable, initialInterval);
                touchedView = view;
                touchedView.setPressed(true);
                clickListener.onClick(view);
                current = view.getBackground();
                switch(view.getId()) {
                    case R.id.btn_xPlus:
                        view.setBackgroundResource(R.drawable.xplussel);
                        break;
                    case R.id.btn_xMinus:
                        view.setBackgroundResource(R.drawable.xminussel);
                        break;
                    case R.id.btn_yPlus:
                        view.setBackgroundResource(R.drawable.yplussel);
                        break;
                    case R.id.btn_yMinus:
                        view.setBackgroundResource(R.drawable.yminussel);
                        break;
                    case R.id.btn_zPlus:
                        view.setBackgroundResource(R.drawable.zplussel);
                        break;
                    case R.id.btn_zMinus:
                        view.setBackgroundResource(R.drawable.zminussel);
                        break;
                    case R.id.btn_jointMinus:
                        view.setBackgroundResource(R.drawable.jointbuttonsel);
                        break;
                    case R.id.btn_jointPlus:
                        view.setBackgroundResource(R.drawable.jointbuttonsel);
                        break;
                    case R.id.btn_unitPlus:
                        view.setBackgroundResource(R.drawable.savebuttonsel);
                        break;
                    case R.id.btn_unitMinus:
                        view.setBackgroundResource(R.drawable.savebuttonsel);
                        break;
                    default:
                        view.setBackgroundColor(Color.argb(255, 255, 193, 7));
                        break;
                }
                return true;
            case MotionEvent.ACTION_UP:
                /*switch(view.getId()) {
                    case R.id.btn_xPlus:
                    case R.id.btn_xMinus:
                    case R.id.btn_yPlus:
                    case R.id.btn_yMinus:
                    case R.id.btn_zPlus:
                    case R.id.btn_zMinus:
                        view.setBackground(current);
                        break;
                    default:
                        view.setBackgroundColor(Color.argb(255, 215, 215, 215));
                        break;
                }*/
            case MotionEvent.ACTION_CANCEL:
                view.setBackgroundDrawable(current);
                handler.removeCallbacks(handlerRunnable);
                touchedView.setPressed(false);
                touchedView = null;
                return true;
        }
        return false;
    }
}
