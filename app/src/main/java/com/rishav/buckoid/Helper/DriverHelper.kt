package com.rishav.buckoid.Helper

import com.google.api.services.drive.Drive
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.Executor
import java.util.concurrent.Executors


    class DriverHelper constructor(mDriveService: Drive){
        private val mExecutor:Executor = Executors.newSingleThreadExecutor()
        private val mDriverService:Drive
            get() {
                TODO()
            }


    }

