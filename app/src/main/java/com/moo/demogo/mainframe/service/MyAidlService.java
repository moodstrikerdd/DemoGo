package com.moo.demogo.mainframe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.moo.demogo.IMyAidlInterface;
import com.moo.demogo.bean.AidlBean;

public class MyAidlService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }

    private IMyAidlInterface.Stub bind = new IMyAidlInterface.Stub() {

        @Override
        public void getProcessUid(AidlBean aidlBean) throws RemoteException {
            if(aidlBean == null){
                aidlBean = new AidlBean();
            }
            aidlBean.setUid(10101);
        }
    };
}
