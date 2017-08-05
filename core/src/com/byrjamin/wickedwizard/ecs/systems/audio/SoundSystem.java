package com.byrjamin.wickedwizard.ecs.systems.audio;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
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
import com.badlogic.gdx.utils.Queue;
import com.byrjamin.wickedwizard.assets.Mix;
import com.byrjamin.wickedwizard.assets.MusicStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.SoundFileStrings;
import com.byrjamin.wickedwizard.ecs.components.audio.SoundEmitterComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;

import java.util.Map;

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


    private Array<SoundEmitterComponent> soundFadeArray = new Array<SoundEmitterComponent>();



    private Sound sound;

    public SoundSystem(AssetManager assetManager){
        super(Aspect.all(SoundEmitterComponent.class));
        this.assetManager = assetManager;
        sound = assetManager.get(SoundFileStrings.coinPickUp, Sound.class);

    }
/*
    public void insertSoundEmitter(Entity e, Mix m){
        SoundEmitterComponent soundEmitterComponent = new SoundEmitterComponent(m);
        sound = assetManager.get(m.getFileName(), Sound.class);
        soundEmitterComponent.soundId =  sound.loop(m.getVolume(), m.getPitch(), 0);
        e.edit().add(soundEmitterComponent);
    }

    public void removeSoundEmitter(Entity e){
        SoundEmitterComponent soundEmitterComponent = soundMapper.get(e);
        sound.stop(soundEmitterComponent.soundId);
        e.edit().remove(soundEmitterComponent);
    }*/

    @Override
    public void inserted(Entity e) {
        SoundEmitterComponent soundEmitterComponent = e.getComponent(SoundEmitterComponent.class);
        Mix m = soundEmitterComponent.mix;
        sound = assetManager.get(m.getFileName(), Sound.class);

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        boolean soundOn = preferences.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

        soundEmitterComponent.soundId =  sound.loop(soundOn ? m.getVolume() : 0, m.getPitch(), 0);
        emitterEntityIds.put(soundEmitterComponent, e);
    }


    @Override
    protected void processSystem() {

        Preferences preferences = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);

        boolean soundOn = preferences.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

        emitterCheck();
        soundFadeOut();

        if(soundOn){
            for(Mix m : upcomingMixes){
                Sound s = assetManager.get(m.getFileName(), Sound.class);

                s.play(m.getVolume(), m.getPitch(), 0);
            }

        } else {
            sound.stop();
        }

        upcomingMixes.clear();

    }

    private void soundFadeOut(){



        for(SoundEmitterComponent sec : soundFadeArray){

            sec.mix.setVolume(sec.mix.getVolume() - (world.delta * 0.35f));
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



    private void emitterCheck(){


        Array<Entity> array = new Array<Entity>();
        array.addAll(emitterEntityIds.values().toArray());

        for(Entity e : array){
            if(!this.getEntities().contains(e)){
                if(emitterEntityIds.containsValue(e, false)) {
                    SoundEmitterComponent soundEmitterComponent = emitterEntityIds.findKey(e, false);
                    soundFadeArray.add(soundEmitterComponent);
                    //sound.stop(l);
                    emitterEntityIds.remove(soundEmitterComponent);
                }
            }
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
