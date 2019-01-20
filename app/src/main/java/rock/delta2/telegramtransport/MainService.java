package rock.delta2.telegramtransport;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import rock.delta2.telegramtransport.Mediator.MediatorMD;
import rock.delta2.telegramtransport.Transport.TelegramTransport;


public class MainService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        startForeground(R.drawable.ic_notify_proc, "dropbox transport", 4523);

        MediatorMD.setTransport(new TelegramTransport(this));

        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public void onDestroy() {
        MediatorMD.onDestroy();
    }

    protected  void startForeground(int ico, String title, int notifyId) {

            Notification.Builder builder = new Notification.Builder(this)
                    .setSmallIcon(ico)
                    .setContentTitle(title)
                    .setContentText("")
                    .setOnlyAlertOnce(true)
                    .setOngoing(true);
            Notification notification;

            notification = builder.build();

            notification.contentIntent = PendingIntent.getActivity(this,
                    0, new Intent(getApplicationContext(), MainActivity.class)
                    , PendingIntent.FLAG_UPDATE_CURRENT);


            startForeground(notifyId, notification);
    }
}
