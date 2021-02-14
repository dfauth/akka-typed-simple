package com.github.dfauth.akka.typed.simple;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

import java.util.Optional;

public interface Command {
    default Behavior<Command> behavior(CommandHandler handler) {
        return Behaviors.receiveMessage(m -> {
            m.despatch(handler);
            return Behaviors.same();
        });
    }

    default Optional<ActorRef<Command>> sender() {
        return Optional.empty();
    }

    String name();

    Behavior<Command> despatch(CommandHandler handler);
}
