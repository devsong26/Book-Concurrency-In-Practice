package try_1.chapter6;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 읽기-쓰기 락을 사용해 Map을 확장
 */
public class ReadWriteMap<K, V> {
    private final Map<K, V> map;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock r = lock.readLock();
    private final Lock w = lock.writeLock();

    public ReadWriteMap(Map<K, V> map){
        this.map = map;
    }

    public V put(K key, V value){
        w.lock();
        try{
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }
    // remove(), putAll(), clear() 메서드도 put()과 동일하게 구현

    public V get(Object key){
        r.lock();
        try{
            return map.get(key);
        } finally {
            r.unlock();
        }
    }
    // 다른 읽기 메서드도 get()과 동일하게 구현
    
    /*
    상태 종속적인 작업의 동기화 구조
    void blockingAction() throws InterruptedException {
        상태 변수에 대한 락 확보
        while(선행조건이 만족하지 않음){
            확보했던 락을 풀어줌
                    선행 조건이 만족할만한 시간만큼 대기
                    인터럽트에 걸리거나 타임아웃이 걸리면 멈춤
                    락을 다시 확보
        }
        작업 실행
        락 해제
    }*/
    
}
