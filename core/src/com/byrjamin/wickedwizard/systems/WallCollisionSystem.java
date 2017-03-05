package com.byrjamin.wickedwizard.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 04/03/2017.
 */
public class WallCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public WallCollisionSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);

        cbc.bound.x = pc.getX();
        cbc.bound.y = pc.getY();
        Rectangle futureRectangle = new Rectangle(cbc.bound);
        futureRectangle.x += vc.velocity.x * world.delta;
        futureRectangle.y += vc.velocity.y * world.delta;
        //BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, cbc.bound);

        //BoundsDrawer.drawBounds();

        //System.out.println(cbc.bound.getX());

        for(Rectangle r : world.getSystem(WallSystem.class).getWalls()){
            Collider.Collision c = Collider.collision(cbc.bound, futureRectangle, r);
            if(c != Collider.Collision.NONE){
                switch(c){
                    case TOP: vc.velocity.y = 0;
                        break;
                }
                pc.position.x = cbc.bound.getX();
                pc.position.y = cbc.bound.getY();
                break;

            }
            //System.out.println("NO COLLISION");
        }

        BoundsDrawer.drawBounds(world.getSystem(RenderingSystem.class).batch, cbc.bound);

        //pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

    }





}
