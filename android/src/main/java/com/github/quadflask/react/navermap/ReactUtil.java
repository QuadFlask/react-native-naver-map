package com.github.quadflask.react.navermap;

import android.graphics.Color;
import android.graphics.PointF;
import androidx.core.util.Consumer;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ReactUtil {
    public static int parseColorString(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        } else if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }

        if (hex.length() == 6) { // rgb
            int rgb = Integer.valueOf(hex, 16);
            return 0xff000000 | rgb;
        } else if (hex.length() == 10) { // argb
            int a = Integer.valueOf(hex.substring(0, 2), 16);
            int rgb = Integer.valueOf(hex.substring(2), 16);
            return a << 24 | rgb;
        }
        return Color.BLACK;
    }

    public static LatLng toNaverLatLng(ReadableMap map) {
        final double latitude = map.getDouble("latitude");
        final double longitude = map.getDouble("longitude");

        return new LatLng(latitude, longitude);
    }

    public static List<LatLng> toLatLngList(ReadableArray array) {
        List<LatLng> coords = new ArrayList<>(array.size());

        for (int i = 0; i < array.size(); i++)
            coords.add(toNaverLatLng(array.getMap(i)));

        return coords;
    }

    public static void getNumber(ReadableMap option, String key, Consumer<Double> consumer) {
        if (option.hasKey(key))
            consumer.accept(option.getDouble(key));
    }

    public static void getFloat(ReadableMap option, String key, Consumer<Float> consumer) {
        if (option.hasKey(key))
            consumer.accept((float) option.getDouble(key));
    }

    public static void getInt(ReadableMap option, String key, Consumer<Integer> consumer) {
        if (option.hasKey(key))
            consumer.accept(option.getInt(key));
    }

    public static void getString(ReadableMap option, String key, Consumer<String> consumer) {
        if (option.hasKey(key))
            consumer.accept(option.getString(key));
    }

    public static void getBoolean(ReadableMap option, String key, Consumer<Boolean> consumer) {
        if (option.hasKey(key))
            consumer.accept(option.getBoolean(key));
    }

    public static void getHexColor(ReadableMap option, String key, Consumer<Integer> consumer) {
        getString(option, key, s -> consumer.accept(parseColorString(s)));
    }

    public static void getLatLng(ReadableMap option, String key, Consumer<LatLng> consumer) {
        if (option.hasKey(key) && option.hasKey("latitude") && option.hasKey("longitude"))
            consumer.accept(new LatLng(option.getDouble("latitude"), option.getDouble("longitude")));
    }

    public static void getLatLngList(ReadableMap option, String key, Consumer<List<LatLng>> consumer) {
        if (option.hasKey(key)) {
            consumer.accept(toLatLngList(option.getArray(key)));
        }
    }

    public static void getAnchor(ReadableMap option, Consumer<PointF> consumer) {
        if (option.hasKey("anchor")) {
            PointF anchor = new PointF(0.5f, 1);  // default anchor to bottom
            final ReadableArray anchorArray = option.getArray("anchor");
            anchor.x = (float) anchorArray.getDouble(0);
            anchor.y = (float) anchorArray.getDouble(1);
            consumer.accept(anchor);
        }
    }
}
