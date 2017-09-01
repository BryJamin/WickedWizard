package com.bryjamin.wickedwizard.ecs.components.audio;

import com.artemis.Component;
import com.bryjamin.wickedwizard.assets.Mix;

/**
 * Created by Home on 24/07/2017.
 */

public class HitSoundComponent extends Component{

    public Mix[] hitSounds = new Mix[]{};

    public HitSoundComponent(){

    }

    public HitSoundComponent(Mix... mixes){
        hitSounds = mixes;
    }

}
