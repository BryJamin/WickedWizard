package com.bryjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.EntitySystem;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BossComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.UnlockMessageSystem;
import com.bryjamin.wickedwizard.factories.arenas.GameCreator;
import com.bryjamin.wickedwizard.factories.arenas.challenges.adventure.AdventureUnlocks;

/**
 * Created by BB on 18/09/2017.
 */

public class BossDefeatUnlockSystem extends EntitySystem {


    private MainGame game;
    private GameCreator gameCreator;

    private boolean isActive;
    private boolean isProcessing;


    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public BossDefeatUnlockSystem(MainGame game, GameCreator gameCreator) {
        super(Aspect.all(BossComponent.class));
        this.game = game;
        this.gameCreator = gameCreator;
    }

    @Override
    protected boolean checkProcessing() {

        if(this.getEntities().size() > 0){
            isProcessing = true;
        }

        return isProcessing;
    }

    @Override
    protected void processSystem() {


        if(this.getEntities().size() <= 0){

            if(gameCreator.getGameType() == GameCreator.GameType.ADVENTURE) {

                String id = gameCreator.getCurrentLevel().id;

                world.getSystem(UnlockMessageSystem.class).createUnlockMessage(id);

                String s = AdventureUnlocks.getUnlock(
                        world.getSystem(FindPlayerSystem.class).getPlayerComponent(PlayerComponent.class).id,
                        id);

                if(s != null) world.getSystem(UnlockMessageSystem.class).createUnlockMessage(s);
            }

            isProcessing = false;
        }


    }
}
