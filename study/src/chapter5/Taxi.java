package chapter5;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import chapter1.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * 객체 간의 데드락을 방지하기 위해 오픈 호출을 사용하는 모습
 **/
@ThreadSafe
public class Taxi {

    @GuardedBy("this")
    private Point location, destination;
    private final Dispatcher dispatcher;

    public Taxi(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public synchronized Point getLocation(){
        return location;
    }

    public synchronized void setLocation(Point location){
        boolean reachedDestination;

        synchronized(this){
            this.location = location;
            reachedDestination = location.equals(destination);
        }
        if(reachedDestination)
            dispatcher.notifyAvailable(this);
    }

    private class Dispatcher {
        @GuardedBy("this") private final Set<Taxi> taxis;
        @GuardedBy("this") private final Set<Taxi> availableTaxis;

        private Dispatcher(Set<Taxi> taxis, Set<Taxi> availableTaxis) {
            this.taxis = taxis;
            this.availableTaxis = availableTaxis;
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public synchronized Image getImage(){
            Set<Taxi> copy;

            synchronized(this){
                copy = new HashSet<>(taxis);
            }

            Image image = new Image();
            for(Taxi t : copy)
                image.drawMarket(t.getLocation());
            return image;
        }
    }
}
