package AndroidDevices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeviceEvents {
	private String SN;
	private String keyWord;
	private String keyXs;
	private String keyYs;
	private String keyXe;
	private String keyYe;

	public DeviceEvents(String SN) {
		this.SN = SN;
	}

	private String KeyNumber(String key) {
		Map<String, String> keyMap = new HashMap<String, String>();
		keyMap.put("keyPower", "26");
		keyMap.put("keyMenu", "82");
		keyMap.put("KeyBack", "4");
		keyMap.put("keyHome", "3");

		return keyMap.get(key);
	}

	public void KeyPress(String keyWord) {
		this.keyWord = keyWord;
		String myKeyNumber = KeyNumber(this.keyWord);

		try {
			String cmdStr = "adb -s " + this.SN + " shell input keyevent " + myKeyNumber;
			Runtime.getRuntime().exec(cmdStr);
			
			System.out.println(this.SN + ":");
			System.out.println("    KeyWord: " + keyWord);
		} catch (Exception e) {
			System.out.print("---------error---------");
		}
	}

	public void ScreenTap(String keyXs, String keyYs) {
		this.keyXs = keyXs;
		this.keyYs = keyYs;
		
		try {
			String cmdStr = "adb -s " + this.SN + " shell input tap " + this.keyXs + " " + this.keyYs;
			Runtime.getRuntime().exec(cmdStr);
			
			System.out.println(this.SN + ":");
			System.out.println("    Tap: [" + this.keyXs + ", " + this.keyYs + "]");
		} catch (Exception e) {
			System.out.print("---------error---------");
		}
	}

	public void ScreenSwipe(String keyXs, String keyYs, String keyXe, String keyYe) {
		this.keyXs = keyXs;
		this.keyYs = keyYs;
		this.keyXe = keyXe;
		this.keyYe = keyYe;
		
		try {
			String cmdStr = "adb -s " + this.SN + " shell input swipe " + this.keyXs + " " + this.keyYs + " " + this.keyXe + " " + this.keyYe;
			Runtime.getRuntime().exec(cmdStr);

			System.out.println(this.SN + ":");
			System.out.println("    Swipe: [" + this.keyXs + ", " + this.keyYs + "] -> [" + this.keyXe + ", " + this.keyYe + "]");
		} catch (Exception e) {
			System.out.print("---------error---------");
		}
	}

	public void KeyPressUser(String keyWord) {
		this.keyWord = keyWord;
		String[] myKeyNum = new String[4];

		if (this.keyWord.equals("keyMenu")) {
			myKeyNum[0] = "1 254 1";
			myKeyNum[1] = "0 0 0";
			myKeyNum[2] = "1 254 0";
			myKeyNum[3] = "0 0 0";
		}

		for (int i = 0; i < myKeyNum.length; i++) {
			String cmdStr = "adb -s " + SN + " shell sendevent /dev/input/event2 " + myKeyNum[i];

			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {
				Runtime.getRuntime().exec(cmdStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println(this.SN + ":");
		System.out.println("    KeyWord: " + keyWord);
	}

}
