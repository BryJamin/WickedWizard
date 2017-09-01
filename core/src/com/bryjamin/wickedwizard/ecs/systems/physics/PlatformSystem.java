package com.bryjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Home on 29/04/2017.
 */

public class PlatformSystem extends EntitySystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent> pm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.PlatformComponent> platm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent> gravm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent> dm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent> vm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent> cbm;


    private static float inputRadius = com.bryjamin.wickedwizard.utils.Measure.units(20f);
    private static final float platformPushDown = com.bryjamin.wickedwizard.utils.Measure.units(0.5f);

    @SuppressWarnings("unchecked")
    public PlatformSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.object.PlatformComponent.class, com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent.class));
    }

    @Override
    protected void processSystem() {

        //TODO change platform disabling to intead be on a timer. like two world.delta ticks


        com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent playerBound = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent.class);
        com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent playerVelocity = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class);

        for(Entity e : this.getEntities()) {

            com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent cbc = cbm.get(e);
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

                    com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent pc = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class);
                    pc.position.x = playerBound.bound.getX();
                    pc.position.y = playerBound.bound.getY();

                }

            }
        }
    }


    public boolean fallThoughPlatform(float x, float y) {

        com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent playerBound = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent.class);
        com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent playerVelocity = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class);
        com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent playerGravity = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent.class);

        //TODO make take in an input parameter and check whether or not to fall through the platform (Distance of like Measure.units 20f or something?

        boolean withinRadius = x >= playerBound.bound.getX() - inputRadius && x <= playerBound.bound.getX() + inputRadius;

        if(!withinRadius) return false;

        for(Entity e : this.getEntities()) {

            com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent cbc = cbm.get(e);
            com.bryjamin.wickedwizard.ecs.components.object.PlatformComponent platform = platm.get(e);


            if(!platform.canPassThrough) {

                Rectangle futureRectangle = new Rectangle(playerBound.bound);
                futureRectangle.x += (playerVelocity.velocity.x * world.delta);
                futureRectangle.y += (playerGravity.gravity * world.delta);

                com.bryjamin.wickedwizard.utils.collider.Collider.Collision c = com.bryjamin.wickedwizard.utils.collider.Collider.cleanCollision(playerBound.bound, futureRectangle, cbc.bound);


                if(c == com.bryjamin.wickedwizard.utils.collider.Collider.Collision.BOTTOM) {
                    platform.canPassThrough = true;
                    world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).position.y -= platformPushDown;
                    return true;
                }

            }
        }


        return false;





    }


}
