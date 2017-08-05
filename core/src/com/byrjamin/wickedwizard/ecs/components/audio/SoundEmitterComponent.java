package com.byrjamin.wickedwizard.ecs.components.audio;

import com.artemis.Component;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;

/**
 * Created by Home on 04/08/2017.
 */

public class SoundEmitterComponent extends Component {

    public Mix mix;

    public float volumeFadeTime  = 1;

    public float volumeFadeFactor = 1;

    public long soundId;

    public SoundEmitterComponent(){
        mix = SoundFileStrings.jumpMix;
    }

    public SoundEmitterComponent(Mix mix){
        this.mix = mix;
    }

    public SoundEmitterComponent(Mix mix, float volumeFadeTime){
        this.mix = mix;
        this.volumeFadeFactor = volumeFadeTime > 0 ? (mix.getVolume() / volumeFadeTime) : mix.getVolume();
        System.out.println(volumeFadeFactor);
    }



}
