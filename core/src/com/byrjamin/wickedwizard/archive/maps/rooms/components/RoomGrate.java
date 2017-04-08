package com.byrjamin.wickedwizard.archive.maps.rooms.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.utils.BoundsDrawer;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 24/01/2017.
 */
public class RoomGrate extends RoomExit{

    private float posX;
    private float posY;

    private float height = Measure.units(10);
    private float width = Measure.units(10);

    private boolean active;

    private TextureRegion currentFrame;

    public RoomGrate(com.byrjamin.wickedwizard.archive.maps.MapCoords roomCoords, com.byrjamin.wickedwizard.archive.maps.MapCoords leaveCoords, EXIT_DIRECTION direction) {
        super(roomCoords, leaveCoords, direction);
        bounds = new Rectangle(posX, posY, width, height);
        currentFrame = PlayScreen.atlas.findRegion("grate");
    }

    public RoomGrate(float posX, float posY, com.byrjamin.wickedwizard.archive.maps.MapCoords roomCoords, com.byrjamin.wickedwizard.archive.maps.MapCoords leaveCoords, EXIT_DIRECTION direction) {
        this(roomCoords, leaveCoords, direction);
        this.posX = posX;
        this.posY = posY;
    }



    public void draw(SpriteBatch batch){
        if(unlocked){
            batch.draw( PlayScreen.atlas.findRegion("highlightgrate"), posX - Measure.units(0.5f), posY - Measure.units(0.5f), width + Measure.units(1), height + Measure.units(1));
        }
        batch.draw(currentFrame, posX, posY, width, height);
        BoundsDrawer.drawBounds(batch, bounds);

    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean hasEntered(Rectangle r){
        return unlocked && r.overlaps(bounds) && active;
    }

    @Override
    public void update(float dt) {

    }

    public float getCenterX(){
        return bounds.x + bounds.getWidth() /2;
    }

    public float getCenterY(){
        return bounds.y + bounds.getHeight() /2;
    }

    public void setCenter(float x, float y) {
        this.posX = x - width / 2;
        this.posY = y - height / 2;
        bounds.x = x - width / 2;
        bounds.y = y - height / 2;
    }



}
