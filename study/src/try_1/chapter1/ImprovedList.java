package try_1.chapter1;

import annotation.ThreadSafe;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * 재구성 기법으로 putIfAbsent 메서드 구현
 */
@ThreadSafe
public class ImprovedList<T> implements List<T> {
    private final List<T> list;

    public ImprovedList(List<T> list){
        this.list = list;
    }

    public synchronized boolean putIfAbsent(T x){
        boolean contains = list.contains(x);
        if (!contains) list.add(x);
        return !contains;
    }

    // 클라이언트 측 락을 활용해 getLast 와 deleteLast를 동기화시킨 모습
    public static Object getLast(Vector list){
        synchronized(list){ // 클라이언트 측 락
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static void deleteLast(Vector list){
        synchronized(list){
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }

    // ArrayIndexOutBoundsException이 발생할 수 있는 반복문 코드
    public void foo(Vector vector){
        synchronized (vector){ // 클라이언트 측 락을 사용해 반복문을 동기화시킨 모습
            for(int i=0; i<vector.size(); i++){
                doSomething(vector.get(i));
            }
        }
    }

    private void doSomething(Object o) {}

    // ... List 클래스의 다른 메서드도 clear와 비슷하게 구현

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        List.super.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        List.super.sort(c);
    }

    public synchronized void clear(){
        list.clear();
    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public T set(int index, T element) {
        return null;
    }

    @Override
    public void add(int index, T element) {

    }

    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public Spliterator<T> spliterator() {
        return List.super.spliterator();
    }
}
