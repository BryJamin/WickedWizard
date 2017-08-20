package com.byrjamin.wickedwizard.ecs.systems.audio;


import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.MusicStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.utils.enums.Level;

/**
 * Music System plays music based on what level you are on.
 *
 * Requires both the room Transition system and the ChangeLevelSystem to work when the game is running
 *
 * Is also used in the Main Menu to play the menu theme.
 *
 */
public class MusicSystem extends BaseSystem {

    private AssetManager assetManager;

    private static Music currentMusic;
    private static Music upcomingMusic;


    private static final float rateOfVolumeDecrease = 0.02f;

    private static Mix currentMix = new Mix("");
    private static Mix upComingMix = new Mix("");



    private enum MusicState {
        PLAYING, PAUSED, WAITING
    }

    private MusicState musicState = MusicState.WAITING;
    private MusicState prePuaseState = MusicState.WAITING;

    private enum FadeState {
        FADE_OUT, FADE_IN, NORMAL
    }

    private FadeState fadeState = FadeState.FADE_IN;


    public MusicSystem(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    @Override
    protected void processSystem() {

/*
        System.out.println(fadeState);
        System.out.println(musicState);*/

        switch(musicState){

            case WAITING:
                if(upComingMix != null){
                    changeTrack(upComingMix);
                    musicState = MusicState.PLAYING;
                    fadeState = FadeState.FADE_IN;
                    upComingMix = null;
                }
                break;

            case PLAYING:

                float volume;

                switch (fadeState) {

                    case FADE_IN:

                        volume = currentMusic.getVolume() + currentMix.getVolume() * rateOfVolumeDecrease
                                >= currentMix.getVolume() ? currentMix.getVolume() : currentMusic.getVolume() + currentMix.getVolume() * rateOfVolumeDecrease;
                        currentMusic.setVolume(volume);

                        if(currentMusic.getVolume() == currentMix.getVolume()) fadeState = FadeState.NORMAL;

                        break;

                    case FADE_OUT:

                        volume = currentMusic.getVolume() - currentMix.getVolume() * rateOfVolumeDecrease
                                <= 0 ? 0 : currentMusic.getVolume() - currentMix.getVolume() * rateOfVolumeDecrease;
                        currentMusic.setVolume(volume);

                        if(currentMusic.getVolume() <= 0) musicState = MusicState.WAITING;

                        break;

                }

                if((fadeState == FadeState.NORMAL || fadeState == FadeState.FADE_IN) && upComingMix != null){
                    fadeState = FadeState.FADE_OUT;
                }

        }


        if(currentMusic == null) return;

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        boolean musicOn = preferences.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);

        if(currentMusic.isPlaying() && !musicOn){
            stopMusic();
        }

        if(musicOn && !currentMusic.isPlaying()) {
            currentMusic.play();
        }

    }

    public void playShopMusic(){

    }


    public void playMainMenuMusic(){
        changeMix(MusicStrings.BG_MAIN_MENU);
    }

    public void playLevelMusic(Level level){
        changeMix(pickLevelMusic(level));
    }

    public void playBossMusic(Level level){
        changeMix(MusicStrings.BG_MAIN_MENU);
    }



    public void changeTrack(Mix mix){


        if(mix.equals(currentMix)) return;


        if(currentMusic != null){


            upComingMix = mix;

            if(currentMusic.isPlaying()) {
                currentMusic.stop();
                currentMusic.dispose();
            }
        }

        currentMix = mix;

        if(assetManager.isLoaded(mix.getFileName(), Music.class)){
            currentMusic = Gdx.audio.newMusic(Gdx.files.internal(mix.getFileName()));
            currentMusic.setLooping(true);
            currentMusic.setVolume(0);
        }
    }

    public void changeMix(Mix mix){
        if(mix.equals(currentMix)) return;

        if(assetManager.isLoaded(mix.getFileName(), Music.class)){
            upComingMix = mix;
        }
    }

    public void fadeOutMusic(){
        fadeState = FadeState.FADE_OUT;
    }



    private Mix pickLevelMusic(Level level){

        Mix mix;
            switch(level){
                case ONE:
                    default:
                    mix = MusicStrings.BG_LEVEL_ONE;
                        break;
                case TWO: mix = MusicStrings.BG_LEVEL_TWO;
                    break;
                case THREE: mix = MusicStrings.BG_LEVEL_THREE;
                    break;
                case FOUR: mix = MusicStrings.BG_LEVEL_FOUR;
                    break;
                case FIVE: mix = MusicStrings.BG_LEVEL_FIVE;
                    break;

            }

        return  mix;
    }


    public void stopMusic(){
        if(currentMusic != null) {
            currentMusic.stop();
        }
    }


    public void pauseMusic(){
        if(currentMusic != null){
            currentMusic.pause();
        }

        prePuaseState = musicState == MusicState.PLAYING ? MusicState.PLAYING : MusicState.WAITING;
        musicState = MusicState.PAUSED;

    }

    public void resumeMusic(){

        musicState = prePuaseState;
        if(currentMusic != null){

            Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
            boolean musicOn = preferences.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);

            if(musicOn) {
                currentMusic.play();
            }
        }
    }



    @Override
    protected void dispose() {
        super.dispose();
        stopMusic();
    }
}
