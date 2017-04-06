package com.byrjamin.wickedwizard.utils;

import com.artemis.Component;
import com.artemis.utils.Bag;

/**
 * Created by Home on 30/03/2017.
 */

public class ComponentBag extends Bag<Component> {

    @Override
    public void add(Component component) {

        if(BagSearch.contains(component.getClass(), this)){
            //TODO throw exception
        }

        super.add(component);
    }
}
