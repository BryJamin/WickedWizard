package com.byrjamin.wickedwizard.archive.maps.rooms.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.utils.BoundsDrawer;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 08/01/2017.
 */
public class RoomDoor extends RoomExit{

    private float posX;
    private float posY;
    private float WIDTH = Measure.units(5);
    private float HEIGHT = Measure.units(20);

    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> closingAnimation;
    private Animation<TextureRegion> openingAnimation;

    private float time = 25;



    public RoomDoor(float posX, float posY, com.byrjamin.wickedwizard.archive.maps.MapCoords roomCoords, com.byrjamin.wickedwizard.archive.maps.MapCoords leaveCoords, EXIT_DIRECTION direction) {
        super(roomCoords, leaveCoords, direction);
        this.posX = posX;
        this.posY = posY;
        bounds = new Rectangle(posX, posY, WIDTH, HEIGHT);
        closingAnimation = AnimationPacker.genAnimation(1 / 35f, "door");
        openingAnimation = AnimationPacker.genAnimation(1 / 35f, "door", Animation.PlayMode.REVERSED);
        currentAnimation = openingAnimation;
    }


    public boolean hasEntered(Rectangle r){
        return unlocked && r.overlaps(bounds);
    }

    public boolean overlaps(Rectangle r){
        return r.overlaps(bounds);
    }

    public void update(float dt){
        time += dt;
    }

    public void draw(SpriteBatch batch){
        BoundsDrawer.drawBounds(batch, bounds);

        float x =  (posX - (WIDTH * 2));
        float y =  posY;

        float height = HEIGHT;
        float width = HEIGHT + Measure.units(5);
        //HEIGHT + Measure.units(5);

        batch.draw(currentAnimation.getKeyFrame(time), x, y, width,height);
       // BoundsDrawer.drawBounds(batch, new Rectangle(x, y, width, height));
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
}
