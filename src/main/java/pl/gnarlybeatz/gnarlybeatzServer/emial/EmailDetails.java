package pl.gnarlybeatz.gnarlybeatzServer.emial;

public class EmailDetails {

    private String sender;
    private String msgBody;
    private String subject;

    public EmailDetails() {
    }

    public EmailDetails(String sender, String msgBody, String subject) {
        this.sender = sender;
        this.msgBody = msgBody;
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public String getSubject() {
        return subject;
    }


    @Override
    public String toString() {
        return "EmailDetails{" +
                "sender='" + sender + '\'' +
                ", msgBody='" + msgBody + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}

