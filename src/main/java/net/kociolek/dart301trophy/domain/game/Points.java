package net.kociolek.dart301trophy.domain.game;

public enum Points {

    WIN(3),
    DRAW(2),
    LOSE(0);

    private int point;

    Points(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
