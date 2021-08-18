package dt.sis.parent.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import dt.sis.parent.R;
import dt.sis.parent.adapters.HealthCategoryCardAdapter;
import dt.sis.parent.adapters.HealthTabAdapter;
import dt.sis.parent.databinding.FragmentHealthBinding;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.DashboardChildrenModel;
import dt.sis.parent.models.HabitsUserModel;
import dt.sis.parent.models.HealthTabModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class HealthFragment extends Fragment {
    FragmentHealthBinding binding;
    String children_list;
    Context mContext;
    SessionManager sessionManager;

    HealthTabAdapter<HealthTabModel> healthTabAdapter;
    HealthCategoryCardAdapter<HabitsUserModel.Habits> habitTypeAdapter;
    List<HealthTabModel> tabModelList;
    List<HabitsUserModel.Habits> habitTypeList;

    List<HabitsUserModel.Habits> habbitsDataList;
    DateFormat dateFormat;
    int studentId;

    public static HealthFragment getInstance() {
        return new HealthFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_health, container, false);
        View mView = binding.getRoot();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);

        dateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());

        setHealthTabHeader();
        setHabitTypeAdapter();

        if(getArguments()!=null)
            children_list = getArguments().getString("children_list");

        if(children_list!=null) {
            Log.e("ChildrenList", children_list + " ");
            Type type = new TypeToken<DashboardChildrenModel.Students>() { }.getType();
            DashboardChildrenModel.Students childrenModel = new Gson().fromJson(children_list, type);
            studentId = childrenModel.getId();
            String userName = childrenModel.getFirstname()+" "+childrenModel.getLastname();
            String picture = childrenModel.getProfilePictureId();
            binding.nameTv.setText(userName);
            picture = SessionManager.IMAGE_FILE_URL + "?pictureId=" + picture + "&tenantId=" + sessionManager.getTenantId();
            Picasso.get().load(picture)
                    .placeholder(R.mipmap.ic_round_user)
                    .error(R.mipmap.ic_round_user)
                    .into(binding.profileImage);

            getDailyHabit();
        }

        setCalenderView();


        return mView;
    }
    private  void selectDatePicker(){
        try {
            final Calendar c = Calendar.getInstance();
            String currentValue = binding.calenderTv.getText().toString().trim();
            Date currentSetDAte  = dateFormat.parse(currentValue);
            currentSetDAte = currentSetDAte!=null ? currentSetDAte: new Date();
            c.setTime(currentSetDAte);
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, monthOfYear, dayOfMonth);
                            String strDate = dateFormat.format(calendar.getTime());
                            binding.calenderTv.setText(strDate);
                            getDailyHabit();

                        }
                    }, mYear, mMonth, mDay);

            // Subtract 1 month from Calendar updated date
            c.add(Calendar.MONTH, -1);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCalenderView() {
        Date date = new Date();
        binding.calenderTv.setText(dateFormat.format(date));

        binding.calenderTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDatePicker();
            }
        });
    }

    private String getDate(){
        String returnTime="";
        try {
            String date = binding.calenderTv.getText().toString().trim();
            Date formatedDate= dateFormat.parse(date);

            if(formatedDate!=null) {
                SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//                newFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                returnTime = newFormatter.format(formatedDate);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return returnTime;
    }

    private void setHealthTabHeader(){
        tabModelList = new ArrayList<>();
        healthTabAdapter = new HealthTabAdapter<HealthTabModel>(mContext, tabModelList, new HealthTabAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                for (HealthTabModel tab:tabModelList) {
                    tab.setCheck(false);
                }
                tabModelList.get(position).setCheck(true);
                healthTabAdapter.updateData(tabModelList);
                int habitId = tabModelList.get(position).getId();
                setHabitType(habitId);
            }
        }) {
            @Override
            public boolean getCheck(int position, HealthTabModel healthTabModel) {
                return  healthTabModel.isCheck();
            }

            @Override
            public String getName(int position, HealthTabModel healthTabModel) {
                return  healthTabModel.getTitle();
            }
        };
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        binding.tabRecyclerView.setLayoutManager(mLayoutManager);
        binding.tabRecyclerView.setAdapter(healthTabAdapter);

    }

    private void setHabitTypeAdapter(){
        habitTypeList = new ArrayList<>();
        habitTypeAdapter = new HealthCategoryCardAdapter<HabitsUserModel.Habits>(mContext,habitTypeList,null) {

            @Override
            public String getHabitType(int position, HabitsUserModel.Habits habits) {
                String value = habits.getHabitTypeName();
                value = value!=null ? value :"";
                return value;
            }

            @Override
            public String getHabitOption(int position, HabitsUserModel.Habits habits) {
                String value = habits.getHabitOptionName();
                value = value!=null ? value :"";
                return value;
            }

            @Override
            public String getImage(int position, HabitsUserModel.Habits habits) {
                String value = habits.getIconurl();
                value = value!=null ? value :"";
                return value;
            }
        };
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        binding.subRecyclerView.setLayoutManager(mLayoutManager);
        binding.subRecyclerView.setAdapter(habitTypeAdapter);

        if(getArguments()!=null)
            children_list = getArguments().getString("children_list");

        if(children_list!=null) {
            Log.e("ChildrenList", children_list + " ");
            DashboardChildrenModel.Students childrenModel = new Gson().fromJson(children_list, new TypeToken<DashboardChildrenModel.Students>() {
            }.getType());
            getDailyHabit();
        }

    }

    private void getDailyHabit() {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        String habitDate = getDate();
        Log.e("UTC_HabitTime",habitDate);
        call = webApis.getDailyHabit(studentId,habitDate);
        ApiServices apiServices = new ApiServices(mContext);

        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    HabitsUserModel modelVal = new Gson().fromJson(response, HabitsUserModel.class);
                    if(modelVal.getResult()!=null) {

                        habbitsDataList= new ArrayList<>(modelVal.getResult().getHabits());
                        List <HabitsUserModel.Habits> uniqueHabitList = new ArrayList<>();

                        for (HabitsUserModel.Habits obj : habbitsDataList) {
                            boolean isFound = false;
                            for (HabitsUserModel.Habits obj2 : uniqueHabitList) {
                                if (obj2.getHabitId()==obj.getHabitId() || (obj2.equals(obj))) {
                                    isFound = true;
                                    break;
                                }
                            }
                            if (!isFound) uniqueHabitList.add(obj);
                        }

                        tabModelList = new ArrayList<>();

                        for(int i=0 ;i<uniqueHabitList.size();i++){
                            String title = uniqueHabitList.get(i).getHabitName();
                            title = title!=null ? title: "";
                            int id = uniqueHabitList.get(i).getHabitId();

                            HealthTabModel tabModel = new HealthTabModel();
                            tabModel.setTitle(title);
                            tabModel.setId(id);
                            tabModel.setCheck(false);
                            tabModelList.add(tabModel);
                        }
                        tabModelList.get(0).setCheck(true);

                        healthTabAdapter.updateData(tabModelList);

                        if(tabModelList.size()>0)

                        setHabitType(tabModelList.get(0).getId());

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void setHabitType(int habitTabId) {
        habitTypeList = new ArrayList<>();
        for(int i =0;i<habbitsDataList.size();i++){
            int id = habbitsDataList.get(i).getHabitId();
            if(id==habitTabId){
                habitTypeList.add(habbitsDataList.get(i));
            }
        }
        habitTypeAdapter.updateData(habitTypeList);
    }

}
