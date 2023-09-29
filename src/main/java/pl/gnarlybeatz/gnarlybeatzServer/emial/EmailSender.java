package pl.gnarlybeatz.gnarlybeatzServer.emial;

public interface EmailSender {
    String send(EmailDetails emailDetails);
}
