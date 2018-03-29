package AndroidDevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class StaticData {

    static public String[] DeviceSNList = null;
    static public int[] DeviceResolutionX = null;
    static public int[] DeviceResolutionY = null;
    static public DeviceListenerThread[] DeviceListenerThread = null;
    static public Process[] DeviceProcess = null;
    static public BufferedWriter[] DeviceBW = null;
    static public BufferedReader[] DeviceBR = null;
    static public String keyWord = null;
    static public String keyXs = null;
    static public String keyYs = null;
    static public String keyXe = null;
    static public String keyYe = null;
    private static float RatioX = 0;
    private static float RatioY = 0;

    static {
        Getdata();
    }

    synchronized static public void Getdata() {
        initOperationData();
        DeviceParameters DPs = new DeviceParameters();
        StaticData.DeviceSNList = DPs.getDeviceSNList();
    }

    public static int getDeviceCount(String SN) {
        for (int i = 0; i < DeviceSNList.length; i++) {
            if (SN.equals(DeviceSNList[i]))
                return i;
        }

        return 0;
    }

    public static void initOperationData() {
        StaticData.keyWord = null;
        StaticData.keyXs = null;
        StaticData.keyYs = null;
        StaticData.keyXe = null;
        StaticData.keyYe = null;
    }

    public static int getResolutionX(String SN) {
        int flag = getDeviceCount(SN);
        return DeviceResolutionX[flag];
    }

    public static int getResolutionY(String SN) {
        int flag = getDeviceCount(SN);
        return DeviceResolutionY[flag];
    }

    public static Process getProcess(String SN) {
        int flag = getDeviceCount(SN);
        return DeviceProcess[flag];
    }

    public static float getRatioX(String SN, int x) {
        int RX = getResolutionX(SN);
        RatioX = (float) (Math.round(((float) x / RX) * 10000)) / 10000;

        return RatioX;
    }

    public static float getRatioY(String SN, int y) {
        int RY = getResolutionY(SN);
        RatioY = (float) (Math.round(((float) y / RY) * 10000)) / 10000;

        return RatioY;
    }

}
