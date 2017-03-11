package com.byrjamin.wickedwizard.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.byrjamin.wickedwizard.components.PlayerComponent;

/**
 * Created by Home on 11/03/2017.
 */
public class FindPlayerSystem extends EntitySystem {
    private Entity player;
    private boolean processingFlag = false;

    public FindPlayerSystem () {
        super(Aspect.all(PlayerComponent.class));
    }

    @Override
    protected void processSystem() {
        if(!this.getEntities().isEmpty()){
            player = this.getEntities().get(0);
        }
    }

    @Override
    protected boolean checkProcessing() {
        if (processingFlag) {
            processingFlag = false;
            return true;
        }
        return false;
    }

    public Entity getPlayer() {
        player = null;
        processingFlag = true;
        this.process();
        return this.getEntities().get(0);
    }
}
