package com.moo.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.moo.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author moodstrikerdd
 * @date 2018/6/6
 * @label
 */
public abstract class DiffUtilCommonAdapter<T> extends CommonAdapter<T> {
    private ItemDiffUtils diff = new ItemDiffUtils();

    public abstract boolean areItemsSame(T oldData, T newData);

    public abstract boolean areContentsSame(T oldData, T newData);

    protected abstract T getChange(T oldData, T newData);

    public DiffUtilCommonAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    public void addData(List<T> addData) {
        List<T> data = new ArrayList<>();
        data.addAll(mDatas);
        data.addAll(addData);
        setData(data);
//        mDatas.clear();
//        mDatas.addAll(data);
//        notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        diff.setOldAndNewData(mDatas, data);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diff);
        diffResult.dispatchUpdatesTo(this);
        mDatas.clear();
        mDatas.addAll(data);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            T t = (T) payloads.get(0);
            convert(holder, t);
        }
    }

    class ItemDiffUtils extends DiffUtil.Callback {
        private List<T> oldDataList, newDataList;

        void setOldAndNewData(List<T> oldDataList, List<T> newDataList) {
            this.oldDataList = oldDataList;
            this.newDataList = newDataList;
        }

        @Override
        public int getOldListSize() {
            return oldDataList == null ? 0 : oldDataList.size();
        }

        @Override
        public int getNewListSize() {
            return newDataList == null ? 0 : newDataList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return areItemsSame(oldDataList.get(oldItemPosition), newDataList.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return areContentsSame(oldDataList.get(oldItemPosition), newDataList.get(newItemPosition));
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return getChange(oldDataList.get(oldItemPosition), newDataList.get(newItemPosition));
        }
    }
}
