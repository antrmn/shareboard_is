package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceException extends RuntimeException{

    private List<String> messages = Collections.emptyList();

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
        messages = List.of(message);
    }

    public ServiceException(List<String> messages){
        super(messages.isEmpty() ? "" : String.join("\n", messages));
        this.messages = Collections.unmodifiableList(messages);
    }

    public List<String> getMessages() {
        return messages;
    }
}
