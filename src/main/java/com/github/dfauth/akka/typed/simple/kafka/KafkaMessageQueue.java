package com.github.dfauth.akka.typed.simple.kafka;

import akka.actor.ActorRef;
import akka.dispatch.Envelope;
import akka.dispatch.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KafkaMessageQueue implements MessageQueue {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageQueue.class);
    private static final MessageQueue EMPTY = new EmptyMessageQueue();

    public static MessageQueue empty() {
        return EMPTY;
    }

    @Override
    public void enqueue(ActorRef receiver, Envelope handle) {

    }

    @Override
    public Envelope dequeue() {
        throw new IllegalStateException("Oops. Unimplemented");
    }

    @Override
    public int numberOfMessages() {
        throw new IllegalStateException("Oops. Unimplemented");
    }

    @Override
    public boolean hasMessages() {
        throw new IllegalStateException("Oops. Unimplemented");
    }

    @Override
    public void cleanUp(ActorRef owner, MessageQueue deadLetters) {

    }

    private static class EmptyMessageQueue implements MessageQueue {
        @Override
        public void enqueue(ActorRef receiver, Envelope handle) {
        }

        @Override
        public Envelope dequeue() {
            return null;
        }

        @Override
        public int numberOfMessages() {
            return 0;
        }

        @Override
        public boolean hasMessages() {
            return false;
        }

        @Override
        public void cleanUp(ActorRef owner, MessageQueue deadLetters) {
        }
    }
}
