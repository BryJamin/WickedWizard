package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.GrappleableComponent;
import com.byrjamin.wickedwizard.ecs.components.LockComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.WallComponent;

/**
 * Created by Home on 24/03/2017.
 */

public class LockSystem extends EntitySystem {

    ComponentMapper<ActiveOnTouchComponent> aotm;
    ComponentMapper<AnimationStateComponent> sm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<GrappleableComponent> gm;
    ComponentMapper<DoorComponent> dm;
    ComponentMapper<LockComponent> lm;



    @SuppressWarnings("unchecked")
    public LockSystem() {
        super(Aspect.all(LockComponent.class, CollisionBoundComponent.class, DoorComponent.class));
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
                if (aotm.has(e)) {
                    aotm.get(e).isEnabled = false;
                    e.edit().remove(GrappleableComponent.class);
                } else {
                    //System.out.println("aw man");
                    e.edit().remove(GrappleableComponent.class);
                    e.edit().add(new WallComponent(cbm.get(e).bound));
                }

                if(sm.has(e)){
                    sm.get(e).setState(AnimationStateComponent.State.LOCKED.getState());
                }

            }
        }
    }


    public void unlockDoors(){

        for(Entity e : this.getEntities()){
            LockComponent lc = lm.get(e);
            if(lc.isLocked()) {
                System.out.println("INSIDE");
                lm.get(e).unlock();
                if (aotm.has(e)) {
                    aotm.get(e).isEnabled = true;
                    e.edit().add(new GrappleableComponent());
                } else {
                    e.edit().add(new GrappleableComponent());
                    e.edit().remove(WallComponent.class);
                }

                if(sm.has(e)){
                    sm.get(e).setState(AnimationStateComponent.State.UNLOCKED.getState());
                }

            }
        }
    }

}
