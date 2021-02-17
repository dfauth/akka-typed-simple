package com.github.dfauth.akka.typed.simple;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.MailboxSelector;
import akka.actor.typed.Props;
import akka.actor.typed.javadsl.Behaviors;
import com.github.dfauth.akka.typed.simple.commands.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.github.dfauth.akka.typed.simple.CommandHandler.toBehavior;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCase {

    private static final Logger logger = LoggerFactory.getLogger(TestCase.class);
    static final ActorTestKit testKit = ActorTestKit.create();
    private Config config = ConfigFactory.load().withFallback(ConfigFactory.load("application.conf"));
    private Props props = MailboxSelector.fromConfig("blah.bablah.mailbox").withDispatcherFromConfig("blah.bablah.dispatcher");

    @org.junit.jupiter.api.AfterAll
    public static void tearDown() {
        testKit.shutdownTestKit();
    }

    @Test
    public void testIt() throws InterruptedException {
        Behavior<Command> behavior = Behaviors.setup(ctx -> toBehavior(new CommandHandler(){
            @Override
            public Optional<Behavior<Command>> handleCreate(CreateCommand c) {
                logger.info("spawn a greeter called: "+c.name());
                ActorRef<Command> ref = ctx.spawn(c.behavior(this), c.name(), props);
                c.sender().ifPresent(s -> s.tell(new CreatedCommand(ref)));
                return same;
            }
        }));

        TestProbe<Command> probe = testKit.createTestProbe(Command.class);
        ActorRef<Command> ref = probe.ref();

        ActorRef<Command> guardian = testKit.spawn(behavior, TestCase.class.getSimpleName(), props);

        guardian.tell(new CreateCommand("Fred", ref, toBehavior(new CommandHandler(){
            @Override
            public Optional<Behavior<Command>> handleSendGreetingTo(SendGreetingToCommand sendGreetingTo) {
                sendGreetingTo.recipient().tell(new GreetingCommand("Hi "+sendGreetingTo.recipient().path().name()));
                return same;
            }
        })));

        ActorRef<Command> fred = probe.expectMessageClass(CreatedCommand.class).ref();
        assertNotNull(fred);

        guardian.tell(new CreateCommand("Wilma", ref, toBehavior(new CommandHandler(){
            @Override
            public Optional<Behavior<Command>> handleGreeting(GreetingCommand greeting) {
                ref.tell(new ReportGreetingReceivedCommand(greeting.name()));
                return same;
            }
        })));

        ActorRef<Command> wilma = probe.expectMessageClass(CreatedCommand.class).ref();

        assertNotNull(wilma);

        fred.tell(new SendGreetingToCommand(wilma));

        String text = probe.expectMessageClass(ReportGreetingReceivedCommand.class).text();
        assertNotNull(text);

        assertEquals("Hi Wilma", text);
    }

}
