package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
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

        int key;

        if(sc.getStateQueue().size != 0) {
            key = sc.getStateQueue().first();

            boolean canRemove = true;

            if(ac.animations.containsKey(key)){
                canRemove = (ac.animations.get(key).isAnimationFinished(sc.stateTime)) && key == sc.getCurrentState();
            }

            if(canRemove)  {
                key = sc.getDefaultState();
                sc.getStateQueue().removeFirst();
            }

            //TODO need to check animation is finished before switching back to the default state.
            //TODO also need to check this works using the turrets

        } else {
            key = sc.getDefaultState();
        }

        if(key != sc.getCurrentState()) {
            sc.setCurrentState(key);
        }

        if(ac.animations.containsKey(sc.getCurrentState())){
            trc.region = ac.animations.get(sc.getCurrentState()).getKeyFrame(sc.stateTime);
        }
    }

}
