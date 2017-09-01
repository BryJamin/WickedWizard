package com.bryjamin.wickedwizard.ecs.components.identifiers;

import com.artemis.Component;

/**
 * Created by Home on 05/04/2017.
 */

public class ChildComponent extends Component {

    public ChildComponent(){

    }

    public ChildComponent(ParentComponent pc){
        pc.children.add(this);
    }

}
