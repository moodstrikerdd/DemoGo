package com.moo.moouidservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import com.moo.moouidservice.bean.AidlBean;

/**
 * @ClassName: UidAidlInterface
 * @Author: moodstrikerdd
 * @CreateDate: 2021/6/11 14:33
 * @Description: java类作用描述
 */
public interface UidAidlInterface extends IInterface {
    AidlBean getAidlUid() throws RemoteException;

    AidlBean[] getAidlUids() throws RemoteException;

    void setClientUid(AidlBean aidlBean) throws RemoteException;

    public static abstract class Stub extends Binder implements UidAidlInterface {
        public static final String DESCRIPTOR = "com.moo.service.UidAidlInterface";

        public static final int TRANSACTION_getAidlUid = Binder.FIRST_CALL_TRANSACTION;
        public static final int TRANSACTION_getAidlUids = Binder.FIRST_CALL_TRANSACTION + 1;
        public static final int TRANSACTION_setClientUid = Binder.FIRST_CALL_TRANSACTION + 2;

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        public static UidAidlInterface asInterface(IBinder binder) {
            if (binder == null) {
                return null;
            }
            IInterface iInterface = binder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof UidAidlInterface) {
                return (UidAidlInterface) iInterface;
            }
            return new Proxy(binder);
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            switch (code) {
                case TRANSACTION_setClientUid:
                    data.enforceInterface(descriptor);
                    AidlBean aidlBean;
                    if ((0 != data.readInt())) {
                        aidlBean = AidlBean.CREATOR.createFromParcel(data);
                    } else {
                        aidlBean = null;
                    }
                    this.setClientUid(aidlBean);
                    reply.writeNoException();
                    if (aidlBean != null) {
                        reply.writeInt(1);
                        aidlBean.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case TRANSACTION_getAidlUid:
                    data.enforceInterface(descriptor);
                    AidlBean result = this.getAidlUid();
                    reply.writeNoException();
                    if (result != null) {
                        reply.writeInt(1);
                        result.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case TRANSACTION_getAidlUids:
                    data.enforceInterface(descriptor);
                    AidlBean[] aidlUids = this.getAidlUids();
                    reply.writeNoException();
                    reply.writeTypedArray(aidlUids, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    return true;
                case IBinder.INTERFACE_TRANSACTION:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static class Proxy implements UidAidlInterface {

            private IBinder mRemote;

            public Proxy(IBinder mRemote) {
                this.mRemote = mRemote;
            }


            @Override
            public IBinder asBinder() {
                return mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public AidlBean getAidlUid() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                AidlBean result;
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(TRANSACTION_getAidlUid, data, reply, 0);
                    reply.readException();
                    if (reply.readInt() != 0) {
                        result = AidlBean.CREATOR.createFromParcel(reply);
                    } else {
                        result = null;
                    }
                } finally {
                    data.recycle();
                    reply.recycle();
                }
                return result;
            }

            @Override
            public AidlBean[] getAidlUids() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                AidlBean[] result;
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(TRANSACTION_getAidlUids, data, reply, 0);
                    reply.readException();
                    result = reply.createTypedArray(AidlBean.CREATOR);
                } finally {
                    data.recycle();
                    reply.recycle();
                }
                return result;
            }

            @Override
            public void setClientUid(AidlBean aidlBean) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (aidlBean != null) {
                        data.writeInt(1);
                        aidlBean.writeToParcel(data, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        data.writeInt(0);
                    }
                    mRemote.transact(TRANSACTION_setClientUid, data, reply, 0);
                    reply.readException();
                    if (0 != reply.readInt()) {
                        aidlBean.readFromParcel(reply);
                    }
                } finally {
                    data.recycle();
                    reply.recycle();
                }
            }

        }
    }
}
