module com.tugalsan.api.thread {
    requires gwt.user;
    requires com.tugalsan.api.executable;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.os;
    requires com.tugalsan.api.validator;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.list;
    exports com.tugalsan.api.thread.client;
    exports com.tugalsan.api.thread.server;
}
