package com.byrjamin.wickedwizard.entity.enemies;

import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.maps.rooms.Room;

/**
 * Created by Home on 18/02/2017.
 */
public abstract class GroundedEnemy extends Enemy {

    private float GRAVITY = Measure.units(1.5f);

    public GroundedEnemy(){
        super();
    }

    public void update(float dt, Room r){
        super.update(dt, r);
        velocity.add(0, -GRAVITY * dt);
        position.add(velocity);
        collisionBound.y = position.y;


        System.out.println("VELOCITY IS " + velocity.y);
        System.out.println("POSITION IS " + position.y);
        System.out.println("Collision BOUND Y is  " + collisionBound.y);

    }


    @Override
    public void applyCollision(Collider.Collision collision) {

        if(collision != Collider.Collision.NONE) {
            System.out.println(collision + "" + collision + "" + collision);
        }

        switch(collision) {
            case TOP:
                if(velocity.y <= 0){
                    velocity.y = 0;
                    //TODO the reason for this is that the collider moves the collision bound so the position needs to be
                    //TODO updated. This may need to be changed
                    position.y = collisionBound.y;
                }
                break;
        }
    }







}
