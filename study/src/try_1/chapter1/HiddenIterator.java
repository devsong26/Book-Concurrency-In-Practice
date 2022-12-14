package try_1.chapter1;

import annotation.GuardedBy;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 문자열 연결 연산 내부에 Iterator가 숨겨져 있는 상황. 이런 코드는 금물!
 */
public class HiddenIterator {

    @GuardedBy("this")
    private final Set<Integer> set = new HashSet<>();

    public synchronized void add(Integer i){
        set.add(i);
    }

    public synchronized void remove(Integer i){
        set.remove(i);
    }

    public void addTenThings(){
        Random r = new Random();
        for(int i=0; i<10; i++) add(r.nextInt());

        System.out.println("DEBUG: added ten elements to " + set);
    }

}
