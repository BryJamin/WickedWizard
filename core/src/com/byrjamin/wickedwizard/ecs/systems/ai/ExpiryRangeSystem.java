package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;

/**
 * Created by Home on 14/05/2017.
 */

public class ExpiryRangeSystem extends EntityProcessingSystem {

    ComponentMapper<ExpiryRangeComponent> erm;
    ComponentMapper<PositionComponent> pm;

    @SuppressWarnings("unchecked")
    public ExpiryRangeSystem() {
        super(Aspect.all(PositionComponent.class, ExpiryRangeComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pc = pm.get(e);
        ExpiryRangeComponent erc = erm.get(e);

        float distanceTravelled = erc.currentPosition.dst(pc.position);

        erc.distanceTravelled += Math.abs(distanceTravelled);
        erc.currentPosition.set(pc.position);

        //System.out.println(erc.distanceTravelled);

        if(erc.distanceTravelled > erc.range){

            System.out.println("Inside");

            world.getSystem(OnDeathSystem.class).kill(e);
        }
    }

}
