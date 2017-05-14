package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Home on 14/05/2017.
 */

public class ExpiryRangeComponent extends Component {

    public Vector3 startingPosition;
    public Vector3 endPosition;

    public Vector3 currentPosition;

    public float range;
    public float distanceTravelled;

    public ExpiryRangeComponent(){
        startingPosition = new Vector3();
        endPosition = new Vector3();
    }

    public ExpiryRangeComponent(Vector3 startingPosition, float range){
        this.currentPosition = startingPosition;
        this.range = range;
    }

    public ExpiryRangeComponent(Vector3 startingPosition, Vector3 endPosition){
        this.startingPosition = startingPosition;
        this.endPosition = endPosition;
    }





}
