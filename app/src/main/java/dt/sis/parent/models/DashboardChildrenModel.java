package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardChildrenModel {

    @Expose
    @SerializedName("__abp")
    private boolean Abp;
    @Expose
    @SerializedName("unAuthorizedRequest")
    private boolean unauthorizedrequest;
    @Expose
    @SerializedName("error")
    private String error;
    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("targetUrl")
    private String targeturl;
    @Expose
    @SerializedName("result")
    private Result result;

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

    public String getTargeturl() {
        return targeturl;
    }

    public void setTargeturl(String targeturl) {
        this.targeturl = targeturl;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        @Expose
        @SerializedName("students")
        private List<Students> students;
        @Expose
        @SerializedName("description")
        private String description;
        @Expose
        @SerializedName("relationWithStudent")
        private String relationwithstudent;
        @Expose
        @SerializedName("gender")
        private String gender;
        @Expose
        @SerializedName("phoneNumber")
        private String phonenumber;
        @Expose
        @SerializedName("mobileNumber")
        private String mobilenumber;
        @Expose
        @SerializedName("email")
        private String email;
        @Expose
        @SerializedName("lastName")
        private String lastname;
        @Expose
        @SerializedName("firstName")
        private String firstname;
        @Expose
        @SerializedName("id")
        private int id;

        public List<Students> getStudents() {
            return students;
        }

        public void setStudents(List<Students> students) {
            this.students = students;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getRelationwithstudent() {
            return relationwithstudent;
        }

        public void setRelationwithstudent(String relationwithstudent) {
            this.relationwithstudent = relationwithstudent;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getMobilenumber() {
            return mobilenumber;
        }

        public void setMobilenumber(String mobilenumber) {
            this.mobilenumber = mobilenumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Students {
        @Expose
        @SerializedName("nanny")
        private String nanny;
        @Expose
        @SerializedName("teacherAssitant")
        private String teacherassitant;
        @Expose
        @SerializedName("classTeacher")
        private String classteacher;
        @Expose
        @SerializedName("sectionName")
        private String sectionname;
        @Expose
        @SerializedName("divisionName")
        private String divisionname;
        @Expose
        @SerializedName("sectionId")
        private int sectionid;
        @Expose
        @SerializedName("divisionId")
        private int divisionid;
        @Expose
        @SerializedName("age")
        private String age;
        @Expose
        @SerializedName("dob")
        private String dob;
        @Expose
        @SerializedName("photoUrl")
        private String photourl;
        @SerializedName("profilePictureId")
        private String profilePictureId;


        @Expose
        @SerializedName("studentCodeEnc")
        private String studentcodeenc;
        @Expose
        @SerializedName("studentCode")
        private String studentcode;
        @Expose
        @SerializedName("lastName")
        private String lastname;
        @Expose
        @SerializedName("firstName")
        private String firstname;
        @Expose
        @SerializedName("studentIdEnc")
        private String studentidenc;
        @Expose
        @SerializedName("id")
        private int id;

        public String getProfilePictureId() {
            return profilePictureId;
        }

        public void setProfilePictureId(String profilePictureId) {
            this.profilePictureId = profilePictureId;
        }

        public String getNanny() {
            return nanny;
        }

        public void setNanny(String nanny) {
            this.nanny = nanny;
        }

        public String getTeacherassitant() {
            return teacherassitant;
        }

        public void setTeacherassitant(String teacherassitant) {
            this.teacherassitant = teacherassitant;
        }

        public String getClassteacher() {
            return classteacher;
        }

        public void setClassteacher(String classteacher) {
            this.classteacher = classteacher;
        }

        public String getSectionname() {
            return sectionname;
        }

        public void setSectionname(String sectionname) {
            this.sectionname = sectionname;
        }

        public String getDivisionname() {
            return divisionname;
        }

        public void setDivisionname(String divisionname) {
            this.divisionname = divisionname;
        }

        public int getSectionid() {
            return sectionid;
        }

        public void setSectionid(int sectionid) {
            this.sectionid = sectionid;
        }

        public int getDivisionid() {
            return divisionid;
        }

        public void setDivisionid(int divisionid) {
            this.divisionid = divisionid;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getPhotourl() {
            return photourl;
        }

        public void setPhotourl(String photourl) {
            this.photourl = photourl;
        }

        public String getStudentcodeenc() {
            return studentcodeenc;
        }

        public void setStudentcodeenc(String studentcodeenc) {
            this.studentcodeenc = studentcodeenc;
        }

        public String getStudentcode() {
            return studentcode;
        }

        public void setStudentcode(String studentcode) {
            this.studentcode = studentcode;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getStudentidenc() {
            return studentidenc;
        }

        public void setStudentidenc(String studentidenc) {
            this.studentidenc = studentidenc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
