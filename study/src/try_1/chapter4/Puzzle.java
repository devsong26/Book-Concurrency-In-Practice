package try_1.chapter4;

import java.util.Set;

/**
 * '블록 이동 퍼즐' 과 같은 퍼즐을 풀기 위한 인터페이스
 */
public interface Puzzle<P, M>{

    P initialPosition();
    boolean isGoal(P position);
    Set<M> legalMoves(P position);
    P move(P position, M move);

}
