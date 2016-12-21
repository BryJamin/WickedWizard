package com.byrjamin.wickedwizard.spelltypes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;


/**
 * Created by Home on 21/12/2016.
 */
public class BlastWave {

    //Required Parameters
    private boolean dispellType;
    //private

    private float SIZE = Measure.units(3);

    private float GROWTH_RATE = Measure.units(1.5f);

    private float centerPointX;
    private float centerPointY;

    //
    private Rectangle right;
    private Rectangle left;
    private Rectangle top;
    private Rectangle bottom;

    private float MOVEMENT = Measure.units(45f);

    public BlastWave(float centerPointX, float centerPointY){
        this.centerPointX = centerPointX;
        this.centerPointY = centerPointY;

        right = new Rectangle(0,0, SIZE, SIZE);
        right.setCenter(centerPointX, centerPointY);
        left  = new Rectangle(0,0, SIZE, SIZE);
        left.setCenter(centerPointX, centerPointY);
        top  = new Rectangle(0,0, SIZE, SIZE);
        top.setCenter(centerPointX, centerPointY);
        bottom  = new Rectangle(0,0, SIZE, SIZE);
        bottom.setCenter(centerPointX, centerPointY);
    }


    public void update(float dt){

        Vector2 temp = new Vector2();


        right.setHeight(right.getHeight() + GROWTH_RATE);
        right.getPosition(temp);
        temp.add(MOVEMENT * dt * -1, 0);
        right.setPosition(temp.x, centerPointY - right.getHeight() / 2);

        left.setHeight(left.getHeight() + GROWTH_RATE);
        left.getPosition(temp);
        temp.add(MOVEMENT * dt, 0);
        left.setPosition(temp.x, centerPointY - left.getHeight() / 2);

        top.setWidth(top.getWidth() + GROWTH_RATE);
        top.getPosition(temp);
        temp.add(0, MOVEMENT * dt);
        top.setPosition(centerPointX - top.getWidth() / 2, temp.y);

        bottom.setWidth(bottom.getWidth() + GROWTH_RATE);
        bottom.getPosition(temp);
        temp.add(0, MOVEMENT * dt * -1);
        bottom.setPosition(centerPointX - bottom.getWidth() / 2, temp.y);

        //left.setCenter(left.getPosition(temp), centerPointY);
        //left.

        System.out.println(left.x);

/*        top.width += MOVEMENT;
        top.setCenter(centerPointX, top.y += MOVEMENT * dt);

        bottom.width += MOVEMENT;
        bottom.setCenter(centerPointX, bottom.y -= MOVEMENT * dt);*/
    }

    public void draw(SpriteBatch batch){

        Array<Rectangle> bounds = new Array<Rectangle>();
       bounds.add(right);
       bounds.add(left);
      bounds.add(top);
      bounds.add(bottom);

        System.out.println("got drew");

        BoundsDrawer.drawBounds(batch, bounds);
    }





}
