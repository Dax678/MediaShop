package org.example.mediashop.Configuration.Exception;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }
}
