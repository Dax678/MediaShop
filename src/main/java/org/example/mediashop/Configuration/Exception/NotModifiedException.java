package org.example.mediashop.Configuration.Exception;

import java.text.MessageFormat;

public class NotModifiedException  extends RuntimeException {
    public NotModifiedException(String message) {
        super(message);
    }

    public NotModifiedException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }
}
