package lookup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import utils.Helpers;

/**
 * Created by mbd on 07.10.2014.
 */
public class MainLookup {

    public static void main(String... args) throws InterruptedException {
        /**
         * create a system with configuration remotelookup. That config open remoting on localhost
         */
        ActorSystem system = ActorSystem.create("system-remote", ConfigFactory.load("remotelookup"));
        /**
         * path to remote actor
         */
        final String path = "akka.tcp://system-greet@127.0.0.1:2555/user/greet";

        /**
         * Create actor that will lookup for remote actor. this is a system local actor
         */
        final ActorRef actor = system.actorOf(
                Props.create(GreetingActorLook.class, path), "lookupActor");
        /**
         * waiting until the connection is established
         */
        while (!Helpers.isConnected){
            Thread.sleep(1000);
        }
        /**
         * Send hi message to remote actor
         */
        actor.tell("Hi", ActorRef.noSender());

    }
}
