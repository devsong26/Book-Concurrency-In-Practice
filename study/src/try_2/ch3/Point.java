package try_2.ch3;

import annotation.Immutable;

/**
 * 값을 변경할 수 없는 Point 객체. DelegatingVehicleTracker에서 사용
 */
@Immutable
public class Point {
    public final int x, y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
}
