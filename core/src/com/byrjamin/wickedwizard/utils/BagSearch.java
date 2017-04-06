package com.byrjamin.wickedwizard.utils;

import com.artemis.utils.Bag;

/**
 * Created by Home on 19/03/2017.
 */

public class BagSearch {

    public static <T, B> boolean contains(Class<T> cls, Bag<B> bag){
        for(B ok : bag){
            if(ok.getClass() == cls){
                return true;
            }
        }
        return false;
    }

    public static <T, B> T getObjectOfTypeClass(Class<T> cls, Bag<B> bag){
        for(B ok : bag){
            if(ok.getClass() == cls){
                return cls.cast(ok);
            }
        }
        return null;
    }

}
