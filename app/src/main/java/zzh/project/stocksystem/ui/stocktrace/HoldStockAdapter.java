package zzh.project.stocksystem.ui.stocktrace;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.HoldStockBean;

public class HoldStockAdapter extends RecyclerView.Adapter<HoldStockAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumb;
        public TextView name;
        public TextView price;
        public TextView code;
        public TextView increase;
        public TextView increPer;
        public TextView total;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            name = (TextView) itemView.findViewById(R.id.Stock_Name);
            price = (TextView) itemView.findViewById(R.id.Stock_Price);
            code = (TextView) itemView.findViewById(R.id.Stock_Code);
            increase = (TextView) itemView.findViewById(R.id.Stock_Increase);
            increPer = (TextView) itemView.findViewById(R.id.Stock_IncrePer);
            total = (TextView) itemView.findViewById(R.id.Stock_Total);
        }
    }

    private Context mContext;
    private List<HoldStockBean> mData;

    public HoldStockAdapter(Context context, List<HoldStockBean> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.adapter_hold_stock_item, parent, false);
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
        HoldStockBean bean = mData.get(position);
        holder.name.setText(bean.name);
        holder.price.setText(String.format("最新价格：%s ¥", bean.nowPri));
        holder.code.setText(String.format("股票代码：%s", bean.gid));
        holder.increase.setText(String.format("涨幅额：%s", bean.increase));
        holder.increPer.setText(String.format("涨跌幅：%s%%", bean.increPer));
        holder.total.setText(String.format("持有：%s", "" + bean.total));
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
