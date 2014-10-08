import lookup.MainLookup;
import remote.MainRemote;

/**
 * Created by mbd on 08.10.2014.
 */
public class Main {

    public static void main(String... args) throws InterruptedException {
        /**
         * run first remote actor and then lookup for the actor from remote system
         */
        MainRemote.main(args);
        Thread.sleep(1000);
        MainLookup.main(args);
    }
}
