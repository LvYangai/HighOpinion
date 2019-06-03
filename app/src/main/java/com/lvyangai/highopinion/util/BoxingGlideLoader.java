/*
 *  Copyright (C) 2017 Bilibili
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.lvyangai.highopinion.util;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bilibili.boxing.loader.IBoxingCallback;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lvyangai.highopinion.R;

/**
 * use https://github.com/bumptech/glide as media loader.
 * can <b>not</b> be used in Production Environment.
 *
 * @author ChenSL
 */
public class BoxingGlideLoader implements IBoxingMediaLoader {
    private static final String TAG = "BoxingGlideLoader";
    private static RequestOptions  optionsIcon  = new RequestOptions()
            .error(R.mipmap.ic_boxing_default_image)
            .centerCrop();
//            .fitCenter();

    @Override
    public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
        String path = "file://" + absPath;
        Log.e(TAG, "displayRawgT: "+path );
        optionsIcon.override(width, height);
        try {
            // https://github.com/bumptech/glide/issues/1531
            Glide.with(img.getContext()).load(path).apply(optionsIcon).into(img);
        } catch(IllegalArgumentException ignore) {
        }

    }

    @Override
    public void displayRaw(@NonNull final ImageView img, @NonNull String absPath, int width, int height, final IBoxingCallback callback) {
        String path = "file://" + absPath;
        Log.e(TAG, "displayRawg Raw: "+path );
        optionsIcon.override(width, height);
        Glide.with(img.getContext()).load(path).apply(optionsIcon).into(img);

    }

}
