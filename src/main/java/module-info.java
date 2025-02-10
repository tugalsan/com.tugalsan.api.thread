module com.tugalsan.api.thread {
    requires gwt.user;
    
    requires com.tugalsan.api.function;
    
    requires com.tugalsan.api.os;
    requires com.tugalsan.api.random;
    requires com.tugalsan.api.stream;
    
    requires com.tugalsan.api.union;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.list;
    exports com.tugalsan.api.thread.client;
    exports com.tugalsan.api.thread.server.async.await;
    exports com.tugalsan.api.thread.server.async.builder;
    exports com.tugalsan.api.thread.server.async.run;
    exports com.tugalsan.api.thread.server.async.scheduled;
    exports com.tugalsan.api.thread.server.sync;
    exports com.tugalsan.api.thread.server.sync.rateLimited;
    exports com.tugalsan.api.thread.server.sync.lockLimited;
}
