package dt.sis.parent.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import dt.sis.parent.R;
import dt.sis.parent.databinding.FragProfileInfoBinding;
import dt.sis.parent.helper.AppUtils;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.DashboardChildrenModel;

public class ProfileFragment extends Fragment {

    String children_list;
    FragProfileInfoBinding binding;
    SessionManager sessionManager;
    Context mContext;


    public static ProfileFragment getInstance() {
        return new ProfileFragment();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_profile_info, container, false);
        View mView = binding.getRoot();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);
        if (getArguments() != null)
            children_list = getArguments().getString("children_list");

        if (children_list != null) {
            Log.e("ChildrenList", children_list + " ");
            DashboardChildrenModel.Students childrenModel = new Gson().fromJson(children_list, new TypeToken<DashboardChildrenModel.Students>() {
            }.getType());
            String userName = childrenModel.getFirstname() + " " + childrenModel.getLastname();
            String className = childrenModel.getDivisionname();
            String sectionName = childrenModel.getSectionname();
            String nannyName = childrenModel.getNanny();
            String classTeacherName = childrenModel.getClassteacher();
            String assistName = childrenModel.getTeacherassitant();
            int studentId = childrenModel.getId();
            String studentCode = childrenModel.getStudentcode();
            int divisionId = childrenModel.getDivisionid();
            String dob = childrenModel.getDob();
            String picture = childrenModel.getProfilePictureId();
            String age = childrenModel.getAge();
            binding.etUsername.setText(userName);
            binding.etClass.setText(className + " Standard");
            binding.etSction.setText(sectionName);
            binding.etRollNo.setText(studentCode);
            String dateOfBirth = AppUtils.getDateFromUTC(dob);
            String[] myDob = dateOfBirth.split("-");
//            binding.tvAge.setText(AppUtils.getAge(myDob[0], myDob[1], myDob[2]));
            binding.tvAge.setText(age);

            picture = SessionManager.IMAGE_FILE_URL + "?pictureId=" + picture + "&tenantId=" + sessionManager.getTenantId();
            Picasso.get().load(picture)
                    .placeholder(R.mipmap.ic_round_user)
                    .error(R.mipmap.ic_round_user)
                    .into(binding.ivProfileimage);

            setInfo(binding.studentCode.keyTv, binding.studentCode.valueTv, "Student Code", studentCode);
            setInfo(binding.sectionCode.keyTv, binding.sectionCode.valueTv, "Section", sectionName);
            setInfo(binding.classTeacher.keyTv, binding.classTeacher.valueTv, "Class Teacher", classTeacherName);
            setInfo(binding.teacherAssist.keyTv, binding.teacherAssist.valueTv, "Teacher Assistant", assistName);
            setInfo(binding.nanny.keyTv, binding.nanny.valueTv, "Nanny", nannyName);
        }
//

        return mView;
    }

    private void setInfo(TextView keyTv, TextView valueTv, String keyStrig, String keyValue) {
        keyStrig = keyStrig != null ? keyStrig : "";
        keyTv.setText(keyStrig);
        keyValue = keyValue != null ? keyValue : "-";
        valueTv.setText(keyValue);
    }
}
