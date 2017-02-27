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

    public GroundedEnemy(GBuilder builder){
        super(builder);
    }

    public static class GBuilder extends EnemyBuilder {
        public GBuilder(float posX, float posY) {
            super(posX, posY);
        }
        @Override
        public EnemyBuilder getThis() {
            return this;
        }
    }

    public void update(float dt, Room r){
        super.update(dt, r);
        velocity.add(0, -GRAVITY * dt);
        position.add(velocity);
        collisionBound.y = position.y;
    }


    @Override
    public void applyCollision(Collider.Collision collision) {

        switch(collision) {
            case TOP:
                if(velocity.y <= 0){
                    velocity.y = 0;
                    //TODO the reason for this is that the collider moves the collision bound so the position needs to be
                    //TODO updated. This may need to be changed
                    position.y = collisionBound.y;
                }
                break;
            case BOTTOM:
                velocity.y = 0;
                position.y = collisionBound.y;
        }
    }







}
