package zzh.project.stocksystem.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.codetail.animation.ViewAnimationUtils;
import zzh.project.stocksystem.R;
import zzh.project.stocksystem.ui.stockdetail.StockDetailActivity;

public class SearchFragment extends Fragment {
    @BindView(R.id.atv_Search_Input)
    EditText mSearchTextView;
    @BindView(R.id.rfl_Search_Bar)
    ViewGroup mBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.frag_search, container, false);
        ButterKnife.bind(this, root);
        root.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                root.getViewTreeObserver().removeOnPreDrawListener(this);
                Animator animation = ViewAnimationUtils.createCircularReveal(mBar, mBar.getWidth(), mBar.getHeight() / 2, 10f, mBar.getWidth());
                animation.setDuration(200);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSearchTextView.requestFocus();
                        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(mSearchTextView, InputMethodManager.SHOW_FORCED);
                    }
                });
                animation.start();
                return true;
            }
        });
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(mSearchTextView);
            }
        });

        mSearchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    close(v);
                    Intent intent = new Intent(getContext(), StockDetailActivity.class);
                    String gid = mSearchTextView.getText() + "";
                    intent.putExtra("gid", gid);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        return root;
    }

    private void close(View v) {
        /*隐藏软键盘*/
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
