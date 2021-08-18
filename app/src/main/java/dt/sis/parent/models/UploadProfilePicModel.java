package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadProfilePicModel {

    @Expose
    @SerializedName("__abp")
    private boolean __abp;
    @Expose
    @SerializedName("unAuthorizedRequest")
    private boolean unAuthorizedRequest;
    @Expose
    @SerializedName("error")
    private String error;
    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("targetUrl")
    private String targetUrl;
    @Expose
    @SerializedName("result")
    private Result result;

    public boolean get__abp() {
        return __abp;
    }

    public void set__abp(boolean __abp) {
        this.__abp = __abp;
    }

    public boolean getUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        @Expose
        @SerializedName("validationErrors")
        private String validationErrors;
        @Expose
        @SerializedName("details")
        private String details;
        @Expose
        @SerializedName("message")
        private String message;
        @Expose
        @SerializedName("code")
        private int code;
        @Expose
        @SerializedName("sessionId")
        private String sessionId;
        @Expose
        @SerializedName("height")
        private int height;
        @Expose
        @SerializedName("width")
        private int width;
        @Expose
        @SerializedName("fileName")
        private String fileName;

        public String getValidationErrors() {
            return validationErrors;
        }

        public void setValidationErrors(String validationErrors) {
            this.validationErrors = validationErrors;
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

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
