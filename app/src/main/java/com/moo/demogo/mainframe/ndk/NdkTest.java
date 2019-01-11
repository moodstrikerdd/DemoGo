package com.moo.demogo.mainframe.ndk;

public class NdkTest {
    static {
        System.loadLibrary("NdkTest");
    }

    public native static String get();
}
