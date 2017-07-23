package com.byrjamin.wickedwizard.ecs.systems.audio;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.MusicStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;

/**
 * Created by Home on 02/06/2017.
 */

public class SoundSystem extends BaseSystem {

    private AssetManager assetManager;

    private Array<Sound> upcomingSounds = new Array<Sound>();

    private Array<Mix> upcomingMixes = new Array<Mix>();



    public SoundSystem(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    @Override
    protected void processSystem() {

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);

        boolean soundOn = preferences.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

        if(soundOn){
            for(Sound s : upcomingSounds){
                s.play();

            }

            for(Mix m : upcomingMixes){
                Sound s = assetManager.get(m.getFileName(), Sound.class);
                s.play(m.getVolume());
            }

        }

        if(upcomingMixes.size > 0) {
            System.out.println(upcomingMixes.size);
        }

        upcomingMixes.clear();
        upcomingSounds.clear();

    }

    //TODO idea track sounds played within the same game frame. If it is the same sound then do not play is.
    //TODO may extend this to multiple game frames (max of 2?)

    //TODO number of sounds avalaible to play within one game frame could be something like 5? They have pritoriy attached


    public void playSound(String soundFile){
        if(assetManager.isLoaded(soundFile, Sound.class) &&
                Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_SOUND, true)){
            Sound s = assetManager.get(soundFile, Sound.class);
            s.play();
            //s.stop();
        }
    }

    public void playSound(String soundFile, float volume){
        if(assetManager.isLoaded(soundFile, Sound.class) &&
                Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_SOUND, true)){
            Sound s = assetManager.get(soundFile, Sound.class);
            s.play(volume);
        }
    }

    public void playSound(Mix mix){

        if(assetManager.isLoaded(mix.getFileName(), Sound.class) &&
                Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_SOUND, true)){
            if(!upcomingMixes.contains(mix, true)) {
                upcomingMixes.add(mix);
            }
        }
    }

    @Override
    protected void dispose() {
        super.dispose();
    }
}
