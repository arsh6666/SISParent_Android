package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginModel {
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
        @SerializedName("roles")
        private List<String> roles;
        @Expose
        @SerializedName("linkId")
        private String linkId;
        @Expose
        @SerializedName("returnUrl")
        private String returnUrl;
        @Expose
        @SerializedName("refreshClientToken")
        private String refreshClientToken;
        @Expose
        @SerializedName("twoFactorRememberClientToken")
        private String twoFactorRememberClientToken;
        @Expose
        @SerializedName("twoFactorAuthProviders")
        private String twoFactorAuthProviders;
        @Expose
        @SerializedName("requiresTwoFactorVerification")
        private boolean requiresTwoFactorVerification;
        @Expose
        @SerializedName("userId")
        private int userId;
        @Expose
        @SerializedName("passwordResetCode")
        private String passwordResetCode;
        @Expose
        @SerializedName("shouldResetPassword")
        private boolean shouldResetPassword;
        @Expose
        @SerializedName("expireInSeconds")
        private int expireInSeconds;
        @Expose
        @SerializedName("encryptedEncodedAccessToken")
        private String encryptedEncodedAccessToken;
        @Expose
        @SerializedName("encryptedAccessToken")
        private String encryptedAccessToken;
        @Expose
        @SerializedName("accessToken")
        private String accessToken;

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public String getLinkId() {
            return linkId;
        }

        public void setLinkId(String linkId) {
            this.linkId = linkId;
        }

        public String getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }

        public String getRefreshClientToken() {
            return refreshClientToken;
        }

        public void setRefreshClientToken(String refreshClientToken) {
            this.refreshClientToken = refreshClientToken;
        }

        public String getTwoFactorRememberClientToken() {
            return twoFactorRememberClientToken;
        }

        public void setTwoFactorRememberClientToken(String twoFactorRememberClientToken) {
            this.twoFactorRememberClientToken = twoFactorRememberClientToken;
        }

        public String getTwoFactorAuthProviders() {
            return twoFactorAuthProviders;
        }

        public void setTwoFactorAuthProviders(String twoFactorAuthProviders) {
            this.twoFactorAuthProviders = twoFactorAuthProviders;
        }

        public boolean getRequiresTwoFactorVerification() {
            return requiresTwoFactorVerification;
        }

        public void setRequiresTwoFactorVerification(boolean requiresTwoFactorVerification) {
            this.requiresTwoFactorVerification = requiresTwoFactorVerification;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getPasswordResetCode() {
            return passwordResetCode;
        }

        public void setPasswordResetCode(String passwordResetCode) {
            this.passwordResetCode = passwordResetCode;
        }

        public boolean getShouldResetPassword() {
            return shouldResetPassword;
        }

        public void setShouldResetPassword(boolean shouldResetPassword) {
            this.shouldResetPassword = shouldResetPassword;
        }

        public int getExpireInSeconds() {
            return expireInSeconds;
        }

        public void setExpireInSeconds(int expireInSeconds) {
            this.expireInSeconds = expireInSeconds;
        }

        public String getEncryptedEncodedAccessToken() {
            return encryptedEncodedAccessToken;
        }

        public void setEncryptedEncodedAccessToken(String encryptedEncodedAccessToken) {
            this.encryptedEncodedAccessToken = encryptedEncodedAccessToken;
        }

        public String getEncryptedAccessToken() {
            return encryptedAccessToken;
        }

        public void setEncryptedAccessToken(String encryptedAccessToken) {
            this.encryptedAccessToken = encryptedAccessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
