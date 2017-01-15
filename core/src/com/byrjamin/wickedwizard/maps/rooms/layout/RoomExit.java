package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
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

    private Animation currentAnimation;

    private Animation closingAnimation;
    private Animation openingAnimation;

    private float time = 25;

    //private bounds


    public RoomExit(float posX, float posY, float WIDTH, float HEIGHT, Room.EXIT_POINT exit) {
        this.posX = posX;
        this.posY = posY;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.exit = exit;
        bound = new Rectangle(posX, posY, WIDTH, HEIGHT);

        closingAnimation = AnimationPacker.genAnimation(1 / 35f, "door");
        openingAnimation = AnimationPacker.genAnimation(1 / 35f, "door", Animation.PlayMode.REVERSED);

        currentAnimation = openingAnimation;

    }


    public boolean hasEntered(Rectangle r){
        return open && r.overlaps(bound);
    }

    public boolean overlaps(Rectangle r){
        return r.overlaps(bound);
    }

    public void update(float dt){
        time += dt;
    }

    public void draw(SpriteBatch batch){
        BoundsDrawer.drawBounds(batch, bound);
        batch.draw(currentAnimation.getKeyFrame(time), posX - Measure.units(10), posY, HEIGHT + Measure.units(5), HEIGHT);
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
