package com.byrjamin.wickedwizard.archive.maps;

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

    public MapCoords(MapCoords e){
        this.x = e.x;
        this.y = e.y;
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

    public void addX(int x){
        this.x += x;
    }

    public void addY(int y){
        this.y += y;
    }

    public void add(int x, int y){
        this.x += x;
        this.y += y;
    }

    @Override
    public String toString() {
        return "MapCoords{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapCoords mapCoords = (MapCoords) o;

        if (x != mapCoords.x) return false;
        return y == mapCoords.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
