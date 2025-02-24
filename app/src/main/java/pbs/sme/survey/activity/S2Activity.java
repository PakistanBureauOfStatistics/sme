package pbs.sme.survey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.util.List;

import pbs.sme.survey.R;
import pbs.sme.survey.model.Section12;
import pk.gov.pbs.utils.StaticUtils;

public class S2Activity extends FormActivity {

    private Button sbtn;
    private Section12 section2_database;
    private RadioGroup is_registered, ownership;

    private final String[] inputValidationOrder= new String[]{
            "started_year","is_registered","agency","is_maintaining","ownership","ownership_other","activity","psic","is_seasonal",
            "jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec","months","is_establishement","emp_count","emp_cost"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s2);
        setDrawer(this,"Section 2");
        setParent(this, S3Activity.class);
        scrollView = findViewById(R.id.scrollView);

        sbtn = findViewById(R.id.btns);
        sbtn.setOnClickListener(v -> {
            sbtn.requestFocus();
            StaticUtils.getHandler().post(this::saveForm);
        });

        is_registered=findViewById(R.id.is_registered);
        is_registered.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.is_registered1){
                    findViewById(R.id.l_agency).setVisibility(View.VISIBLE);
                }
                else{
                    findViewById(R.id.l_agency).setVisibility(View.GONE);
                }
            }
        });
        ownership=findViewById(R.id.ownership);
        ownership.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.ownership3){
                    findViewById(R.id.l_ownership).setVisibility(View.VISIBLE);
                }
                else{
                    findViewById(R.id.l_ownership).setVisibility(View.GONE);
                }
            }
        });
    }


    private void loadForm(){
        List<Section12> s2= dbHandler.query(Section12.class,"uid='"+resumeModel.uid+"' AND (is_deleted=0 OR is_deleted is null)");
        if(s2.size() == 1){
            section2_database = s2.get(0);
            //Part1TextWatcher.IGNORE_TEXT_WATCHER = true;
            setFormFromModel(this, section2_database, inputValidationOrder, "", false, this.findViewById(android.R.id.content));
        }

    }



    private void saveForm() {
        sbtn.setEnabled(false);
        Section12 s2;


        try {
            s2 = (Section12) extractValidatedModelFromForm(this, resumeModel,true, inputValidationOrder,"", Section12.class,false, this.findViewById(android.R.id.content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (s2 == null) {
            mUXToolkit.showAlertDialogue("Failed","فارم کو محفوظ نہیں کر سکتے، براہ کرم آگے بڑھنے سے پہلے تمام ڈیٹا درج کریں۔خالی اندراج یا غلط جوابات دیکھنے کے لیے \"OK\" پر کلک کریں۔"  , alertForEmptyFieldEvent);
            sbtn.setEnabled(true);
            return;
        }
        s2.months=months();

        /////TODO CHECKS////////////////////////////


        Long iid = dbHandler.replace(s2);

        if (iid != null && iid > 0) {
            mUXToolkit.showToast("Success");
            btnn.callOnClick();
        }else{
            mUXToolkit.showToast("Failed");
        }
        sbtn.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadForm();
    }

    public int months(){
        int t=0;
        String[] m={"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
        for(String s: m){
            CheckBox c = findViewById(getResources().getIdentifier(s, "id", getPackageName()));
            if(c.isChecked()){
                t++;
            }
        }
        return t;
    }
}