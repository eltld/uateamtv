package com.github.tarasmazepa.uateam.uateamtv.base;

import com.google.common.base.Preconditions;

public class Result<Data> {
    public final boolean success;
    public final Data data;
    public final Throwable error;

    private Result(boolean success, Data data, Throwable error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <Data> Result<Data> success(Data data) {
        return new Result(true, Preconditions.checkNotNull(data), null);
    }

    public static <Data> Result<Data> fail(Throwable error) {
        return new Result(false, null, Preconditions.checkNotNull(error));
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", data=" + data +
                ", error=" + error +
                '}';
    }
}