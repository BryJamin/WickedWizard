package com.byrjamin.wickedwizard.helper;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 02/01/2017.
 */
public class GravMaster2000 {


    private float GRAVITY = -75;
    private Vector2 gravity;

    public GravMaster2000(){
        gravity = new Vector2();
    }


    public void update(float dt, Rectangle bounds,  Array<Rectangle> platforms){
        //boolean canAdd = false;

        this.gravity.add(0, GRAVITY * dt);
        bounds.y = bounds.y + gravity.y;


        System.out.println(platforms.size);

        for (Rectangle r : platforms){

            if(bounds.overlaps(r)){
                bounds.y = r.y + r.getHeight();
                gravity.y = 0;
                break;
            }
        }


    }

    public float getGRAVITY() {
        return GRAVITY;
    }

    public void setGRAVITY(float GRAVITY) {
        this.GRAVITY = GRAVITY;
    }


    public void jump(float distance){
        gravity.y = distance;
    }
}
