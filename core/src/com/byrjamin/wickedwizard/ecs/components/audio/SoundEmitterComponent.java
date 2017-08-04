package com.byrjamin.wickedwizard.ecs.components.audio;

import com.artemis.Component;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;

/**
 * Created by Home on 04/08/2017.
 */

public class SoundEmitterComponent extends Component {

    public Mix mix;

    public long soundId;

    public SoundEmitterComponent(){
        mix = SoundFileStrings.jumpMix;
    }

    public SoundEmitterComponent(Mix mix){
        this.mix = mix;
    }



}
