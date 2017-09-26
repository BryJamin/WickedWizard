package com.bryjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 29/04/2017.
 */

public class PlatformSystem extends EntitySystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.PlatformComponent> platm;
    ComponentMapper<GravityComponent> gravm;
    ComponentMapper<DirectionalComponent> dm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;


    private static float inputRadius = Measure.units(20f);
    private static final float platformPushDown = Measure.units(0.5f);

    @SuppressWarnings("unchecked")
    public PlatformSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.object.PlatformComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void processSystem() {

        //TODO change platform disabling to intead be on a timer. like two world.delta ticks


        CollisionBoundComponent playerBound = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
        VelocityComponent playerVelocity = world.getSystem(FindPlayerSystem.class).getPlayerComponent(VelocityComponent.class);

        for(Entity e : this.getEntities()) {

            CollisionBoundComponent cbc = cbm.get(e);
            com.bryjamin.wickedwizard.ecs.components.object.PlatformComponent platform = platm.get(e);

            if(platform.canPassThrough){

                if(playerBound.bound.getY() >= cbc.bound.getY() + cbc.bound.getHeight() && !playerBound.bound.overlaps(cbc.bound)) {
                    platform.canPassThrough = false;
                }

            } else {

                if(playerBound.bound.getY() < cbc.bound.getY() + cbc.bound.getHeight() - platformPushDown) {
                    platform.canPassThrough = true;
                }

            }


            if(!platform.canPassThrough) {

                Rectangle futureRectangle = new Rectangle(playerBound.bound);
                futureRectangle.x += (playerVelocity.velocity.x * world.delta);
                futureRectangle.y += (playerVelocity.velocity.y * world.delta);

                com.bryjamin.wickedwizard.utils.collider.Collider.Collision c = com.bryjamin.wickedwizard.utils.collider.Collider.cleanCollision(playerBound.bound, futureRectangle, cbc.bound);

                if(c == com.bryjamin.wickedwizard.utils.collider.Collider.Collision.BOTTOM && playerVelocity.velocity.y <= 0) {
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


    public boolean fallThoughPlatform(float x, float y) {

        CollisionBoundComponent playerBound = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
        VelocityComponent playerVelocity = world.getSystem(FindPlayerSystem.class).getPlayerComponent(VelocityComponent.class);
        GravityComponent playerGravity = world.getSystem(FindPlayerSystem.class).getPlayerComponent(GravityComponent.class);

        //TODO make take in an input parameter and check whether or not to fall through the platform (Distance of like Measure.units 20f or something?

        boolean withinRadius = x >= playerBound.bound.getX() - inputRadius && x <= playerBound.bound.getX() + inputRadius;

        if(!withinRadius) return false;

        for(Entity e : this.getEntities()) {

            CollisionBoundComponent cbc = cbm.get(e);
            com.bryjamin.wickedwizard.ecs.components.object.PlatformComponent platform = platm.get(e);


            if(!platform.canPassThrough) {

                Rectangle futureRectangle = new Rectangle(playerBound.bound);
                futureRectangle.x += (playerVelocity.velocity.x * world.delta);
                futureRectangle.y += (playerGravity.gravity * world.delta);

                com.bryjamin.wickedwizard.utils.collider.Collider.Collision c = com.bryjamin.wickedwizard.utils.collider.Collider.cleanCollision(playerBound.bound, futureRectangle, cbc.bound);


                if(c == com.bryjamin.wickedwizard.utils.collider.Collider.Collision.BOTTOM) {
                    platform.canPassThrough = true;
                    world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class).position.y -= platformPushDown;
                    return true;
                }

            }
        }


        return false;





    }


}
