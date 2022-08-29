package try_1.chapter4;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.LinkedList;
import java.util.List;

/**
 * 퍼즐 풀기 프레임웍의 Node 클래스
 */
@Immutable
public class Node<P, M> {

    final P pos;
    final M move;
    final Node<P, M> prev;

    Node(P pos, M move, Node<P, M> prev){
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }

    List<M> asMoveList(){
       List<M> solution = new LinkedList<>();
       for (Node<P, M> n = this; n.move != null; n = n.prev)
           solution.add(0, n.move);
       return solution;
    }

}
