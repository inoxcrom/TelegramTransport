package rock.delta2.telegramtransport.Mediator;

public interface ICommandCheckMessage {
    void CheckMessage(String inTxt, boolean repeatCmd, boolean isSilent, String msgId);
    void CheckMessage(String inTxt, String msgId);
}
