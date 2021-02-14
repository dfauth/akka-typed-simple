package com.github.dfauth.akka.typed.simple.commands;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import com.github.dfauth.akka.typed.simple.Command;
import com.github.dfauth.akka.typed.simple.CommandHandler;

import java.util.Optional;

public class CreateCommand implements Command {

    private final String name;
    private final Behavior<Command> behavior;
    private final ActorRef<Command> ref;

    public CreateCommand(String name, ActorRef<Command> ref, Behavior<Command> behavior) {
        this.name = name;
        this.ref = ref;
        this.behavior = behavior;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Optional<ActorRef<Command>> sender() {
        return Optional.ofNullable(ref);
    }

    @Override
    public Behavior<Command> behavior(CommandHandler handler) {
        return behavior;
    }

    @Override
    public Behavior<Command> despatch(CommandHandler handler) {
        return handler.handleCreate(this).orElseThrow();
    }
}
