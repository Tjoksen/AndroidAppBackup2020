package com.talentjoko.todoapplication.data.exceptions;

import androidx.annotation.NonNull;

public class FirebaseRxDataCastException extends Exception {

    public FirebaseRxDataCastException() {
    }

    public FirebaseRxDataCastException(@NonNull String detailMessage) {
        super(detailMessage);
    }

    public FirebaseRxDataCastException(@NonNull String detailMessage, @NonNull Throwable throwable) {
        super(detailMessage, throwable);
    }

    public FirebaseRxDataCastException(@NonNull Throwable throwable) {
        super(throwable);
    }
}