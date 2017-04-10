package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

/**
 * Created by Home on 07/03/2017.
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

        if(ac.animations.containsKey(sc.getState())){
            trc.region = ac.animations.get(sc.getState()).getKeyFrame(sc.stateTime);
        }
    }

}
