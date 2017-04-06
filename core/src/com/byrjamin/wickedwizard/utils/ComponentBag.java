package com.byrjamin.wickedwizard.utils;

import com.artemis.Component;
import com.artemis.utils.Bag;


/**
 * Extension of the Bag<Component> class used to throw an error if duplicate
 * components are added into the bag
 */
public class ComponentBag extends Bag<Component> {

    /**
     * The componenet Bag checks to see if you are adding duplicate components.
     * @param component - Component to be added
     */
    @Override
    public void add(Component component) {

        if(BagSearch.contains(component.getClass(), this)){
            //TODO throw exception
        }

        super.add(component);
    }
}
