package pbs.sme.survey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.List;

import pbs.sme.survey.R;
import pbs.sme.survey.model.Section12;
import pk.gov.pbs.utils.StaticUtils;

public class S2Activity extends FormActivity {

    private Button sbtn;
    private Section12 modelDatabase;
    private RadioGroup is_registered;
    Spinner sownership, mownership;

    private final String[] inputValidationOrder= new String[]{
            "started_year","is_registered","exports","imports","stocks","agency","is_maintaining","sownership","mownership","ownership_other","organization","activity","psic","is_seasonal",
            "jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec","months","is_establishement","emp_count","male_count","female_count","emp_unpaid","male_unpaid","female_unpaid","emp_cost"
    };
    private final int[] small=new int[]{R.id.small1,R.id.small2,R.id.small3,R.id.small4, R.id.small5};
    private final int[] medium=new int[]{R.id.medium1,R.id.medium2,R.id.medium3,R.id.medium4, R.id.medium5};


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

        if(resumeModel.emp_count!=null && resumeModel.emp_count>=50){
            for(int l : medium){
                findViewById(l).setVisibility(View.VISIBLE);
            }
            for(int l : small){
                findViewById(l).setVisibility(View.GONE);
            }
            setOwnership(R.id.mownership,4);
        }
        else{
            for(int l : small){
                findViewById(l).setVisibility(View.VISIBLE);
            }
            for(int l : medium){
                findViewById(l).setVisibility(View.GONE);
            }
            setOwnership(R.id.mownership,3);

        }

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

    }


    private void loadForm(){
        List<Section12> s2= dbHandler.query(Section12.class,"uid='"+resumeModel.uid+"' AND (is_deleted=0 OR is_deleted is null)");
        if(s2.size() == 1){
            modelDatabase = s2.get(0);
            //Part1TextWatcher.IGNORE_TEXT_WATCHER = true;
            setFormFromModel(this, modelDatabase, inputValidationOrder, "", false, this.findViewById(android.R.id.content));
        }

    }



    private void saveForm() {
        sbtn.setEnabled(false);
        Section12 sec;

        if(modelDatabase!=null){
            sec=modelDatabase;
        }
        else{
            sec=resumeModel;
        }
        try {
            sec = (Section12) extractValidatedModelFromForm(this, sec,true, inputValidationOrder,"", Section12.class,false, this.findViewById(android.R.id.content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (sec == null) {
            mUXToolkit.showAlertDialogue("Failed","فارم کو محفوظ نہیں کر سکتے، براہ کرم آگے بڑھنے سے پہلے تمام ڈیٹا درج کریں۔خالی اندراج یا غلط جوابات دیکھنے کے لیے \"OK\" پر کلک کریں۔"  , alertForEmptyFieldEvent);
            sbtn.setEnabled(true);
            return;
        }

        if(sec.emp_count<=50){
            sec.months=months();
        }

        if(sec.started_year<1900 || sec.started_year>2025){
            setScrollAndBorderAnimation(findViewById(R.id.started_year));
            mUXToolkit.showAlertDialogue("Failed","Started Year should be 1900-2025"  , alertForEmptyFieldEvent);
            sbtn.setEnabled(true);
            return;
        }

        if(sec.months>12){
            setScrollAndBorderAnimation(findViewById(R.id.months));
            mUXToolkit.showAlertDialogue("Failed","Number of months work 0-12"  , alertForEmptyFieldEvent);
            sbtn.setEnabled(true);
            return;
        }

        try{
            if(sec.emp_count!=(sec.male_count+sec.female_count)){
                setScrollAndBorderAnimation(findViewById(R.id.emp_count));
                mUXToolkit.showAlertDialogue("Failed","Male+Female Employees <> Total"  , alertForEmptyFieldEvent);
                sbtn.setEnabled(true);
                return;
            }
        }
        catch (Exception e){
            setScrollAndBorderAnimation(findViewById(R.id.emp_count));
            mUXToolkit.showAlertDialogue("Failed","Male+Female Employees <> Total"  , alertForEmptyFieldEvent);
            sbtn.setEnabled(true);
            return;
        }

        try{
            if(sec.emp_count>50 && sec.emp_unpaid!=(sec.male_unpaid+sec.female_unpaid)){
                setScrollAndBorderAnimation(findViewById(R.id.emp_unpaid));
                mUXToolkit.showAlertDialogue("Failed","Male+Female Unpaid <> Total"  , alertForEmptyFieldEvent);
                sbtn.setEnabled(true);
                return;
            }
        }
        catch (Exception e){
            setScrollAndBorderAnimation(findViewById(R.id.emp_unpaid));
            mUXToolkit.showAlertDialogue("Failed","Male+Female Unpaid <> Total"  , alertForEmptyFieldEvent);
            sbtn.setEnabled(true);
            return;
        }

        /////TODO CHECKS////////////////////////////

        setCommonFields(sec);
        Long iid = dbHandler.replace(sec);

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

    private void setOwnership(int id, int pos){
        Spinner spn=findViewById(id);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==pos){
                    findViewById(R.id.l_ownership).setVisibility(View.VISIBLE);
                }
                else{
                    findViewById(R.id.l_ownership).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}