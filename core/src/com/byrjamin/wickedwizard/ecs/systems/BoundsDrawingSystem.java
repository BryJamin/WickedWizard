package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.EntitySystem;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;

/**
 * Created by Home on 18/03/2017.
 */

public class BoundsDrawingSystem extends EntitySystem {

    ComponentMapper<HealthComponent> hm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<BlinkComponent> bm;

    @SuppressWarnings("unchecked")
    public BoundsDrawingSystem() {
        super(Aspect.all(CollisionBoundComponent.class));
    }


    @Override
    protected void processSystem() {
        for(Entity e : this.getEntities()){
            BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, cbm.get(e).bound);
        }


        BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch,
                world.getSystem(PlayerInputSystem.class).movementArea);

    }

}
