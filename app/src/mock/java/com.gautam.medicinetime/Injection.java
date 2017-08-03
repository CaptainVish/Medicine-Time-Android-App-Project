package com.gautam.medicinetime;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gautam.medicinetime.data.FakeMedicineLocalDataSource;
import com.gautam.medicinetime.data.source.MedicineRepository;
import com.gautam.medicinetime.data.source.local.MedicinesLocalDataSource;

/**
 * Created by gautam on 13/07/17.
 */

public class Injection {

    public static MedicineRepository provideMedicineRepository(@NonNull Context context) {
        return MedicineRepository.getInstance(MedicinesLocalDataSource.getInstance(context));
    }
}
