package try_2.ch2;


import jdk.nashorn.internal.codegen.OptimisticTypesPersistence;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 생성 메서드에서 this 변수가 외부로 유출되지 않도록 팩토리 메서드를 사용하는 모습
 */
public class SafeListener {
    private final EventListener listener;

    private SafeListener(){
        listener = this::doSomething;
    }

    private void doSomething(Event e) {
        System.out.println(e);
    }

    public static SafeListener newInstance(EventSource source){
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }

    /**
     * 기본 변수형의 로컬 변수와 객체형의 로컬 변수에 대한 스택 한정
     */
    public int loadTheArk(Collection<Animal> candidates){
        SortedSet<Animal> animals;
        int numPairs = 0;
        Animal candidate = null;

        //animals 변수는 메소드에 한정되어 있으며, 유출돼서는 안 된다.
        animals = new TreeSet<>(new SpeciesGenderComparator());
        animals.addAll(candidates);
        for (Animal a : animals){
            if (candidate == null || !candidate.isPotentialMate(a))
                candidate = a;
            else {
                OptimisticTypesPersistence ark = null;
                ark.load(new AnimalPair(candidate, a));
                ++numPairs;
                candidate = null;
            }
        }

        return numPairs;
    }

}
