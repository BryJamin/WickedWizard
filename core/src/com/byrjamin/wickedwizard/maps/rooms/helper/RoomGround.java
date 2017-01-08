package com.byrjamin.wickedwizard.maps.rooms.helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.maps.rooms.Room;

/**
 * Created by Home on 08/01/2017.
 */
public class RoomGround {

    private Array<Rectangle> bounds = new Array<Rectangle>();
    private boolean isBottom;

    private float height;

    private float WIDTH = 200;



    private float[] xPositions;

    private TextureRegion groundTexture;

    public RoomGround(TextureRegion groundTexture, Room room,float height, boolean isBottom){
        this.groundTexture = groundTexture;
        this.isBottom = isBottom;

        float columns = 11;
        WIDTH = room.WIDTH / columns;
        this.height = height;
        xPositions = new float[(int) columns];



        if(isBottom){

            float gap = 4;

            for(int i = 0; i < gap; i++){
                xPositions[i] = WIDTH * i;
            }

            bounds.add(new Rectangle(0,0, WIDTH * gap, height));

            room.getPlatforms().add(new RoomPlatform(0 + WIDTH * gap, 0 + (height / 4) * 3, WIDTH * 3, height / 4));

            for(int i = 7; i < columns; i++){
                xPositions[i] = WIDTH * i;
            }

            bounds.add(new Rectangle(room.WIDTH - (WIDTH * gap) ,0, WIDTH * gap, height));


        } else {
            for(int i = 0; i < columns; i++){
                xPositions[i] = WIDTH * i;
            }

            bounds.add(new Rectangle(0,0,room.WIDTH, height));
        }


    }

    public void update(float dt){

    }


    public void draw(SpriteBatch batch){
        for(float x : xPositions) {
            batch.draw(groundTexture, x, 0, WIDTH, height);
        }

        BoundsDrawer.drawBounds(batch, bounds);
    }


    public Array<Rectangle> getBounds() {
        return bounds;
    }

    public float getHeight() {
        return height;
    }
}
