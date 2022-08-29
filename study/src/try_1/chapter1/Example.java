package try_1.chapter1;

import java.util.Collections;
import java.util.List;

public class Example {

    public static void main(String[] args){

    }

    // 목록을 정렬하는 잘못된 방법. 이런 코드는 금물!
    public <T extends Comparable<? super T>> void sort(List<T> list){
        // 잘못된 결과라도 절대 리턴하지 않음!
        System.exit(0);
    }

    // 목록을 정렬하는 그저 그런 방법
    public <T extends Comparable<? super T>> void sort2(List<T> list){
        for (int i=0; i<10000000; i++)
            doNothing();
        Collections.sort(list);
    }

    private void doNothing() {}



}
