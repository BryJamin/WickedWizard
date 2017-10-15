package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;

/**
 * Created by BB on 18/03/2017.
 *
 * Debugging system used to see Boundaries of hitboxes, collision bounds and proximity bounds
 *
 */

public class BoundsDrawingSystem extends EntitySystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ProximityTriggerAIComponent> ptam;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.WallComponent> wm;

    private boolean isDrawing;

    private Array<Rectangle> bounds = new Array<Rectangle>();
    private Array<Rectangle> hitboxes = new Array<Rectangle>();
    private Array<Rectangle> proxhitboxes = new Array<Rectangle>();


    public BoundsDrawingSystem() {
        super(Aspect.one(CollisionBoundComponent.class, ProximityTriggerAIComponent.class, com.bryjamin.wickedwizard.ecs.components.object.WallComponent.class));
        this.isDrawing = Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY).getBoolean(PreferenceStrings.DEV_BOUND, false);
    }


    @Override
    protected void processSystem() {

        if(!isDrawing) return;

        for(Entity e : this.getEntities()){
            if(cbm.has(e)) {
                bounds.add(cbm.get(e).bound);
                for (com.bryjamin.wickedwizard.utils.collider.HitBox hb : cbm.get(e).hitBoxes) {
                    hitboxes.add(hb.hitbox);
                }
            }

            if(wm.has(e)) {
                bounds.add(wm.get(e).bound);
            }

            if(ptam.has(e)) {
                for (com.bryjamin.wickedwizard.utils.collider.HitBox hb : ptam.get(e).proximityHitBoxes) {
                    proxhitboxes.add(hb.hitbox);
                }
            }
        }


        if(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.input.PlayerInputSystem.class) != null) {
          //  bounds.add(world.getSystem(PlayerInputSystem.class).movementArea);
        }

        com.bryjamin.wickedwizard.utils.BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, bounds);
        com.bryjamin.wickedwizard.utils.BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, Color.CYAN, hitboxes);
        com.bryjamin.wickedwizard.utils.BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, Color.PINK, proxhitboxes);


        bounds.clear();
        hitboxes.clear();
        proxhitboxes.clear();



    }

}
