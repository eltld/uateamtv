package com.github.tarasmazepa.uateam.uateamtv.task;

import android.os.AsyncTask;

import com.github.tarasmazepa.uateam.uateamtv.base.Result;

public abstract class ResultTask<Param, Progress, Data> extends AsyncTask<Param, Progress, Result<Data>> {
    @Override
    protected Result<Data> doInBackground(Param... params) {
        try {
            return Result.success(produceData(params));
        } catch (Throwable throwable) {
            return Result.fail(throwable);
        }
    }

    protected abstract Data produceData(Param... params) throws Throwable;
}
