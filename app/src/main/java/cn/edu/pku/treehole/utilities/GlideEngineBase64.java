package cn.edu.pku.treehole.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.listener.OnImageCompleteCallback;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

import cn.edu.pku.treehole.data.hole.HoleRepository;


public class GlideEngineBase64 implements ImageEngine {

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param url       资源url
     * @param imageView 图片承载控件
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        if (!ImageLoaderUtils.assertValidRequest(context)) {
            return;
        }
        // 获取url
        String uri = url.split(",")[1];
        byte[] imageByteArray = Base64.decode(uri, Base64.DEFAULT);
        Glide.with(context)
                .asBitmap()
                .load(imageByteArray)
                .into(imageView);
    }

    /**
     * 加载网络图片适配长图方案
     *
     * @param context       上下文
     * @param url           资源url
     * @param imageView     图片承载控件
     * @param longImageView 长图承载控件
     * @param callback      网络图片加载回调监听
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView, SubsamplingScaleImageView longImageView,
                          OnImageCompleteCallback callback) {
        if (!ImageLoaderUtils.assertValidRequest(context)) {
            return;
        }
        String uri = url.split(",")[1];
        byte[] imageByteArray = Base64.decode(uri, Base64.DEFAULT);
        Glide.with(context)
                .asBitmap()
                .load(imageByteArray)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        if (callback != null) {
                            callback.onShowLoading();
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                        boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                resource.getHeight());
                        longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                        imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                        if (eqLongImage) {
                            // 加载长图
                            longImageView.setQuickScaleEnabled(true);
                            longImageView.setZoomEnabled(true);
                            longImageView.setDoubleTapZoomDuration(100);
                            longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                            longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                            longImageView.setImage(ImageSource.cachedBitmap(resource),
                                    new ImageViewState(0, new PointF(0, 0), 0));
                        } else {
                            // 普通图片
                            imageView.setImageBitmap(resource);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * 加载相册目录
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadFolderImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        if (!ImageLoaderUtils.assertValidRequest(context)) {
            return;
        }
        String uri = url.split(",")[1];
        byte[] imageByteArray = Base64.decode(uri, Base64.DEFAULT);
        Glide.with(context)
                .asBitmap()
                .load(imageByteArray)
                .override(180, 180)
                .centerCrop()
                .sizeMultiplier(0.5f)
//                .placeholder(R.drawable.picture_image_placeholder)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.
                                        create(context.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(8);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        if (!ImageLoaderUtils.assertValidRequest(context)) {
            return;
        }
        String uri = url.split(",")[1];
        byte[] imageByteArray = Base64.decode(uri, Base64.DEFAULT);
        Glide.with(context)
                .asBitmap()
                .load(imageByteArray)
                .override(200, 200)
                .centerCrop()
//                .placeholder(R.drawable.picture_image_placeholder)
                .into(imageView);
    }


    private GlideEngineBase64() {
    }

    private static GlideEngineBase64 instance;

    public static GlideEngineBase64 createGlideEngine() {
        if (null == instance) {
            synchronized (GlideEngineBase64.class) {
                if (null == instance) {
                    instance = new GlideEngineBase64();
                }
            }
        }
        return instance;
    }
}
