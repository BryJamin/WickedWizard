package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

/**
 * Created by Home on 08/01/2017.
 */
public class RoomGround {

    private Array<Rectangle> bounds = new Array<Rectangle>();
    private boolean isBottom;

    private float height;

    private float width = 200;

    int noOfColumns;
    float noOfRows = 2;


    private float size = Measure.units(5);


    private float[] xPositions;
    private float[] yPositions;

    private TextureRegion groundTexture;

    public RoomGround(TextureRegion groundTexture, Room room, float width , float height, boolean isBottom){
        this.groundTexture = groundTexture;
        this.isBottom = isBottom;

        float columns = 11;

        noOfColumns = (int) (width / size);

        System.out.println(noOfColumns);

        this.width = width / columns;
        this.height = height;
        xPositions = new float[50];
        yPositions = new float[2];
        yPositions[0] = 0;
        yPositions[1] = 100;

        if(isBottom){

            int gap = 7;

            for(int i = 0; i < gap; i++){
                xPositions[i] = size * i;
            }

            bounds.add(new Rectangle(0,0, size * gap, height));

            room.getPlatforms().add(new RoomPlatform(0 + size * gap, 0 + (height / 4) * 3, size * 6, height / 4));

            for(int i = 13; i < noOfColumns + 1; i++){
                xPositions[i] = size * i;
            }

            bounds.add(new Rectangle(width - (size * gap) ,0, size * gap, height));


        } else {
            for(int i = 0; i < noOfColumns + 1; i++){
                xPositions[i] = size * i;
            }

            bounds.add(new Rectangle(0,0,width, height));
        }


    }

    public void update(float dt){

    }


    public void draw(SpriteBatch batch){



        for(float x : xPositions) {
            for(float y: yPositions) {
                batch.draw(groundTexture, x, y, size + 1, size + 1);
            }
        }


        //BoundsDrawer.drawBounds(batch, bounds);
    }


    public Array<Rectangle> getBounds() {
        return bounds;
    }

    public float getHeight() {
        return height;
    }
}
