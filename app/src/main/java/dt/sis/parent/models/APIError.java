package dt.sis.parent.models;

import com.google.gson.annotations.SerializedName;

public class APIError {

    @SerializedName("__abp")
    private boolean Abp;
    @SerializedName("unAuthorizedRequest")
    private boolean unauthorizedrequest;
    @SerializedName("error")
    private Error error;
    @SerializedName("success")
    private boolean success;
    @SerializedName("targetUrl")
    private String targeturl;
    @SerializedName("result")
    private String result;

    public boolean getAbp() {
        return Abp;
    }

    public void setAbp(boolean Abp) {
        this.Abp = Abp;
    }

    public boolean getUnauthorizedrequest() {
        return unauthorizedrequest;
    }

    public void setUnauthorizedrequest(boolean unauthorizedrequest) {
        this.unauthorizedrequest = unauthorizedrequest;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTargeturl() {
        return targeturl;
    }

    public void setTargeturl(String targeturl) {
        this.targeturl = targeturl;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static class Error {
        @SerializedName("validationErrors")
        private String validationerrors;
        @SerializedName("details")
        private String details;
        @SerializedName("message")
        private String message;
        @SerializedName("code")
        private int code;

        public String getValidationerrors() {
            return validationerrors;
        }

        public void setValidationerrors(String validationerrors) {
            this.validationerrors = validationerrors;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
