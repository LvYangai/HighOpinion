package com.lvyangai.highopinion.ui.home.homefragment.like.page;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseChildFragment;
import com.lvyangai.highopinion.activity.BaseFragment;
import com.lvyangai.highopinion.activity.BaseLazyFragment;
import com.lvyangai.highopinion.bean.LikePageItemBean;
import com.lvyangai.highopinion.databinding.FragmentLikePageBinding;
import com.lvyangai.highopinion.layoutmanagergroup.echelon.EchelonLayoutManager;
import com.lvyangai.highopinion.ui.web.WebActivity;
import com.lvyangai.highopinion.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class LikePageFragment extends BaseLazyFragment implements LikePageContract.likePageView {


    private View view;
    private FragmentLikePageBinding binding;
    private Context context;
    private EchelonLayoutManager mLayoutManager;
    private List<LikePageItemBean.LikeBean> dataList;
    private static final String TAG = "LikePageFragment";
    private LikePagePresenter presenter;
    private int appUserId = 0;
    private LikePageQuickAdapter adapter;
    public LikePageFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        if (view == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_like_page,container,false);
            view = binding.getRoot();
        }
        return view;
    }

    @Override
    protected void initData() {
        context = getActivity();
        presenter = new LikePagePresenter(this);
        appUserId = MyApplication.getUserid();
    }

    @Override
    protected void dataProcess() {
        dataList = new ArrayList<>();
        adapter = new LikePageQuickAdapter(R.layout.item_echelon);
        adapter.replaceData(dataList);
        mLayoutManager = new EchelonLayoutManager(context);
        binding.likePageRecycler.setLayoutManager(mLayoutManager);
        binding.likePageRecycler.setAdapter(adapter);
//        binding.likePageRecycler.setAdapter(new MyLikePageAdapter());
        binding.fragLikePageRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                     presenter.getDataList("text",appUserId);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("intent_web_url", "index.php/article/"+dataList.get(position).getLike_pageId()+".html");
                intent.putExtra("intent_web_title", dataList.get(position).getLike_title());
                intent.putExtra("intent_web_userid", dataList.get(position).getLike_uid());
                intent.putExtra("intent_web_isLike", true);
                intent.putExtra("intent_web_clickLike", dataList.get(position).getLike_click());
                intent.putExtra("intent_web_pageId", dataList.get(position).getLike_pageId());
                startActivity(intent);
            }
        });
        binding.fragLikePageRefreshLayout.autoRefresh();
    }

    @Override
    protected void destroy() {

    }

    @Override
    protected void onFirstUserVisible() {
        dataProcess();
    }

    @Override
    public void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    public void retrofitSuccess(Response<LikePageItemBean> response) {
        dataList = response.body().getLike();
        if (dataList.size()!= 0){
            adapter.replaceData(dataList);
        }else {
            ToastUtil.showShortToast("暂时没有收藏呢");
        }
        binding.fragLikePageRefreshLayout.finishRefresh();
        binding.fragLikePageRefreshLayout.resetNoMoreData();

    }

    @Override
    public void retrofitFailed(String log) {
        ToastUtil.showShortToast(log);
        binding.fragLikePageRefreshLayout.finishRefresh();
        binding.fragLikePageRefreshLayout.resetNoMoreData();
    }

    class LikePageQuickAdapter extends BaseQuickAdapter<LikePageItemBean.LikeBean,BaseViewHolder>{

        RequestOptions optionsImage,optionsIcon;
        public LikePageQuickAdapter(int layoutResId) {
            super(layoutResId);
            optionsImage  = new RequestOptions()
                    .error(R.mipmap.loading_image_error)
                    .fitCenter();

            optionsIcon  = new RequestOptions()
                    .error(R.mipmap.icon_user_error)
                    .fitCenter();
        }

        @Override
        protected void convert(BaseViewHolder helper, LikePageItemBean.LikeBean item) {
            helper.setText(R.id.tv_nickname,""+item.getLike_keywords())
                    .setText(R.id.tv_desc,""+item.getLike_title());
            Glide.with(mContext).load(item.getLike_thumb()).apply(optionsImage).into((ImageView) helper.itemView.findViewById(R.id.img_bg));
            Glide.with(mContext).load(item.getLike_user_icon()).apply(optionsIcon).into((ImageView) helper.itemView.findViewById(R.id.img_icon));
        }
    }


}
