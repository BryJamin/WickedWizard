package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 14/01/2017.
 */
public class RoomWall {

    private float posX;
    private float posY;
    private float WIDTH;
    private float HEIGHT;

    private float TEXT_SIZE;

    private int noOfColumns;

    private Rectangle bounds;

    private TextureRegion skin;


    public RoomWall(float posX, float posY, float WIDTH, float HEIGHT) {
        this.posX = posX;
        this.posY = posY;
        this.WIDTH = WIDTH;
        this.TEXT_SIZE = WIDTH;
        this.HEIGHT = HEIGHT;
        bounds = new Rectangle(posX, posY, WIDTH, HEIGHT);

        noOfColumns = (int) HEIGHT / (int) WIDTH;

        skin = PlayScreen.atlas.findRegion("brick");

    }


    public boolean overLaps(Rectangle r){
        return bounds.overlaps(r);
    }

    public void draw(SpriteBatch batch){
        BoundsDrawer.drawBounds(batch, bounds);

        for(int i = 0; i < noOfColumns; i++) {
            batch.draw(skin, posX, posY + (TEXT_SIZE * i), TEXT_SIZE, TEXT_SIZE);
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
