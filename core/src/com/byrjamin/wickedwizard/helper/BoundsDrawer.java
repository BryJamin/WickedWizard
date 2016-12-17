package com.byrjamin.wickedwizard.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 17/12/2016.
 */
public class BoundsDrawer {

    private static ShapeRenderer shapeRenderer;

    public static void drawBounds(SpriteBatch batch, Rectangle r){

        initialize();

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(r.getX(),r.getY(),r.getWidth(), r.getHeight());
        shapeRenderer.end();

        batch.begin();
    }


    public static void drawBounds(SpriteBatch batch, Array<Rectangle> bounds){

        initialize();

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Rectangle r : bounds) {
            shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }
        shapeRenderer.end();


        batch.begin();
    }



    private static void initialize(){
        if(shapeRenderer == null){
            shapeRenderer = new ShapeRenderer();
        }
    }
}
