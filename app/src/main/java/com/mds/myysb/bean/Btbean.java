package com.mds.myysb.bean;

import android.bluetooth.BluetoothDevice;

public class Btbean {

    private boolean is;
    private BluetoothDevice remoteDevice;

    public Btbean(boolean is, BluetoothDevice remoteDevice) {
        this.is = is;
        this.remoteDevice = remoteDevice;
    }

    public boolean isIs() {
        return is;
    }

    public BluetoothDevice getRemoteDevice() {
        return remoteDevice;
    }

    public void setIs(boolean is) {
        this.is = is;
    }

    public void setRemoteDevice(BluetoothDevice remoteDevice) {
        this.remoteDevice = remoteDevice;
    }

}
