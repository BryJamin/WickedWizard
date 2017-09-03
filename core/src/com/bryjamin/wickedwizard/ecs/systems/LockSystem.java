package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.object.GrappleableComponent;
import com.bryjamin.wickedwizard.ecs.components.object.LockComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.object.WallComponent;

/**
 * Created by Home on 24/03/2017.
 */

public class LockSystem extends EntitySystem {


    ComponentMapper<AnimationStateComponent> sm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<GrappleableComponent> gm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> dm;
    private ComponentMapper<LockComponent> lm;


    //TODO maybe put this into the DoorSystem?


    @SuppressWarnings("unchecked")
    public LockSystem() {
        super(Aspect.all(LockComponent.class, CollisionBoundComponent.class, com.bryjamin.wickedwizard.ecs.components.object.DoorComponent.class));
    }

    @Override
    protected void processSystem() {

    }



    @Override
    protected boolean checkProcessing() {
        return false;
    }


    public void lockDoors(){
        //System.out.println("lock door?");
        for(Entity e : this.getEntities()){
            LockComponent lc = lm.get(e);
            if(!lc.isLocked()) {
                lc.lock();
                e.edit().add(new WallComponent(cbm.get(e).bound));
                if(sm.has(e)){
                    sm.get(e).setDefaultState(AnimationStateComponent.State.LOCKED.getState());
                }

            }
        }
    }


    public void unlockDoors(){

        for(Entity e : this.getEntities()){
            LockComponent lc = lm.get(e);
            if(lc.isLocked()) {
                lm.get(e).unlock();
                e.edit().remove(WallComponent.class);
                if(sm.has(e)){
                    sm.get(e).setDefaultState(AnimationStateComponent.State.UNLOCKED.getState());
                }
            }
        }
    }

}
