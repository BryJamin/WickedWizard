package com.byrjamin.wickedwizard.helper;

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


    public void update(float dt, Vector2 position, Array<Rectangle> platforms){

        this.gravity.add(0, GRAVITY * dt);

        boolean canAdd = true;

        for (Rectangle r : platforms){
            if(r.contains(position.x, position.y) || r.getY() + r.getHeight() <= position.y ){
                if(position.y + gravity.y >= (r.y + r.getHeight())){
                    //System.out.println("INSIDE 1");
                    //Allows for jumping
                    position.add(gravity);
                    break;
                } else {
                    //System.out.println("INSIDE 2");
                    position.y = r.y + r.getHeight();
                    canAdd = false;
                    break;
                }
            }
        }

        if(canAdd){
            position.add(gravity);
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
