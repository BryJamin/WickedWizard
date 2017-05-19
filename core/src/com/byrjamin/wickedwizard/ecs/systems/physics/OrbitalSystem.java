package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.OrbitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 16/05/2017.
 */

public class OrbitalSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<OrbitComponent> om;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public OrbitalSystem() {
        super(Aspect.all(PositionComponent.class, CollisionBoundComponent.class, OrbitComponent.class));
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        OrbitComponent oc = om.get(e);

        oc.angle += oc.speedInDegrees;

        float x = (float) (oc.centerOfOrbit.x + (oc.radius * Math.cos(Math.toRadians(oc.angle))));
        float y = (float) (oc.centerOfOrbit.y + (oc.radius * Math.sin(Math.toRadians(oc.angle))));

        if(cbm.has(e)){
            CollisionBoundComponent cbc = cbm.get(e);
            cbc.bound.setCenter(x + oc.offsetX,y + oc.offsetY);

            pc.position.x = cbc.bound.x;
            pc.position.y = cbc.bound.y;

        } else {
            pc.position.x = x;
            pc.position.y = y;
        }

/*        pc.position.x = (float) (oc.centerOfOrbit.x + (oc.radius * Math.cos(Math.toRadians(oc.angle)))) + oc.offsetX;
        pc.position.y = (float) (oc.centerOfOrbit.y + (oc.radius * Math.sin(Math.toRadians(oc.angle)))) + oc.offsetY;


        System.out.println(pc.position.x);

        CollisionBoundComponent cbc = cbm.get(e);
        cbc.bound.x = pc.getX();
        cbc.bound.y = pc.getY();*/


/*


        (float) (Measure.units(100) * Math.sin(angle)



        if (cbm.has(e)) {
            CollisionBoundComponent cbc = cbm.get(e);
            cbc.bound.x = pc.getX();
            cbc.bound.y = pc.getY();

            for (HitBox hb : cbc.hitBoxes) {
                hb.hitbox.x = pc.getX() + hb.offsetX;
                hb.hitbox.y = pc.getY() + hb.offsetY;
            }

        }*/

    }

}