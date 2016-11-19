package zzh.project.stocksystem.ui.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.NewsBean;

class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd mm:HH:ss");

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            text = (TextView) itemView.findViewById(R.id.News_Text);
            date = (TextView) itemView.findViewById(R.id.News_Date);
        }
    }

    private Context mContext;
    private List<NewsBean> mData;

    NewsListAdapter(Context context, List<NewsBean> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.adapter_news_item, parent, false);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClicked((Integer) v.getTag(R.id.item_position));
                }
            }
        });
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsBean bean = mData.get(position);
        holder.text.setText(bean.message);
        holder.date.setText(dateFormat.format(bean.date));
        holder.itemView.setTag(R.id.item_position, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private OnItemClickedListener mListener;

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mListener = listener;
    }

    public static interface OnItemClickedListener {
        void onItemClicked(int position);
    }
}
