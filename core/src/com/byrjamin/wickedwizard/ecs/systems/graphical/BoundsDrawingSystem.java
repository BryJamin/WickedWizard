package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.utils.BoundsDrawer;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 18/03/2017.
 */

public class BoundsDrawingSystem extends EntitySystem {

    ComponentMapper<HealthComponent> hm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ProximityTriggerAIComponent> ptam;
    ComponentMapper<WallComponent> wm;

    ComponentMapper<BlinkComponent> bm;

    public boolean isDrawing;

    @SuppressWarnings("unchecked")
    public BoundsDrawingSystem() {
        super(Aspect.one(CollisionBoundComponent.class, ProximityTriggerAIComponent.class, WallComponent.class));
    }

    public BoundsDrawingSystem(boolean isDrawing) {
        super(Aspect.one(CollisionBoundComponent.class, ProximityTriggerAIComponent.class, WallComponent.class));
        this.isDrawing = isDrawing;
    }


    @Override
    protected void processSystem() {

        //System.out.println(enabled);


        Array<Rectangle> bounds = new Array<Rectangle>();
        Array<Rectangle> hitboxes = new Array<Rectangle>();
        Array<Rectangle> proxhitboxes = new Array<Rectangle>();


        if(!isDrawing) return;

        for(Entity e : this.getEntities()){
            if(cbm.has(e)) {
                bounds.add(cbm.get(e).bound);
                for (HitBox hb : cbm.get(e).hitBoxes) {
                    hitboxes.add(hb.hitbox);
                }
            }

            if(wm.has(e)) {
                bounds.add(wm.get(e).bound);
            }

            if(ptam.has(e)) {
                for (HitBox hb : ptam.get(e).proximityHitBoxes) {
                    proxhitboxes.add(hb.hitbox);
                }
            }
        }


        if(world.getSystem(PlayerInputSystem.class) != null) {
          //  bounds.add(world.getSystem(PlayerInputSystem.class).movementArea);
        }


        BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, bounds);
        BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, Color.CYAN, hitboxes);
        BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, Color.PINK, proxhitboxes);
     //   BoundsDrawer.drawBounds();




    }

}
