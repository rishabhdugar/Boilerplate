package com.domain.template.io.bus.event;

public class NetworkEvent extends Event {

    int eventType;

    public NetworkEvent() {
    }

    public NetworkEvent(int eventType) {
        this.eventType = eventType;
    }

    public NetworkEvent(int eventType, String subscriber) {
        this.eventType = eventType;
        setSubscriber(subscriber);
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}
