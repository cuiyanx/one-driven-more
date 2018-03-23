package AndroidDevices;

public class StaticData {

	static public String[] PhoneSNList = null;
	static private int[] PhoneResolutionX = null;
	static private int[] PhoneResolutionY = null;
	static public String ListenerSN = null;
	static public int ListenerResolutionX = 0;
	static public int ListenerResolutionY = 0;
	static public float ListenerRatioX = 0;
	static public float ListenerRatioY = 0;
	static public String keyXs = null;
	static public String keyYs = null;
	static public String keyXe = null;
	static public String keyYe = null;

	static {
		Getdata();
	}

	synchronized static public void Getdata() {
		System.out.println("Scan phones....");
		
		DeviceParameters DPs = new DeviceParameters();
		PhoneSNList = DPs.GetPhoneSNList();
		PhoneResolutionX = new int[PhoneSNList.length];
		PhoneResolutionY = new int[PhoneSNList.length];

		for (int i = 0; i < PhoneSNList.length; i++) {
			System.out.println("    SN[" + i + "]: " + PhoneSNList[i]);

			int[] XYer = DPs.GetPhoneResolution(PhoneSNList[i]);
			PhoneResolutionX[i] = XYer[0];
			PhoneResolutionY[i] = XYer[1];
			System.out.println("    Resolution[" + i + "]: " + XYer[0] + "*" + XYer[1]);
			
			int[] LWer = DPs.GetPhoneLW(PhoneSNList[i]);
			System.out.println("    LW[" + i + "]: " + LWer[0] + "*" + LWer[1]);
			
			int[] Version = DPs.GetPhoneVersion(PhoneSNList[i]);
			System.out.println("    Version[" + i + "]: " + Version[0] + "." + Version[1] + "." + Version[2] + "\n");
		}
	}

	public static int getResolutionX(String SN) {
		for (int i = 0; i < PhoneSNList.length; i++) {
			if (SN.equals(PhoneSNList[i])) {
				return PhoneResolutionX[i];
			}
		}
		
		return 0 ;
	}

	public static int getResolutionY(String SN) {
		for (int i = 0; i < PhoneSNList.length; i++) {
			if (SN.equals(PhoneSNList[i])) {
				return PhoneResolutionY[i];
			}
		}
		
		return 0 ;
	}

	public static void setListenerSN(String SN) {
		ListenerSN = SN;
		DeviceParameters DPs = new DeviceParameters();

		for (int i = 0; i < PhoneSNList.length; i++) {
			if (PhoneSNList[i].equals(ListenerSN)) {
				int[] XYer = DPs.GetPhoneResolution(PhoneSNList[i]);
				ListenerResolutionX = XYer[0];
				ListenerResolutionY = XYer[1];
			}
		}
	}

	public static void setListenerRatioX(int x) {
		ListenerRatioX = (float) (Math.round(((float) x / ListenerResolutionX) * 10000)) / 10000;
	}

	public static void setListenerRatioY(int y) {
		ListenerRatioY = (float) (Math.round(((float) y / ListenerResolutionY) * 10000)) / 10000;
	}

}
