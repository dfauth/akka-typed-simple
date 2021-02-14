package com.github.dfauth.akka.typed.simple.commands;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import com.github.dfauth.akka.typed.simple.Command;
import com.github.dfauth.akka.typed.simple.CommandHandler;

public class CreatedCommand implements Command {

    private ActorRef<Command> ref;

    public CreatedCommand(ActorRef<Command> ref) {
        this.ref = ref;
    }

    @Override
    public String name() {
        return ref.path().name();
    }

    @Override
    public Behavior<Command> despatch(CommandHandler handler) {
        return handler.handleCreated(ref).orElseThrow();
    }

    public ActorRef<Command> ref() {
        return ref;
    }
}
