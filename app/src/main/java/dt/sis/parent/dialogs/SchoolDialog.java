package dt.sis.parent.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.adapters.SchoolTanentListAdapter;
import dt.sis.parent.databinding.DialogSchoolTanentBinding;
import dt.sis.parent.models.SchoolTenantModel;

public class SchoolDialog extends AlertDialog {

    private ItemClickListener itemClickListener;
    private Context mContext;
    DialogSchoolTanentBinding binding;
    List<SchoolTenantModel.Result> optionList;

    SchoolTanentListAdapter<SchoolTenantModel.Result> habitsOptionListAdapter;
    int checkedIndex = -1;

    public interface ItemClickListener {
        public void onCancelClick();
        public void onSaveClick(List<SchoolTenantModel.Result> optionList);
    }

    public void setCheckedIndex(int checkedIndex) {
        this.checkedIndex = checkedIndex;
    }

    public SchoolDialog(Context mContext, List<SchoolTenantModel.Result> optionList, ItemClickListener itemClickListener) {
        super(mContext);
        this.mContext = mContext;
        this.optionList = optionList;
        this.itemClickListener = itemClickListener;
        initialize();
    }

    private void setListAdapter( final List<SchoolTenantModel.Result> optionList){
        habitsOptionListAdapter = new SchoolTanentListAdapter<SchoolTenantModel.Result>(mContext, optionList, optionItemClickListener) {
            @Override
            public String getKeyName(int position, SchoolTenantModel.Result result) {
                String value =  result.getName();
                value = value!=null ? value :"";
                return value;

            }

        };
        habitsOptionListAdapter.setSelectedIndex(checkedIndex);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        binding.rvTenants.setLayoutManager(mLayoutManager);

        binding.rvTenants.setAdapter(habitsOptionListAdapter);

        binding.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null){
                    itemClickListener.onSaveClick(optionList);
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
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_school_tanent,null);
        binding = DataBindingUtil.bind(mView);

        setListAdapter(optionList);
        setView(mView);
    }

private SchoolTanentListAdapter.ItemClickListener optionItemClickListener = new SchoolTanentListAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            for (SchoolTenantModel.Result obj: optionList) {
                obj.setChecked(false);
            }
            optionList.get(position).setChecked(true);
            habitsOptionListAdapter.setSelectedIndex(position);
            habitsOptionListAdapter.notifyDataSetChanged();

        }
    };
}