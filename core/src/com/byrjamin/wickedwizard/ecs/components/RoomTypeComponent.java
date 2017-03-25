package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 25/03/2017.
 */

public class RoomTypeComponent extends Component{

    public enum Type {
        BATTLE, ITEM, SHOP
    }

    private Type type;

    public RoomTypeComponent(){
        this(Type.BATTLE);
    }

    public RoomTypeComponent(Type type){
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
