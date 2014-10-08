package lookup;

import akka.actor.*;
import akka.japi.Procedure;
import scala.concurrent.duration.Duration;
import utils.Helpers;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by mbd on 07.10.2014.
 * Greeting Actor Look is the actor that looks for remote actor
 */
public class GreetingActorLook extends UntypedActor {

    private final String path;
    private ActorRef remoteActor = null;

    /**
     *
     * @param path the path to remote actor
     * After setting the path send a Identify request. this request is a built-in message that is recognized by all actors.
     * The remote actor responds with an ActorIdentify when it received Identify message
     */
    public GreetingActorLook(String path) {
        this.path = path;
        sendIdentifyRequest();
    }

    /**
     * send Identify after 3 sec
     */
    private void sendIdentifyRequest() {
        getContext().actorSelection(path).tell(new Identify(path), getSelf());
        getContext()
                .system()
                .scheduler()
                .scheduleOnce(Duration.create(3, SECONDS), getSelf(),
                        ReceiveTimeout.getInstance(), getContext().dispatcher(), getSelf());
    }

    /**
     * get message, if is ActorIdentify then set remoteActor var
     * and made the current actor to be active so he will can interpret the normal messages
     * if receive ReceiveTimeout the try again to sendIdentify
     * @param message
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ActorIdentity) {
            remoteActor = ((ActorIdentity) message).getRef();
            if (remoteActor == null) {
                System.out.println("Remote actor not available: " + path);
            } else {
                getContext().watch(remoteActor);
                getContext().become(active, true);
                Helpers.isConnected = true;
            }

        } else if (message instanceof ReceiveTimeout) {
            sendIdentifyRequest();

        } else {
            System.out.println("Not ready yet");

        }
    }

    /**
     * Active state
     * when receive Terminated then send identify and restart the process, so call unbecome to
     * go back to initial state
     */
    Procedure<Object> active = (Object message) -> {
        if (message instanceof String) {
            // send message to server actor
            System.out.println("Sending message: " + message);
            remoteActor.tell(message, getSelf());

        } else if (message instanceof Terminated) {
            System.out.println("Remote Actor terminated");
            sendIdentifyRequest();
            getContext().unbecome();

        } else if (message instanceof ReceiveTimeout) {
            // ignore

        } else {
            unhandled(message);
        }
    };
}
