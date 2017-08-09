package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PlatformComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 29/04/2017.
 */

public class PlatformSystem extends EntitySystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<PlatformComponent> platm;
    ComponentMapper<GravityComponent> gravm;
    ComponentMapper<DirectionalComponent> dm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public PlatformSystem() {
        super(Aspect.all(PlatformComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void processSystem() {

        //TODO change platform disabling to intead be on a timer. like two world.delta ticks


        CollisionBoundComponent playerBound = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
        VelocityComponent playerVelocity = world.getSystem(FindPlayerSystem.class).getPlayerComponent(VelocityComponent.class);

        for(Entity e : this.getEntities()) {

            CollisionBoundComponent cbc = cbm.get(e);
            PlatformComponent platform = platm.get(e);

            if(platform.canPassThrough){

                platform.timer -= world.delta;

                if(playerBound.bound.getY() >= cbc.bound.getY() + cbc.bound.getHeight() + Measure.units(1) && !playerBound.bound.overlaps(cbc.bound)
                        || platform.timer <= 0) {
                    platform.canPassThrough = false;
                }

            } else {

                if(playerBound.bound.getY() < cbc.bound.getY() + cbc.bound.getHeight() - Measure.units(1)) {
                    platform.canPassThrough = true;
                }

            }


            if(!platform.canPassThrough) {

                Rectangle futureRectangle = new Rectangle(playerBound.bound);
                futureRectangle.x += (playerVelocity.velocity.x * world.delta);
                futureRectangle.y += (playerVelocity.velocity.y * world.delta);

                Collider.Collision c = Collider.cleanCollision(playerBound.bound, futureRectangle, cbc.bound);

                if(c == Collider.Collision.BOTTOM) {
                    playerBound.getRecentCollisions().add(c);
                    playerVelocity.velocity.y = 0;
                    playerBound.bound.y = cbc.bound.y + cbc.bound.getHeight();

                    PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class);
                    pc.position.x = playerBound.bound.getX();
                    pc.position.y = playerBound.bound.getY();

                }

            }
        }
    }


    public boolean fallThoughPlatform() {

        CollisionBoundComponent playerBound = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
        VelocityComponent playerVelocity = world.getSystem(FindPlayerSystem.class).getPlayerComponent(VelocityComponent.class);
        GravityComponent playerGravity = world.getSystem(FindPlayerSystem.class).getPlayerComponent(GravityComponent.class);

        //TODO make take in an input parameter and check whether or not to fall through the platform (Distance of like Measure.units 20f or something?

        for(Entity e : this.getEntities()) {

            CollisionBoundComponent cbc = cbm.get(e);
            PlatformComponent platform = platm.get(e);


            if(!platform.canPassThrough) {

                Rectangle futureRectangle = new Rectangle(playerBound.bound);
                futureRectangle.x += (playerVelocity.velocity.x * world.delta);
                futureRectangle.y += (playerGravity.gravity * world.delta);

                Collider.Collision c = Collider.cleanCollision(playerBound.bound, futureRectangle, cbc.bound);


                if(c == Collider.Collision.BOTTOM) {
                    platform.canPassThrough = true;
                    platform.timer = PlatformComponent.FALLTHROUGH_TIME;
                    return true;
                    //PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class);
                }

            }
        }


        return false;





    }


}
