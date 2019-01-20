package rock.delta2.telegramtransport.Transport;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import rock.delta2.telegramtransport.BuildConfig;
import rock.delta2.telegramtransport.Helper;
import rock.delta2.telegramtransport.LoginActivity;
import rock.delta2.telegramtransport.Mediator.ITransport;
import rock.delta2.telegramtransport.Mediator.MediatorMD;
import rock.delta2.telegramtransport.Preferences.PreferencesHelper;
import rock.delta2.telegramtransport.Preferences.TelegramPreferences;

public class TelegramTransport implements ITransport , Client.ResultHandler, Client.ExceptionHandler{

    Context _context;


    Timer _Timer;
    OnlineTimertask _TimerTask;

    Client _client;

    boolean isWaitCode = false;
    boolean isWaitPhone = false;
    boolean isWaitPass = false;
    String passHint;


    long lastMsgId = 0;

    public TelegramTransport (Context c){

    }


    @Override
    public void onResult(TdApi.Object object) {
        Helper.Log("tdlib onResult", object.toString());

        if (object instanceof TdApi.UpdateAuthorizationState) {
            TdApi.UpdateAuthorizationState state = (TdApi.UpdateAuthorizationState) object;
            onResult(state.authorizationState);
            return;
        }

        //if(object instanceof TdApi.Un)

        if (object instanceof TdApi.AuthorizationStateWaitPhoneNumber && !isWaitPhone) {
            isWaitPhone = true;
            PreferencesHelper.SetPhoneNum("");
        //    _callback.onConnectedTransport(false);
        }
        else if (isWaitPhone && !PreferencesHelper.getPhoneNum().equals("")){
            isWaitPhone = false;
            send2t(new TdApi.SetAuthenticationPhoneNumber(
                    PreferencesHelper.getPhoneNum()
                    , false
                    , false));
        }
        else if (object instanceof TdApi.AuthorizationStateWaitCode && !isWaitCode) {
            isWaitCode = true;
            PreferencesHelper.code = "";
        //    _callback.onConnectedTransport(false);
        }
        else if (isWaitCode && !PreferencesHelper.code.equals("")){
            isWaitCode = false;
            send2t(new TdApi.CheckAuthenticationCode(PreferencesHelper.code,
                    PreferencesHelper.getPhoneNum(), ""));
        }

        else if (object instanceof TdApi.AuthorizationStateWaitPassword && !isWaitPass) {
            isWaitPass = true;
            PreferencesHelper.pass = "";

            TdApi.AuthorizationStateWaitPassword o = (TdApi.AuthorizationStateWaitPassword)object;

            passHint = o.passwordHint;
      //      _callback.onConnectedTransport(false);
        }
        else if(isWaitPass && !PreferencesHelper.pass.equals("")){
            isWaitPass = false;
            send2t(new TdApi.CheckAuthenticationPassword(PreferencesHelper.pass));

        }



        else if (object instanceof TdApi.AuthorizationStateWaitTdlibParameters) {
            TdApi.TdlibParameters parameters = new TdApi.TdlibParameters();
            parameters.databaseDirectory = Helper.getWorkDirpath();
            parameters.useMessageDatabase = true;
            parameters.apiId = TelegramPreferences.apiId;
            parameters.apiHash = TelegramPreferences.apiHash;
            parameters.deviceModel = Build.MODEL;
            parameters.systemLanguageCode = "en";
            parameters.systemVersion = Build.VERSION.CODENAME;
            parameters.applicationVersion = BuildConfig.VERSION_NAME;
            parameters.enableStorageOptimizer = true;

            send2t(new TdApi.SetTdlibParameters(parameters));

        } else if (object instanceof TdApi.AuthorizationStateWaitEncryptionKey) {
            send2t(new TdApi.CheckDatabaseEncryptionKey());

        }


        //  if(object instanceof TdApi.AuthorizationStateReady)
        //  {
        //      setConnectFlag();
        //  }


        else if (object instanceof TdApi.AuthorizationStateReady){
            if(PreferencesHelper.existsChatId())
                setConnectFlag();
            else
                registerUserStart();
        }

        else if(object instanceof TdApi.UpdateNewChat){
            if (!PreferencesHelper.existsChatId()) {
                setConnectFlag();
                TdApi.UpdateNewChat c = (TdApi.UpdateNewChat) object;
                PreferencesHelper.SetChatId(c.chat.id);
            }

        }


        else if(object instanceof TdApi.UpdateOption){
            TdApi.UpdateOption u = (TdApi.UpdateOption)object;
            if (u.name.equals("my_id")) {
                PreferencesHelper.SetMyId(((TdApi.OptionValueInteger) u.value).value);
            }
        }

        else if(object instanceof TdApi.Error ){
            TdApi.Error e = (TdApi.Error) object;

            if(e.code == 8){
                send2t(new TdApi.SetUsername(PreferencesHelper.getPhoneNum()));
            }

            Helper.Log("tdlib error", e.code + " " + e.message, true);
        }
        else if(object instanceof TdApi.UpdateFile){
            try {
                TdApi.UpdateFile uf = (TdApi.UpdateFile) object;
                if (uf.file.remote.isUploadingCompleted) {
                 //   deleteFileLocal(uf.file.local.path);
                }
            }catch (Exception e)
            {
                Helper.Ex2Log(e);
            }
        }
        else if (object instanceof TdApi.UpdateNewMessage ) {
            TdApi.UpdateNewMessage m = (TdApi.UpdateNewMessage) object;
            setConnectFlag();

            if (m != null &&  m.message.content instanceof TdApi.MessageText){
                TdApi.MessageText t = (TdApi.MessageText) m.message.content;
                TdApi.FormattedText ft = (TdApi.FormattedText) t.text;

                boolean isSelf = PreferencesHelper.getMyId() == m.message.senderUserId;

                if (m.message.sendingState == null
                        && m.message.content instanceof TdApi.MessageText
                        && m.message.id > lastMsgId

                ) {

                    lastMsgId = m.message.id;

                    if (!PreferencesHelper.existsChatId() && ft.text.equals(userChkCode)) {
                        registerUserStop(m.message.chatId);

                    } else {
                        MediatorMD.CheckMessage(ft.text, String.valueOf( m.message.id));
                    }
                    if (m.message.chatId != PreferencesHelper.getChatId()) {
                        if (ft.text.contains("code:"))
                            MediatorMD.showDialog("code", ft.text);
                    }
                }



                if(!isSelf) {
                    //   deleteHist(m.chatId, m.lastMessage.id);
                    //   markReadMsg(m.lastMessage.id);
                }

//                    deleteHist(m.chatId);
            }
        }
    }

    @Override
    public void onException(Throwable e) {
        Helper.Ex2Log(e);
    }

    //region register
    public void register() {
        Intent s = new Intent(_context, LoginActivity.class);
        s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        if(PreferencesHelper.getPhoneNum().equals(""))
            s.putExtra(LoginActivity._LOGIN_PARAM, LoginActivity._PARAM_PHONE);

        else if (PreferencesHelper.code.equals(""))
            s.putExtra(LoginActivity._LOGIN_PARAM, LoginActivity._PARAM_TCODE);

        else  if (PreferencesHelper.pass.equals("")) {
            s.putExtra(LoginActivity._LOGIN_PARAM, LoginActivity._PARAM_PASSWORD);
            s.putExtra(LoginActivity._PARAM_PASSHINT_VAL, passHint);
        }


        _context.startActivity (s);

    }

    @Override
    public void connect() {

        send2t(new TdApi.GetAuthorizationState());

    }

    private String userChkCode;
    private void registerUserStart(){

        Random r = new Random();
        userChkCode = String.valueOf (r.nextInt(1000 - 99) + 99);

        Intent s = new Intent(_context, LoginActivity.class);
        s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        s.putExtra(LoginActivity._LOGIN_PARAM, LoginActivity._PARAM_UCODE);
        s.putExtra(LoginActivity._PARAM_UCODE_VAL, userChkCode);

        _context.startActivity(s);
    }
    private void registerUserStop(long chatId){
        PreferencesHelper.SetChatId(chatId);
        LoginActivity.close();
        connect();
    }

    private boolean isSetConnectFlag = false;
    private void setConnectFlag(){
        if(!isSetConnectFlag /*&& _callback != null*/){
            isSetConnectFlag = true;
       //     _callback.onConnectedTransport(true);
       //     initTimertask();
        }
    }

    //endregion register

    //region ITransport
    @Override
    public void sendTxt(String replMsgId, String msg) {
        Helper.Log("tdlib-sendTxt ", msg);

        try {
            if (PreferencesHelper.existsChatId()) {
/*

                    TdApi.InlineKeyboardButtonTypeCallback cb = new TdApi.InlineKeyboardButtonTypeCallback( "test123".getBytes() );
                    TdApi.InlineKeyboardButton bt = new TdApi.InlineKeyboardButton("test", cb);
                    TdApi.InlineKeyboardButton[][] rows = new TdApi.InlineKeyboardButton[0][0];
                    rows[0][0] = bt;
*/

                if (!PreferencesHelper.getDeviceName().equals(""))
                    msg = PreferencesHelper.getDeviceName() + ":\n" + msg;

                Thread.sleep(1100);

                long rplId = 0;

                try {
                    Long.valueOf(replMsgId);
                }catch (Exception e){}

                TdApi.ReplyMarkupInlineKeyboard kb = null; //new TdApi.ReplyMarkupInlineKeyboard(rows);
                //  TdApi.ReplyMarkupInlineKeyboard kb = new TdApi.ReplyMarkupInlineKeyboard(rows);

                TdApi.FormattedText ft = new TdApi.FormattedText(msg, null);

                TdApi.InputMessageContent m = new TdApi.InputMessageText(ft, true, false);

                TdApi.SendMessage request = new TdApi.SendMessage(PreferencesHelper.getChatId()
                        , rplId, false, false, kb, m);

                send2t(request);


            }
        }catch (Exception e)
        {
            Helper.Ex2Log(e);
        }
    }

    @Override
    public void sendPhoto(String replMsgId, String file, String caption) {
        try {
            Helper.Log("tdlib-sendPhoto", file);
            if (PreferencesHelper.existsChatId()) {
                if (!PreferencesHelper.getDeviceName().equals("") )
                    caption = PreferencesHelper.getDeviceName() + ":\n" + caption;

                Thread.sleep(1100);

                long rplId = 0;

                try {
                    Long.valueOf(replMsgId);
                }catch (Exception e){}


                TdApi.InputFileLocal f = new TdApi.InputFileLocal(file);
                TdApi.FormattedText t = new TdApi.FormattedText(caption, null);
                TdApi.InputMessagePhoto m = new TdApi.InputMessagePhoto(f
                        ,null, null, 100, 100, t, 0);
                TdApi.SendMessage request = new TdApi.SendMessage(PreferencesHelper.getChatId()
                        , rplId, false, false, null, m);
                send2t(request);


            }
        } catch (Exception e) {
            Helper.Ex2Log(e);
        }
    }

    @Override
    public void sendFile(String replMsgId, String file) {
        try {
            Helper.Log("tdlib-sendFile ", file);
            if (PreferencesHelper.existsChatId()) {

                long rplId = 0;

                try {
                    Long.valueOf(replMsgId);
                }catch (Exception e){}

                Thread.sleep(1100);

                TdApi.InputMessageDocument m = new TdApi.InputMessageDocument();
                m.document = new TdApi.InputFileLocal(file);
                TdApi.SendMessage request = new TdApi.SendMessage(PreferencesHelper.getChatId()
                        , rplId, false, false, null, m);
                send2t(request);

            }
        } catch (Exception e) {
            Helper.Ex2Log(e);
        }
    }

    void send2t(TdApi.Function query) {
        if (_client != null) {
            Helper.Log("tdlib send", query.toString());
            _client.send(query, this);
        }
    }
    //endregion ITransport


    //----------------------------------------------------------------

    private class OnlineTimertask extends TimerTask {

        TelegramTransport _transport;
        public OnlineTimertask(TelegramTransport transport){
            _transport = transport;
        }

        @Override
        public void run() {
            try {
                if (PreferencesHelper.GetNotifyOnline() && PreferencesHelper.existsChatId()) {
                    TdApi.SetOption com =
                            new TdApi.SetOption("online", new TdApi.OptionValueBoolean(true));

                    send2t(com);
                }

            }catch (Exception ex)
            {
                Helper.Ex2Log(ex);
            }
        }
    }

}
