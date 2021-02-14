package com.github.dfauth.akka.typed.simple.commands;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import com.github.dfauth.akka.typed.simple.Command;
import com.github.dfauth.akka.typed.simple.CommandHandler;

public class SendGreetingToCommand implements Command {

    private ActorRef<Command> recipient;

    public SendGreetingToCommand(ActorRef<Command> recipient) {
        this.recipient = recipient;
    }

    @Override
    public String name() {
        return recipient.path().name();
    }

    @Override
    public Behavior<Command> despatch(CommandHandler handler) {
        return handler.handleSendGreetingTo(this).orElseThrow();
    }

    public ActorRef<Command> recipient() {
        return recipient;
    }
}
