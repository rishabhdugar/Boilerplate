package com.sarkisian.template.util;

public class UnsupportedUriException extends IllegalArgumentException {

    public UnsupportedUriException() {

    }

    public UnsupportedUriException(String message) {
        super("Unsupported URI: " + message);
    }

}
