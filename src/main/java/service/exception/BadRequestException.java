package service.exception;

import java.util.Collections;
import java.util.List;

public class BadRequestException extends ServiceException {
    private List<String> messages = Collections.emptyList();

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
        messages = List.of(message);
    }

    public BadRequestException(List<String> messages){
        super(messages.isEmpty() ? "" : String.join("; ", messages));
        this.messages = Collections.unmodifiableList(messages);
    }

    public List<String> getMessages() {
        return messages;
    }
}
