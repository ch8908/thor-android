package com.osolve.thor.client;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;

import bolts.Task;

/**
 * Created by Kros on 7/22/14.
 */
public abstract class CompletableRequest<T> extends Request<T> {
    private final Task<T>.TaskCompletionSource completionSource;

    public CompletableRequest(int method, String url) {
        super(method, url, null);
        completionSource = Task.create();
    }

    @Override
    public void deliverError(VolleyError error) {
        completionSource.setError(error);
    }

    @Override
    protected void deliverResponse(T response) {
        completionSource.setResult(response);
    }

    public final Task<T> execute(final RequestQueue queue) {
        queue.add(this);
        return onExecuteTask(completionSource.getTask());
    }

    public final Task<T> onExecuteTask(Task<T> task) {
        return task;
    }
}
