package try_2.ch2;

import annotation.Immutable;

import java.util.HashSet;
import java.util.Set;

/**
 * 일반 객체를 사용해 불변 객체를 구성한 모습
 */
@Immutable
public class ThreeStooges {
    private final Set<String> stooges = new HashSet<>();

    public ThreeStooges(){
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }

    public boolean isStooges(String name){
        return stooges.contains(name);
    }
}
