package test;

import AndroidDevices.DeviceListener;

public class testmain {

	public static void main(String[] args) {
		DeviceListener DeviceListener = new DeviceListener(args[0]);
		DeviceListener.Start();
	}
}
