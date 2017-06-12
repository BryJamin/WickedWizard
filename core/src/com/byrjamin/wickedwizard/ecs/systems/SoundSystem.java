package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.MusicStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;

/**
 * Created by Home on 02/06/2017.
 */

public class SoundSystem extends BaseSystem {

    private AssetManager assetManager;

    private Music currentMusic;
    private Sound sound;

    public SoundSystem(AssetManager assetManager){
        this.assetManager = assetManager;
        currentMusic = assetManager.get(MusicStrings.song8);
    }

    @Override
    protected void processSystem() {

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        boolean musicOn = preferences.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);
        if(currentMusic.isPlaying() && !musicOn){
            stopMusic();
        }

        if(musicOn && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    //TODO idea track sounds played within the same game frame. If it is the same sound then do not play is.
    //TODO may extend this to multiple game frames (max of 2?)

    //TODO number of sounds avalaible to play within one game frame could be something like 5? They have pritoriy attached



    public void playMusic(String musicFile){
        if(assetManager.isLoaded(musicFile, Music.class)){
            currentMusic = assetManager.get(musicFile, Music.class);
            currentMusic.setLooping(true);
            //if(Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_MUSIC, true)
        }
    }

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
            Sound s = assetManager.get(mix.getFileName(), Sound.class);
            s.play(mix.getVolume());
        }
    }

    public void stopMusic(){
        currentMusic.stop();
    }

    @Override
    protected void dispose() {
        super.dispose();
        stopMusic();
    }
}
