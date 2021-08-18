package dt.sis.parent.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.adapters.SchoolTanentListAdapter;
import dt.sis.parent.databinding.DialogSchoolOrganizationBinding;
import dt.sis.parent.models.OrganizationModel;

public class OrganizationDialog extends AlertDialog {

    private ItemClickListener itemClickListener;
    private Context mContext;
    DialogSchoolOrganizationBinding binding;
    List<OrganizationModel.Result> organisationList;

    SchoolTanentListAdapter<OrganizationModel.Result> habitsOptionListAdapter;
    int checkedIndex = -1;

    public interface ItemClickListener {
        public void onCancelClick();
        public void onSaveClick(List<OrganizationModel.Result> optionList);
    }

    public void setCheckedIndex(int checkedIndex) {
        this.checkedIndex = checkedIndex;
    }

    public OrganizationDialog(Context mContext, List<OrganizationModel.Result> organisationList, ItemClickListener itemClickListener) {
        super(mContext);
        this.mContext = mContext;
        this.organisationList = organisationList;
        this.itemClickListener = itemClickListener;
        initialize();
    }

    private void setListAdapter( final List<OrganizationModel.Result> organisationList){
        Log.e("OrganisationLists",new Gson().toJson(organisationList)+" ");

        habitsOptionListAdapter = new SchoolTanentListAdapter<OrganizationModel.Result>(mContext, organisationList, optionItemClickListener) {
            @Override
            public String getKeyName(int position, OrganizationModel.Result result) {
                String value =  result.getOrganizationunitname();
                value = value!=null ? value :"";
                return value;

            }

        };
        habitsOptionListAdapter.setSelectedIndex(checkedIndex);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        binding.rvOrganization.setLayoutManager(mLayoutManager);

        binding.rvOrganization.setAdapter(habitsOptionListAdapter);

        binding.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null){
                    itemClickListener.onSaveClick(organisationList);
                }
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null){
                    itemClickListener.onCancelClick();
                }

            }
        });
    }
    private void initialize() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_school_organization,null);
        binding = DataBindingUtil.bind(mView);

        setListAdapter(organisationList);
        setView(mView);
    }

private SchoolTanentListAdapter.ItemClickListener optionItemClickListener = new SchoolTanentListAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            for (OrganizationModel.Result obj: organisationList) {
                obj.setChecked(false);
            }
            organisationList.get(position).setChecked(true);
            habitsOptionListAdapter.setSelectedIndex(position);
            habitsOptionListAdapter.notifyDataSetChanged();

        }
    };
}