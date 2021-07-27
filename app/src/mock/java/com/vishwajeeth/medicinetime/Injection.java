package com.vishwajeeth.medicinetime;

import android.content.Context;
import androidx.annotation.NonNull;

import com.vishwajeeth.medicinetime.data.source.MedicineRepository;
import com.vishwajeeth.medicinetime.data.source.local.MedicinesLocalDataSource;

/**
 * Created by vishwajeeth on 07/07/21.
 */

public class Injection {

    public static MedicineRepository provideMedicineRepository(@NonNull Context context) {
        return MedicineRepository.getInstance(MedicinesLocalDataSource.getInstance(context));
    }
}
