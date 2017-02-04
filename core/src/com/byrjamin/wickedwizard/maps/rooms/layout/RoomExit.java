package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.MapCoords;

/**
 * Created by Home on 08/01/2017.
 */
public class RoomExit {

    private float posX;
    private float posY;
    private float WIDTH;
    private float HEIGHT;
    private float rotation;
    private boolean open = true;
    private boolean active = true;
    private boolean flip;

    private Rectangle bound;

    private Animation currentAnimation;

    private Animation closingAnimation;
    private Animation openingAnimation;

    private float time = 25;

    private MapCoords leaveCoords;
    private MapCoords roomCoords;

    private int leaveX;
    private int leaveY;

    //private bounds


    public RoomExit(float posX, float posY, float WIDTH, float HEIGHT,MapCoords roomCoords, MapCoords leaveCoords, boolean flip) {
        this.posX = posX;
        this.posY = posY;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        this.leaveCoords = leaveCoords;
        this.roomCoords = roomCoords;

        this.flip = flip;
        bound = new Rectangle(posX, posY, WIDTH, HEIGHT);


        closingAnimation = AnimationPacker.genAnimation(1 / 35f, "door");
        openingAnimation = AnimationPacker.genAnimation(1 / 35f, "door", Animation.PlayMode.REVERSED);

        currentAnimation = openingAnimation;

    }


    public boolean hasEntered(Rectangle r){
        return open && r.overlaps(bound) && active;
    }

    public boolean overlaps(Rectangle r){
        return r.overlaps(bound);
    }

    public void update(float dt){
        time += dt;
    }

    public void draw(SpriteBatch batch){
        BoundsDrawer.drawBounds(batch, bound);

        float x =  flip ? posX - HEIGHT / 2 : (posX - (WIDTH * 2));
        float y =  flip ? (posY - (HEIGHT * 1.5f))  : posY;

        float height = flip ? WIDTH : HEIGHT;
        float width = flip ? WIDTH + Measure.units(5): HEIGHT + Measure.units(5);
        //HEIGHT + Measure.units(5);

        batch.draw(currentAnimation.getKeyFrame(time), x, y, (width / 2), (height / 2), width,height,1,1,flip ? 90 : rotation);
       // BoundsDrawer.drawBounds(batch, new Rectangle(x, y, width, height));
    }


    public void lock(){
        open = false;
    }

    public void unlock(){
        open = true;
    }

    public void lockAnimation(){
        if(currentAnimation != closingAnimation) {
            currentAnimation = closingAnimation;
            time = 0;
        }
    }

    public void unlockAnimation(){
        if(currentAnimation != openingAnimation) {
            currentAnimation = openingAnimation;
            time = 0;
        }
    }

    public MapCoords getLeaveCoords() {
        return leaveCoords;
    }

    public void setLeaveCoords(MapCoords leaveCoords) {
        this.leaveCoords = leaveCoords;
    }

    public MapCoords getRoomCoords() {
        return roomCoords;
    }

    public void setRoomCoords(MapCoords roomCoords) {
        this.roomCoords = roomCoords;
    }

    public boolean isUnlocked() {
        return open;
    }

    public Rectangle getBound() {
        return bound;
    }

    public boolean isOpen() {
        return open;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
