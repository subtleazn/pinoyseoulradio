package com.pinoyseoul.radio.settings;

public interface Configs {

    /// SINGLE Radio setting
    Boolean SINGLE = false;
    // Autostart
    Boolean AUTOSTART = true;
    /// ads setting
    Boolean ADS_OFF = true;

    /// RADIO STREAM
    String R_320 = "https://sungalaxy.stream.laut.fm/sungalaxy"; // use this if need one channel
    String R_192 = "https://sungalaxy.stream.laut.fm/sungalaxy";
    String R_64  = "https://sungalaxy.stream.laut.fm/sungalaxy";

    /// web setting
    String W_FB = "https://www.facebook.com/fatboyslim";
    String COVER = "https://www.designformusic.com/wp-content/uploads/2019/06/Ear-Delicious-1000x1000.jpg";

    //// - - - - - NOT CHANGE - - - - - !!!! /////

    String APPNAME = "Simple";
    String TITLE_RADIO = "title";
    String COWNTDOWN = "count";
    String FOLDER_REC = "/SimpleRadio/";
    String SETTINGS_STREAM_QUALITY = "stream";
    String SETTINGS_BLUR_RADIUS = "blur";
    String COVER_RADIO = "cover";
    boolean RESUME_RADIO_ON_PHONE_CALL = true;


}
