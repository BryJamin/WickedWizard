package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.utils.Bag;
import com.byrjamin.wickedwizard.utils.BagSearch;

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


    public <T> T getPC(Class<T> cls){

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
/*    public Entity getPCEntity(PlayerComponent pc){

        world.getAspectSubscriptionManager().get(Aspect.all(PlayerComponent.class));

        try {
            T t = BagSearch.getObjectOfTypeClass(cls, playerBag);
            if (t == null) {
                throw new Exception("Player Component does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BagSearch.getObjectOfTypeClass(cls, playerBag);
    }*/

}
