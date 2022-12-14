package try_1.chapter5;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 락 점유 시간 단축
 */
@ThreadSafe
public class BetterAttributeStore {
    @GuardedBy("this") private final Map<String, String>
            attributes = new HashMap<>();

    public boolean userLocationMatches(String name, String regexp){
        String key = "users." + name + ".location";
        String location;
        
        synchronized(this){
            location = attributes.get(key);
        }
        
        if(location == null)
            return false;
        else 
            return Pattern.matches(regexp, location);
    }

}
