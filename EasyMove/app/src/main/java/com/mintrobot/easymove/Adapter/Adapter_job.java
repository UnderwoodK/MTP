package com.mintrobot.easymove.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mintrobot.easymove.DB.Object_job;
import com.mintrobot.easymove.FragmentJog;
import com.mintrobot.easymove.FragmentSetting;
import com.mintrobot.easymove.R;

public class Adapter_job extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    TextView tv_jobNo, tv_jobType, tv_jobName;

    public Adapter_job(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return FragmentSetting.jobs.size();
    }

    @Override
    public Object getItem(int position) {
        return FragmentSetting.jobs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View v, ViewGroup parent) {
        if(v == null) {
            v = inflater.inflate(R.layout.adapter_job, parent, false);
        }
        Object_job tempJob = FragmentSetting.jobs.get(i);
        tv_jobNo = v.findViewById(R.id.tv_jobNo);
        tv_jobType = v.findViewById(R.id.tv_jobType);
        tv_jobName = v.findViewById(R.id.tv_jobName);
        tv_jobNo.setText(String.valueOf(tempJob.getJobNo()));
        if(tempJob.getMoveMethod() == (byte)0x01) {
            tv_jobType.setText("moveJ");
        } else if(tempJob.getMoveMethod() == (byte)0x03) {
            tv_jobType.setText("moveL");
        } else if(tempJob.getMoveMethod() == (byte)0xFA || tempJob.getMoveMethod() == (byte)0xFB) {
            tv_jobType.setText("Track");
        } else if(tempJob.getMoveMethod() == (byte)0x13) {
            tv_jobType.setText("I/O Write");
        } else {
            tv_jobType.setText("Error");
        }
        tv_jobName.setText(tempJob.getJobName());

        // 선택한게 있을 때(시작넘버 지정안되어 있고 종료넘버 지정안되어 있을 때)
        if(i == FragmentJog.selectedJobNo) {
            v.setBackgroundColor(Color.argb(130, 76, 218, 100));
        } else if(FragmentJog.startJobNo != 99999 && FragmentJog.endJobNo ==99999 && i == FragmentJog.startJobNo) {
            v.setBackgroundColor(Color.argb(130, 0, 122, 255));
        } else if(FragmentJog.startJobNo == 99999 && FragmentJog.endJobNo != 99999 && i == FragmentJog.endJobNo) {
            v.setBackgroundColor(Color.argb(130, 0, 122, 255));
        } else if(FragmentJog.startJobNo != 99999 && FragmentJog.endJobNo != 99999 && i >= FragmentJog.startJobNo && i <= FragmentJog.endJobNo) {
            v.setBackgroundColor(Color.argb(130, 0, 122, 255));
        } else if(FragmentJog.searchItems.contains(i)) {
            v.setBackgroundColor(Color.argb(130, 76, 218, 100));
        } else {
            v.setBackgroundColor(Color.argb(255, 255, 255, 255));
        }

        return v;
    }

    public TextView getTv_jobNo() {
        return tv_jobNo;
    }

    public void setTv_jobNo(TextView tv_jobNo) {
        this.tv_jobNo = tv_jobNo;
    }

    public TextView getTv_jobType() {
        return tv_jobType;
    }

    public void setTv_jobType(TextView tv_jobType) {
        this.tv_jobType = tv_jobType;
    }

    public TextView getTv_jobName() {
        return tv_jobName;
    }

    public void setTv_jobName(TextView tv_jobName) {
        this.tv_jobName = tv_jobName;
    }
}

