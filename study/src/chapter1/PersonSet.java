package chapter1;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * 한정 기법으로 스레드 안정성 확보
 */
@ThreadSafe
public class PersonSet {

    @GuardedBy("this")
    private final Set<Person> mySet = new HashSet<>();

    public synchronized void addPerson(Person p){
        mySet.add(p);
    }

    public synchronized boolean containsPerson(Person p){
        return mySet.contains(p);
    }

}
