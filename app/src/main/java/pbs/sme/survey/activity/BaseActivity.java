package pbs.sme.survey.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import pbs.sme.survey.R;
import pbs.sme.survey.model.Baseline;
import pbs.sme.survey.model.Section12;
import pk.gov.pbs.utils.StaticUtils;

public class BaseActivity extends FormActivity {

    private Button sbtn;
    private Baseline modelDatabase;

    private final String[] inputValidationOrder= new String[]{
    "a1","a2","a3","b1","b2","b3","b4","b5","b6","b6_other",
            "b7","b8","b9","b10","b10_other",
            "c1","c1_other","c2","c3","d1","d2","d2_other","d3","d4","d5",
            "d6","d7","d7_other","e1","e1_other","e2","e3",
            "e4","e5","f1","f2","f3","f4","f5","f6","f6_other",
            "f7","f8","f9","f9_other","f10",
            "f10_other","f11","export1","export2","export2_other","export3",
            "export4","export5","time_spent","survey"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setDrawer(this,"Baseline");
        setParent(this,HomeActivity.class);
        scrollView = findViewById(R.id.scrollView);

        sbtn = findViewById(R.id.btns);
        sbtn.setOnClickListener(v -> {
            sbtn.requestFocus();
            StaticUtils.getHandler().post(this::saveForm);
        });

    }

    private void loadForm(){
        List<Baseline> s2= dbHandler.query(Baseline.class,"uid='"+resumeModel.uid+"' AND (is_deleted=0 OR is_deleted is null)");
        if(s2.size() == 1){
            modelDatabase = s2.get(0);
            //Part1TextWatcher.IGNORE_TEXT_WATCHER = true;
            setFormFromModel(this, modelDatabase, inputValidationOrder, "", true, this.findViewById(android.R.id.content));
        }

    }



    private void saveForm() {
        sbtn.setEnabled(false);
        Baseline sec=null;

        if(modelDatabase!=null){
            sec=modelDatabase;
        }

        try {
            sec = (Baseline) extractValidatedModelFromForm(this, sec,true, inputValidationOrder,"", Baseline.class,true, this.findViewById(android.R.id.content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (sec == null) {
            mUXToolkit.showAlertDialogue("Failed","فارم کو محفوظ نہیں کر سکتے، براہ کرم آگے بڑھنے سے پہلے تمام ڈیٹا درج کریں۔خالی اندراج یا غلط جوابات دیکھنے کے لیے \"OK\" پر کلک کریں۔"  , alertForEmptyFieldEvent);
            sbtn.setEnabled(true);
            return;
        }

        /////TODO CHECKS////////////////////////////


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