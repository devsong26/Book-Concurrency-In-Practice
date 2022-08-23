package chapter5;

import annotation.GuardedBy;
import chapter1.Point;

import java.util.Set;

/**
객체 간에 발생하는 락 순서에 의한 데드락, 이런 코드는 금물!
 **/
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
        this.location = location;
        if(location.equals(destination))
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
            Image image = new Image();
            for(Taxi t : taxis)
                image.drawMarket(t.getLocation());
            return image;
        }
    }
}
