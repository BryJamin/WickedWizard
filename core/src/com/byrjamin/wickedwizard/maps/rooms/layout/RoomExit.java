package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.maps.rooms.Room;

/**
 * Created by Home on 08/01/2017.
 */
public class RoomExit {

    private float posX;
    private float posY;
    private float WIDTH;
    private float HEIGHT;
    private boolean open = false;
    private Room.EXIT_POINT exit;

    private Rectangle bound;

    //private bounds


    public RoomExit(float posX, float posY, float WIDTH, float HEIGHT, Room.EXIT_POINT exit) {
        this.posX = posX;
        this.posY = posY;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.exit = exit;
        bound = new Rectangle(posX, posY, WIDTH, HEIGHT);
    }


    public boolean canEnter(Rectangle r){
        return open && r.overlaps(bound);
    }

    public boolean overlaps(Rectangle r){
        return r.overlaps(bound);
    }

    public void draw(SpriteBatch batch){
        BoundsDrawer.drawBounds(batch, bound);
    }


    public void lock(){
        open = false;
    }

    public void unlock(){
        open = true;
    }

    public boolean isUnlocked() {
        return open;
    }

    public Room.EXIT_POINT getExit() {
        return exit;
    }

    public Rectangle getBound() {
        return bound;
    }
}
