// IMyAidlInterface.aidl
package com.moo.demogo;

// Declare any non-default types here with import statements
import com.moo.demogo.bean.AidlBean;
interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    void getProcessUid(out AidlBean aidlBean);
}
