package chapter1;

import jdk.nashorn.internal.codegen.OptimisticTypesPersistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class TestClass {

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

        for(Animal a : animals){
            if(candidate == null || !candidate.isPotentialMate(a)){
                candidate = a;
            } else {
                OptimisticTypesPersistence.load(new AnimalPair(candidate, a));
                ++numPairs;
                candidate = null;
            }
        }

        return numPairs;
    }

    /**
     * ThreadLocal을 사용해 스레드 한정 상태를 유지
     */
    private static ThreadLocal<Connection> connectionHolder
            = ThreadLocal.withInitial(() -> {
        try {
            return DriverManager.getConnection("DB_URL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    });

    public static Connection getConnection(){
        return connectionHolder.get();
    }

}
