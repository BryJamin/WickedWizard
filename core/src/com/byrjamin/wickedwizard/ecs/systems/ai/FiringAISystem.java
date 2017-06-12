package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.assets.SoundStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.SoundSystem;

/**
 * Created by Home on 11/03/2017.
 */
public class FiringAISystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<WeaponComponent> wm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<FiringAIComponent> fm;

    @SuppressWarnings("unchecked")
    public FiringAISystem() {
        super(Aspect.all(PositionComponent.class, WeaponComponent.class, FiringAIComponent.class));
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        WeaponComponent wc = wm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);

        FiringAIComponent fc = fm.get(e);
/*
        if((fc.firingDelay -= world.delta) > 0) return;*/

        wc.timer.update(world.delta);

        switch(fc.ai){
            case TARGETED:
                if(wc.timer.isFinishedAndReset()){

                    CollisionBoundComponent pcbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
                    double angleOfTravel = (Math.atan2(pcbc.getCenterY() - cbc.getCenterY(), pcbc.getCenterX() - cbc.getCenterX()));
                    wc.weapon.fire(world, e, cbc.getCenterX(), cbc.getCenterY(), angleOfTravel);
                    world.getSystem(SoundSystem.class).playSound(SoundStrings.enemyFireMix);

                    if(world.getMapper(AnimationStateComponent.class).has(e)){
                        e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);
                    }
                }
                break;
            case UNTARGETED:
                if(wc.timer.isFinishedAndReset()){
                    wc.weapon.fire(world,e, cbc.getCenterX(), cbc.getCenterY(), fc.firingAngleInRadians);
                    world.getSystem(SoundSystem.class).playSound(SoundStrings.enemyFireMix);
                    if(world.getMapper(AnimationStateComponent.class).has(e)){
                        e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);
                    }

                }


                break;
        }

    }

}