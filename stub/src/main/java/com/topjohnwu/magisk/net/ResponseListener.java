package com.topjohnwu.liorsmagic.net;

public interface ResponseListener<T> {
    void onResponse(T response);
}
