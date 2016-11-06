package zzh.project.stocksystem.ui.stocktrace;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zzh.project.stocksystem.R;
import zzh.project.stocksystem.bean.TradeBean;

public class TradeListAdapter extends RecyclerView.Adapter<TradeListAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView flag;
        public TextView name;
        public TextView code;
        public TextView cost;
        public TextView price;
        public TextView total;
        public TextView date;
        public ImageView status;

        public ViewHolder(View itemView) {
            super(itemView);
            flag = (TextView) itemView.findViewById(R.id.Trade_Flag);
            name = (TextView) itemView.findViewById(R.id.Trade_Name);
            price = (TextView) itemView.findViewById(R.id.Trade_Price);
            code = (TextView) itemView.findViewById(R.id.Trade_Code);
            cost = (TextView) itemView.findViewById(R.id.Trade_Cost);
            total = (TextView) itemView.findViewById(R.id.Trade_Total);
            date = (TextView) itemView.findViewById(R.id.Trade_Date);
            status = (ImageView) itemView.findViewById(R.id.Trade_Status);
        }
    }

    private Context mContext;
    private List<TradeBean> mData;

    public TradeListAdapter(Context context, List<TradeBean> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.adapter_trade_item, parent, false);
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
        TradeBean bean = mData.get(position);
        holder.name.setText(bean.stockName);
        holder.code.setText(String.format("股票代号：%s", bean.stockCode));
        holder.price.setText(String.format("单价：%s", bean.uPrice));
        holder.total.setText(String.format("数量：%s", bean.amount));
        holder.cost.setText(String.format("金额：%s", Float.parseFloat(bean.uPrice) * bean.amount + " ¥"));
        if (bean.type.equals("buy")) {
            holder.flag.setText("买");
            holder.flag.setBackgroundColor(mContext.getResources().getColor(R.color.olivedrab));
        } else {
            holder.flag.setText("卖");
            holder.flag.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
        }
        holder.date.setText(String.format("日期：%s", bean.date));
        holder.status.setImageResource(bean.status == 0 ? R.mipmap.ic_review : R.mipmap.ic_pass);
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
