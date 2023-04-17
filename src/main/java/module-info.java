module com.tugalsan.api.thread {
    requires jdk.incubator.concurrent;
    requires gwt.user;
    requires com.tugalsan.api.runnable;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.os;
    requires com.tugalsan.api.random;
    requires com.tugalsan.api.stream;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.validator;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.list;
    exports com.tugalsan.api.thread.client;
    exports com.tugalsan.api.thread.server;
    exports com.tugalsan.api.thread.server.core;
}
