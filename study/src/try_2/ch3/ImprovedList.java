package try_2.ch3;

import annotation.ThreadSafe;
import try_2.ch1.Widget;

import java.util.*;

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
        if(!contains){
            list.add(x);
        }
        return !contains;
    }

    public synchronized void clear(){
        list.clear();
    }

    /**
     * ... List 클래스의 다른 메소드도 clear와 비슷하게 구현
     */
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

    /**
     * 올바르게 동작하지 않을 수 있는 상태의 메서드
     */
//    public static Object getLast(Vector list){
//        int lastIndex = list.size() - 1;
//        return list.get(lastIndex);
//    }
//
//    public static void deleteLast(Vector list){
//        int lastIndex = list.size() - 1;
//        list.remove(lastIndex);
//    }

    /**
     * 클라이언트 측 락을 활용해 getLast와 deleteLast를 동기화시킨 모습
     */
    public static Object getLast(Vector list){
        synchronized(list){
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

    /**
     * ArrayIndexOutOfBoundsException이 발생할 수 있는 반복문 코드
     */
    private void foo(Vector vector){
        for(int i=0; i<vector.size(); i++)
            doSomething(vector.get(i));
    }

    private void doSomething(Object o) {
    }

    /**
     * 클라이언트 측 락을 사용해 반복문을 동기화시킨 모습
     */
    private void foo2(Vector vector){
        synchronized(vector){
            for(int i=0; i<vector.size(); i++){
                doSomething(vector.get(i));
            }
        }
    }

    /**
     * Iterator를 사용해 List 클래스의 값을 반복해 뽑아내는 모습
     */
    private void foo3(){
        List<Widget> widgetList = Collections.synchronizedList(new ArrayList<>());

        // ConcurrentModificationException 이 발생할 수 있다.
        for(Widget w : widgetList)
            doSomething(w);
    }

}
