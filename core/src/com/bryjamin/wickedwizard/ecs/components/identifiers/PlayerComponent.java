package com.bryjamin.wickedwizard.ecs.components.identifiers;

import com.artemis.Component;

/**
 * Created by Home on 03/03/2017.
 */
public class PlayerComponent extends Component{

    public String id = "";

    public PlayerComponent(){

    }

    public PlayerComponent(String id){
        this.id = id;
    }

    public static int DEFAULT = 0;
    public static int FIRING = 1;


}
