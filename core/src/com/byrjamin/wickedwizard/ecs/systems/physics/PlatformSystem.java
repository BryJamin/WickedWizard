package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PlatformComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;
import com.byrjamin.wickedwizard.utils.collider.HitBox;
import com.byrjamin.wickedwizard.utils.enums.Direction;

import javafx.geometry.Pos;

/**
 * Created by Home on 29/04/2017.
 */

public class PlatformSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<PlatformComponent> platm;
    ComponentMapper<DirectionalComponent> dm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public PlatformSystem() {
        super(Aspect.all(PlatformComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void process(Entity e) {


        CollisionBoundComponent playerBound = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
        VelocityComponent playerVelocity = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);
        CollisionBoundComponent cbc = cbm.get(e);
        PlatformComponent platform = platm.get(e);

        System.out.println(platform.canPassThrough);

        if(platform.canPassThrough){

            if(playerBound.bound.getY() > cbc.bound.getY() + cbc.bound.getHeight() && !playerBound.bound.overlaps(cbc.bound)) {
                platform.canPassThrough = false;
            }

        } else {

            if(playerBound.bound.getY() < cbc.bound.getY() + cbc.bound.getHeight() - Measure.units(1)) {
                platform.canPassThrough = true;
            }




        }

/*        if(playerBound.bound.getY() < cbc.bound.getY() + cbc.bound.getHeight() - Measure.units(1) &&
                playerBound.bound.overlaps(cbc.bound)) {
            platform.canPassThrough = true;
        } else {
            platform.canPassThrough = false;
        }*/


        if(!platform.canPassThrough) {


            Rectangle futureRectangle = new Rectangle(playerBound.bound);
            futureRectangle.x += (playerVelocity.velocity.x * world.delta);
            futureRectangle.y += (playerVelocity.velocity.y * world.delta);

            Collider.Collision c = Collider.cleanCollision(playerBound.bound, futureRectangle, cbc.bound);

            if(c == Collider.Collision.TOP) {
                playerBound.getRecentCollisions().add(c);
                playerVelocity.velocity.y = 0;
                playerBound.bound.y = cbc.bound.y + cbc.bound.getHeight();

                PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
                pc.position.x = playerBound.bound.getX();
                pc.position.y = playerBound.bound.getY();

            }

        }

    }

}
