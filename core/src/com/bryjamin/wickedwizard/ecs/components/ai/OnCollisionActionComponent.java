package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;

/**
 * Created by Home on 10/06/2017.
 */

public class OnCollisionActionComponent extends Component{

    public Action left;
    public Action right;
    public Action top;
    public Action bottom;

    public OnCollisionActionComponent(){
    }

    public OnCollisionActionComponent(Action left, Action right, Action top, Action bottom){
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }


}
