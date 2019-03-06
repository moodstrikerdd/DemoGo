package com.moo.demogo.mainframe.leakcanary;

import com.moo.demogo.R;
import com.moo.demogo.base.BaseActivity;
import com.moo.demogo.utils.SPUtils;

public class LeakCanaryActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_leakcanary;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        LeakThread leakThread = new LeakThread();
        leakThread.start();
    }

    class LeakThread extends Thread {
        @Override
        public void run() {
            try {
                SPUtils.init(LeakCanaryActivity.this);
                Thread.sleep(6 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
