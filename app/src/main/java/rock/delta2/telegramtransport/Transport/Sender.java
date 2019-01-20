package rock.delta2.telegramtransport.Transport;

import android.content.Context;
import android.content.Intent;

import rock.delta2.telegramtransport.Mediator.ISender;

public class Sender  implements ISender {

    Context context;

    public Sender(Context c){
        context = c;
    }

    public void sendCommand(String replMsgId, String cmd) {
        final Intent intent=new Intent();
        intent.setAction("rock.delta2.cmd");
        intent.putExtra("replMsgId",replMsgId);
        intent.putExtra("cmd",cmd);
        context.sendBroadcast(intent);
    }
}
