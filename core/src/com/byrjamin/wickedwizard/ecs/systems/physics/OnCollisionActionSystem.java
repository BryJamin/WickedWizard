package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.OnCollisionActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.utils.collider.Collider;

import static com.byrjamin.wickedwizard.utils.collider.Collider.Collision.BOTTOM;
import static com.byrjamin.wickedwizard.utils.collider.Collider.Collision.TOP;
import static com.byrjamin.wickedwizard.utils.collider.Collider.Collision.LEFT;
import static com.byrjamin.wickedwizard.utils.collider.Collider.Collision.RIGHT;

/**
 * Created by Home on 10/06/2017.
 */

public class OnCollisionActionSystem extends EntityProcessingSystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<OnCollisionActionComponent> ocam;

    @SuppressWarnings("unchecked")
    public OnCollisionActionSystem() {
        super(Aspect.all(OnCollisionActionComponent.class, CollisionBoundComponent.class));
    }


    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        CollisionBoundComponent cbc = cbm.get(e);
        OnCollisionActionComponent ocac = ocam.get(e);

        for(Collider.Collision c : cbc.getRecentCollisions()) {
            System.out.println(c);
        }

        if(cbc.getRecentCollisions().contains(BOTTOM, false)) performAction(ocac.top, world, e);
        if(cbc.getRecentCollisions().contains(TOP, false)) performAction(ocac.bottom, world, e);
        if(cbc.getRecentCollisions().contains(LEFT, false)) performAction(ocac.left, world, e);
        if(cbc.getRecentCollisions().contains(RIGHT, false)) performAction(ocac.right, world, e);


    }

    public void performAction(Action a, World world, Entity e){
        if(a != null){
            a.performAction(world, e);
        }
    }



}
