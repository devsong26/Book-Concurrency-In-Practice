package try_1.chapter1;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * 값을 변경할 수 없는 Point 객체, DelegatingVehicleTracker 사용
 */
@Immutable
public class Point {
    public final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
