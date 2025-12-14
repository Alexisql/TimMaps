package com.alexis.timmaps.data.remote.processqr.service;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProcessQrService {

    private static final String END_POINT = "https://noderedtest.coordinadora.com/api/v1/validar";
    private final RequestQueue queue;

    @Inject
    public ProcessQrService(RequestQueue queue) {
        this.queue = queue;
    }

    public void validateCodeQr(String base64, Response.Listener<JSONObject> ok, Response.ErrorListener error) {

        JSONObject body = new JSONObject();
        try {
            body.put("data", base64);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                END_POINT,
                body,
                ok,
                error
        );

        queue.add(request);
    }
}
