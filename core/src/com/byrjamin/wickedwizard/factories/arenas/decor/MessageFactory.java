package com.byrjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.enums.Level;

/**
 * Created by BB on 19/08/2017.
 */

public class MessageFactory {


    public ComponentBag nextLevelMessageBagAndMusic(final Level level){

        ComponentBag messageAction = new ComponentBag();
        messageAction.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(MessageBannerSystem.class).createLevelBanner(level.getName());
                world.getSystem(MusicSystem.class).playLevelMusic(level);
            }
        }, 0f, false));
        messageAction.add(new ExpireComponent(1f));

        return messageAction;

    }



}
