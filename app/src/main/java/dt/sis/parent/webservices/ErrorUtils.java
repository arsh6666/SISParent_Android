package dt.sis.parent.webservices;

import android.content.Context;

import java.io.IOException;
import java.lang.annotation.Annotation;

import dt.sis.parent.models.APIError;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static APIError parseError(Context mContext, Response<?> response) {
        Converter<ResponseBody, APIError> converter =
                ApiClient.getClient(mContext).responseBodyConverter(APIError.class,new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}