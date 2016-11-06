package zzh.project.stocksystem.ui.win;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zzh.project.stocksystem.R;

public class TradePopWin extends PopupWindow {
    private Context context;
    @BindView(R.id.tv_Pop_Title)
    TextView mTitle;
    @BindView(R.id.tv_Num_Label)
    TextView mLabel;
    @BindView(R.id.et_Num)
    TextView mNum;
    @BindView(R.id.btn_Done)
    Button mDone;

    private Integer mMax;

    public TradePopWin(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        View root = LayoutInflater.from(context).inflate(R.layout.pop_win_buy, null, false);
        setContentView(root);
        ButterKnife.bind(this, root);

        setWidth((int) (context.getResources().getDisplayMetrics().widthPixels * (7 / 8f)));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#dd000000")));
    }


    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setLabel(String label) {
        mLabel.setText(label);
    }

    public void setDone(String done) {
        mDone.setText(done);
    }

    public void setMax(int max) {
        mMax = max;
    }

    @OnClick(R.id.btn_Add)
    public void add() {
        int num = Integer.valueOf(mNum.getText().toString().trim());
        num++;
        if (mMax != null && num > mMax) {
            return;
        } else if (num > 9999) {
            return;
        }
        mNum.setText(num + "");
    }

    @OnClick(R.id.btn_Reduce)
    public void reduce() {
        int num = Integer.valueOf(mNum.getText().toString().trim());
        num--;
        if (num < 1) {
            return;
        }
        mNum.setText(num + "");
    }

    @OnClick(R.id.btn_Done)
    public void done() {
        if (mListener != null) {
            mListener.done(Integer.valueOf(mNum.getText().toString().trim()));
        }
    }

    public static interface OnDoneListener {
        void done(int num);
    }

    private OnDoneListener mListener;

    public void setOnDoneListener(OnDoneListener listener) {
        mListener = listener;
    }
}
