package com.byrjamin.wickedwizard.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Used to draw Rectangles/Array of Rectangles used throughtout the program.
 *
 * This is just so I can more easily see bounds of the sprites I'm creating.
 *
 */
public class BoundsDrawer {

    private static ShapeRenderer shapeRenderer = new ShapeRenderer();;

    /**
     * Draws the boundarys of an Array of Rectangles
     * @param batch - The SpriteBatch
     * @param bounds - Rectangle Array
     */
    public static void drawBounds(SpriteBatch batch, Array<? extends Rectangle> bounds){

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Rectangle r : bounds) {
            shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }
        shapeRenderer.end();


        batch.begin();
    }

    public static void drawBounds(SpriteBatch batch, Rectangle... bounds){

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Rectangle r : bounds) {
            shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }
        shapeRenderer.end();


        batch.begin();
    }

    public static void drawBounds(SpriteBatch batch, Circle... bounds){

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Circle r : bounds) {
            shapeRenderer.circle(r.x, r.y, r.radius);
        }
        shapeRenderer.end();


        batch.begin();
    }

}
