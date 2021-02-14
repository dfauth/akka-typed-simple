package com.github.dfauth.akka.typed.simple;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import com.github.dfauth.akka.typed.simple.commands.CreateCommand;
import com.github.dfauth.akka.typed.simple.commands.GreetingCommand;
import com.github.dfauth.akka.typed.simple.commands.ReportGreetingReceivedCommand;
import com.github.dfauth.akka.typed.simple.commands.SendGreetingToCommand;

import java.util.Optional;
import java.util.stream.Stream;

public class CommandHandler {

    public static Optional<Behavior<Command>> same = Optional.of(Behaviors.same());

    public static Behavior<Command> toBehavior(CommandHandler... handlers) {
        return Behaviors.receiveMessage(m ->
            Stream.of(handlers).map(h -> m.despatch(h)).findFirst().orElseThrow()
        );
    }

    public Optional<Behavior<Command>> handleSendGreetingTo(SendGreetingToCommand sendGreetingTo) {
        return Optional.empty();
    }

    public Optional<Behavior<Command>> handleGreeting(GreetingCommand greeting) {
        return Optional.empty();
    }

    public Optional<Behavior<Command>> handleCreated(ActorRef<Command> ref) {
        return Optional.empty();
    }

    public Optional<Behavior<Command>> handleCreate(CreateCommand c) {
        return Optional.empty();
    }

    public Optional<Behavior<Command>> handleReportGreetingReceived(ReportGreetingReceivedCommand reportGreetingReceivedCommand) {
        return Optional.empty();
    }
}
