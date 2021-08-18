package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileModel {

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
        @SerializedName("isGoogleAuthenticatorEnabled")
        private boolean isGoogleAuthenticatorEnabled;
        @Expose
        @SerializedName("qrCodeSetupImageUrl")
        private String qrCodeSetupImageUrl;
        @Expose
        @SerializedName("timezone")
        private String timezone;
        @Expose
        @SerializedName("isPhoneNumberConfirmed")
        private boolean isPhoneNumberConfirmed;
        @Expose
        @SerializedName("emailAddress")
        private String emailAddress;
        @Expose
        @SerializedName("userName")
        private String userName;
        @Expose
        @SerializedName("surname")
        private String surname;
        @Expose
        @SerializedName("phoneNumber")
        private String phoneNumber;
        @Expose
        @SerializedName("name")
        private String name;

        public boolean getIsGoogleAuthenticatorEnabled() {
            return isGoogleAuthenticatorEnabled;
        }

        public void setIsGoogleAuthenticatorEnabled(boolean isGoogleAuthenticatorEnabled) {
            this.isGoogleAuthenticatorEnabled = isGoogleAuthenticatorEnabled;
        }

        public String getQrCodeSetupImageUrl() {
            return qrCodeSetupImageUrl;
        }

        public void setQrCodeSetupImageUrl(String qrCodeSetupImageUrl) {
            this.qrCodeSetupImageUrl = qrCodeSetupImageUrl;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public boolean getIsPhoneNumberConfirmed() {
            return isPhoneNumberConfirmed;
        }

        public void setIsPhoneNumberConfirmed(boolean isPhoneNumberConfirmed) {
            this.isPhoneNumberConfirmed = isPhoneNumberConfirmed;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
