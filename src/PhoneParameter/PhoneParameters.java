package PhoneParameter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PhoneParameters {

	private String SN = null;
	private Process process = null;
	
	public PhoneParameters() {
	}
	
	public String[] GetPhoneSNList() {
		String strCommands;
		String cmdStr = "cmd /c adb devices";
		String[] phoneSNsTmp = new String[50];
		int SNCountTmp = 0;
		
		try {
			this.process = Runtime.getRuntime().exec(cmdStr);
		} catch (IOException e) {
			System.err.println("Create process error...\n" + e);
			System.exit(1);
		}

		InputStream inStreams = this.process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inStreams));
		
		try {
			while ((strCommands = br.readLine()) != null) {
				String[] strCommandsArray = strCommands.split("\\s+");
				
				if (strCommandsArray.length > 1) {
					if (strCommandsArray[1].equals("device")) {
						phoneSNsTmp[SNCountTmp] = strCommandsArray[0];
						SNCountTmp = SNCountTmp + 1;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] phoneSNs = new String[SNCountTmp];
		int SNCount = 0;
		
		for (int i = 0; i < phoneSNsTmp.length; i++) {
			if (phoneSNsTmp[i] != null) {
				phoneSNs[SNCount] = phoneSNsTmp[i];
				SNCount = SNCount + 1;
			}
		}

		return phoneSNs;
	}
	
	public int[] GetPhoneLW(String SN) {
		this.SN = SN;
		this.process = null;
		String strCommands;
		int[] XY = new int[2];
		String cmdStr = "cmd /c adb -s " + this.SN + " shell getevent -p";
		
		try {
			process = Runtime.getRuntime().exec(cmdStr);
		} catch (IOException e) {
			System.err.println("Create process error...\n" + e);
			System.exit(1);
		}

		InputStream inStreams = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inStreams));
		
		try {
			while ((strCommands = br.readLine()) != null) {
				String[] strCommandsArray = strCommands.split("\\s+");

				if (strCommandsArray.length > 1) {
					if ((strCommandsArray[1].equals("0035")) && (strCommandsArray[5].equals("min")) && (strCommandsArray[7].equals("max"))) {
						String[] strArrayX = strCommandsArray[8].split("\\,");
						XY[0] = Integer.parseInt(strArrayX[0]);
					}
					
					if ((strCommandsArray[1].equals("0036")) && (strCommandsArray[5].equals("min")) && (strCommandsArray[7].equals("max"))) {
						String[] strArrayY = strCommandsArray[8].split("\\,");
						XY[1] = Integer.parseInt(strArrayY[0]);

						break;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return XY;
	}
	
	public int[] GetPhoneResolution(String SN) {
		this.SN = SN;
		this.process = null;
		String strCommands;
		int[] XY = new int[2];
		String cmdStr = "cmd /c adb -s " + this.SN + " shell wm size";
		
		try {
			process = Runtime.getRuntime().exec(cmdStr);
		} catch (IOException e) {
			System.err.println("Create process error...\n" + e);
			System.exit(1);
		}

		InputStream inStreams = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inStreams));
		
		try {
			while ((strCommands = br.readLine()) != null) {
				String[] strCommandsArray = strCommands.split("\\s+");
				
				if ((strCommandsArray[0].equals("Physical")) && (strCommandsArray[1].equals("size:"))) {
					String[] strArrayX = strCommandsArray[2].split("x");
					XY[0] = Integer.parseInt(strArrayX[0]);
					XY[1] = Integer.parseInt(strArrayX[1]);
					
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if ((XY[0] == 0) || (XY[1] == 0)) {
			int[] XYtemp = GetPhoneLW(this.SN);
			XY[0] = XYtemp[0];
			XY[1] = XYtemp[1];
		}
		
		return XY;
	}
	
	public int[] GetPhoneVersion(String SN) {
		this.SN = SN;
		this.process = null;
		int[] Version = new int[3];
		String strCommands;
		String cmdStr = "cmd /c adb -s " + this.SN + " shell getprop ro.build.version.release";
		
		try {
			process = Runtime.getRuntime().exec(cmdStr);
		} catch (IOException e) {
			System.err.println("Create process error...\n" + e);
			System.exit(1);
		}

		InputStream inStreams = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inStreams));
		
		try {
			while ((strCommands = br.readLine()) != null) {
				String[] strCommandsArray = strCommands.split("\\.");
				
				for (int i = 0; i < strCommandsArray.length; i++) {
					System.out.println("strCommandsArray[" + i + "]:" + strCommandsArray[i]);
				}
				
				Version[0] = Integer.parseInt(strCommandsArray[0]);
				Version[1] = Integer.parseInt(strCommandsArray[1]);
				Version[2] = Integer.parseInt(strCommandsArray[2]);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Version;
	}
}
