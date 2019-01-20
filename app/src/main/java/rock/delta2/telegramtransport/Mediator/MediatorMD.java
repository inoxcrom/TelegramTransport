package rock.delta2.telegramtransport.Mediator;


public class MediatorMD {

    //region ITransport
    private static ITransport _Transport;

    public static void setTransport(ITransport t){
        _Transport = t;
    }

    public static void sendText(String replMsgId, String txt){
        if(_Transport != null)
            _Transport.sendTxt(replMsgId, txt);
    }

    public static void sendPhoto(String replMsgId, String file, String caption){
        if(_Transport != null)
            _Transport.sendPhoto(replMsgId, file, caption);
    }

    public static void sendFile(String replMsgId, String file){
        if(_Transport != null)
            _Transport.sendFile(replMsgId, file);
    }


    //endregion ITransport


    public static void onDestroy(){
        _Transport = null;

    }

}

