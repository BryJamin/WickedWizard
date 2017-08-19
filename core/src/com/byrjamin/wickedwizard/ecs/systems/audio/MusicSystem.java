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


    private static final float rateOfVolumeDecrease = 0.01f;

    private static Mix currentMix = new Mix("");
    private static Mix upComingMix = new Mix("");


    public MusicSystem(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    @Override
    protected void processSystem() {



        if(upComingMix != null && currentMusic != null){

            if(currentMusic.getVolume() <= 0){
                changeTrack(upComingMix);
                upComingMix = null;
            }

            float volume = currentMusic.getVolume() - currentMix.getVolume() * rateOfVolumeDecrease
                    <= 0 ? 0 : currentMusic.getVolume() - currentMix.getVolume() * rateOfVolumeDecrease;

            currentMusic.setVolume(volume);

            return;
        }



        if(currentMusic == null) return;

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        boolean musicOn = preferences.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);



        float volume = currentMusic.getVolume() + currentMix.getVolume() * rateOfVolumeDecrease
                >= currentMix.getVolume() ? currentMix.getVolume() : currentMusic.getVolume() + currentMix.getVolume() * rateOfVolumeDecrease;

        currentMusic.setVolume(volume);

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
        changeTrack(MusicStrings.BG_MAIN_MENU);
    }

    public void playLevelMusic(Level level){
        changeMix(pickLevelMusic(level));
    }

    public void playBossMusic(Level level){
        changeTrack(MusicStrings.BG_MAIN_MENU);
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

    @Override
    protected void dispose() {
        super.dispose();
        stopMusic();
    }
}
