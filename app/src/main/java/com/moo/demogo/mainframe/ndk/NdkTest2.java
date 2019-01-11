package com.moo.demogo.mainframe.ndk;

public class NdkTest2 {
    static {
        System.loadLibrary("NdkTest2");
    }
    public native static String get();
}
