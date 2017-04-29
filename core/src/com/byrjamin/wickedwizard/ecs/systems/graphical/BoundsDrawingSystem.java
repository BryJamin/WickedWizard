package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.utils.BoundsDrawer;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

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
/*        for(Entity e : this.getEntities()){
            BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, cbm.get(e).bound);

            for(HitBox hb : cbm.get(e).hitBoxes) {
                BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, Color.CYAN, hb.hitbox);
            }
        }*/


        if(world.getSystem(PlayerInputSystem.class) != null) {
            BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch,
                    world.getSystem(PlayerInputSystem.class).movementArea);
        }


/*        if(world.getSystem(CameraSystem.class) != null){

            BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch,
                    world.getSystem(CameraSystem.class).getLeft());

            BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch,
                    world.getSystem(CameraSystem.class).getRight());

            BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch,
                    world.getSystem(CameraSystem.class).getBottom());
            BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch,
                    world.getSystem(CameraSystem.class).getTop());

        }*/





    }

}
