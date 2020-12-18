package com.kdimitrov.baseApp.profiles;

import java.util.Locale;

public enum ApplicationProfile {
    MESSAGE_QUEUE(Constants.MESSAGE_QUEUE),
    SERVER(Constants.SERVER);

    private final String value;

    ApplicationProfile(String value) {
        if (!value.equals(this.name().toLowerCase(Locale.ROOT))) {
            throw new AssertionError("The values of this enum should be lower case versions of its name");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static final class Constants {
        public static final String MESSAGE_QUEUE = "message_queue";
        public static final String SERVER = "server";

        private Constants() {
            throw new AssertionError("This class is not meant to be instantiated!");
        }
    }
}

