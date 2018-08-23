package com.example.danazone04.danazone.ui.splash.main.history;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.danazone04.danazone.BaseContainerFragment;
import com.example.danazone04.danazone.BaseFragment;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.RecyclerViewUtils;
import com.example.danazone04.danazone.bean.Run;
import com.example.danazone04.danazone.sqlite.DBManager;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_history)
public class HistoryFragment extends BaseContainerFragment {
    @ViewById
    RecyclerView mRecycleView;

    private HistoryAdapter mAdapter;
    private List<Run> mRun;
    private DBManager dbManager;

    @Override
    protected void afterViews() {
        dbManager = new DBManager(getContext());
        setUpAdapter();
    }

    private void setUpAdapter() {
        RecyclerViewUtils.Create().setUpVertical(getContext(), mRecycleView);
        mRun = new ArrayList<>();
        mRun = dbManager.getAllHistory();
        mAdapter = new HistoryAdapter(getContext(), mRun, new HistoryAdapter.OnHistoryClickListener() {
            @Override
            public void onClickItem(int position) {
                dbManager.deleteStudent(mRun.get(position));
                mRun.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        mRecycleView.setAdapter(mAdapter);

    }
}
