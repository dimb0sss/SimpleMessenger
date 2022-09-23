package com.lvovds.simplemessenger;

import com.lvovds.simplemessenger.notification.MessageSend;
import com.lvovds.simplemessenger.notification.Notification;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @Headers({"Authorization: key=AAAAHR1X2fQ:APA91bHo5kZbOQSjqqIMwhO0KDs9MiP_rNhtuk--0mgSMuZy20MtWDN_w999IfQgWOa7-s2rCsThClgz3lHahOspPdUmBBRuk2e4CCSWSiG3se2wgZ3mbtb9PfyE9YtmvQnKfd0ACpHs"})
    @POST("send")
    Single<String> messageResponse(@Body MessageSend messageSend);


}