package com.byrjamin.wickedwizard.maps;

/**
 * Created by Home on 03/02/2017.
 */
public class MapCoords {

    private int x;
    private int y;

    public MapCoords(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public boolean sameCoords(MapCoords mapCoords){
        return x == mapCoords.getX() && y == mapCoords.getY();
    }

    @Override
    public String toString() {
        return "MapCoords{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
