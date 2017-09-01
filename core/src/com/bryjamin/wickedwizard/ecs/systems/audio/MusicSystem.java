package com.bryjamin.wickedwizard.ecs.systems.audio;


import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.bryjamin.wickedwizard.assets.Mix;
import com.bryjamin.wickedwizard.assets.MusicStrings;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;

/**
 * Music System plays music based on what level you are on.
 *
 * Requires both the room Transition system and the ChangeLevelSystem to work when the game is running
 *
 * Is also used in the Main Menu to play the menu theme.
 *
 */
public class MusicSystem extends BaseSystem {


    private static final float MASTER_VOLUME = 1.0f;

    private static Music currentMusic;

    private static final float rateOfVolumeDecrease = 0.02f;

    private static Mix currentMix = new Mix("");
    private static Mix upComingMix = new Mix("");


    public static boolean MUSIC_ON = false;



    private enum MusicState {
        PLAYING, PAUSED, WAITING
    }

    private MusicState musicState = MusicState.WAITING;
    private MusicState prePuaseState = MusicState.WAITING;

    private enum FadeState {
        FADE_OUT, FADE_IN, NORMAL
    }

    private FadeState fadeState = FadeState.FADE_IN;


    public MusicSystem(){

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        MUSIC_ON =  preferences.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);
    }

    @Override
    protected void processSystem() {


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

                        currentMusic.setVolume(volume * MASTER_VOLUME);

                        if(currentMusic.getVolume() == currentMix.getVolume() * MASTER_VOLUME) fadeState = FadeState.NORMAL;

                        break;

                    case FADE_OUT:

                        volume = currentMusic.getVolume() - currentMix.getVolume() * rateOfVolumeDecrease
                                <= 0 ? 0 : currentMusic.getVolume() - currentMix.getVolume() * rateOfVolumeDecrease;
                        currentMusic.setVolume(volume * MASTER_VOLUME);

                        if(currentMusic.getVolume() <= 0) musicState = MusicState.WAITING;

                        break;

                    case NORMAL:

                        if(currentMusic.getVolume() != currentMix.getVolume() * MASTER_VOLUME && MUSIC_ON){
                            currentMusic.setVolume(currentMix.getVolume() * MASTER_VOLUME);
                        }

                }

                if((fadeState == FadeState.NORMAL || fadeState == FadeState.FADE_IN) && upComingMix != null){
                    fadeState = FadeState.FADE_OUT;
                }

        }

        if(currentMusic == null) return;

        if(!MUSIC_ON){
            if(currentMusic.getVolume() != 0) currentMusic.setVolume(0);
        } else {
            if(!currentMusic.isPlaying()) {
                currentMusic.play();
            }
        }

    }

    public void playShopMusic(){

    }


    public void playMainMenuMusic(){
        changeMix(MusicStrings.BG_MAIN_MENU);
    }

    public void playLevelMusic(com.bryjamin.wickedwizard.utils.enums.Level level){
        changeMix(pickLevelMusic(level));
    }

    public void playBossMusic(com.bryjamin.wickedwizard.utils.enums.Level level){
        changeMix(MusicStrings.BG_MAIN_MENU);
    }



    private void changeTrack(Mix mix){


        if(mix.equals(currentMix)) return;

        if(currentMusic != null){
            currentMusic.stop();
            currentMusic.dispose();
        }

        currentMix = mix;

        if(Gdx.files.internal(mix.getFileName()).exists()) {
            currentMusic = Gdx.audio.newMusic(Gdx.files.internal(mix.getFileName()));
            currentMusic.setLooping(true);
            currentMusic.setVolume(0);
        }
    }

    public void changeMix(Mix mix){
        if(mix.equals(currentMix)) return;

        if(Gdx.files.internal(mix.getFileName()).exists()) {
            upComingMix = mix;
        }
    }

    public void fadeOutMusic(){
        fadeState = FadeState.FADE_OUT;
    }



    private Mix pickLevelMusic(com.bryjamin.wickedwizard.utils.enums.Level level){

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


    public void muteMusic(){
        if(currentMusic != null) {
            currentMusic.setVolume(0);
            MUSIC_ON = false;
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

            if(MUSIC_ON) {
                currentMusic.play();
            }
        }
    }



    @Override
    protected void dispose() {
        super.dispose();
        //muteMusic();
    }
}
