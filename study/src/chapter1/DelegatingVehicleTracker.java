package chapter1;

import annotation.ThreadSafe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 스레드 안전성을 ConcurrentHashMap 클래스에 위임한 추적 프로그램
 */
@ThreadSafe
public class DelegatingVehicleTracker {

    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points){
        locations = new ConcurrentHashMap<>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

//    public Map<String, Point> getLocations(){
//        return unmodifiableMap;
//    }

    public Point getLocation(String id){
        return locations.get(id);
    }

    public void setLocations(String id, int x, int y){
        if(locations.replace(id, new Point(x, y)) == null){
            throw new IllegalArgumentException("invalid vehicle name: " + id);
        }
    }

    //위치정보에 대한 고정 스냅샷을 만들어 내는 메서드
    public Map<String, Point> getLocations(){
        return Collections.unmodifiableMap(
            new HashMap<>(locations));
    }

}
