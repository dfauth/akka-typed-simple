package com.github.dfauth.akka.typed.simple.kafka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.Mailbox;
import akka.dispatch.MailboxType;
import akka.dispatch.MessageQueue;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

import java.util.HashMap;
import java.util.Map;


public class KafkaMailbox implements MailboxType {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMailbox.class);

    private akka.actor.ActorSystem.Settings settings;
    private com.typesafe.config.Config config;
    private Map<String, Mailbox> mailboxes = new HashMap<>();

    public KafkaMailbox(ActorSystem.Settings settings, Config config) {
        this.settings = settings;
        this.config = config;
    }

    @Override
    public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
        return owner.map(r -> mailboxes.get(r.path())).getOrElse(() -> KafkaMessageQueue.empty());
    }
}
