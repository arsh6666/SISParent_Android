package dt.sis.parent.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dt.sis.parent.R;
import dt.sis.parent.adapters.GuardianTypeListAdapter;
import dt.sis.parent.databinding.DialogDisplayCommentBinding;
import dt.sis.parent.databinding.DialogGuardianTypeBinding;
import dt.sis.parent.models.GuardianTypeModel;

public class GuardianTypeListDialog extends AlertDialog {

    private ItemClickListener itemClickListener;
    private Context mContext;
    DialogGuardianTypeBinding binding;
    List<GuardianTypeModel.Result> guardianTypeList;
    GuardianTypeListAdapter<GuardianTypeModel.Result> guardianTypeListAdapter;
    int guardianTypeId =-1;


    public interface ItemClickListener {
        public void onCancelItemClick(View view);
        public void onSelectItemClick(int id, String name);
    }

    public GuardianTypeListDialog(Context mContext,int guardianTypeId, List<GuardianTypeModel.Result> guardianTypeList, ItemClickListener itemClickListener) {
        super(mContext);
        this.mContext = mContext;
        this.guardianTypeId=guardianTypeId;
        this.guardianTypeList = guardianTypeList;
        this.itemClickListener = itemClickListener;
        initialize();
    }

    private void initialize() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_guardian_type,null);
        binding = DataBindingUtil.bind(mView);
        setAdapter();

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null){
                    itemClickListener.onCancelItemClick(view);
                }

            }
        });
        binding.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedPos = guardianTypeListAdapter.getSelectedPos();
                int id = guardianTypeList.get(selectedPos).getId();
                String name = guardianTypeList.get(selectedPos).getName();
                if(itemClickListener!=null){
                    itemClickListener.onSelectItemClick(id,name);
                }
            }
        });

        setView(mView);
    }

    private void setAdapter() {
        guardianTypeListAdapter = new GuardianTypeListAdapter<GuardianTypeModel.Result>(mContext, guardianTypeList, new GuardianTypeListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                guardianTypeListAdapter.setSelectedPos(position);
                guardianTypeListAdapter.notifyDataSetChanged();
            }
        }) {
            @Override
            public String getOrganizationName(int position, GuardianTypeModel.Result result) {
                String value = result.getName();
                value = value!=null ? value :"";
                return value;
            }
        };
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvBranches.setLayoutManager(mLayoutManager);
        binding.rvBranches.setAdapter(guardianTypeListAdapter);
        for(int i=0; i<guardianTypeList.size() ; i++){
            if(guardianTypeList.get(i).getId()==guardianTypeId){
                guardianTypeListAdapter.setSelectedPos(i);
                break;
            }
        }
        guardianTypeListAdapter.notifyDataSetChanged();
    }
}