package com.gautam.medicinetime.alarm;

import android.content.Intent;
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

public class ReminderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ReminderPresenter mReminderPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_actvity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (!intent.hasExtra(ReminderFragment.EXTRA_ID)) {
            finish();
            return;
        }
        long id = intent.getLongExtra(ReminderFragment.EXTRA_ID, 0);
        ReminderFragment reminderFragment = (ReminderFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (reminderFragment == null) {
            reminderFragment = ReminderFragment.newInstance(id);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), reminderFragment, R.id.contentFrame);
        }

        //Create MedicinePresenter
        mReminderPresenter = new ReminderPresenter(Injection.provideMedicineRepository(ReminderActivity.this), reminderFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mReminderPresenter != null) {
                mReminderPresenter.finishActivity();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mReminderPresenter != null) {
            mReminderPresenter.finishActivity();
        }
    }
}
