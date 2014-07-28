package com.osolve.thor.client;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.osolve.json.ClientJsonMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import bolts.Task;

/**
 * Created by Kros on 7/22/14.
 */
public class RequestBuilder {

    private RequestQueue requestQueue;
    private String urlPrefix;
    private int method;
    private boolean allowCache;
    private String path;
    private Map<String, String> params;
    private Map<String, String> headers;
    private VolleyError emptyResponseError;
    private Object jsonBody;
    private Request.Priority priority = Request.Priority.NORMAL;
    private Integer port;
    private String host;

    public RequestBuilder(final RequestQueue queue) {
        this.requestQueue = queue;
    }

    public RequestBuilder withUrlPrefix(final String urlPrefix) {
        this.urlPrefix = urlPrefix;
        return this;
    }

    public RequestBuilder withHttpGetAllowCache() {
        method = Request.Method.GET;
        allowCache = true;
        return this;
    }

    public RequestBuilder withHttpPost() {
        method = Request.Method.POST;
        return this;
    }

    public RequestBuilder withPath(final String pathStartWithSlash) {
        if (pathStartWithSlash.length() > 0 && !pathStartWithSlash.startsWith("/")) {
            throw new IllegalArgumentException("path must start with slash '/' - " + pathStartWithSlash);
        }

        if (TextUtils.isEmpty(path)) {
            path = pathStartWithSlash;
        } else {
            path += pathStartWithSlash;
        }
        return this;
    }

    public RequestBuilder withPort(final int port) {
        this.port = port;
        return this;
    }

    public RequestBuilder withPriority(final Request.Priority priority) {
        this.priority = priority;
        return this;
    }

    public RequestBuilder withAddHeader(final String field, final String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(field, value);
        return this;
    }

    public RequestBuilder withAddHeaders(final Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = new HashMap<String, String>();
        }
        this.headers.putAll(headers);
        return this;
    }

    /**
     * value can be primitive, array, or any {@link Iterable}. when value is Array or Iterable, make
     * sure String of each item do not include any comma <code>,</code>
     */
    public RequestBuilder withAddParam(final String key, final Object value) {
        if (params == null) {
            // use TreeMap because volley use url as cache key, so order is important
            params = new TreeMap<String, String>();
        }
        if (value == null) {
            params.put(key, null);
        } else if (value instanceof Iterable) {
            params.put(key, TextUtils.join(",", (Iterable<?>) value));
        } else if (value.getClass().isArray()) {
            params.put(key, TextUtils.join(",", (Object[]) value));
        } else {
            params.put(key, value.toString());
        }
        return this;
    }

    public RequestBuilder withAddParams(final Map<String, String> params) {
        if (this.params == null) {
            // use TreeMap because volley use url as cache key, so order is important
            this.params = new TreeMap<String, String>();
        }
        this.params.putAll(params);
        return this;
    }

    /**
     * complete the request with a volley error if received empty http response. if not specified.
     * prefer the emptyResponseValue in {@link #build(Class, Object)}'s
     * <p/>
     * <p>
     * note that this setting overwrite setting of emptyResponseValue
     * </p>
     */
    public RequestBuilder withEmptyResponseAsError(final VolleyError emptyResponseError) {
        this.emptyResponseError = emptyResponseError;
        return this;
    }

    public RequestBuilder withHost(final String host) {
        this.host = host;
        return this;
    }

    public <T> Requestable<T> build(final TypeReference<T> returnTypeReference) {
        return build(returnTypeReference, null);
    }

    public <T> Requestable<T> build(final TypeReference<T> returnTypeReference,
                                    final T emptyResponseValue) {
        return new JacksonRequest<T>(method,
                buildFullUrl(),
                emptyResponseValue,
                returnTypeReference,
                null);
    }

    public <T> Requestable<T> build(final Class<T> returnTypeClass) {
        return build(returnTypeClass, null);
    }

    public <T> Requestable<T> build(final Class<T> returnTypeClass, final T emptyResponseValue) {
        return new JacksonRequest<T>(method, buildFullUrl(), emptyResponseValue, null, returnTypeClass);
    }

    private String buildFullUrl() {

        if (Request.Method.GET == method) {
            return urlPrefix + path + convertParamsToString();
        } else if (Request.Method.POST == method) {
            return urlPrefix + path;
        }
        return urlPrefix + path;
    }

    private String convertParamsToString() {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder paramsString = new StringBuilder();
        paramsString.append("?");
        for (String key : params.keySet()) {
            paramsString.append(key).append("=").append(params.get(key)).append("&");
        }
        int i = paramsString.lastIndexOf("&");
        if (i == paramsString.length() - 1) {
            paramsString.deleteCharAt(i);
        }
        return paramsString.toString();
    }

    public boolean isJsonBody() {
        return jsonBody != null;
    }

    private class JacksonRequest<T> extends CompletableRequest<T> implements Requestable<T> {
        private TypeReference<T> typeReference;
        private Class<T> typeClass;
        private T emptyResponseValue;

        private JacksonRequest(final int method,
                               final String url,
                               final T emptyResponseValue,
                               final TypeReference<T> typeReference,
                               final Class<T> typeClass) {
            super(method, url);
            this.emptyResponseValue = emptyResponseValue;
            this.typeReference = typeReference;
            this.typeClass = typeClass;
        }

        private T decodeResponseJson(final NetworkResponse response) throws ParseError {
            try {
                if (typeReference != null) {
                    return ClientJsonMapper.getInstance().readValue(response.data, typeReference);
                }
                if (typeClass != null) {
                    return ClientJsonMapper.getInstance().readValue(response.data, typeClass);
                }
                throw new IllegalStateException();
            } catch (final IOException e) {
                throw new ParseError(response);
            }
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            if (isPostJsonBody()) {
                try {
                    return ClientJsonMapper.getInstance().writeValueAsBytes(jsonBody);
                } catch (final IOException e) {
                    throw new RuntimeException("unexpected", e);
                }
            } else {
                return super.getBody();
            }
        }

        @Override
        public String getBodyContentType() {

            if (isPostJsonBody()) {
                return "application/json";
            }

            return super.getBodyContentType();
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> createdHeaders = null;
            if (headers != null) {
                if (createdHeaders == null) {
                    createdHeaders = new HashMap<String, String>();
                }
                createdHeaders.putAll(headers);
            }

            if (createdHeaders != null) {
                return createdHeaders;
            }
            return super.getHeaders();
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            if (method == Method.POST) {
                return params;
            }
            return null;
        }

        @Override
        public Priority getPriority() {
            if (priority == null) {
                return super.getPriority();
            } else {
                return priority;
            }
        }

        private boolean isPostJsonBody() {
            return method == Method.POST && jsonBody != null;
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                if (typeClass == Void.class) {
                    return Response.success(null, null);
                }

                T decoded = emptyResponseValue;

                if (response.data != null && response.data.length > 0) {
                    decoded = decodeResponseJson(response);
                } else {
                    if (emptyResponseError != null) {
                        return Response.error(emptyResponseError);
                    }
                }

                final com.android.volley.Cache.Entry cacheEntry;
                if (allowCache && method == Method.GET) {
                    cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                } else {
                    cacheEntry = null;
                }
                return Response.success(decoded, cacheEntry);
            } catch (final ParseError e) {
                return Response.error(e);
            }
        }

        @Override
        public Task<T> request() {
            return execute(requestQueue);
        }
    }
}
