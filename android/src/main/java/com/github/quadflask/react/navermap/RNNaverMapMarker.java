package com.github.quadflask.react.navermap;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import androidx.annotation.Nullable;

public class RNNaverMapMarker extends RNNaverMapFeature<Marker> {
    private final DraweeHolder<GenericDraweeHierarchy> imageHolder;
    private boolean animated = false;
    private int duration = 500;
    private TimeInterpolator easingFunction;

    public RNNaverMapMarker(Context context) {
        super(context);
        feature = new Marker();
        imageHolder = DraweeHolder.create(createDraweeHierarchy(), context);
        imageHolder.onAttach();
    }

    private GenericDraweeHierarchy createDraweeHierarchy() {
        return new GenericDraweeHierarchyBuilder(getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setFadeDuration(0)
                .build();
    }

    public void setCoordinate(LatLng latLng) {
        if (animated && this.duration > 0) setCoordinateAnimated(latLng, this.duration);
        else feature.setPosition(latLng);
    }

    public void setCoordinateAnimated(LatLng finalPosition, int duration) {
        if (Double.isNaN(feature.getPosition().latitude)) {
            feature.setPosition(finalPosition);
        } else {
            Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
            ObjectAnimator animator = ObjectAnimator.ofObject(
                    feature,
                    property,
                    ReactUtil::interpolate,
                    finalPosition);
            animator.setDuration(duration);
            if (easingFunction != null)
                animator.setInterpolator(easingFunction);
            animator.start();
        }
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public void setEasing(Integer easing) {
        switch (easing) {
            case 0:
                easingFunction = new LinearInterpolator();
                break;
            case 1:
                easingFunction = new AccelerateDecelerateInterpolator();
                break;
            case 2:
                easingFunction = new AccelerateInterpolator();
                break;
            case 3:
                easingFunction = new DecelerateInterpolator();
                break;
            case 4:
                easingFunction = new BounceInterpolator();
                break;
            default:
                easingFunction = null;
        }
    }

    public void setDuration(Integer duration) {
        if (duration != null && duration >= 0) {
            this.duration = duration;
        }
    }

    public void setAnchor(float x, float y) {
        feature.setAnchor(new PointF(x, y));
    }

    public void setIconTintColor(int color) {
        feature.setIconTintColor(color);
    }

    public void setFlat(boolean flat) {
        feature.setFlat(flat);
    }

    public void setRotation(float rotation) {
        feature.setAngle(rotation);
    }

    public void setWidth(int width) {
        feature.setWidth(width);
    }

    public void setHeight(int height) {
        feature.setHeight(height);
    }

    public void setImage(String uri) {
        if (uri != null) {
            OverlayImage overlayImage = OverlayImages.get(uri);
            if (overlayImage != null) {
                setOverlayImage(overlayImage);
            } else {
                if (uri.startsWith("http://") || uri.startsWith("https://") || uri.startsWith("file://") || uri.startsWith("asset://")) {
                    ImageRequest imageRequest = ImageRequestBuilder
                            .newBuilderWithSource(Uri.parse(uri))
                            .build();

                    final DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                            .fetchDecodedImage(imageRequest, this);

                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(imageRequest)
                            .setControllerListener(new BaseControllerListener<ImageInfo>() {
                                @Override
                                public void onFinalImageSet(
                                        String id,
                                        @Nullable final ImageInfo imageInfo,
                                        @Nullable Animatable animatable) {
                                    CloseableReference<CloseableImage> imageReference = null;
                                    OverlayImage overlayImage = null;
                                    try {
                                        imageReference = dataSource.getResult();
                                        if (imageReference != null) {
                                            CloseableImage image = imageReference.get();
                                            if (image instanceof CloseableStaticBitmap) {
                                                CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                                                Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                                                if (bitmap != null) {
                                                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                                                    overlayImage = OverlayImage.fromBitmap(bitmap);
                                                    OverlayImages.put(uri, overlayImage);
                                                }
                                            }
                                        }
                                    } finally {
                                        dataSource.close();
                                        if (imageReference != null) {
                                            CloseableReference.closeSafely(imageReference);
                                        }
                                    }
                                    if (overlayImage != null)
                                        setOverlayImage(overlayImage);
                                }
                            })
                            .setOldController(imageHolder.getController())
                            .build();
                    imageHolder.setController(controller);
                } else {
                    int rid = getRidFromName(uri);
                    final OverlayImage overlayImage1 = OverlayImage.fromResource(rid);
                    OverlayImages.put(uri, overlayImage1);
                    setOverlayImage(overlayImage1);
                }
            }
        }
    }

    private void setOverlayImage(OverlayImage image) {
        feature.setIcon(image);
    }

    private int getRidFromName(String name) {
        return getContext().getResources().getIdentifier(name, "drawable", getContext().getPackageName());
    }
}
