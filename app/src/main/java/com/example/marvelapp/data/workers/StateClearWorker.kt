package com.example.marvelapp.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.marvelapp.domain.usecases.ClearStateOrderUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StateClearWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val clearStateOrderUseCase: ClearStateOrderUseCase
) : CoroutineWorker(appContext = context, params = params) {

    override suspend fun doWork(): Result {
        return try {
            clearStateOrderUseCase.clearState()
            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }
    }
}