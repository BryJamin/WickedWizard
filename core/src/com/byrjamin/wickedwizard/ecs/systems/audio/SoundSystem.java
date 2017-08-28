package com.byrjamin.wickedwizard.ecs.systems.audio;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
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


    private OrderedMap<SoundEmitterComponent, Entity> emitterEntityIds = new OrderedMap<SoundEmitterComponent, Entity>();


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


    @Override
    public void inserted(Entity e) {
        SoundEmitterComponent soundEmitterComponent = e.getComponent(SoundEmitterComponent.class);
        Mix m = soundEmitterComponent.mix;
        sound = assetManager.get(m.getFileName(), Sound.class);

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        boolean soundOn = preferences.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

        soundEmitterComponent.soundId =  sound.loop(soundOn ? m.getVolume() : 0, m.getPitch(), 0);
        emitterEntityIds.put(soundEmitterComponent, e);


        System.out.println("INSIDE INSERTED OF SOUND SYSTEM ");
        System.out.println("sound on is" + soundOn);

    }


    @Override
    protected void processSystem() {

        emitterCheck();
        soundFadeOut();

        for(Mix m : upcomingMixes){
            Sound s = assetManager.get(m.getFileName(), Sound.class);
            s.play(SOUNDON ? m.getVolume() : 0, m.getPitch(), 0);
        }

        upcomingMixes.clear();

    }

    private void soundFadeOut(){



        for(SoundEmitterComponent sec : soundFadeArray){

            sec.mix.setVolume(sec.mix.getVolume() - (world.delta * sec.volumeFadeFactor));
            System.out.println(sec.mix.getVolume());



            if(sec.mix.getVolume() <= 0){
                sound.setVolume(sec.soundId, 0);
                sound.stop(sec.soundId);
                soundFadeArray.removeValue(sec, true);
            } else {
                sound.setVolume(sec.soundId, sec.mix.getVolume());
            }



        }

    }


    /**
     * Checks to see if the entities who are emitting sound are still alive
     * If they aren't alive they are removed from the array of entities and added to
     * a soundfadeArray
     */
    private void emitterCheck(){

        copyEntityArray.addAll(emitterEntityIds.values().toArray());

        for(Entity e : copyEntityArray){
            if(!this.getEntities().contains(e)){
                if(emitterEntityIds.containsValue(e, false)) {
                    SoundEmitterComponent soundEmitterComponent = emitterEntityIds.findKey(e, false);
                    soundFadeArray.add(soundEmitterComponent);
                    //sound.stop(l);
                    emitterEntityIds.remove(soundEmitterComponent);
                }
            }
        }

        copyEntityArray.clear();

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
