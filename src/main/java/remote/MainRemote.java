package remote;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * Created by mbd on 07.10.2014.
 */
public class MainRemote {

    public static void main(String... args){
        /**
         * just create a normal system with configurations remote
         */
        ActorSystem sys2 = ActorSystem.create("system-greet", ConfigFactory.load("remote"));
        ActorRef greet = sys2.actorOf(Props.create(GreetingActor.class), "greet");
   }
}
