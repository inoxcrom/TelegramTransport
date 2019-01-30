package rock.delta2.telegramtransport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import rock.delta2.telegramtransport.Preferences.PreferencesHelper;

public class MainActivity extends AppCompatActivity {


    CheckBox cbText;
    CheckBox cbPhoto;
    CheckBox cbFile;
    CheckBox cbLocation;
    CheckBox cbAutoStart;
    Button btClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbText  = findViewById(R.id.cbText);
        cbFile  = findViewById(R.id.cbFile);
        cbPhoto = findViewById(R.id.cbPhoto);
        cbLocation = findViewById(R.id.cbLocation);
        cbAutoStart = findViewById(R.id.cbAutoStart);
        btClose = findViewById(R.id.btClose);

        init();
    }

    private void init(){
        cbText.setChecked(PreferencesHelper.getSendText());
        cbFile.setChecked(PreferencesHelper.getSendFile());
        cbPhoto.setChecked(PreferencesHelper.getSendPhoto());
        cbAutoStart.setChecked(PreferencesHelper.getAutoStart());
        cbLocation.setChecked(PreferencesHelper.getSendLocation());
    }

    public void onClick(View view) {
        if (view.equals(cbText))
            PreferencesHelper.setSendText(cbText.isChecked());
        else if (view.equals(cbFile))
            PreferencesHelper.setSendFile(cbFile.isChecked());
        else if (view.equals(cbPhoto))
            PreferencesHelper.setSendPhoto(cbPhoto.isChecked());
        else if (view.equals(cbAutoStart))
            PreferencesHelper.setAutoStart(cbAutoStart.isChecked());
        else if (view.equals(cbLocation))
        PreferencesHelper.setSendLocation(cbLocation.isChecked());
    }

    public void onCloseClick(View view) {
        stopService(new Intent(this, MainService.class));
        finish();
    }
}
