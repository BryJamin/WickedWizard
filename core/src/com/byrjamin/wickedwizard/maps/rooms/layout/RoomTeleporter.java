package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 24/01/2017.
 */
public class RoomTeleporter {

    private float posX;
    private float posY;

    private float height = Measure.units(10);
    private float width = Measure.units(10);

    private boolean active;
    private boolean open = true;

    private Rectangle bounds;

    private Color drawingColor = Color.WHITE;

    private MapCoords leaveCoords;
    private MapCoords roomCoords;

    private TextureRegion currentFrame;

    public RoomTeleporter(float posX, float posY, MapCoords roomCoords, MapCoords leaveCoords) {
        this.posX = posX;
        this.posY = posY;

        this.roomCoords = roomCoords;
        this.leaveCoords = leaveCoords;
        bounds = new Rectangle(posX, posY, width, height);

        currentFrame = PlayScreen.atlas.findRegion("grate");
    }



    public void draw(SpriteBatch batch){

        if(open){
            batch.draw( PlayScreen.atlas.findRegion("highlightgrate"), posX - Measure.units(0.5f), posY - Measure.units(0.5f), width + Measure.units(1), height + Measure.units(1));
        } else {
            //drawingColor = Color.RED;
        }

        if(active && open) {
            //drawingColor = Color.WHITE;
        }

       // batch.setColor(drawingColor);
        batch.draw(currentFrame, posX, posY, width, height);
       // batch.setColor(Color.WHITE);

        drawingColor = Color.WHITE;
        BoundsDrawer.drawBounds(batch, bounds);
        //BoundsDrawer.drawBounds(batch, new Rectangle(posX - Measure.units(10), posY - Measure.units(10), Measure.units(20), Measure.units(20)));
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

    public void lock(){
        open = false;
    }

    public void unlock(){
        open = true;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isOpen() {
        return open;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean hasEntered(Rectangle r){
        return open && r.overlaps(bounds) && active;
    }

    public float getCenterX(){
        return bounds.x + bounds.getWidth() /2;
    }

    public float getCenterY(){
        return bounds.y + bounds.getHeight() /2;
    }



}
