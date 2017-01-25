package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 24/01/2017.
 */
public class RoomTeleporter {

    private float posX;
    private float posY;

    private float height = Measure.units(10);
    private float width = Measure.units(5);

    private boolean flip;

    private boolean active;
    private boolean open;

    private Room.EXIT_POINT exit;

    private Rectangle bounds;

    private Color drawingColor = Color.WHITE;

    private TextureRegion currentFrame;

    public RoomTeleporter(float posX, float posY, Room.EXIT_POINT exit, boolean flip) {
        this.posX = posX;
        this.posY = posY;

        this.flip = flip;
        this.exit = exit;
        bounds = new Rectangle(posX, posY, width, height);

        currentFrame = PlayScreen.atlas.findRegion("tele");
    }



    public void draw(SpriteBatch batch){

        if(open){
            drawingColor = Color.GREEN;
        } else {
            drawingColor = Color.RED;
        }

        if(active && open) {
            drawingColor = Color.WHITE;
        }

        batch.setColor(drawingColor);
        batch.draw(currentFrame, posX - Measure.units(8f), posY - Measure.units(10), Measure.units(20), Measure.units(20));
        batch.setColor(Color.WHITE);

        drawingColor = Color.WHITE;
        BoundsDrawer.drawBounds(batch, bounds);
        //BoundsDrawer.drawBounds(batch, new Rectangle(posX - Measure.units(10), posY - Measure.units(10), Measure.units(20), Measure.units(20)));
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

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean hasEntered(Rectangle r){
        return open && r.overlaps(bounds) && active;
    }


    public Room.EXIT_POINT getExit() {
        return exit;
    }
}
