package com.github.dfauth.akka.typed.simple;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.Envelope;
import akka.dispatch.MailboxType;
import akka.dispatch.MessageQueue;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class SimpleMailbox implements MailboxType {

    private static final Logger logger = LoggerFactory.getLogger(SimpleMailbox.class);

    private akka.actor.ActorSystem.Settings settings;
    private com.typesafe.config.Config config;

    public SimpleMailbox(ActorSystem.Settings settings, Config config) {
        this.settings = settings;
        this.config = config;
    }

    @Override
    public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {

        String _owner = owner.map(r ->
            r.path().name()
        ).getOrElse(() -> "none");

        Consumer<Envelope> logOwner = e -> owner.foreach(r -> {
            logger.info("owner is {} message is {} sender is {}", r.path().name(), e.message(), e.sender());
            return null;
        });

        BlockingQueue<Envelope> q = new ArrayBlockingQueue<>(10);
        return new MessageQueue() {
            @Override
            public void enqueue(ActorRef receiver, Envelope handle) {
                q.offer(handle);
                logger.info("enqueue: receiver: {}, handle: {} owner: {}", receiver, handle, _owner);
            }

            @Override
            public boolean hasMessages() {
                logger.info("hasMessages {} owner: {}",(q.size()>0),_owner);
                return q.size() > 0;
            }

            @Override
            public int numberOfMessages() {
                logger.info("numberOfMessages {} owner: {}",q.size(), _owner);
                return q.size();
            }

            @Override
            public void cleanUp(ActorRef owner, MessageQueue deadLetters) {
                logger.info("cleanUp owner: {}, deadletters: {}", _owner, deadLetters);
                q.clear();
            }

            @Override
            public Envelope dequeue() {
                logger.info("dequeue peek: {} owner: {}",q.peek(),_owner);
                Optional<Envelope> e = Optional.ofNullable(q.poll());
                e.ifPresent(logOwner);
                return e.orElse(null);
            }
        };
    }

}
