package rock.delta2.telegramtransport;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;



public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView logo  = (ImageView) findViewById(R.id.ivLogo);
       // logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));

        Intent returnIntent = new Intent();
        setResult(AppCompatActivity.RESULT_CANCELED, returnIntent);
    }

    public void onClick(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }




}
