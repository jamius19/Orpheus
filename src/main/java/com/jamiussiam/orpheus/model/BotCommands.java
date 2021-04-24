package com.jamiussiam.orpheus.model;

public enum BotCommands {

    BOT_PLAY("p"), BOT_DISCONNECT("dc"), BOT_SKIP("skip"), BOT_LOOP("loop");

    private final String value;

    BotCommands(String value) {
        this.value = GlobalValues.BOT_PREFIX + value;
    }

    public String getValue() {
        return value;
    }

    public boolean isCommandGiven(String command) {
        return command.startsWith(value);
    }

    public String getFilteredQuery(String query) {
        return query.substring(value.length()).trim();
    }
}
