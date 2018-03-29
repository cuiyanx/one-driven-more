package AndroidDevices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DeviceListenerThread extends Thread {

    private String SN = null;

    public DeviceListenerThread(String SN) {
        this.SN = SN;
    }

    @Override
    public void run() {
        String type = null;
        String code = null;
        String value = null;
        Process process = null;
        String cmdStr = null;
        String strCommands = null;

        System.out.println("    Start to listen: " + this.SN);

        cmdStr = "adb -s " + this.SN + " shell getevent -l";

        try {
            process = Runtime.getRuntime().exec(cmdStr);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        InputStream inStreams = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inStreams));
        DataHanding DataHanding = new DataHanding(this.SN);

        try {
            while ((strCommands = br.readLine()) != null) {
                String[] strCommandsArray = strCommands.split("\\s+");
                type = null;
                code = null;
                value = null;

                if (strCommandsArray.length > 3) {
                    if (strCommandsArray[1].equals("EV_ABS")) {
                        if (strCommandsArray[2].equals("ABS_MT_TRACKING_ID")
                                || strCommandsArray[2].equals("ABS_MT_POSITION_X")
                                || strCommandsArray[2].equals("ABS_MT_POSITION_Y")) {
                            type = strCommandsArray[1];
                            code = strCommandsArray[2];
                            value = strCommandsArray[3];
                        }
                    } else if (strCommandsArray[1].equals("EV_KEY")) {
                        if (strCommandsArray[2].equals("BTN_TOUCH") || strCommandsArray[2].equals("KEY_BACK")
                                || (strCommandsArray[2].equals("KEY_MENU") || strCommandsArray[2].equals("00fe"))
                                || (strCommandsArray[2].equals("KEY_HOME")
                                        || strCommandsArray[2].equals("KEY_HOMEPAGE"))
                                || strCommandsArray[2].equals("KEY_POWER")
                                || strCommandsArray[2].equals("KEY_VOLUMEDOWN")
                                || strCommandsArray[2].equals("KEY_VOLUMEUP")) {
                            type = strCommandsArray[1];
                            code = strCommandsArray[2];
                            value = strCommandsArray[3];
                        }
                    }

                    if ((type != null) && (code != null) && (value != null)) {
                        DataHanding.ProcessingData(type, code, value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Stop to listen: " + this.SN);

        process.destroy();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

}
