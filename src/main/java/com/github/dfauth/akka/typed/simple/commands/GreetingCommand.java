package com.github.dfauth.akka.typed.simple.commands;

import akka.actor.typed.Behavior;
import com.github.dfauth.akka.typed.simple.Command;
import com.github.dfauth.akka.typed.simple.CommandHandler;

public class GreetingCommand implements Command {

    private String text;

    public GreetingCommand(String text) {
        this.text = text;
    }

    @Override
    public String name() {
        return text;
    }

    @Override
    public Behavior<Command> despatch(CommandHandler handler) {
        return handler.handleGreeting(this).orElseThrow();
    }
}
