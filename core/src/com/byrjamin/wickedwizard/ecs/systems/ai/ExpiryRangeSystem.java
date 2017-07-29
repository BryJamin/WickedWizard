package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;

/**
 * Created by BB on 14/05/2017.
 *
 * System used for expiring entities based on how far they have travelled.
 *
 * Per iteration the distance they have travelled from the last interation is tallied up.
 *
 * If the total distance travelled is greater than the ExpiryRangeComponent's range the entity
 * is killed using the OnDeathSystem
 *
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

        if(erc.distanceTravelled > erc.range){
            world.getSystem(OnDeathSystem.class).kill(e);
        }
    }

}
