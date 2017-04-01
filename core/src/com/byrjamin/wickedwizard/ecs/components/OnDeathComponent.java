package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.utils.ComponentBag;


/**
 * Created by Home on 01/04/2017.
 */

public class OnDeathComponent extends Component {


    private Array<ComponentBag> componentBags = new Array<ComponentBag>();


    public void addComponenetsToBag(Component... components){

        ComponentBag bag = new ComponentBag();

        for(Component c : components) {
            bag.add(c);
        }

        componentBags.add(bag);

    }

    public Array<ComponentBag> getComponentBags() {
        return componentBags;
    }
}
