package com.byrjamin.wickedwizard.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.components.BounceComponent;
import com.byrjamin.wickedwizard.components.BulletComponent;
import com.byrjamin.wickedwizard.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;
import com.byrjamin.wickedwizard.components.WallComponent;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 04/03/2017.
 */
public class GroundCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WallComponent> wm;
    ComponentMapper<BulletComponent> bm;

    @SuppressWarnings("unchecked")
    public GroundCollisionSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, CollisionBoundComponent.class).exclude(BounceComponent.class));
    }


    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(WallComponent.class));
        IntBag entityIds = subscription.getEntities();

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);

        cbc.bound.x = pc.getX();
        cbc.bound.y = pc.getY();
        Rectangle futureRectangle = new Rectangle(cbc.bound);
        futureRectangle.x += vc.velocity.x * world.delta;
        futureRectangle.y += vc.velocity.y * world.delta;

        for(int i = 0; i < entityIds.size(); i++) {

            Rectangle r = wm.get(entityIds.get(i)).bound;
            Collider.Collision c = Collider.collision(cbc.bound, futureRectangle, r);
            if(c != Collider.Collision.NONE){
                switch(c){
                    case TOP: vc.velocity.y = 0;
                        //break;
                }
                pc.position.x = cbc.bound.getX();
                pc.position.y = cbc.bound.getY();

                if(bm.has(e)){
                    e.deleteFromWorld();
                }

            }
            BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, r);
        }

        BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, cbc.bound);

        //pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

    }





}
