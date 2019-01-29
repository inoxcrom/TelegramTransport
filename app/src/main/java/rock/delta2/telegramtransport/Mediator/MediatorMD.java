package rock.delta2.telegramtransport.Mediator;


public class MediatorMD {

    //region ITransport
    private static ITransport _Transport;

    public static void setTransport(ITransport t){
        _Transport = t;
    }


    public static ITransport getTransport(){
        return  _Transport ;
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

    public static void sendLocation(String replMsgId, String lat, String lon){
        if(_Transport != null)
            _Transport.sendLocation (replMsgId, lat, lon);
    }


    //endregion ITransport


    //region ISender
    private static ISender _Sender;

    public static void setSender(ISender s){
        _Sender = s;
    }

    public static void sendCommand(String replMsgId, String cmd){
        if(_Sender != null)
            _Sender.sendCommand(replMsgId, cmd);
    }

    //endregion ISender


    //region show dlg
    private static IDialogShow m_DialogShow;
    public static void registerDialogShow(IDialogShow val){
        m_DialogShow = val;
    }
    public static void showDialog(String capt, String msg){
        if(m_DialogShow != null)
            m_DialogShow.showDialog(capt, msg);
    }
    //endregion show dlg

    //region  ICommandCheckMessage

    private static ICommandCheckMessage _ICommandCheckMessage;

    static public void registerCommandCheckMessage(ICommandCheckMessage val){
        _ICommandCheckMessage = val;
    }

    static public void CheckMessage(String inTxt, boolean repeatCmd, boolean isSilent, String msgId){
        if(_ICommandCheckMessage != null)
            _ICommandCheckMessage.CheckMessage(inTxt, repeatCmd, isSilent, msgId);
    }
    static public void CheckMessage(String inTxt, String msgId){
        if(_ICommandCheckMessage != null)
            _ICommandCheckMessage.CheckMessage(inTxt, msgId);
    }

    //endregion  ICommandCheckMessage

    public static void onDestroy(){
        _Transport = null;
        _Sender = null;

    }

}

