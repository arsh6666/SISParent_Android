package dt.sis.parent.webservices;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dt.sis.parent.BuildConfig;
import dt.sis.parent.R;
import dt.sis.parent.helper.SessionManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Er. Shashank on 21 Sep 2019.
 */

public class ApiClient {
    public static String BASE_URL = SessionManager.BASE_URL;

    public static Retrofit getClient(final Context mContext) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.cache(null);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Authorization", new SessionManager(mContext).getToken())
                        .addHeader("Abp.TenantID", new SessionManager(mContext).getTenantId())
                        .addHeader("Abp.OrganizationId", new SessionManager(mContext).getOrganizationId())
                        .addHeader(".AspNetCore.Culture", "c=en |uic=en")
                        .addHeader(".Abp.DeviceType", "ANDROID")
                        .addHeader(".Abp.DeviceId", new SessionManager(mContext).getFirebaseToken())
                        .addHeader(".Abp.ApplicationName", "ParentApp")
                        .addHeader(".Abp.ApplicationVersion", BuildConfig.VERSION_NAME)
                        .method(original.method(), original.body())
                        .build();

                Log.e("REQUEST_HEADERS",request.headers().toString());


                return chain.proceed(request);
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(httpClient.build())
                    .build();

        return retrofit;
    }

}