package chapter6;

import annotation.ThreadSafe;

/**
 * 성질 급한 초기화
 */
@ThreadSafe
public class EagerInitialization {
    private static Resource resource = new Resource();

    public static Resource getResource(){
        return resource;
    }
}
