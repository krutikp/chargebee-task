import java.util.concurrent.atomic.AtomicInteger;

public class CircuitBreakerTest {
     AtomicInteger attempts= new AtomicInteger(0);
    public void myprocess() throws Exception {
      boolean endProcess=false;
        do {

            try {
                processMethod(true);
            } catch (Exception e) {
                Circuitbreaker.error.set(true);
                if(attempts.incrementAndGet() > 10)
                    endProcess=true;

                if(Circuitbreaker.ISOPEN.get())
                    System.out.println("FALL BACk::"+fallbackMethod());

                Thread.sleep(1000);
            }
        }while(!endProcess);
        Circuitbreaker.exit = true;

        System.out.println("Process Completes with Max attempts");
    }



    public String processMethod(boolean flag) throws  Exception{
        if(flag) {
            throw new Exception();
        }
        return  "all good";
    }

    public String fallbackMethod(){


        return "Fallback method msg";
    }

    public static void main(String args[]){

        Thread observerThread = new Thread(new Circuitbreaker(2));
        observerThread.start();
        CircuitBreakerTest test = new CircuitBreakerTest();

        try {
            test.myprocess();
        }catch (Exception e){}finally {

        }

    }
}
