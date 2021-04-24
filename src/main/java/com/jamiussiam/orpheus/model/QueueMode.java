package com.jamiussiam.orpheus.model;

public enum QueueMode {
    FULL, SINGLE, DISABLED;

    public static QueueMode nextState(QueueMode queueMode) {
        switch (queueMode) {
            case FULL:
                return SINGLE;
            case SINGLE:
                return DISABLED;
            default:
                return FULL;
        }
    }
}
