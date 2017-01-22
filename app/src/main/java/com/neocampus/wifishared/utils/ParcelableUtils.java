package com.neocampus.wifishared.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Field;

/**
 * Created by Hirochi ☠ on 12/01/17.
 */

public class ParcelableUtils {
    public static byte[] marshall(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // This is extremely important!
        return parcel;
    }

    public static <T> T unmarshall(byte[] bytes, Class<T> aClassParcelable)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = aClassParcelable.getField("CREATOR");
        return unmarshall(bytes, (Parcelable.Creator<T>) field.get(null));
    }

    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }
}
