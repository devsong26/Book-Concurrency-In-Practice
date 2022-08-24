package chapter6;

import annotation.ThreadSafe;

/**
 * 선행 조건이 맞지 않으면 그냥 멈춰버리는 버퍼 클래스
 */
@ThreadSafe
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V>{
    public GrumpyBoundedBuffer(int size){
        super(size);
    }

    public synchronized void put(V v) throws BufferFullException {
        if(isFull())
            throw new BufferFullException();
        doPut(v);
    }

    public synchronized V take() throws BufferEmptyException {
        if(isEmpty())
            throw new BufferEmptyException();
        return doTake();
    }

    //  GrumpyBoundedBuffer 를 호출하기 위한 호출자 측의 코드
//    public void foo(){
//        while(true){
//            try{
//                V item = buffer.take();
//                // 값을 사용한다.
//                break;
//            } catch (BufferEmptyException e){
//                Thread.sleep(SLEEP_GRANULARITY);
//            }
//        }
//    }

}
