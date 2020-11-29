package com.example.rssreader.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Result<T> {

    private final T data;
    private final String message;

    public final T getData() {
        return this.data;
    }

    public final String getMessage() {
        return this.message;
    }

    private Result(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public static final class Success<T> extends Result<T> {
        public Success(T data) {
            super(data, null);
        }
    }

    public static final class Error<T> extends Result<T> {
        public Error(@Nullable T data, @NotNull String message) {
            super(data, message);
        }
    }
}
