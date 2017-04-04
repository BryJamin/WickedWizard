package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.factories.BulletFactory;

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

        wc.timer.update(world.delta);

        switch(fc.ai){
            case TARGETED:
                if(wc.timer.isFinishedAndReset()){
                    Entity player = world.getSystem(FindPlayerSystem.class).getPlayer();
                    CollisionBoundComponent pcbc = cbm.get(player);

                    double angleOfTravel = (Math.atan2(pcbc.getCenterY() - cbc.getCenterY(), pcbc.getCenterX() - cbc.getCenterX()));
                    Entity bullet = BulletFactory.createEnemyBullet(world, cbc.getCenterX(), cbc.getCenterY(), angleOfTravel);
                    for(Component c : wc.additionalComponenets){
                        bullet.edit().add(c);
                    }
                }
                break;
            case UNTARGETED:
                break;
        }

    }

}