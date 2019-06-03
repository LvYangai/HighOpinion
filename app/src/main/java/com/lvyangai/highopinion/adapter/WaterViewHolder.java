package com.lvyangai.highopinion.adapter;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/12.
 * 描述：不可以用
 * 邮箱：1076977275@qq.com
 */

public class WaterViewHolder extends BaseViewHolder implements View.OnClickListener {
    private final AdapterView.OnItemClickListener mListener;
    private int mPosition = -1;

    public WaterViewHolder(View itemView, AdapterView.OnItemClickListener mListener) {
        super(itemView);
        this.mListener = mListener;
        itemView.setOnClickListener(this);

        /*
         * 设置水波纹背景
         */
        if (itemView.getBackground() == null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = itemView.getContext().getTheme();
            int top = itemView.getPaddingTop();
            int bottom = itemView.getPaddingBottom();
            int left = itemView.getPaddingLeft();
            int right = itemView.getPaddingRight();
            if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
                itemView.setBackgroundResource(typedValue.resourceId);
            }
            itemView.setPadding(left, top, right, bottom);
        }
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            int position = getAdapterPosition();
            if(position >= 0){
                mListener.onItemClick(null, v, position, getItemId());
            } else if (mPosition > -1) {
                mListener.onItemClick(null, v, mPosition, getItemId());
            }
        }
    }
}
