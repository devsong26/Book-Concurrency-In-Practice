package try_1.chapter6;

import annotation.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 트라이버(Treiber) 알고리즘으로 대기 상태에 들어가지 않도록 구현한 스택(Treiber, 1986)
 */
@ThreadSafe
public class ConcurrentStack<E> {
    AtomicReference<Node<E>> top = new AtomicReference<>();

    public void push(E item){
        Node<E> newHead = new Node<>(item);
        Node<E> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }

    public E pop(){
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null)
                return null;
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }

    private static class Node<E>{
        public final E item;
        public Node<E> next;

        public Node(E item){
            this.item = item;
        }
    }

}
