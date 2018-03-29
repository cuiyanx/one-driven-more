package AndroidDevices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DeviceListener {

    public DeviceListener() {
    }

    public void init() {
        System.out.println("Scan android devices....\n");

        StaticData.DeviceResolutionX = new int[StaticData.DeviceSNList.length];
        StaticData.DeviceResolutionY = new int[StaticData.DeviceSNList.length];
        StaticData.DeviceProcess = new Process[StaticData.DeviceSNList.length];
        StaticData.DeviceBW = new BufferedWriter[StaticData.DeviceSNList.length];
        StaticData.DeviceBR = new BufferedReader[StaticData.DeviceSNList.length];
        StaticData.DeviceListenerThread = new DeviceListenerThread[StaticData.DeviceSNList.length];
        DeviceParameters DPs = new DeviceParameters();

        for (int i = 0; i < StaticData.DeviceSNList.length; i++) {
            System.out.println("SN[" + i + "]: " + StaticData.DeviceSNList[i]);

            String cmdStr = "adb -s " + StaticData.DeviceSNList[i] + " shell";
            try {
                StaticData.DeviceProcess[i] = Runtime.getRuntime().exec(cmdStr);
                StaticData.DeviceBW[i] = new BufferedWriter(
                        new OutputStreamWriter(StaticData.DeviceProcess[i].getOutputStream()));
                StaticData.DeviceBR[i] = new BufferedReader(
                        new InputStreamReader(StaticData.DeviceProcess[i].getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            int[] XYer = DPs.getDeviceResolution(i);
            StaticData.DeviceResolutionX[i] = XYer[0];
            StaticData.DeviceResolutionY[i] = XYer[1];
            System.out.println("    Resolution[" + i + "]: " + XYer[0] + "*" + XYer[1]);

            int[] LWer = DPs.getDeviceLW(i);
            System.out.println("    LW[" + i + "]: " + LWer[0] + "*" + LWer[1]);

            int[] Version = DPs.getDeviceVersion(i);
            System.out.println("    Version[" + i + "]: " + Version[0] + "." + Version[1] + "." + Version[2] + "\n");
        }
    }

    public void StartListening() {
        System.out.println("Start to listen android devices....");

        for (int i = 0; i < StaticData.DeviceSNList.length; i++) {
            StaticData.DeviceListenerThread[i] = new DeviceListenerThread(StaticData.DeviceSNList[i]);
            StaticData.DeviceListenerThread[i].start();
        }
    }

}
