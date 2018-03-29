package AndroidDevices;

import java.util.HashMap;
import java.util.Map;

public class DeviceEvents {
    private String keyWord;

    public DeviceEvents() {
    }

    private String KeyNumber(String key) {
        Map<String, String> keyMap = new HashMap<String, String>();
        keyMap.put("keyHome", "3");
        keyMap.put("KeyBack", "4");
        keyMap.put("keyVolumeUp", "24");
        keyMap.put("keyVolumeDown", "25");
        keyMap.put("keyPower", "26");
        keyMap.put("keyMenu", "82");

        return keyMap.get(key);
    }

    public void KeyPress(int count, String keyWord) {
        this.keyWord = keyWord;
        String myKeyNumber = KeyNumber(this.keyWord);

        try {
            String cmdStr = "input keyevent " + myKeyNumber;
            StaticData.DeviceBW[count].write(cmdStr);
            StaticData.DeviceBW[count].newLine();
            StaticData.DeviceBW[count].flush();

            System.out.println(StaticData.DeviceSNList[count] + ":");
            System.out.println("    Key: " + keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ScreenTap(int count, String keyXs, String keyYs) {
        try {
            String cmdStr = "input tap " + keyXs + " " + keyYs;
            StaticData.DeviceBW[count].write(cmdStr);
            StaticData.DeviceBW[count].newLine();
            StaticData.DeviceBW[count].flush();

            System.out.println(StaticData.DeviceSNList[count] + ":");
            System.out.println("    Tap: [" + keyXs + ", " + keyYs + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ScreenSwipe(int count, String keyXs, String keyYs, String keyXe, String keyYe) {
        try {
            String cmdStr = "input swipe " + keyXs + " " + keyYs + " " + keyXe + " " + keyYe;
            StaticData.DeviceBW[count].write(cmdStr);
            StaticData.DeviceBW[count].newLine();
            StaticData.DeviceBW[count].flush();

            System.out.println(StaticData.DeviceSNList[count] + ":");
            System.out.println("    Swipe: [" + keyXs + ", " + keyYs + "] -> [" + keyXe + ", " + keyYe + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
