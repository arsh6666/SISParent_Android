package dt.sis.parent.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dt.sis.parent.R;
import dt.sis.parent.adapters.HealthCategoryCardAdapter;
import dt.sis.parent.adapters.HealthTabAdapter;
import dt.sis.parent.databinding.FragmentAttendanceBinding;
import dt.sis.parent.helper.DrawableUtils;
import dt.sis.parent.helper.SessionManager;
import dt.sis.parent.models.AttendanceModel;
import dt.sis.parent.models.DashboardChildrenModel;
import dt.sis.parent.models.HabitsUserModel;
import dt.sis.parent.models.HealthTabModel;
import dt.sis.parent.webservices.ApiClient;
import dt.sis.parent.webservices.ApiServices;
import dt.sis.parent.webservices.WebApis;
import retrofit2.Call;

public class AttendanceFragment extends Fragment {
    FragmentAttendanceBinding binding;
    String children_list;
    Context mContext;
    SessionManager sessionManager;

    HealthTabAdapter<HealthTabModel> healthTabAdapter;
    HealthCategoryCardAdapter<HabitsUserModel.Habits> habitTypeAdapter;
    List<HealthTabModel> tabModelList;
    List<HabitsUserModel.Habits> habitTypeList;

    List<AttendanceModel.Result> attendanceListList;
    DateFormat dateFormat;
    int studentId;
    List<EventDay> events;

    public static AttendanceFragment getInstance() {
        return new AttendanceFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_attendance, container, false);
        View mView = binding.getRoot();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);

        dateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());

        if(getArguments()!=null)
            children_list = getArguments().getString("children_list");

        if(children_list!=null) {
            Log.e("ChildrenList", children_list + " ");
            Type type = new TypeToken<DashboardChildrenModel.Students>() { }.getType();
            DashboardChildrenModel.Students childrenModel = new Gson().fromJson(children_list, type);
            studentId = childrenModel.getId();
            String userName = childrenModel.getFirstname()+" "+childrenModel.getLastname();
            String picture = childrenModel.getPhotourl();

            getAttendance();
        }

        setCalenderSetup();

        return mView;
    }

    private void setCalenderSetup() {
        try {
            String showTime = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(new Date());
            binding.tvDate.setText(showTime);
        } catch (Exception e) {
            e.printStackTrace();
        }


        binding.llGate.setVisibility(View.VISIBLE);
        binding.calendarView.setForwardButtonImage(mContext.getResources().getDrawable(R.drawable.ic_right_arrow_24));
        binding.calendarView.setOnDayClickListener(onDayClickListener);
    }

    OnDayClickListener onDayClickListener  = new OnDayClickListener() {
        @Override
        public void onDayClick(EventDay eventDay) {
            String intime = "", otime = "";
            String kes = eventDay.getCalendar().getTime().toString();
            String ed = String.valueOf(eventDay.isEnabled());
            SimpleDateFormat timeFormat = new SimpleDateFormat("KK:mm:ss a",Locale.getDefault());
            SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date d1 = null;
            try {
                d1 = sdf3.parse(kes);
                String datetime = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(d1);
                String showTime = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(d1);

                String righttime = datetime + "T00:00:00Z";
                binding.tvDate.setText(showTime);
                for (int i = 0; i < attendanceListList.size(); i++) {
                    if (attendanceListList.get(i).getAttendancedate().equalsIgnoreCase(righttime)) {

                        binding.llGate.setVisibility(View.VISIBLE);
                        if (attendanceListList.get(i).getPresent().equalsIgnoreCase("1")) {

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                            Date date1 = null;
                            Date date2 = null;
                            try {
                                date1 = sdf.parse(attendanceListList.get(i).getCheckintime());
                                intime = date1!=null ? timeFormat.format(date1) :"";

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            try {
                                date2 = sdf.parse(attendanceListList.get(i).getCheckouttime());
                                otime = date2!=null? timeFormat.format(date2) : "";

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (attendanceListList.get(i).getCheckinblockname() != null && !attendanceListList.get(i).getCheckinblockname().equalsIgnoreCase("Not Detected")) {
                                binding.tvTimein.setText(intime + " ");
                                binding.tvIn.setVisibility(View.VISIBLE);
                                binding.tvTimein.setVisibility(View.VISIBLE);
                                binding.tvIn.setText("" + attendanceListList.get(i).getCheckinblockname());
                            } else {

                                binding.tvIn.setText("Not Detected");
                                binding.tvTimein.setVisibility(View.GONE);
                            }

                            if (attendanceListList.get(i).getCheckoutblockname() != null && !attendanceListList.get(i).getCheckoutblockname().equalsIgnoreCase("Not Detected")) {
                                binding.tvTimeout.setText(otime + " ");
                                binding.tvOut.setText("" + attendanceListList.get(i).getCheckoutblockname());
                                binding.tvOut.setVisibility(View.VISIBLE);
                                binding.tvTimeout.setVisibility(View.VISIBLE);

                            } else {
                                binding.tvOut.setText("Not Detected");
                                binding.tvTimeout.setVisibility(View.GONE);

                            }
                        } else {

                            binding.tvIn.setText("Not Detected");
                            binding.tvOut.setText("Not Detected");
                            binding.tvTimeout.setVisibility(View.GONE);
                            binding.tvTimein.setVisibility(View.GONE);
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void getAttendance() {
        Call<JsonObject> call;
        WebApis webApis = ApiClient.getClient(mContext).create(WebApis.class);
        JsonObject postParam = new JsonObject();
        String tenantId = sessionManager.getTenantId();
        call = webApis.getAttendance(studentId,tenantId);
        ApiServices apiServices = new ApiServices(mContext);

        apiServices.callWebServices(call,new ApiServices.ServiceCallBack() {
            @Override
            public void success(String  response) {
                try{
                    AttendanceModel modelVal = new Gson().fromJson(response, AttendanceModel.class);
                    if(modelVal.getResult()!=null) {

                        attendanceListList = new ArrayList<>(modelVal.getResult());
                        events = new ArrayList<>();

                        setCalenderEvents();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

    }

    private void setCalenderEvents() {
        int present = 0;
        int public_holiday = 0;
        int absent = 0;
        String attend = "";
        String showa = "1";
        for (int i = 0; i < attendanceListList.size(); i++) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            Date date = null;
            try {
                date = inputFormat.parse(attendanceListList.get(i).getAttendancedate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int day = Integer.parseInt(new SimpleDateFormat("dd", Locale.ENGLISH).format(date));
            int year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date));
            int Month = Integer.parseInt(new SimpleDateFormat("MM", Locale.ENGLISH).format(date));


            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, Month - 1);
            cal.set(Calendar.DAY_OF_MONTH, day);

            if (day < 9) {
                if (Month < 9) {
                    attend = "0" + day + "-0" + Month + "-" + year;
                } else {
                    attend = "0" + day + "-" + Month + "-" + year;
                }
            } else {
                if (Month < 9) {
                    attend = day + "-0" + Month + "-" + year;
                } else {
                    attend = day + "-" + Month + "-" + year;
                }
            }

            if (showa.equalsIgnoreCase("1")) {

                if (attendanceListList.get(i).getPresent().equalsIgnoreCase("1")) {
                    present++;
                    events.add(new EventDay(cal, DrawableUtils.getCircleDrawableWithText(mContext, "")));
                    binding.calendarView.setEvents(events);

                } else if (attendanceListList.get(i).getPresent().equalsIgnoreCase("0")) {
                    absent++;
                    events.add(new EventDay(cal, DrawableUtils.getThreeDots(mContext, "")));
                    binding.calendarView.setEvents(events);

                } else if (attendanceListList.get(i).getPresent().equalsIgnoreCase("-1")) {
                    public_holiday++;
                    events.add(new EventDay(cal, DrawableUtils.getGerThreeDots(mContext, "")));
                    binding.calendarView.setEvents(events);

                }

            }
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String nowdate = df.format(c);

            if (attend.equalsIgnoreCase(nowdate)) {

                showa = "0";
            }

        }

        binding.tvPresent.setText(present + "");
        binding.tvPublicHoliday.setText(public_holiday + "");
        binding.tvAbsent.setText(absent + "");
    }
}
