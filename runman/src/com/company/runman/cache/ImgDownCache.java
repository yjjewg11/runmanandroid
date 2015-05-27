package com.company.runman.cache;

import android.content.Context;
import android.graphics.Bitmap;
import com.company.runman.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Administrator on 2015/5/24.
 */
public class ImgDownCache {
static ImageLoader imageLoader=null;
    public static ImageLoader getInstance(Context context){
        ImgDownCache d=new ImgDownCache(context);
        return imageLoader;
    }
    public ImgDownCache(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        if(imageLoader==null){
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                    .diskCacheExtraOptions(480, 800, null)
                    .threadPriority(Thread.NORM_PRIORITY - 2) // default
                    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                    .memoryCacheSize(2 * 1024 * 1024)
                    .memoryCacheSizePercentage(13) // default
                    .diskCache(new UnlimitedDiscCache(cacheDir)) // default
                    .diskCacheSize(50 * 1024 * 1024)
                    .diskCacheFileCount(100)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                    .imageDownloader(new BaseImageDownloader(context)) // default
                    .imageDecoder(new BaseImageDecoder(true)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                    .writeDebugLogs()
                    .build();
            imageLoader= ImageLoader.getInstance();
            imageLoader.init(config);
        }

    }
}
