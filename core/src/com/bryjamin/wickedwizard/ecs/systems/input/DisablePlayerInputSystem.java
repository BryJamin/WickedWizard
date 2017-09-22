package com.bryjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.bryjamin.wickedwizard.ecs.components.DisablePlayerInputComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;

/**
 * Created by BB on 14/09/2017.
 */

public class DisablePlayerInputSystem extends EntitySystem {

    ComponentMapper<AnimationStateComponent> sm;


    private PlayerInputSystem playerInputSystem;

    @SuppressWarnings("unchecked")
    public DisablePlayerInputSystem() {
        super(Aspect.all(DisablePlayerInputComponent.class));
    }

    @Override
    protected void processSystem() {

        if(playerInputSystem == null){
            playerInputSystem = world.getSystem(PlayerInputSystem.class);
        }

        if(this.getEntities().size() > 0 && !playerInputSystem.disableInput){
            playerInputSystem.disableInput = true;
        } else if(this.getEntities().size() == 0 && playerInputSystem.disableInput){
            playerInputSystem.disableInput = false;
        }

    }


}

