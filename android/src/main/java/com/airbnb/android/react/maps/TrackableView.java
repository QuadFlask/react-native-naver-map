package com.airbnb.android.react.maps;

public interface TrackableView {
    boolean updateCustomForTracking();

    void update(int width, int height);
}
