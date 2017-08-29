package com.byrjamin.wickedwizard.ecs.components.audio;

import com.artemis.Component;
import com.badlogic.gdx.audio.Music;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;

/**
 * Created by Home on 04/08/2017.
 */

public class SoundEmitterComponent extends Component {

    public Mix mix;
    public Music music;


    public float volumeFadeTime  = 1;

    public float volumeFadeFactor = 1;
    public float currentVolume = 0;

    public long soundId;

    public SoundEmitterComponent(){
        mix = SoundFileStrings.jumpMix;
    }

    public SoundEmitterComponent(Mix mix){
        this.mix = mix;
        this.currentVolume = mix.getVolume();
    }

    public SoundEmitterComponent(Mix mix, float volumeFadeTime){
        this.mix = mix;
        this.volumeFadeFactor = volumeFadeTime > 0 ? (mix.getVolume() / volumeFadeTime) : mix.getVolume();
        this.currentVolume = mix.getVolume();
        //System.out.println(volumeFadeFactor);
    }



}
