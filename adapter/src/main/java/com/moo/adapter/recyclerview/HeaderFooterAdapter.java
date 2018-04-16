package com.moo.adapter.recyclerview;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moo.adapter.ViewHolder;

/**
 * @author moodstrikerdd
 * @date 2018/4/12
 * @label 头尾Adapter
 */

public class HeaderFooterAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private SparseArrayCompat<View> headerViews = new SparseArrayCompat<>();
    private SparseArrayCompat<ViewHolder> headerHolders = new SparseArrayCompat<>();
    private SparseArrayCompat<View> footerViews = new SparseArrayCompat<>();
    private SparseArrayCompat<ViewHolder> footerHolders = new SparseArrayCompat<>();

    private static final int HEADER_TYPE_START_INDEX = 10000;
    private static final int FOOTER_TYPE_START_INDEX = 20000;

    private RecyclerView.Adapter<ViewHolder> innerAdapter;

    public HeaderFooterAdapter(Context context, RecyclerView.Adapter adapter) {
        mContext = context;
        innerAdapter = adapter;
    }

    public void addHeaderView(int layoutId) {
        View headerView = LayoutInflater.from(mContext).inflate(layoutId, null);
        addHeaderView(headerView);
    }

    public void addHeaderView(View headerView) {
        int index = headerViews.size() + HEADER_TYPE_START_INDEX;
        headerViews.put(index, headerView);
        headerHolders.put(index, ViewHolder.get(mContext, headerView));
    }

    public void addFooterView(int layoutId) {
        View footerView = LayoutInflater.from(mContext).inflate(layoutId, null);
        addFooterView(footerView);
    }

    public void addFooterView(View footerView) {
        int index = footerViews.size() + FOOTER_TYPE_START_INDEX;
        footerViews.put(index, footerView);
        footerHolders.put(index, ViewHolder.get(mContext, footerView));
    }

    public SparseArrayCompat<ViewHolder> getHeaderHolders() {
        return headerHolders;
    }

    public SparseArrayCompat<ViewHolder> getFooterHolders() {
        return footerHolders;
    }

    private int getHeaderCount() {
        return headerViews.size();
    }

    private int getFooterCount() {
        return footerViews.size();
    }

    private int getContentCount() {
        return innerAdapter.getItemCount();
    }

    private boolean isHeaderView(int position) {
        return position < getHeaderCount();
    }

    private boolean isFooterView(int position) {
        return position >= getHeaderCount() + getContentCount();
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getContentCount() + getFooterCount();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (isHeaderView(position)) {
            viewType = headerViews.keyAt(position);
        } else if (isFooterView(position)) {
            viewType = footerViews.keyAt(position - getHeaderCount() - getContentCount());
        } else {
            viewType = innerAdapter.getItemViewType(position - getHeaderCount());
        }
        return viewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        if (headerViews.get(viewType) != null) {
            holder = ViewHolder.get(mContext, headerViews.get(viewType));
        } else if (footerViews.get(viewType) != null) {
            holder = ViewHolder.get(mContext, footerViews.get(viewType));
        } else {
            holder = innerAdapter.onCreateViewHolder(parent, viewType);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isHeaderView(position)) {
            convertHeaderViews(headerHolders);
        } else if (isFooterView(position)) {
            convertFooterViews(footerHolders);
        } else {
            innerAdapter.onBindViewHolder(holder, position - getHeaderCount());
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        innerAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (headerViews.get(viewType) != null) {
                        return 2;
                    } else if (footerViews.get(viewType) != null) {
                        return 6;
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        innerAdapter.onViewAttachedToWindow(holder);

        int adapterPosition = holder.getAdapterPosition();

        if (isHeaderView(adapterPosition) || isFooterView(adapterPosition)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                slp.setFullSpan(true);
            }
        }
    }

    /**
     * 绑定headerView
     *
     * @param headerHolders header ViewHolder集合
     */
    protected void convertHeaderViews(SparseArrayCompat<ViewHolder> headerHolders) {
    }

    /**
     * 绑定footerView
     *
     * @param footerHolders footer ViewHolder集合
     */
    protected void convertFooterViews(SparseArrayCompat<ViewHolder> footerHolders) {
    }

}
