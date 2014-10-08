package remote;

import akka.actor.UntypedActor;

/**
 * Created by mbd on 07.10.2014.
 * Just a simple actor class
 */
public class GreetingActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String){
            System.out.println("Received message: " + message.toString());
        }
    }
}
