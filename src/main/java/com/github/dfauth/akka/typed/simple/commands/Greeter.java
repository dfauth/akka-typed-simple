package com.github.dfauth.akka.typed.simple.commands;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import com.github.dfauth.akka.typed.simple.Command;
import com.github.dfauth.akka.typed.simple.CommandHandler;

import java.util.Optional;

public class Greeter extends CommandHandler {

    private final String name;

    public Greeter(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public Optional<Behavior<Command>> handleSendGreetingTo(SendGreetingToCommand sendGreetingTo) {
        sendGreetingTo.recipient().tell(new ReportGreetingReceivedCommand("Hi " + sendGreetingTo.recipient().path().name()));
        return Optional.of(Behaviors.same());
    }
}
