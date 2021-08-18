package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class AboutUsModel {


    @Expose
    @SerializedName("__abp")
    private boolean __abp;
    @Expose
    @SerializedName("unAuthorizedRequest")
    private boolean unAuthorizedRequest;
    @Expose
    @SerializedName("success")
    private boolean success;
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

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        @Expose
        @SerializedName("aboutUsInformation")
        private String aboutUsInformation;

        public String getAboutUsInformation() {
            return aboutUsInformation;
        }

        public void setAboutUsInformation(String aboutUsInformation) {
            this.aboutUsInformation = aboutUsInformation;
        }
    }
}
