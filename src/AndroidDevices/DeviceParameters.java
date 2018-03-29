package AndroidDevices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DeviceParameters {

    private int Max = 50;

    public DeviceParameters() {
    }

    public String[] getDeviceSNList() {
        Process process = null;
        String strCommands;
        String cmdStr = "adb devices";
        String[] deviceSNsTmp = new String[Max];
        int SNCountTmp = 0;

        try {
            process = Runtime.getRuntime().exec(cmdStr);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        InputStream inStreams = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inStreams));

        try {
            while ((strCommands = br.readLine()) != null) {
                String[] strCommandsArray = strCommands.split("\\s+");

                if (strCommandsArray.length > 1) {
                    if (strCommandsArray[1].equals("device")) {
                        deviceSNsTmp[SNCountTmp] = strCommandsArray[0];
                        SNCountTmp = SNCountTmp + 1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] deviceSNs = new String[SNCountTmp];
        int SNCount = 0;

        for (int i = 0; i < deviceSNsTmp.length; i++) {
            if (deviceSNsTmp[i] != null) {
                deviceSNs[SNCount] = deviceSNsTmp[i];
                SNCount = SNCount + 1;
            }
        }

        return deviceSNs;
    }

    public int[] getDeviceLW(int count) {
        String strCommands;
        String cmdStr = "getevent -p | grep -A 1 0035";
        int[] XY = new int[2];

        try {
            StaticData.DeviceBW[count].write(cmdStr);
            StaticData.DeviceBW[count].newLine();
            StaticData.DeviceBW[count].flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while ((strCommands = StaticData.DeviceBR[count].readLine()) != null) {
                String[] strCommandsArray = strCommands.split("\\s+");

                if (strCommandsArray.length > 1
                        && (strCommandsArray[1].startsWith("0035") || strCommandsArray[1].startsWith("0036"))) {
                    if ((strCommandsArray[1].equals("0035")) && (strCommandsArray[5].equals("min"))
                            && (strCommandsArray[7].equals("max"))) {
                        String[] strArrayX = strCommandsArray[8].split("\\,");
                        XY[0] = Integer.parseInt(strArrayX[0]) + 1;
                    }

                    if ((strCommandsArray[1].equals("0036")) && (strCommandsArray[5].equals("min"))
                            && (strCommandsArray[7].equals("max"))) {
                        String[] strArrayY = strCommandsArray[8].split("\\,");
                        XY[1] = Integer.parseInt(strArrayY[0]) + 1;

                        break;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return XY;
    }

    public int[] getDeviceResolution(int count) {
        String strCommands;
        String cmdStr = "dumpsys window displays | grep init";
        int[] XY = new int[2];

        try {
            StaticData.DeviceBW[count].write(cmdStr);
            StaticData.DeviceBW[count].newLine();
            StaticData.DeviceBW[count].flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while ((strCommands = StaticData.DeviceBR[count].readLine()) != null) {
                String[] strCommandsArray = strCommands.split("\\s+");

                if (strCommandsArray.length > 1 && strCommandsArray[1].startsWith("init")) {
                    String[] strArray1 = strCommandsArray[1].split("=");
                    String[] strArray2 = strArray1[1].split("x");
                    XY[0] = Integer.parseInt(strArray2[0]);
                    XY[1] = Integer.parseInt(strArray2[1]);

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ((XY[0] == 0) || (XY[1] == 0)) {
            int[] XYtemp = getDeviceLW(count);
            XY[0] = XYtemp[0];
            XY[1] = XYtemp[1];
        }

        return XY;
    }

    public int[] getDeviceVersion(int count) {
        String strCommands;
        String cmdStr = "getprop ro.build.version.release";
        int[] Version = new int[3];

        try {
            StaticData.DeviceBW[count].write(cmdStr);
            StaticData.DeviceBW[count].newLine();
            StaticData.DeviceBW[count].flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while ((strCommands = StaticData.DeviceBR[count].readLine()) != null) {
                String[] strCommandsArray = strCommands.split("\\s+");

                if (strCommandsArray.length == 1 && !strCommandsArray[0].equals("")) {
                    String[] strCommandsArrayTmp = strCommandsArray[0].split("\\.");
                    Version[0] = Integer.parseInt(strCommandsArrayTmp[0]);
                    Version[1] = Integer.parseInt(strCommandsArrayTmp[1]);
                    Version[2] = Integer.parseInt(strCommandsArrayTmp[2]);

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Version;
    }

}
