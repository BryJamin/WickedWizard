package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.StateComponent;
import com.byrjamin.wickedwizard.ecs.components.TextureRegionComponent;

/**
 * Created by Home on 07/03/2017.
 */
public class AnimationSystem extends EntityProcessingSystem {

    ComponentMapper<StateComponent> sm;
    ComponentMapper<AnimationComponent> am;
    ComponentMapper<TextureRegionComponent> trm;

    @SuppressWarnings("unchecked")
    public AnimationSystem() {
        super(Aspect.all(StateComponent.class, TextureRegionComponent.class, AnimationComponent.class));
    }

    @Override
    protected void process(Entity e) {
        AnimationComponent ac = am.get(e);
        StateComponent sc = sm.get(e);
        TextureRegionComponent trc = trm.get(e);

        if(ac.animations.containsKey(sc.getState())){
            trc.region = ac.animations.get(sc.getState()).getKeyFrame(sc.stateTime);
        }
    }

}
