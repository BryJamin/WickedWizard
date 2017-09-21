package com.bryjamin.wickedwizard.factories.items.conditionals;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.Condition;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.items.Companion;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;

/**
 * Created by BB on 21/09/2017.
 */

public class ItemLabyrinthCodex implements Companion {


    @Override
    public boolean applyEffect(World world, Entity player) {

        player.getComponent(ConditionalActionComponent.class).add(new Condition() {

            Array<ArenaMap> maps = new Array<ArenaMap>();

            @Override
            public boolean condition(World world, Entity entity) {

                RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                ArenaMap arenaMap = rts.getCurrentMap();

                boolean contains = false;

                if(!maps.contains(arenaMap, true)){
                    maps.add(arenaMap);
                    contains = true;
                }

                return contains;
            }
        }, new Action() {
            @Override
            public void performAction(World world, Entity e) {

                RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                ArenaMap arenaMap = rts.getCurrentMap();

                for(Arena arena : arenaMap.getRoomArray()){
                    if(!arenaMap.getVisitedArenas().contains(arena)){
                        switch (arena.arenaType){
                            case TRAP:
                            case NORMAL:
                                arenaMap.getUnvisitedButAdjacentArenas().add(arena);
                                break;
                        }
                    }
                }

            }
        });


        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Conditionals.labyrinthCodex;
    }

}
