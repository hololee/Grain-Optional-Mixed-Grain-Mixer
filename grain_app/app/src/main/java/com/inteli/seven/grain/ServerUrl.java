package com.inteli.seven.grain;

public class ServerUrl {

    final static String downloadStateUrl = "http://lccandol.cafe24.com/download_state.php?id_num=2018";
    final static String downloadGrainUrl = "http://lccandol.cafe24.com/download_grain.php?id_num=2018";
    final static String updateStateUrl = "http://lccandol.cafe24.com/state_upload.php?id_num=2018&state=0"; //2->0 로 변환.
    final static String startWorkingUrl = "http://lccandol.cafe24.com/state_working.php?id_num=2018&state=1&data=";

    public static String getDownloadGrainUrl() {
        return downloadGrainUrl;
    }

    public static String getDownloadStateUrl() {
        return downloadStateUrl;
    }

    public static String getUpdateStateUrl() {
        return updateStateUrl;
    }

    public static String getStartWorkingUrl(String data) {
        return startWorkingUrl + data;
    }
}


