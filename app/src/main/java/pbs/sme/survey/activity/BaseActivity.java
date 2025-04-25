package pbs.sme.survey.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.List;

import pbs.sme.survey.R;
import pbs.sme.survey.model.Baseline;
import pbs.sme.survey.model.Section12;
import pbs.sme.survey.model.Section34;
import pk.gov.pbs.utils.StaticUtils;

public class BaseActivity extends FormActivity {

    private Button sbtn;
    private Baseline modelDatabase;

    private final String[] inputValidationOrder= new String[]{
    "a1","a2",
            "a3a","a3b","a3c","a3d","a3e",
            "b1","b2",
            "b3a","b3b","b3c","b3d","b3e","b3f",
            "b4a","b4b","b4c","b4d","b4e","b4f",
            "b5a","b5b","b5c","b5d","b5e",
            "b6a","b6b","b6c","b6d","b6e","b6f",
            "b6_other",
            "b7","b8","b9",
            "b10a","b10b","b10c","b10d","b10e","b10f",
            "b10_other",
            "c1a","c1b","c1c","c1d","c1e","c1e",
            //"c1_other",
            "c2a","c2b","c2c","c2d","c2e",
            "c3",
            "wapda","solar","generator",
            "d1","d2","d2_other",
            "d3a","d3b","d3c","d3d","d3e",
            "d4","d5",
            "d6",
            "d7a","d7b","d7c","d7d","d7e",
            //"d7_other",
            "e1a","e1b","e1c","e1d",
            "e1_other","e2",
            "e3a","e3b","e3c","e3d","e3e",
            "e4a","e4b","e4c","e4d",
            "e5a","e5b","e5b","e5d",

            "f1","f2","f3","f4",
            "f5a","f5b","f5c","f5d","f5e",
            "f6a", "f6b", "f6c", "f6d", "f6e",
            //"f6_other",
            "f7","f8",
            "f9_scheme",
            "f10a","f10f","f10c","f10d","f10e","f10f",
            "f10_other",
            "f11a","f11b","f11c","f11d","f11e","f11f",
            "f11_other",
            "export1",
            "export2a","export2b","export2c","export2d","export2e","export2f","export2g","export2h",
            "export3a","export3b","export3c","export3d","export3e","export3f",
            "export4a","export4b","export4c","export4d","export4e","export4f",

            "export2_other",
            "export5a","export5b","export5c","export5d","export5e",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setDrawer(this,"Baseline");
        setParent(this, RespondentActivity.class);
        scrollView = findViewById(R.id.scrollView);

        sbtn = findViewById(R.id.btns);
        sbtn.setOnClickListener(v -> {
            sbtn.requestFocus();
            StaticUtils.getHandler().post(this::saveForm);
        });
        init();

    }

    public void init(){

        try{
            //Arsalan SME specific code, remove this line if required
            List<Section12> list= dbHandler.query(Section12.class," uid='"+resumeModel.uid+"' AND (is_deleted=0 OR is_deleted is null)");
            if(list!=null && list.size()>0){
                if(list.get(0).exports!=null && list.get(0).exports==2){
                    findViewById(R.id.export).setVisibility(GONE);
                }
            }
        }
        catch (Exception e){

        }

        RadioGroup c3=findViewById(R.id.c3);
        c3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                EditText wapda=findViewById(R.id.wapda);
                EditText solar=findViewById(R.id.solar);
                EditText generator=findViewById(R.id.generator);
                if(checkedId==R.id.c33){
                    wapda.setEnabled(true);
                    solar.setEnabled(true);
                    generator.setEnabled(false);
                }
                else if(checkedId==R.id.c34){
                    wapda.setEnabled(false);
                    solar.setEnabled(false);
                    generator.setEnabled(true);
                }
                else if(checkedId==R.id.c35){
                    wapda.setEnabled(true);
                    solar.setEnabled(true);
                    generator.setEnabled(true);
                }
                else{
                    wapda.setEnabled(false);
                    solar.setEnabled(false);
                    generator.setEnabled(false);
                }
            }
        });
        CheckBox b6=findViewById(R.id.b6f);
        b6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.b6_other).getParent();
                if(isChecked){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });

        CheckBox b10=findViewById(R.id.b10d);
        b10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.b10_other).getParent();
                if(isChecked){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });


        /*RadioGroup c1=findViewById(R.id.c1);
        c1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.c1_other).getParent();
                if(checkedId==R.id.c16){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });*/

        RadioGroup d2=findViewById(R.id.d2);
        d2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.d2_other).getParent();
                if(checkedId==R.id.d24){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });

        /*RadioGroup d7=findViewById(R.id.d7);
        d7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.d7_other).getParent();
                if(checkedId==R.id.d76){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });*/

        CheckBox e1=findViewById(R.id.e1d);
        e1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.e1_other).getParent();
                if(isChecked){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });


        /*RadioGroup f6=findViewById(R.id.f6);
        f6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.f6_other).getParent();
                if(checkedId==R.id.f66){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });*/

        RadioGroup f8=findViewById(R.id.f8);
        f8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ViewGroup f8=(ViewGroup) findViewById(R.id.f9_scheme).getParent();
                ViewGroup f9=(ViewGroup) findViewById(R.id.f10a).getParent();
                if(checkedId==R.id.f81){
                    f8.setVisibility(VISIBLE);
                    f9.setVisibility(GONE);
                }
                else{
                    f8.setVisibility(GONE);
                    f9.setVisibility(VISIBLE);
                }
            }
        });

        CheckBox f10=findViewById(R.id.f10f);
        f10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.f10_other).getParent();
                if(isChecked){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });
        CheckBox f11=findViewById(R.id.f11f);
        f11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.f11_other).getParent();
                if(isChecked){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });


        CheckBox h2=findViewById(R.id.export2h);
        h2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.export_other).getParent();
                if(b){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });


        RadioGroup b1=findViewById(R.id.b1);
        b1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.b2).getParent();
                if(checkedId==R.id.b12){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });

        RadioGroup b2=findViewById(R.id.b2);
        b2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.b3a).getParent();
                if(checkedId==R.id.b21){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });



        RadioGroup b9=findViewById(R.id.b9);
        b9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.b10a).getParent();
                if(checkedId==R.id.b92){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });

        RadioGroup d1=findViewById(R.id.d1);
        d1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ViewGroup parent=(ViewGroup) findViewById(R.id.d2).getParent();
                if(checkedId==R.id.d12){
                    parent.setVisibility(VISIBLE);
                }
                else{
                    parent.setVisibility(GONE);
                }
            }
        });

        RadioGroup ex1=findViewById(R.id.export1);
        ex1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ViewGroup e2=(ViewGroup) findViewById(R.id.export2a).getParent();
                ViewGroup e3=(ViewGroup) findViewById(R.id.export3a).getParent();
                if(checkedId==R.id.export11){
                    e2.setVisibility(VISIBLE);
                    e3.setVisibility(GONE);
                }
                else{
                    e2.setVisibility(GONE);
                    e3.setVisibility(VISIBLE);
                }
            }
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
}