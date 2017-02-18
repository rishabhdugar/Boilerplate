package com.sarkisian.template.io.bus.event;

public abstract class Event {

    private String subscriber;

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public static class EventType {

        public static class Api {
            public static final int LOGIN_COMPLETED = 100;
            public static final int LOGOUT_COMPLETED = 101;

            public class Error {
                public static final int UNKNOWN = 200;
                public static final int NO_NETWORK = 201;
                public static final int PAGE_NOT_FOUND = 202;
                public static final int BAD_REQUEST = 203;
                public static final int UNAUTHORIZED = 204;
                public static final int SERVER_TIMEOUT = 205;
                public static final int CONNECTION_REFUSED = 206;
            }
        }

        public static class Network {
            public static final int CONNECTED = 300;
            public static final int DISCONNECTED = 301;
        }

    }

}
