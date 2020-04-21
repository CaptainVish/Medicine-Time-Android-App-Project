package com.gautam.medicinetime.report;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gautam.medicinetime.R;
import com.gautam.medicinetime.data.source.History;
import com.gautam.medicinetime.views.RobotoLightTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gautam on 13/07/17.
 */

public class MonthlyReportFragment extends Fragment implements MonthlyReportContract.View {

    @BindView(R.id.rv_history_list)
    RecyclerView rvHistoryList;

    @BindView(R.id.progressLoader)
    ProgressBar progressLoader;

    @BindView(R.id.noMedIcon)
    ImageView noMedIcon;

    @BindView(R.id.noMedText)
    RobotoLightTextView noMedText;

    @BindView(R.id.no_med_view)
    View noMedView;

    Unbinder unbinder;

    @BindView(R.id.filteringLabel)
    TextView filteringLabel;

    @BindView(R.id.tasksLL)
    LinearLayout tasksLL;

    private HistoryAdapter mHistoryAdapter;

    private MonthlyReportContract.Presenter presenter;

    public static MonthlyReportFragment newInstance() {
        Bundle args = new Bundle();
        MonthlyReportFragment fragment = new MonthlyReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryAdapter = new HistoryAdapter(new ArrayList<History>());
        setHasOptionsMenu(true);
    }

    private void setAdapter() {
        rvHistoryList.setAdapter(mHistoryAdapter);
        rvHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistoryList.setHasFixedSize(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        setAdapter();
        return view;
    }

    @Override
    public void onResume() {
            super.onResume();
            presenter.start();
    }

    @Override
    public void setPresenter(MonthlyReportContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        progressLoader.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showHistoryList(List<History> historyList) {
        mHistoryAdapter.replaceData(historyList);
        tasksLL.setVisibility(View.VISIBLE);
        noMedView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showNoHistory() {
        showNoHistoryView(
                getString(R.string.no_history),
                R.drawable.icon_my_health
        );
    }

    @Override
    public void showTakenFilterLabel() {
        filteringLabel.setText(R.string.taken_label);
    }

    @Override
    public void showIgnoredFilterLabel() {
        filteringLabel.setText(R.string.ignore_label);
    }

    @Override
    public void showAllFilterLabel() {
        filteringLabel.setText(R.string.all_label);
    }

    @Override
    public void showNoTakenHistory() {
        showNoHistoryView(
                getString(R.string.no_taken_med_history),
                R.drawable.icon_my_health
        );
    }

    @Override
    public void showNoIgnoredHistory() {
        showNoHistoryView(
                getString(R.string.no_ignored_history),
                R.drawable.icon_my_health
        );
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_history, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.all:
                        presenter.setFiltering(FilterType.ALL_MEDICINES);
                        break;
                    case R.id.taken:
                        presenter.setFiltering(FilterType.TAKEN_MEDICINES);
                        break;
                    case R.id.ignored:
                        presenter.setFiltering(FilterType.IGNORED_MEDICINES);
                        break;
                }
                presenter.loadHistory(true);
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
        }
        return true;
    }

    private void showNoHistoryView(String mainText, int iconRes) {
        tasksLL.setVisibility(View.GONE);
        noMedView.setVisibility(View.VISIBLE);

        noMedText.setText(mainText);
        noMedIcon.setImageDrawable(getResources().getDrawable(iconRes));
    }
}
