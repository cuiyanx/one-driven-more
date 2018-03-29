package AndroidDevices;

public class DataHanding {

    private DeviceEvents DeviceEvents;
    private String SN;
    private String Xtemp;
    private String Ytemp;
    private boolean Touchflag = false;

    public DataHanding(String SN) {
        DeviceEvents = new DeviceEvents();
        this.SN = SN;
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
            float RatioXs = StaticData.getRatioX(this.SN, Xs);
            float RatioYs = StaticData.getRatioY(this.SN, Ys);
            float RatioXe = StaticData.getRatioX(this.SN, Xe);
            float RatioYe = StaticData.getRatioY(this.SN, Ye);

            System.out.println("\n" + this.SN + ":");
            System.out.println("    [" + Xs + ", " + Ys + "] -> [" + Xe + ", " + Ye + "]");

            for (int i = 0; i < StaticData.DeviceSNList.length; i++) {
                if (!StaticData.DeviceSNList[i].equals(this.SN)) {
                    int ResolutionX = StaticData.DeviceResolutionX[i];
                    int ResolutionY = StaticData.DeviceResolutionY[i];
                    int count = i;

                    if (((Xs - 10) <= Xe && Xe <= (Xs + 10)) && ((Ys - 10) <= Ye && Ye <= (Ys + 10))) {
                        StaticData.keyXs = Integer.toString((int) (ResolutionX * RatioXs));
                        StaticData.keyYs = Integer.toString((int) (ResolutionY * RatioYs));

                        new Thread() {
                            public void run() {
                                DeviceEvents.ScreenTap(count, StaticData.keyXs, StaticData.keyYs);
                            }
                        }.start();
                    } else {
                        StaticData.keyXs = Integer.toString((int) (ResolutionX * RatioXs));
                        StaticData.keyYs = Integer.toString((int) (ResolutionY * RatioYs));
                        StaticData.keyXe = Integer.toString((int) (ResolutionX * RatioXe));
                        StaticData.keyYe = Integer.toString((int) (ResolutionY * RatioYe));

                        new Thread() {
                            public void run() {
                                DeviceEvents.ScreenSwipe(count, StaticData.keyXs, StaticData.keyYs, StaticData.keyXe, StaticData.keyYe);
                            }
                        }.start();
                    }
                }
            }
        }
    }

    private void CopyKeyOperation(String code) {
        if (code.equals("KEY_BACK"))
            StaticData.keyWord = "KeyBack";
        else if (code.equals("KEY_MENU") || code.equals("00fe"))
            StaticData.keyWord = "keyMenu";
        else if (code.equals("KEY_HOME") || code.equals("KEY_HOMEPAGE"))
            StaticData.keyWord = "keyHome";
        else if (code.equals("KEY_POWER"))
            StaticData.keyWord = "keyPower";
        else if (code.equals("KEY_VOLUMEDOWN"))
            StaticData.keyWord = "keyVolumeDown";
        else if (code.equals("KEY_VOLUMEUP"))
            StaticData.keyWord = "keyVolumeUp";

        System.out.println("\n" + this.SN + ":");
        System.out.println("    KeyWord: " + StaticData.keyWord);

        for (int i = 0; i < StaticData.DeviceSNList.length; i++) {
            if (!StaticData.DeviceSNList[i].equals(this.SN)) {
                int count = i;

                new Thread() {
                    public void run() {
                        DeviceEvents.KeyPress(count, StaticData.keyWord);
                    }
                }.start();
            }
        }
    }

    public void ProcessingData(String type, String code, String value) {
        if ((type.equals("EV_ABS") && code.equals("ABS_MT_TRACKING_ID")) || (type.equals("EV_KEY") && code.equals("BTN_TOUCH"))) {
            if ((value.equals("ffffffff")) || (value.equals("UP"))) {
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
            if (type.equals("EV_ABS") && code.equals("ABS_MT_POSITION_X")) {
                if (StaticData.keyXs == null) {
                    StaticData.keyXs = value;
                } else {
                    StaticData.keyXe = value;
                }
            }

            if (type.equals("EV_ABS") && code.equals("ABS_MT_POSITION_Y")) {
                if (StaticData.keyYs == null) {
                    StaticData.keyYs = value;
                } else {
                    StaticData.keyYe = value;
                }
            }
        } else {
            if (type.equals("EV_ABS") && code.equals("ABS_MT_POSITION_X"))
                this.Xtemp = value;

            if (type.equals("EV_ABS") && code.equals("ABS_MT_POSITION_Y"))
                this.Ytemp = value;
        }

        if (type.equals("EV_KEY")) {
            if (code.equals("KEY_BACK") || (code.equals("KEY_MENU") || code.equals("00fe")) || (code.equals("KEY_HOME") || code.equals("KEY_HOMEPAGE"))
                    || code.equals("KEY_POWER") || code.equals("KEY_VOLUMEDOWN") || code.equals("KEY_VOLUMEUP")) {
                if (value.equals("ffffffff") || value.equals("UP")) {
                    CopyKeyOperation(code);
                }
            }
        }
    }

}
