package com.airbnb.android.react.maps;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;

public class ViewChangesTracker {

    private static ViewChangesTracker instance;
    private Handler handler;
    private LinkedList<TrackableView> markers = new LinkedList<>();
    private boolean hasScheduledFrame = false;
    private Runnable updateRunnable;
    private final long fps = 2; // FIXME flickering custom view

    private ViewChangesTracker() {
        handler = new Handler(Looper.myLooper());
        updateRunnable = () -> {
            hasScheduledFrame = false;
            update();

            if (markers.size() > 0) {
                handler.postDelayed(updateRunnable, 1000 / fps);
            }
        };
    }

    public static ViewChangesTracker getInstance() {
        if (instance == null) {
            synchronized (ViewChangesTracker.class) {
                instance = new ViewChangesTracker();
            }
        }

        return instance;
    }

    public void addMarker(TrackableView marker) {
        markers.add(marker);

        if (!hasScheduledFrame) {
            hasScheduledFrame = true;
            handler.postDelayed(updateRunnable, 1000 / fps);
        }
    }

    public void removeMarker(TrackableView marker) {
        markers.remove(marker);
    }

    public boolean containsMarker(TrackableView marker) {
        return markers.contains(marker);
    }

    private LinkedList<TrackableView> markersToRemove = new LinkedList<>();

    public void update() {
        for (TrackableView marker : markers) {
            if (!marker.updateCustomForTracking()) {
                markersToRemove.add(marker);
            } else {
                marker.update(0, 0);
            }
        }

        // Remove markers that are not active anymore
        if (markersToRemove.size() > 0) {
            markers.removeAll(markersToRemove);
            markersToRemove.clear();
        }
    }
}
