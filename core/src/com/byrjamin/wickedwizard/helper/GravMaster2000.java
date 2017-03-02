package com.byrjamin.wickedwizard.helper;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 02/01/2017.
 */
public class GravMaster2000 {


    public float GRAVITY = -1;
    private Vector2 gravity;

    public GravMaster2000(){
        gravity = new Vector2();
    }


    public void update(float dt, Vector2 velocity){
        //boolean canAdd = false;
        this.gravity.add(0, GRAVITY * dt);
        velocity.add(gravity);
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

    public void resetGravVelocity(){
            gravity.y = 0;
    }
}
