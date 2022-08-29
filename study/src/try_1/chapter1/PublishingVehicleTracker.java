package try_1.chapter1;

import annotation.ThreadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 내부 상태를 안전하게 공개하는 차량 추적 프로그램
 */
@ThreadSafe
public class PublishingVehicleTracker {

    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiableMap;

    public PublishingVehicleTracker(
            Map<String, SafePoint> locations){
        this.locations = new ConcurrentHashMap<>(locations);
        this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
    }

    public Map<String, SafePoint> getLocations(){
        return unmodifiableMap;
    }

    public SafePoint getLocation(String id){
        return locations.get(id);
    }

    public void setLocations(String id, int x, int y){
        if(!locations.containsKey(id)){
            throw new IllegalArgumentException(
                    "Invalid vehicle name: " + id);
        }
        locations.get(id).set(x, y);
    }

}
