package chapter6;

import annotation.ThreadSafe;

/**
 * 늦은 초기화 홀더 클래스 구문
 */
@ThreadSafe
public class ResourceFactory {
    private static class ResourceHolder {
        public static Resource resource = new Resource();
    }

    public static Resource getResource(){
        return ResourceHolder.resource;
    }
}
