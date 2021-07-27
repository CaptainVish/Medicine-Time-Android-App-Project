package com.vishwajeeth.medicinetime.report;

import com.vishwajeeth.medicinetime.BasePresenter;
import com.vishwajeeth.medicinetime.BaseView;
import com.vishwajeeth.medicinetime.data.source.History;
import java.util.List;

/**
 * Created by vishwajeeth on 13/07/17.
 */

public interface MonthlyReportContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showHistoryList(List<History> historyList);

        void showLoadingError();

        void showNoHistory();

        void showTakenFilterLabel();

        void showIgnoredFilterLabel();

        void showAllFilterLabel();

        void showNoTakenHistory();

        void showNoIgnoredHistory();

        boolean isActive();

        void showFilteringPopUpMenu();

    }

    interface Presenter extends BasePresenter {

        void loadHistory(boolean showLoading);

        void setFiltering(FilterType filterType);

        FilterType getFilterType();
    }
}
