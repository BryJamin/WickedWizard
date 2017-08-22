package com.byrjamin.wickedwizard.ecs.components.identifiers;


import com.artemis.Component;

/**
 * Created by BB on 22/08/2017.
 *
 * Component that counts down time
 *
 */

public class ChallengeTimerComponent extends Component {

    public float time = 0;

    public ChallengeTimerComponent(){};

    public ChallengeTimerComponent(float time){
        this.time = time;
    }



}
