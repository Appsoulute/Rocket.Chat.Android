package chat.rocket.models;

import java.util.List;

/**
 * Created by julio on 20/11/15.
 */
public class Messages {
    private List<Message> messages;
    private long unreadNotLoaded;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public long getUnreadNotLoaded() {
        return unreadNotLoaded;
    }

    public void setUnreadNotLoaded(long unreadNotLoaded) {
        this.unreadNotLoaded = unreadNotLoaded;
    }
}
