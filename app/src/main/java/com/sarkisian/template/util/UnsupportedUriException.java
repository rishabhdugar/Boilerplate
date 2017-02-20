package com.sarkisian.template.util;

public class UnsupportedUriException extends IllegalArgumentException{

    public UnsupportedUriException(String msg) {
        super("Unsupported URI: " + msg);
    }

}
