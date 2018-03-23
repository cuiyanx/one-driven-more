package AndroidDevices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DeviceListener {

	private static Process process;
	private String cmdStr;
	public String strCommands;

	public DeviceListener(String SN) {
		StaticData.setListenerSN(SN);
	}

	public void Start() {
		System.out.println("Start to listen: " + StaticData.ListenerSN);

		this.cmdStr = "adb -s " + StaticData.ListenerSN + " shell getevent -l";

		try {
			process = Runtime.getRuntime().exec(this.cmdStr);
		} catch (IOException e) {
			System.err.println("Create process error...\n" + e);
			System.exit(1);
		}

		InputStream inStreams = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inStreams));
		DataHanding DataHanding = new DataHanding();

		try {
			while ((this.strCommands = br.readLine()) != null) {
				String[] strCommandsArray = this.strCommands.split("\\s+");
				String type = null;
				String code = null;
				String value = null;

				if (strCommandsArray.length > 3) {
					if (strCommandsArray[1].equals("EV_ABS") && strCommandsArray[2].equals("ABS_MT_TRACKING_ID")) {
						type = strCommandsArray[1];
						code = strCommandsArray[2];
						value = strCommandsArray[3];
					}

					if (strCommandsArray[1].equals("EV_ABS") && strCommandsArray[2].equals("ABS_MT_POSITION_X")) {
						type = strCommandsArray[1];
						code = strCommandsArray[2];
						value = strCommandsArray[3];
					}

					if (strCommandsArray[1].equals("EV_ABS") && strCommandsArray[2].equals("ABS_MT_POSITION_Y")) {
						type = strCommandsArray[1];
						code = strCommandsArray[2];
						value = strCommandsArray[3];
					}

					if (strCommandsArray[1].equals("EV_KEY") && strCommandsArray[2].equals("BTN_TOUCH")) {
						type = strCommandsArray[1];
						code = strCommandsArray[2];
						value = strCommandsArray[3];
					}

					if (strCommandsArray[1].equals("EV_KEY") && strCommandsArray[2].equals("KEY_BACK")) {
						type = strCommandsArray[1];
						code = strCommandsArray[2];
						value = strCommandsArray[3];
					}

					if (strCommandsArray[1].equals("EV_KEY") && (strCommandsArray[2].equals("KEY_MENU") || strCommandsArray[2].equals("00fe"))) {
						type = strCommandsArray[1];
						code = strCommandsArray[2];
						value = strCommandsArray[3];
					}

					if (strCommandsArray[1].equals("EV_KEY") && (strCommandsArray[2].equals("KEY_HOME") || strCommandsArray[2].equals("KEY_HOMEPAGE"))) {
						type = strCommandsArray[1];
						code = strCommandsArray[2];
						value = strCommandsArray[3];
					}

					if ((type != null) && (code != null) && (value != null)) {
						DataHanding.ProcessingData(type, code, value);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		process.destroy();

		try {
			process.waitFor();
		} catch (InterruptedException e) {
		}

		System.exit(0);
	}

}
