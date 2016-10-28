package zzh.project.stocksystem.module.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.codetail.animation.ViewAnimationUtils;
import zzh.project.stocksystem.R;

public class SearchFragment extends Fragment {
    @BindView(R.id.atv_Search_Input)
    AppCompatAutoCompleteTextView mSearchTextView;
    @BindView(R.id.rfl_Search_Bar)
    ViewGroup mBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, root);
        root.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                root.getViewTreeObserver().removeOnPreDrawListener(this);
                Animator animation = ViewAnimationUtils.createCircularReveal(mBar, mBar.getWidth(), mBar.getHeight() / 2, 10f, mBar.getWidth());
                animation.setDuration(200);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.setTarget(mBar);
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
        return root;
    }
}
