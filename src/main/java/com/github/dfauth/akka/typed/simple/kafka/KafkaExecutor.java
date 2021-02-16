package com.github.dfauth.akka.typed.simple.kafka;

import akka.dispatch.DispatcherPrerequisites;
import akka.dispatch.ExecutorServiceConfigurator;
import akka.dispatch.ExecutorServiceFactory;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;


public class KafkaExecutor extends ExecutorServiceConfigurator {

    private static final Logger logger = LoggerFactory.getLogger(KafkaExecutor.class);
    private final List<String> topics;
    private String name;

    public KafkaExecutor(Config config, DispatcherPrerequisites prerequisites) {
        super(config, prerequisites);
        topics = config.getStringList("akka.kafka.topics");
        name = String.format("%s-executor",topics.stream().collect(Collectors.joining("-")));
    }

    @Override
    public ExecutorServiceFactory createExecutorServiceFactory(String id, ThreadFactory threadFactory) {
        return () -> Executors.newSingleThreadExecutor(r -> new Thread(null, r, name));
    }
}
