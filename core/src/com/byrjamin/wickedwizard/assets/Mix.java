package com.byrjamin.wickedwizard.assets;

/**
 * Created by Home on 03/06/2017.
 */

public class Mix {

    private String fileName;
    private float volume;
    private float pitch = 1.0f;

    public Mix(String fileName) {
        this.fileName = fileName;
    }

    public Mix(String fileName, float volume) {
        this.fileName = fileName;
        this.volume = volume;
    }


    public Mix(MixMaker  mixMaker) {
        this.fileName = mixMaker.file;
        this.volume = mixMaker.volume;
        this.pitch = mixMaker.pitch;
    }

    public static class MixMaker {

        //Mandatory
        private final String file;

        //Optional
        private float volume = 1.0f;
        private float pitch = 1.0f;

        public MixMaker(String file){
            this.file = file;
        }

        public MixMaker volume(float val)
        { volume = val; return this; }

        public MixMaker pitch(float val)
        { pitch = val; return this; }

        public Mix build(){
            return new Mix(this);
        }
    }


    public String getFileName() {
        return fileName;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
