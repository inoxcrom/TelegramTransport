package rock.delta2.telegramtransport;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;


import rock.delta2.telegramtransport.Preferences.PreferencesHelper;

public class StarterApp extends AppCompatActivity {

    public final int _LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter_app);

        PreferencesHelper.init(this);

        Helper.setWorkDir(this.getFilesDir());

        checkAllPermission();
    }


    //region permision
    public void checkAllPermission()
    {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = this.checkCallingOrSelfPermission(permission);
        if (res != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        startApp();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0){
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        checkAllPermission();
                    else
                        finish();
                }
                return;
            }
        }
    }
    //endregion permision

    private void startApp(){
        startService(new Intent(this, MainService.class));

        finish();
    }

}
