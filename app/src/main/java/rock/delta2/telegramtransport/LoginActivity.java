package rock.delta2.telegramtransport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import rock.delta2.telegramtransport.Mediator.MediatorMD;
import rock.delta2.telegramtransport.Preferences.PreferencesHelper;


public class LoginActivity extends AppCompatActivity {
    public final static String _LOGIN_PARAM = "LOGIN_PARAM";
    public final static String _PARAM_PHONE = "_PARAM_PHONE";
    public final static String _PARAM_TCODE = "_PARAM_CODE_TELEGRAM";
    public final static String _PARAM_UCODE = "_PARAM_CODE_USER";
    public final static String _PARAM_PASSWORD = "_PARAM_PASSWORD";
    public final static String _PARAM_UCODE_VAL = "_PARAM_CODE_USER_VAL";
    public final static String _PARAM_PASSHINT_VAL = "_PARAM_PASSHINT_VAL";


    EditText edPhone;
    EditText edCode;
    EditText edPass;

    Button btNext1;
    Button btNext2;
    Button btPass;

    LinearLayout llPhone;
    LinearLayout llCode;
    LinearLayout llCodeUser;
    LinearLayout llPassword;

    static LoginActivity _LoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _LoginActivity = this;

        setContentView(R.layout.activity_login);

        ImageView logo  = (ImageView) findViewById(R.id.ivLogo);
        logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));

        edPhone = (EditText)findViewById(R.id.etPhoneNum);
        edCode = (EditText)findViewById(R.id.etCode);
        edPass = (EditText)findViewById(R.id.edPass);
        btNext1 = (Button)findViewById(R.id.btNext1);
        btNext2 = (Button)findViewById(R.id.btNext2);

        edPhone.setText(PreferencesHelper.getPhoneNum());

        llPhone = (LinearLayout)findViewById(R.id.llPhone);
        llCode = (LinearLayout)findViewById(R.id.llCode);
        llCodeUser = (LinearLayout)findViewById(R.id.llCodeUser);
        llPassword = (LinearLayout)findViewById(R.id.llPassword);


        Intent intent = getIntent();
        String mode = intent.getStringExtra(_LOGIN_PARAM);


        if(mode.equals(_PARAM_PHONE)){
            llCode.setVisibility(View.GONE);
            llCodeUser.setVisibility(View.GONE);
            llPassword.setVisibility(View.GONE);

            ((TextView)findViewById(R.id.tvRegInfo)).setText(getText(R.string.phone_need));
        }
        else if(mode.equals(_PARAM_TCODE)){
            llPhone.setVisibility(View.GONE);
            llCodeUser.setVisibility(View.GONE);
            llPassword.setVisibility(View.GONE);

            ((TextView)findViewById(R.id.tvRegInfo)).setText(getText(R.string.code_need));
        }
        else if(mode.equals(_PARAM_UCODE)){
            llPhone.setVisibility(View.GONE);
            llCode.setVisibility(View.GONE);
            btNext1.setVisibility(View.GONE);
            btNext2.setVisibility(View.GONE);
            llPassword.setVisibility(View.GONE);

            ((TextView)findViewById(R.id.chkCode)).setText(intent.getStringExtra(_PARAM_UCODE_VAL));
            ((TextView)findViewById(R.id.tvRegInfo)).setText(getText(R.string.code_need));
        }
        else if(mode.equals(_PARAM_PASSWORD)){
            llPhone.setVisibility(View.GONE);
            llCode.setVisibility(View.GONE);
            llCodeUser.setVisibility(View.GONE);

            ((TextView)findViewById(R.id.tvRegInfo)).setText(intent.getStringExtra(_PARAM_PASSHINT_VAL));
        }
    }


    public void ConnectClick(View v){
        PreferencesHelper.SetPhoneNum(edPhone.getText().toString());
        PreferencesHelper.code = edCode.getText().toString();

        MediatorMD.getTransport().connect();
    }

    public void PasswordClick(View v){

        PreferencesHelper.pass = edPass.getText().toString();

        MediatorMD.getTransport().connect();
    }

    public static void close(){
        if(_LoginActivity != null)
            _LoginActivity.finish();
    }
}
