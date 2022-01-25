import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Circuitbreaker implements Runnable {

    // reachout to fallback method if reach to threashold 2

    //miantian couter, threshodl config
    //time peirod

    //fallback mehtod

    static AtomicInteger  counter= new AtomicInteger(0);
    final int threashold;
    static boolean exit = false;
    LocalDateTime dateTime= LocalDateTime.now();
    LocalDateTime open= null;

    static AtomicBoolean error = new AtomicBoolean(false);

    static AtomicBoolean ISOPEN = new AtomicBoolean(false);
    public Circuitbreaker(int threashold){
        this.threashold = threashold;
    }


    @Override
    public void run(){

        while(!exit){

            if(error.get()){
                counter.incrementAndGet();
                System.out.println("counter set::" + counter.intValue());
                error.set(false);
            }

            if( !ISOPEN.get() && counter.get() >=threashold){
                open = LocalDateTime.now();
                System.out.println("Circuit is open");
                System.out.println("Open Timer::" + open.toString() );
                ISOPEN.set(true);
                System.out.println("Open Timer::" + ISOPEN.get());

            }

            if(open != null && open.plusSeconds(10).isBefore(LocalDateTime.now())){
                System.out.println("Open Timer::" + open.toString() );
                counter.set(0);

                System.out.println("counter reset::" + counter.intValue());

                open=null;
                error.set(false);
                ISOPEN.set(false);
            }




        }

    }
}
