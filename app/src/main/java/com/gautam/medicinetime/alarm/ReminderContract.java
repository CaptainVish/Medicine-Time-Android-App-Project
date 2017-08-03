package com.gautam.medicinetime.alarm;

import com.gautam.medicinetime.BasePresenter;
import com.gautam.medicinetime.BaseView;
import com.gautam.medicinetime.data.source.History;
import com.gautam.medicinetime.data.source.MedicineAlarm;

/**
 * Created by gautam on 13/07/17.
 */

public interface ReminderContract {

    interface View extends BaseView<Presenter> {

        void showMedicine(MedicineAlarm medicineAlarm);

        void showNoData();

        boolean isActive();

        void onFinish();

    }

    interface Presenter extends BasePresenter {

        void finishActivity();

        void onStart(long id);

        void loadMedicineById(long id);

        void addPillsToHistory(History history);

    }
}
