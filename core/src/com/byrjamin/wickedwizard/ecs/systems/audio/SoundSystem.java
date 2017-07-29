package com.byrjamin.wickedwizard.ecs.systems.audio;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.MusicStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;

/**
 * Created by BB on 02/06/2017.
 *
 * Sound System plays sounds that are queued up inside of the system per iteration
 *
 * Update:
 * In future it may be prudent to have a range that sounds can be played from.
 * The only issue is you'd need to have a way to track the position the sound is being played from.
 * Which may need to be stored either as a static? method that translates volume based on position or
 * you use Pair to track position and upcoming sounds.
 *
 * This is to avoid hearing sound from miles away that you shouldn't hear
 *
 */

public class SoundSystem extends BaseSystem {

    private AssetManager assetManager;

    private Array<Mix> upcomingMixes = new Array<Mix>();

    public SoundSystem(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    @Override
    protected void processSystem() {

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);

        boolean soundOn = preferences.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

        if(soundOn){
            for(Mix m : upcomingMixes){
                Sound s = assetManager.get(m.getFileName(), Sound.class);
                s.play(m.getVolume(), m.getPitch(), 0);
            }
        }

        upcomingMixes.clear();

    }


    /**
     * Queues up a Mix to be played in the process of the SoundSystem
     * To avoid amplification of sound, if the same mix is played (As all mixes come from the same static methods)
     * that mix is not queued up.
     * @param mix - The mix to be played
     */
    public void playSound(Mix mix){

        if(assetManager.isLoaded(mix.getFileName(), Sound.class) &&
                Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_SOUND, true)){
            if(!upcomingMixes.contains(mix, true)) {
                upcomingMixes.add(mix);
            }
        }
    }

    /**
     * This runs the playSound method for a random mix from an Array of Mixes
     * @param mixes - Array of mixes
     */
    public void playRandomSound(Mix... mixes){
        playSound(mixes[MathUtils.random.nextInt(mixes.length)]);
    }

    @Override
    protected void dispose() {
        super.dispose();
    }
}
