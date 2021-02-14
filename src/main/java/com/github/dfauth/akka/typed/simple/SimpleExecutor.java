package com.github.dfauth.akka.typed.simple;

import akka.dispatch.DispatcherPrerequisites;
import akka.dispatch.ExecutorServiceConfigurator;
import akka.dispatch.ExecutorServiceFactory;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class SimpleExecutor extends ExecutorServiceConfigurator {

    private static final Logger logger = LoggerFactory.getLogger(SimpleExecutor.class);
    private static Thread t = null;

    public SimpleExecutor(Config config, DispatcherPrerequisites prerequisites) {
        super(config, prerequisites);
    }

    @Override
    public ExecutorServiceFactory createExecutorServiceFactory(String id, ThreadFactory threadFactory) {
        return () -> Executors.newSingleThreadExecutor(r -> {
            if(t == null) {
                t = new Thread(null, runWith(r), "i only got this thread");
            }
            return t;
        });
    }

    private Runnable runWith(Runnable r) {
        return () -> {
            r.run();
        };
    }
}
