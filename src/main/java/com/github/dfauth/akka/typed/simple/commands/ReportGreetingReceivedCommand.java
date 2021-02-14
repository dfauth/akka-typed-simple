package com.github.dfauth.akka.typed.simple.commands;

import akka.actor.typed.Behavior;
import com.github.dfauth.akka.typed.simple.Command;
import com.github.dfauth.akka.typed.simple.CommandHandler;

public class ReportGreetingReceivedCommand implements Command {

    private final String text;

    public ReportGreetingReceivedCommand(String text) {
        this.text = text;
    }

    @Override
    public String name() {
        return text;
    }

    public String text() {
        return text;
    }

    @Override
    public Behavior<Command> despatch(CommandHandler handler) {
        return handler.handleReportGreetingReceived(this).orElseThrow();
    }
}
