package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowCameraComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;

/**
 * Created by BB on 19/09/2017.
 */

public class FollowCameraSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<FollowCameraComponent> fm;

    private Camera gamecam;

    @SuppressWarnings("unchecked")
    public FollowCameraSystem(Camera gamecam) {
        super(Aspect.all(FollowCameraComponent.class, PositionComponent.class));
        this.gamecam = gamecam;
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pc = pm.get(e);
        FollowCameraComponent fc = fm.get(e);
        pc.position.set(gamecam.position.x + fc.offsetX, gamecam.position.y + fc.offsetY, 0);
    }

}
