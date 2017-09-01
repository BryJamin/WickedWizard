package com.byrjamin.wickedwizard.ecs.systems.audio;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.ecs.components.audio.SoundEmitterComponent;

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

public class SoundSystem extends EntitySystem {


    private static final float MASTER_VOLUME = 1.0f;

    private AssetManager assetManager;

    private Array<Mix> upcomingMixes = new Array<Mix>();

    private Array<Mix[]> upcomingMixesMixes = new Array<Mix[]>();


    private Array<Music> musicToBeDisposed = new Array<Music>();

    private OrderedMap<SoundEmitterComponent, Entity> activeEmitterMap = new OrderedMap<SoundEmitterComponent, Entity>();
    private OrderedMap<SoundEmitterComponent, Music> activeLoopedMusic = new OrderedMap<SoundEmitterComponent, Music>();

    private Array<SoundEmitterComponent> soundFadeArray = new Array<SoundEmitterComponent>();


    public static boolean SOUNDON = false;

    public float silenceSoundId;



    private Sound sound;

    public SoundSystem(AssetManager assetManager){
        super(Aspect.all(SoundEmitterComponent.class));
        this.assetManager = assetManager;
        sound = assetManager.get(SoundFileStrings.soundOfSilence, Sound.class);
        silenceSoundId = sound.loop(0f);
        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        SOUNDON =  preferences.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if(!enabled){
            sound.stop();
            for(Music m : activeLoopedMusic.values().toArray()){
                m.pause();
            }
        } else {
            for(Music m : activeLoopedMusic.values().toArray()){
                m.play();
            }
        }

    }

    @Override
    protected void processSystem() {



        for(Entity e : this.getEntities()){

            SoundEmitterComponent soundEmitterComponent = e.getComponent(SoundEmitterComponent.class);

            boolean isNewSound = true;

            for(SoundEmitterComponent sec : activeEmitterMap.orderedKeys()){

                if(soundEmitterComponent.mix.equals(sec.mix) || soundEmitterComponent.equals(sec)){
                    isNewSound = false;
                }

            }

            if(isNewSound) {

                Music loopedSound = Gdx.audio.newMusic(Gdx.files.internal(soundEmitterComponent.mix.getFileName()));
                loopedSound.setLooping(true);
                loopedSound.setVolume(SOUNDON ? soundEmitterComponent.mix.getVolume() * MASTER_VOLUME : 0);
                loopedSound.play();


                activeLoopedMusic.put(soundEmitterComponent, loopedSound);
                activeEmitterMap.put(soundEmitterComponent, e);
            }

        }

        soundFadeOut();

        for(Mix m : upcomingMixes){
            Sound s = assetManager.get(m.getFileName(), Sound.class);
            s.play(SOUNDON ? m.getVolume() * MASTER_VOLUME : 0, m.getPitch(), 0);
        }

        upcomingMixes.clear();
        upcomingMixesMixes.clear();

        if(musicToBeDisposed.size > 5){
            this.disposeMusic();
        }

    }

    private void soundFadeOut(){


        for(SoundEmitterComponent sec : soundFadeArray){


            sec.currentVolume -= world.delta * sec.volumeFadeFactor * MASTER_VOLUME;

            if(sec.currentVolume <= 0){
                Music m = Gdx.audio.newMusic(Gdx.files.internal(sec.mix.getFileName())); //activeLoopedMusic.get(sec);
                m.setVolume(0);
                m.stop();
                m.dispose();
                //musicToBeDisposed.add(m);
                soundFadeArray.removeValue(sec, true);
                activeLoopedMusic.remove(sec);
            } else {
                Music m = activeLoopedMusic.get(sec);
                m.setVolume(SOUNDON ? sec.currentVolume * MASTER_VOLUME : 0);
            }



        }

    }


    @Override
    public void removed(Entity e) {

        if(activeEmitterMap.containsValue(e, false)){
            SoundEmitterComponent soundEmitterComponent = activeEmitterMap.findKey(e, false);
            soundFadeArray.add(soundEmitterComponent);
            activeEmitterMap.remove(soundEmitterComponent);
        }

    }

    public void disposeMusic(){
        for(Music m : musicToBeDisposed){
            m.dispose();
        }
        musicToBeDisposed.clear();
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

        if(!upcomingMixesMixes.contains(mixes, true)) {
            playSound(mixes[MathUtils.random.nextInt(mixes.length)]);
            upcomingMixesMixes.add(mixes);
        }
    }

    @Override
    protected void dispose() {
        super.dispose();
        sound.stop();
    }
}
