package com.github.dfauth.akka.typed.simple.kafka;

import akka.dispatch.DispatcherPrerequisites;
import akka.dispatch.ExecutorServiceConfigurator;
import akka.dispatch.ExecutorServiceFactory;
import com.github.dfauth.actor.kafka.avro.ActorMessage;
import com.github.dfauth.akka.typed.simple.config.KafkaConfig;
import com.github.dfauth.kafka.Stream;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;
import java.util.stream.Collectors;


public class KafkaExecutor extends ExecutorServiceConfigurator implements Function<ConsumerRecord<String, ActorMessage>, Long> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaExecutor.class);
    private final KafkaConfig kafkaConfig;
    private String name;

    public KafkaExecutor(Config config, DispatcherPrerequisites prerequisites) {
        super(config, prerequisites);
        this.kafkaConfig = new KafkaConfig(ConfigFactory.load());
        name = String.format("%s-executor",kafkaConfig.topics().stream().collect(Collectors.joining("-")));
        Stream<String, ActorMessage> stream = Stream.Builder.stringKeyBuilder(kafkaConfig.envelopeHandler().envelopeSerde())
                .withProperties(kafkaConfig.properties())
                .withTopics(kafkaConfig.topics())
                .withGroupId(kafkaConfig.groupId())
                .withRecordProcessor(this)
                .build();
        stream.start();
    }

    @Override
    public ExecutorServiceFactory createExecutorServiceFactory(String id, ThreadFactory threadFactory) {
        return () -> Executors.newSingleThreadExecutor(r -> new Thread(null, r, name));
    }

    @Override
    public Long apply(ConsumerRecord<String, ActorMessage> r) {
        return r.offset();
    }
}
