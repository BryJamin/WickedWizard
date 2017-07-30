package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkOnHitComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.utils.BoundsDrawer;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by BB on 18/03/2017.
 *
 * Debugging system used to see Boundaries of hitboxes, collision bounds and proximity bounds
 *
 */

public class BoundsDrawingSystem extends EntitySystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ProximityTriggerAIComponent> ptam;
    ComponentMapper<WallComponent> wm;

    private boolean isDrawing;

    private Array<Rectangle> bounds = new Array<Rectangle>();
    private Array<Rectangle> hitboxes = new Array<Rectangle>();
    private Array<Rectangle> proxhitboxes = new Array<Rectangle>();


    public BoundsDrawingSystem() {
        super(Aspect.one(CollisionBoundComponent.class, ProximityTriggerAIComponent.class, WallComponent.class));
        this.isDrawing = Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_BOUND, false);
    }


    @Override
    protected void processSystem() {

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


        bounds.clear();
        hitboxes.clear();
        proxhitboxes.clear();



    }

}
