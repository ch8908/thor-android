package com.osolve.thor.client;

import bolts.Task;

/**
 * Created by Kros on 7/22/14.
 */
public interface Requestable<T> {
    Task<T> request();
}
