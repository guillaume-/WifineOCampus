package com.neocampus.wifishared.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Field;

/**
 * ParcelableUtils permet d'éffectuer des actions en relation avec le transtypage
 */
public class ParcelableUtils {

    /**
     * Transtype un objet de type Parcelable en tableau de byte
     * @param parceable objet à convertir
     * @return tableau de byte
     */
    public static byte[] marshall(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    /**
     * Transtype un tableau de byte en objet de type Parcel
     * @param bytes tableau de byte à convertir
     * @return tableau de byte
     */
    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // This is extremely important!
        return parcel;
    }

    /**
     * Transtype un tableau de byte en une certaine instance de classe
     * @param bytes tableau de byte à convertir
     * @param aClassParcelable classe de conversion
     * @return instance de la classe
     */
    public static <T> T unmarshall(byte[] bytes, Class<T> aClassParcelable)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = aClassParcelable.getField("CREATOR");
        return unmarshall(bytes, (Parcelable.Creator<T>) field.get(null));
    }

    /**
     * * Transtype un tableau de byte en une certaine instance via un gestionnaire de conversion
     * @param bytes tableau de byte à convertir
     * @param creator gestionnaire de conversion
     * @param <T> type dans lequel convertir
     * @return instance résultant de la conversion
     */
    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }
}
