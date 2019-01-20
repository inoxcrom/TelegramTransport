package rock.delta2.telegramtransport.Preferences;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesHelper {

    public static final String APP_PREFERENCES = "preference";

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String DEVICE_NAME = "DEVICE_NAME";
    public static final String AUTO_START = "AUTO_START";


    private static SharedPreferences mSettings;
    private static PreferenceValue _autoStart;
    private static PreferenceValue _accesToken;
    private static PreferenceValue _deviceName;


    public static void init(Context context) {
        mSettings = context.getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE);

        _accesToken = new PreferenceValue(mSettings, ACCESS_TOKEN, "");
        _deviceName = new PreferenceValue(mSettings, DEVICE_NAME, "");
        _autoStart  = new PreferenceValue(mSettings, AUTO_START, true);


    }

    //region ACCESS_TOKEN
    public static void setToken(String val) {
        _accesToken.setStr(val);
    }

    public static String getToken() {
        return _accesToken.getStr();
    }
    //endregion ACCESS_TOKEN

    //region DEVICE_NAME
    public static void setDeviceName(String val) {
        _deviceName.setStr(val);
    }

    public static String getDeviceName() {
        return _deviceName.getStr();
    }
    //endregion DEVICE_NAME


    //region AUTO_START
    public static void setAutoStart(boolean val) {
        _autoStart.setBool(val);
    }

    public static boolean getAutoStart() {
        return _autoStart.getBool();
    }
    //endregion AUTO_START
}
