package com.alexis.timmaps.data.remote.processqr.repository;

import android.util.Base64;

import com.alexis.timmaps.data.remote.processqr.mapper.QrMapper;
import com.alexis.timmaps.data.remote.processqr.model.QrResponse;
import com.alexis.timmaps.domain.processqr.model.Qr;
import com.alexis.timmaps.domain.processqr.repository.IValidateQrRepository;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;

@Singleton
public class ValidateQrRepositoryImpl implements IValidateQrRepository {

    private static final String END_POINT = "https://noderedtest.coordinadora.com/api/v1/validar";
    private final RequestQueue queue;

    @Inject
    public ValidateQrRepositoryImpl(RequestQueue queue) {
        this.queue = queue;
    }

    @Override
    public Single<Qr> validateCodeQr(String codeQr) {
        return Single.create(emitter -> {

            JSONObject body = new JSONObject();
            try {
                body.put("data", encodeToBase64(codeQr));
            } catch (Exception exception) {
                emitter.onError(new IllegalArgumentException(exception));
                return;
            }

            Response.Listener<JSONObject> successListener = response -> {
                try {
                    String correcto = response.getString("Correcto");
                    String data = response.getString("data");
                    QrResponse qrResponse = new QrResponse(correcto, data);
                    emitter.onSuccess(QrMapper.toDomain(qrResponse));
                } catch (JSONException e) {
                    emitter.onError(new IllegalArgumentException("Estructura Incorrecta", e));
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    END_POINT,
                    body,
                    successListener,
                    emitter::onError
            );

            queue.add(request);
        });
    }

    @Override
    public String encodeToBase64(String data) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(dataBytes, Base64.NO_WRAP);
    }
}
