package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by Home on 31/12/2016.
 */
public class RoomBackground {

    private float x;
    private float y;

    private float BACKGROUND_WIDTH;
    private float BACKGROUND_HEIGHT;

    private float TILE_WIDTH;
    private float TILE_HEIGHT;

    private float TILE_SIZE;

    private int columns;
    private int rows;

    private Array<TextureAtlas.AtlasRegion> backgrounds;

    private int[] backgroundSelection;


    public RoomBackground(Array<TextureAtlas.AtlasRegion> backgrounds, float x, float y, float BACKGROUND_WIDTH, float BACKGROUND_HEIGHT, float TILE_SIZE){

        this.backgrounds = backgrounds;

        this.x = x;
        this.y = y;
        this.BACKGROUND_WIDTH = BACKGROUND_WIDTH;
        this.BACKGROUND_HEIGHT = BACKGROUND_HEIGHT;
        this.TILE_SIZE = TILE_SIZE;

        this.columns = ((int) BACKGROUND_WIDTH / (int) TILE_SIZE) + 1;
        this.rows = ((int) BACKGROUND_HEIGHT / (int) TILE_SIZE) + 1;

        backgroundSelection = new int[columns * rows];


        System.out.println("Columns are " +columns);
        System.out.println("Rows are " + rows);

        backgroundSetUp();

    }


    public void draw(SpriteBatch batch){

        int count = 0;

        for(int i = 0; i < columns; i++){
            for(int j = 0; j < rows; j ++){

                batch.draw(backgrounds.get(backgroundSelection[count]), x + (TILE_SIZE * i), y + (TILE_SIZE * j), TILE_SIZE, TILE_SIZE);
                count++;
            }

        }

    }



    public void backgroundSetUp(){

        Random random = new Random();

        for(int i = 0; i < backgroundSelection.length; i++){
            if(i != 0) {
                do {
                    backgroundSelection[i] = random.nextInt(backgrounds.size);
                } while (backgroundSelection[i - 1] == backgroundSelection[i]);
            } else {
                backgroundSelection[i] = random.nextInt(backgrounds.size);
            }
        }
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
}
