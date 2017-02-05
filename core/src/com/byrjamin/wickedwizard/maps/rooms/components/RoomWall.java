package com.byrjamin.wickedwizard.maps.rooms.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import java.util.Random;

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

    private Array<? extends TextureRegion> skins;

    private int[] wallSelection;

    public RoomWall(float posX, float posY, float WIDTH, float HEIGHT, float TILE_SIZE, Array<? extends TextureRegion> skins) {
        this(posX, posY, WIDTH, HEIGHT, TILE_SIZE);
        this.skins = skins;
        wallSelection = new int[noOfRows * noOfColumns];
        wallSetUp();
    }

    public RoomWall(float posX, float posY, float WIDTH, float HEIGHT, float TILE_SIZE) {

        this.posX = posX;
        this.posY = posY;
        this.WIDTH = WIDTH;
        this.TILE_SIZE = TILE_SIZE;
        this.HEIGHT = HEIGHT;
        bounds = new Rectangle(posX, posY, WIDTH, HEIGHT);

        noOfRows = (int) HEIGHT / (int) TILE_SIZE;
        noOfColumns = (int) WIDTH / (int) TILE_SIZE;

    }


    public boolean overLaps(Rectangle r){
        return bounds.overlaps(r);
    }

    public void draw(SpriteBatch batch){
        BoundsDrawer.drawBounds(batch, bounds);

        int count = 0;

        for(int j = 0; j < noOfColumns; j++) {
            for (int i = 0; i < noOfRows; i++) {
                if(skins != null) {
                    batch.draw(skins.get(wallSelection[count]), (int) (posX + (TILE_SIZE * j)), (int) (posY + (TILE_SIZE * i)), (int) TILE_SIZE, (int) TILE_SIZE);
                    count++;
                }
            }
        }
    }


    public void wallSetUp(){
        Random random = new Random();
        for(int i = 0; i < wallSelection.length; i++){
            wallSelection[i] = random.nextInt(skins.size);
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
