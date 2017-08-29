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

    private ComponentMapper<SoundEmitterComponent> soundMapper;

    private AssetManager assetManager;

    private Array<Mix> upcomingMixes = new Array<Mix>();



    private OrderedMap<SoundEmitterComponent, Entity> activeEmitterMap = new OrderedMap<SoundEmitterComponent, Entity>();


    Array<Entity> copyEntityArray = new Array<Entity>();


    private Array<SoundEmitterComponent> soundFadeArray = new Array<SoundEmitterComponent>();


    public static boolean SOUNDON = false;



    private Sound sound;

    public SoundSystem(AssetManager assetManager){
        super(Aspect.all(SoundEmitterComponent.class));
        this.assetManager = assetManager;
        sound = assetManager.get(SoundFileStrings.coinPickUp, Sound.class);

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        SOUNDON =  preferences.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

    }


/*    @Override
    public void inserted(Entity e) {
        SoundEmitterComponent soundEmitterComponent = e.getComponent(SoundEmitterComponent.class);
        Mix m = soundEmitterComponent.mix;
        sound = assetManager.get(m.getFileName(), Sound.class);

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        boolean soundOn = preferences.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

        soundEmitterComponent.soundId =  sound.loop(soundOn ? m.getVolume() : 0, m.getPitch(), 0);
        activeEmitterMap.put(soundEmitterComponent, e);


        System.out.println("INSIDE INSERTED OF SOUND SYSTEM ");
        System.out.println("sound on is" + soundOn);

    }*/


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
                loopedSound.setVolume(SOUNDON ? soundEmitterComponent.mix.getVolume() : 0);
                loopedSound.play();

                System.out.println("soundEmitterComponent mix volume is " +  soundEmitterComponent.mix.getVolume());
                System.out.println("Volume is: + " + loopedSound.getVolume());

                soundEmitterComponent.music = loopedSound;
                activeEmitterMap.put(soundEmitterComponent, e);
            }

        }

        soundFadeOut();

        for(Mix m : upcomingMixes){
            Sound s = assetManager.get(m.getFileName(), Sound.class);
            s.play(SOUNDON ? m.getVolume() : 0, m.getPitch(), 0);
        }

        upcomingMixes.clear();

    }

    private void soundFadeOut(){


        for(SoundEmitterComponent sec : soundFadeArray){

            sec.currentVolume -= world.delta * sec.volumeFadeFactor;

            if(sec.currentVolume <= 0){
                sec.music.setVolume(0);
                sec.music.stop();
                sec.music.dispose();
                soundFadeArray.removeValue(sec, true);
            } else {
                sec.music.setVolume(SOUNDON ? sec.currentVolume : 0);
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
        sound.stop();

    }
}
