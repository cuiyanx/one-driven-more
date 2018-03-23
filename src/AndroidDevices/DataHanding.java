package AndroidDevices;

public class DataHanding {

	private String type;
	private String code;
	private String value;
	private String Xtemp;
	private String Ytemp;
	private boolean Touchflag = false;

	public DataHanding() {
	}

	private void InitPointXY() {
		if (this.Touchflag == false) {
			StaticData.keyXs = null;
			StaticData.keyYs = null;
			StaticData.keyXe = null;
			StaticData.keyYe = null;
			this.Touchflag = true;
		}
	}

	private void CopyScreenOperation() {			
		if (StaticData.keyXs != null && StaticData.keyYs != null) {
			if (StaticData.keyXe == null || StaticData.keyYe == null) {
				StaticData.keyXe = StaticData.keyXs;
				StaticData.keyYe = StaticData.keyYs;
			}

			int Xs = Integer.parseInt(StaticData.keyXs, 16);
			int Xe = Integer.parseInt(StaticData.keyXe, 16);
			int Ys = Integer.parseInt(StaticData.keyYs, 16);
			int Ye = Integer.parseInt(StaticData.keyYe, 16);

			System.out.println("Listener:");
			System.out.println("    [" + Xs + ", " + Ys + "] -> [" + Xe + ", " + Ye + "]");

			for (int i = 0; i < StaticData.PhoneSNList.length; i++) {
				if (!StaticData.PhoneSNList[i].equals(StaticData.ListenerSN)) {
					int XR = StaticData.getResolutionX(StaticData.PhoneSNList[i]);
					int YR = StaticData.getResolutionY(StaticData.PhoneSNList[i]);
					
					if (((Xs - 10) <= Xe && Xe <= (Xs + 10)) && ((Ys - 10) <= Ye && Ye <= (Ys + 10))) {
						StaticData.setListenerRatioX(Xs);
						StaticData.setListenerRatioY(Ys);
						StaticData.keyXs = Integer.toString((int)(XR * StaticData.ListenerRatioX));
						StaticData.keyYs = Integer.toString((int)(YR * StaticData.ListenerRatioY));

						DeviceEvents DeviceEvents = new DeviceEvents(StaticData.PhoneSNList[i]);
						DeviceEvents.ScreenTap(StaticData.keyXs, StaticData.keyYs);
					} else {
						StaticData.setListenerRatioX(Xs);
						StaticData.setListenerRatioY(Ys);
						StaticData.keyXs = Integer.toString((int)(XR * StaticData.ListenerRatioX));
						StaticData.keyYs = Integer.toString((int)(YR * StaticData.ListenerRatioY));
						StaticData.setListenerRatioX(Xe);
						StaticData.setListenerRatioY(Ye);
						StaticData.keyXe = Integer.toString((int)(XR * StaticData.ListenerRatioX));
						StaticData.keyYe = Integer.toString((int)(YR * StaticData.ListenerRatioY));

						DeviceEvents DeviceEvents = new DeviceEvents(StaticData.PhoneSNList[i]);
						DeviceEvents.ScreenSwipe(StaticData.keyXs, StaticData.keyYs, StaticData.keyXe, StaticData.keyYe);
					}
				}
			}
			
			System.out.println("");
		}
	}

	private void CopyKeyOperation(String code) {
		this.code = code;
		String keyWord = null;

		if (this.code.equals("KEY_BACK"))
			keyWord = "KeyBack";

		if (this.code.equals("KEY_MENU") || this.code.equals("00fe"))
			keyWord = "keyMenu";

		if (this.code.equals("KEY_HOME") || this.code.equals("KEY_HOMEPAGE"))
			keyWord = "keyHome";

		System.out.println("Listener:");
		System.out.println("    KeyWord: " + keyWord);

		for (int i = 0; i < StaticData.PhoneSNList.length; i++) {
			if (!StaticData.PhoneSNList[i].equals(StaticData.ListenerSN)) {
				DeviceEvents DeviceEvents = new DeviceEvents(StaticData.PhoneSNList[i]);

				if (StaticData.PhoneSNList[i].equals("33004ba0a76db253")) {
					DeviceEvents.KeyPressUser(keyWord);

					return;
				}

				DeviceEvents.KeyPress(keyWord);
			}
		}

		System.out.println("");
	}

	public void ProcessingData(String type, String code, String value) {
		this.type = type;
		this.code = code;
		this.value = value;

		if ((this.type.equals("EV_ABS") && this.code.equals("ABS_MT_TRACKING_ID")) || (this.type.equals("EV_KEY") && this.code.equals("BTN_TOUCH"))) {
			if ((this.value.equals("ffffffff")) || (this.value.equals("UP"))) {
				if (this.Touchflag == true) {
					this.Touchflag = false;

					if ((StaticData.keyXs == null) || (StaticData.keyYs == null)) {
						StaticData.keyXs = this.Xtemp;
						StaticData.keyYs = this.Ytemp;
					}

					CopyScreenOperation();
				}
			} else {
				InitPointXY();
			}
		}

		if (this.Touchflag == true) {
			if (this.type.equals("EV_ABS") && this.code.equals("ABS_MT_POSITION_X")) {
				if (StaticData.keyXs == null) {
					StaticData.keyXs = this.value;
				} else {
					StaticData.keyXe = this.value;
				}
			}

			if (this.type.equals("EV_ABS") && this.code.equals("ABS_MT_POSITION_Y")) {
				if (StaticData.keyYs == null) {
					StaticData.keyYs = this.value;
				} else {
					StaticData.keyYe = this.value;
				}
			}
		} else {
			if (this.type.equals("EV_ABS") && this.code.equals("ABS_MT_POSITION_X"))
				this.Xtemp = this.value;

			if (this.type.equals("EV_ABS") && this.code.equals("ABS_MT_POSITION_Y"))
				this.Ytemp = this.value;
		}

		if (this.type.equals("EV_KEY") && this.code.equals("KEY_BACK")) {
			if (this.value.equals("UP"))
				CopyKeyOperation(this.code);
		}

		if (this.type.equals("EV_KEY") && (this.code.equals("KEY_MENU") || this.code.equals("00fe"))) {
			if (this.value.equals("UP"))
				CopyKeyOperation(this.code);
		}

		if (this.type.equals("EV_KEY") && (this.code.equals("KEY_HOME") || this.code.equals("KEY_HOMEPAGE"))) {
			if (this.value.equals("UP"))
				CopyKeyOperation(this.code);
		}
	}

}
