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

    private float TILE_SIZE;

    private int noOfRows;
    private int noOfColumns;

    private Rectangle bounds;

    private TextureRegion skin;


    public RoomWall(float posX, float posY, float WIDTH, float HEIGHT, float TILE_SIZE) {

        this.posX = posX;
        this.posY = posY;
        this.WIDTH = WIDTH;
        this.TILE_SIZE = TILE_SIZE;
        this.HEIGHT = HEIGHT;
        bounds = new Rectangle(posX, posY, WIDTH, HEIGHT);

        noOfRows = (int) HEIGHT / (int) TILE_SIZE;
        noOfColumns = (int) WIDTH / (int) TILE_SIZE;

/*        System.out.println("No rows is" + noOfColumns);
        System.out.println(noOfRows);*/

        skin = PlayScreen.atlas.findRegion("brick");

    }


    public boolean overLaps(Rectangle r){
        return bounds.overlaps(r);
    }

    public void draw(SpriteBatch batch){
        BoundsDrawer.drawBounds(batch, bounds);

        for(int j = 0; j < noOfColumns; j++) {
            for (int i = 0; i < noOfRows; i++) {
                batch.draw(skin, posX + (TILE_SIZE * j), posY + (TILE_SIZE * i), TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
