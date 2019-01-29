package rock.delta2.telegramtransport;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import rock.delta2.telegramtransport.Mediator.IDialogShow;
import rock.delta2.telegramtransport.Mediator.MediatorMD;
import rock.delta2.telegramtransport.Transport.Sender;
import rock.delta2.telegramtransport.Transport.TelegramTransport;


public class MainService extends Service implements IDialogShow {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        startForeground(R.drawable.ic_notify_proc, "telegram transport", 7512);

        MediatorMD.registerDialogShow(this);

        MediatorMD.setTransport(new TelegramTransport(this));
        MediatorMD.setSender(new Sender(this));

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

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

    @Override
    public void showDialog(String capt, String msg) {
        try {
            AlertDialog.Builder b = new AlertDialog.Builder(this)
                    .setTitle(capt)
                    .setMessage(msg)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog dialog = b.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();


        }catch (Exception e){
            Helper.Ex2Log(e);
        }
    }
}
