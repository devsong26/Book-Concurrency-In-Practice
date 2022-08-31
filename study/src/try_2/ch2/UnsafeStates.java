package try_2.ch2;

import java.util.HashSet;
import java.util.Set;

/**
 * 내부적으로 사용할 변수를 외부에 공개, 이런 코드는 금물!
 */
public class UnsafeStates {
    private String[] states = new String[]{
        "AK", "AL"
    };
    public String[] getStates() {
        return states;
    }

    /**
     * 객체 공개
     */
    public static Set<Secret> knownSecrets;

    public void initialize(){
        knownSecrets = new HashSet<>();
    }

}
