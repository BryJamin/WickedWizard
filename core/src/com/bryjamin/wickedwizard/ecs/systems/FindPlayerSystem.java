package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.utils.BagSearch;


/**
 * Created by Home on 11/03/2017.
 */
public class FindPlayerSystem extends BaseSystem {

    private Bag<Component> playerBag;

    public FindPlayerSystem(Bag<Component> playerBag) {
        this.playerBag = playerBag;
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }


    public <T> T getPlayerComponent(Class<T> cls){

        try {
            T t = BagSearch.getObjectOfTypeClass(cls, playerBag);
            if (t == null) {
                throw new Exception("Player Component does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BagSearch.getObjectOfTypeClass(cls, playerBag);
    }

    public Entity getPlayerEntity(){

        IntBag bag = world.getAspectSubscriptionManager().get(Aspect.all(PlayerComponent.class)).getEntities();

        for(int i = 0; i < bag.size(); i++){
            if(world.getEntity(bag.get(i)).getComponent(PlayerComponent.class) == BagSearch.getObjectOfTypeClass(PlayerComponent.class, playerBag)){
                return world.getEntity(bag.get(i));
            };
        };


        return null;
    }



}
