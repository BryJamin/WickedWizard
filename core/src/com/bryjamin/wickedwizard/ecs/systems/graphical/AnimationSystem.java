package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

/**
 * Created by BB on 07/03/2017.
 *
 * Controls the current State of Animation an Entity is in.
 *
 * Checks to see if a temporary animation has been queued up or if the default animation need to be displayed
 * Temporary animations have priority over default animations
 *
 */
public class AnimationSystem extends EntityProcessingSystem {

    ComponentMapper<AnimationStateComponent> sm;
    ComponentMapper<AnimationComponent> am;
    ComponentMapper<TextureRegionComponent> trm;

    @SuppressWarnings("unchecked")
    public AnimationSystem() {
        super(Aspect.all(AnimationStateComponent.class, TextureRegionComponent.class, AnimationComponent.class));
    }

    @Override
    protected void process(Entity e) {
        AnimationComponent ac = am.get(e);
        AnimationStateComponent sc = sm.get(e);
        TextureRegionComponent trc = trm.get(e);

        int state;

        if(sc.getStateQueue().size != 0) {

            state = sc.getStateQueue().first();
            boolean canRemove = true;

            if(ac.animations.containsKey(state)){
                canRemove = (ac.animations.get(state).isAnimationFinished(sc.stateTime)) && state == sc.getCurrentState();
            }

            if(canRemove)  {
                state = sc.getDefaultState();
                sc.getStateQueue().removeFirst();
            }

        } else {
            state = sc.getDefaultState();
        }

        if(state != sc.getCurrentState()) {
            sc.setCurrentState(state);
        }

        if(ac.animations.containsKey(sc.getCurrentState())){
            trc.region = ac.animations.get(sc.getCurrentState()).getKeyFrame(sc.stateTime);
        }
    }


}
