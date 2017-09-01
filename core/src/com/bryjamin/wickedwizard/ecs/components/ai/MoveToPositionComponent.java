package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Home on 04/07/2017.
 */

public class MoveToPositionComponent extends Component{

    public Vector3 moveToPosition;


    public boolean isOnTargetX;

    public MoveToPositionComponent(){
        moveToPosition = new Vector3();
    }


    public MoveToPositionComponent(Vector3 moveToPosition){
        this.moveToPosition = moveToPosition;
    }


}
