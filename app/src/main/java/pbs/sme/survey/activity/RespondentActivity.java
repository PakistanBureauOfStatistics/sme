package pbs.sme.survey.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import pbs.sme.survey.R;
import pbs.sme.survey.model.Section12;
import pk.gov.pbs.utils.StaticUtils;

public class RespondentActivity extends FormActivity {

    private Button sbtn;
    private Section12 modelDatabase;
    CheckBox cb_same;
    Spinner designation;
    EditText name;


    private final String[] inputValidationOrder= new String[]{
            "designation","name","interview_status","respondent_behaviour"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respondent);
        setDrawer(this,"Respondent Section");
        setParent(this, HomeActivity.class);
        scrollView = findViewById(R.id.scrollView);


        sbtn = findViewById(R.id.btns);
        sbtn.setOnClickListener(v -> {
            sbtn.requestFocus();
            StaticUtils.getHandler().post(this::saveForm);
        });

        designation=findViewById(R.id.designation);
        name=findViewById(R.id.name);

        designation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    if(resumeModel.owner!=null){
                        name.setText(resumeModel.owner);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadForm(){
        List<Section12> s1= dbHandler.query(Section12.class,"uid='"+resumeModel.uid+"' AND (is_deleted=0 OR is_deleted is null)");
        if(s1.size() == 1){
            resumeModel=modelDatabase = s1.get(0);
            //Part1TextWatcher.IGNORE_TEXT_WATCHER = true;
            setFormFromModel(this, modelDatabase, inputValidationOrder, "",false, this.findViewById(android.R.id.content));
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
            sec = (Section12) extractValidatedModelFromForm(this, sec,true, inputValidationOrder,"", Section12.class, false, this.findViewById(android.R.id.content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (sec == null) {
            mUXToolkit.showAlertDialogue("Failed","فارم کو محفوظ نہیں کر سکتے، براہ کرم آگے بڑھنے سے پہلے تمام ڈیٹا درج کریں۔خالی اندراج یا غلط جوابات دیکھنے کے لیے \"OK\" پر کلک کریں۔"  , alertForEmptyFieldEvent);
            sbtn.setEnabled(true);
            return;
        }




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



}