package com.tugalsan.api.thread.server.killable;

public class TS_ThreadKillableBuilder {

    public static <T> TS_ThreadKillableBuilder1Class<T> ofClass(Class<T> clazz) {
        return new TS_ThreadKillableBuilder1Class(clazz);
    }
}
