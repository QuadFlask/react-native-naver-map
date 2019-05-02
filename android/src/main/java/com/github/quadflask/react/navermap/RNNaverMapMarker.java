package com.github.quadflask.react.navermap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;

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

import javax.annotation.Nullable;

public class RNNaverMapMarker extends RNNaverMapFeature<Marker> {
    private final DraweeHolder<GenericDraweeHierarchy> imageHolder;

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
        feature.setPosition(latLng);
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
                    final OverlayImage overlayImage1 = OverlayImage.fromAsset(uri);
                    OverlayImages.put(uri, overlayImage1);
                    setOverlayImage(overlayImage1);
                }
            }
        }
    }

    private void setOverlayImage(OverlayImage image) {
        feature.setIcon(image);
    }
}
