package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CommentsListModel implements Serializable {


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
    private List<Result> result;

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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }


    public static class Result {
        @Expose
        @SerializedName("parentReadAt")
        private String parentReadAt;
        @Expose
        @SerializedName("teacherReadAt")
        private String teacherReadAt;
        @Expose
        @SerializedName("isTeacherRead")
        private boolean isTeacherRead;
        @Expose
        @SerializedName("isParentRead")
        private boolean isParentRead;
        @Expose
        @SerializedName("isCreatedByParent")
        private boolean isCreatedByParent;
        @Expose
        @SerializedName("sectionId")
        private int sectionId;
        @Expose
        @SerializedName("divisionId")
        private int divisionId;
        @Expose
        @SerializedName("sectionName")
        private String sectionName;
        @Expose
        @SerializedName("divisionName")
        private String divisionName;
        @Expose
        @SerializedName("parentComment")
        private String parentComment;
        @Expose
        @SerializedName("teacherComment")
        private String teacherComment;
        @Expose
        @SerializedName("lastName")
        private String lastName;
        @Expose
        @SerializedName("middleName")
        private String middleName;
        @Expose
        @SerializedName("firstName")
        private String firstName;
        @Expose
        @SerializedName("commentDate")
        private String commentDate;
        @Expose
        @SerializedName("id")
        private String id;

        public String getParentReadAt() {
            return parentReadAt;
        }

        public void setParentReadAt(String parentReadAt) {
            this.parentReadAt = parentReadAt;
        }

        public String getTeacherReadAt() {
            return teacherReadAt;
        }

        public void setTeacherReadAt(String teacherReadAt) {
            this.teacherReadAt = teacherReadAt;
        }

        public boolean getIsTeacherRead() {
            return isTeacherRead;
        }

        public void setIsTeacherRead(boolean isTeacherRead) {
            this.isTeacherRead = isTeacherRead;
        }

        public boolean getIsParentRead() {
            return isParentRead;
        }

        public void setIsParentRead(boolean isParentRead) {
            this.isParentRead = isParentRead;
        }

        public boolean getIsCreatedByParent() {
            return isCreatedByParent;
        }

        public void setIsCreatedByParent(boolean isCreatedByParent) {
            this.isCreatedByParent = isCreatedByParent;
        }

        public int getSectionId() {
            return sectionId;
        }

        public void setSectionId(int sectionId) {
            this.sectionId = sectionId;
        }

        public int getDivisionId() {
            return divisionId;
        }

        public void setDivisionId(int divisionId) {
            this.divisionId = divisionId;
        }

        public String getSectionName() {
            return sectionName;
        }

        public void setSectionName(String sectionName) {
            this.sectionName = sectionName;
        }

        public String getDivisionName() {
            return divisionName;
        }

        public void setDivisionName(String divisionName) {
            this.divisionName = divisionName;
        }

        public String getParentComment() {
            return parentComment;
        }

        public void setParentComment(String parentComment) {
            this.parentComment = parentComment;
        }

        public String getTeacherComment() {
            return teacherComment;
        }

        public void setTeacherComment(String teacherComment) {
            this.teacherComment = teacherComment;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getCommentDate() {
            return commentDate;
        }

        public void setCommentDate(String commentDate) {
            this.commentDate = commentDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}