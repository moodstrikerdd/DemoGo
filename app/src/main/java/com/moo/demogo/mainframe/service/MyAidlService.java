package com.moo.demogo.mainframe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.moo.moouidservice.UidAidlInterface;
import com.moo.moouidservice.bean.AidlBean;

import java.util.Arrays;

public class MyAidlService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }

    private UidAidlInterface.Stub bind = new UidAidlInterface.Stub() {

        @Override
        public AidlBean getAidlUid() throws RemoteException {
            AidlBean aidlBean = new AidlBean();
            aidlBean.setUid(100);
            Log.e("aidl", "服务端发送：" + aidlBean.getUid());
            return aidlBean;
        }

        @Override
        public AidlBean[] getAidlUids() throws RemoteException {
            AidlBean[] aidlBeans = new AidlBean[3];
            for (int i = 0; i < aidlBeans.length; i++) {
                AidlBean aidlBean = new AidlBean();
                aidlBean.setUid(100 + i);
                aidlBeans[i] = aidlBean;
            }
            Log.e("aidl", "服务端接收：" + Arrays.toString(aidlBeans));
            return aidlBeans;
        }

        @Override
        public void setClientUid(AidlBean aidlBean) throws RemoteException {
            Log.e("aidl", "服务端接收：" + aidlBean.getUid());
        }
    };
}
