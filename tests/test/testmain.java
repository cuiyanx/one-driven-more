package test;

import AndroidDevices.DeviceListener;

public class testmain {

    public static void main(String[] args) {
        DeviceListener DeviceListener = new DeviceListener();
        DeviceListener.init();
        DeviceListener.StartListening();
    }
}
