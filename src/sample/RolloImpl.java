package sample;


import javafx.util.Callback;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RolloImpl implements Rollo{

    private double delta = 0;
    private double status = 0.5;
    private double max = 1;
    private double min = 0;
    private Callback cb = null;

    private ExecutorService exSvc = Executors.newSingleThreadExecutor();

    class CallableMini implements Callable {

        private Rollo r;

        public CallableMini(Rollo rollo){
            r = rollo;
        }

        @Override
        public Object call() throws Exception {
            try {
                while (status >= min && status <= max){
                    status += delta;
                    Thread.sleep(50); // sollte 5 Sekunden dauern
                }
                if(status >= max){
                    delta = 0;
                    status = max;
                }else if(status <= min){
                    delta = 0;
                    status = min;
                }
                System.out.println("task complete:" + getStatus());
                cb.call(r);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                System.out.println("task interrupted");
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void Up() {
        delta = 0.01;
        try{
            exSvc.shutdownNow();
            exSvc = Executors.newSingleThreadExecutor();
        }catch (Exception e){
            e.printStackTrace();
        }
        exSvc.submit(new CallableMini(this));
    }

    @Override
    public void Down() {
        delta = -0.01;
        try{
            exSvc.shutdownNow();
            exSvc = Executors.newSingleThreadExecutor();
        }catch (Exception e){
            e.printStackTrace();
        }
        exSvc.submit(new CallableMini(this));
    }

    @Override
    public String getStatus() {
        if(status <= min || status >= max){
            delta = 0;
            return "0";
        }else if(status > min && status < max && delta < 0){
            return "<0";
        }else if(status > min && status < max && delta > 0){
            return ">0";
        }
        return String.valueOf(status);
    }

    @Override
    public void setCallback(Callback cb) {
        this.cb = cb;
    }

}


