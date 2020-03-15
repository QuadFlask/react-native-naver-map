package com.github.quadflask.react.navermap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;

import androidx.annotation.Nullable;

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
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;

import java.util.List;

public class RNNaverMapPathOverlay extends ClickableRNNaverMapFeature<PathOverlay> {
    private final DraweeHolder<GenericDraweeHierarchy> imageHolder;

    public RNNaverMapPathOverlay(EventEmittable emitter, Context context) {
        super(emitter, context);
        feature = new PathOverlay();
        imageHolder = DraweeHolder.create(createDraweeHierarchy(), context);
        imageHolder.onAttach();
    }

    private GenericDraweeHierarchy createDraweeHierarchy() {
        return new GenericDraweeHierarchyBuilder(getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setFadeDuration(0)
                .build();
    }

    public void setCoords(List<LatLng> coords) {
        feature.setCoords(coords);
    }

    public void setWidth(float widthInScreenPx) {
        feature.setWidth(Math.round(widthInScreenPx));
    }

    public void setZIndex(int zIndex) {
        feature.setZIndex(zIndex);
    }

    public void setProgress(float progress) {
        feature.setProgress(progress);
    }

    public void setOutlineWidth(float widthInScreenPx) {
        feature.setOutlineWidth(Math.round(widthInScreenPx));
    }

    public void setColor(int color) {
        feature.setColor(color);
    }

    public void setPassedColor(int color) {
        feature.setPassedColor(color);
    }

    public void setOutlineColor(int color) {
        feature.setOutlineColor(color);
    }

    public void setPassedOutlineColor(int color) {
        feature.setPassedOutlineColor(color);
    }

    public void setPatternInterval(int patternInterval) {
        feature.setPatternInterval(patternInterval);
    }

    public void setPattern(String uri) {
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
        feature.setPatternImage(image);
    }

    private int getRidFromName(String name) {
        return getContext().getResources().getIdentifier(name, "drawable", getContext().getPackageName());
    }
}
