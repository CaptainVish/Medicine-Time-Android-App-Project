package com.gautam.medicinetime.report;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.gautam.medicinetime.Injection;
import com.gautam.medicinetime.R;
import com.gautam.medicinetime.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthlyReportActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_TYPE = "current_filtering_type";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MonthlyReportPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Create Fragment
        MonthlyReportFragment monthlyReportFragment = (MonthlyReportFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (monthlyReportFragment == null) {
            monthlyReportFragment = MonthlyReportFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), monthlyReportFragment, R.id.contentFrame);
        }

        //Create TaskPresenter
        presenter = new MonthlyReportPresenter(Injection.provideMedicineRepository(MonthlyReportActivity.this), monthlyReportFragment);

        //Load previous saved Instance
        if (savedInstanceState != null) {
            FilterType taskFilterType = (FilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_TYPE);
            presenter.setFiltering(taskFilterType);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_TYPE, presenter.getFilterType());
        super.onSaveInstanceState(outState);
    }
}
