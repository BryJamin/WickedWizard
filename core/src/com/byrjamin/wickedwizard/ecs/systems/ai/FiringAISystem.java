package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.utils.BulletMath;

/**
 * Created by BB on 11/03/2017.
 *
 * System used for enemies who use a Weapon
 *
 * Calculates the angle to fire a weapon from based on the Firing AI of the Enemy
 *
 * Calculates when to fire the weapon by ticking down it's timer
 *
 */
public class FiringAISystem extends EntityProcessingSystem {

    ComponentMapper<WeaponComponent> wm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<FiringAIComponent> fm;

    @SuppressWarnings("unchecked")
    public FiringAISystem() {
        super(Aspect.all(PositionComponent.class, WeaponComponent.class, FiringAIComponent.class));
    }

    @Override
    protected void process(Entity e) {


        WeaponComponent wc = wm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);

        FiringAIComponent fc = fm.get(e);

        wc.timer.update(world.delta);

        float x = cbc.getCenterX() + fc.offsetX;
        float y = cbc.getCenterY() + fc.offsetY;

        switch(fc.ai){
            case TARGET_PLAYER:

                if(wc.timer.isFinishedAndReset()){

                    wc.weapon.fire(world, e, x, y, firingAngleToPlayerInRadians(x, y));
                    if(world.getMapper(AnimationStateComponent.class).has(e))
                        e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);
                }

                break;
            case UNTARGETED:

                if(wc.timer.isFinishedAndReset()){

                    wc.weapon.fire(world,e, x, y, fc.firingAngleInRadians);
                    if(world.getMapper(AnimationStateComponent.class).has(e))
                        e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);

                }

                break;
        }

    }


    /**
     *
     * @param x - Starting x position of the shot fired
     * @param y - Starting y position of the shot fired
     * @return - Angle in radians the player is from the starting position
     */
    private double firingAngleToPlayerInRadians(float x, float y){
        CollisionBoundComponent playercbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
        return BulletMath.angleOfTravel(x, y, playercbc.getCenterX(), playercbc.getCenterY());
    }


}