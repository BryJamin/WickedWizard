package com.byrjamin.wickedwizard.ecs.components.audio;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.assets.Mix;

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
