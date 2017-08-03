package com.gautam.medicinetime.report;

import android.support.annotation.NonNull;

import com.gautam.medicinetime.data.source.History;
import com.gautam.medicinetime.data.source.MedicineDataSource;
import com.gautam.medicinetime.data.source.MedicineRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gautam on 13/07/17.
 */

public class MonthlyReportPresenter implements MonthlyReportContract.Presenter {


    private final MedicineRepository mMedicineRepository;

    private final MonthlyReportContract.View mMonthlyReportView;

    private FilterType mCurrentFilteringType = FilterType.ALL_MEDICINES;

    public MonthlyReportPresenter(@NonNull MedicineRepository medicineRepository, MonthlyReportContract.View monthlyReportView) {
        this.mMedicineRepository = medicineRepository;
        this.mMonthlyReportView = monthlyReportView;
        mMonthlyReportView.setPresenter(this);
    }


    @Override
    public void start() {
        loadHistory(true);
    }


    @Override
    public void loadHistory(boolean showLoading) {
        loadHistoryFromDb(showLoading);
    }

    private void loadHistoryFromDb(final boolean showLoading) {
        if (showLoading) {
            mMonthlyReportView.setLoadingIndicator(true);
        }
        mMedicineRepository.getMedicineHistory(new MedicineDataSource.LoadHistoryCallbacks() {
            @Override
            public void onHistoryLoaded(List<History> historyList) {
                List<History> historyShowList = new ArrayList<>();

                //We will filter the History based on request type
                for (History history : historyList) {
                    switch (mCurrentFilteringType) {
                        case ALL_MEDICINES:
                            historyShowList.add(history);
                            break;
                        case TAKEN_MEDICINES:
                            if (history.getAction() == 1) {
                                historyShowList.add(history);
                            }
                            break;
                        case IGNORED_MEDICINES:
                            if (history.getAction() == 2) {
                                historyShowList.add(history);
                            }
                            break;
                    }
                }
                processHistory(historyShowList);
                if (!mMonthlyReportView.isActive()) {
                    return;
                }
                if (showLoading) {
                    mMonthlyReportView.setLoadingIndicator(false);
                }

            }

            @Override
            public void onDataNotAvailable() {
                if (!mMonthlyReportView.isActive()) {
                    return;
                }
                if (showLoading) {
                    mMonthlyReportView.setLoadingIndicator(false);
                }
                mMonthlyReportView.showLoadingError();
            }
        });

    }

    private void processHistory(List<History> historyList) {

        if (historyList.isEmpty()) {
            // Show a message indicating there are no history for that filter type.
            processEmptyHistory();
        } else {
            //Show the list of history
            mMonthlyReportView.showHistoryList(historyList);
            //Set filter label's text
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mCurrentFilteringType) {
            case ALL_MEDICINES:
                mMonthlyReportView.showAllFilterLabel();
                break;
            case TAKEN_MEDICINES:
                mMonthlyReportView.showTakenFilterLabel();
                break;
            case IGNORED_MEDICINES:
                mMonthlyReportView.showIgnoredFilterLabel();
                break;
            default:
                mMonthlyReportView.showAllFilterLabel();
        }
    }


    private void processEmptyHistory() {
        switch (mCurrentFilteringType) {
            case ALL_MEDICINES:
                mMonthlyReportView.showNoHistory();
                break;
            case TAKEN_MEDICINES:
                mMonthlyReportView.showNoTakenHistory();
                break;
            case IGNORED_MEDICINES:
                mMonthlyReportView.showNoIgnoredHistory();
                break;
            default:
                mMonthlyReportView.showNoHistory();
                break;
        }
    }


    @Override
    public void setFiltering(FilterType filterType) {
        mCurrentFilteringType = filterType;
    }

    @Override
    public FilterType getFilterType() {
        return mCurrentFilteringType;
    }
}
