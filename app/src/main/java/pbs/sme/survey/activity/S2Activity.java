package pbs.sme.survey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pbs.sme.survey.R;
import pbs.sme.survey.model.Section12;
import pk.gov.pbs.utils.StaticUtils;

public class S2Activity extends FormActivity {

    private Button sbtn;
    private Section12 modelDatabase;
    private RadioGroup is_registered;
    Spinner sownership, mownership, organization;
    Spinner psic2, psic;

    private final String[] inputValidationOrder= new String[]{
            "started_year","is_registered","exports","imports","stocks","agency","is_maintaining","sownership","mownership","ownership_other","organization","activity","is_seasonal",
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

        psic2=findViewById(R.id.psic2);
        psic=findViewById(R.id.psic);
        sownership=findViewById(R.id.sownership);
        mownership=findViewById(R.id.mownership);
        organization=findViewById(R.id.organization);

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
        psic2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    showpsic(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void loadForm(){
        List<Section12> s2= dbHandler.query(Section12.class,"uid='"+resumeModel.uid+"' AND (is_deleted=0 OR is_deleted is null)");
        if(s2.size() == 1){
            modelDatabase = s2.get(0);
            //Part1TextWatcher.IGNORE_TEXT_WATCHER = true;
            setFormFromModel(this, modelDatabase, inputValidationOrder, "", false, this.findViewById(android.R.id.content));
            try{
                int ind=Integer.parseInt(modelDatabase.psic.trim().substring(0,2))-9;
                if(ind>0 && ind<=33){
                    psic2.setSelection(ind);
                    showpsic(ind);
                    for(int i=0; i<psic.getAdapter().getCount(); i++){
                        String pisc=psic.getAdapter().getItem(i).toString().trim();
                        if(pisc.startsWith(modelDatabase.psic)){
                            final int pos=i;
                            new Handler().postDelayed(()->psic.setSelection(pos),500);
                            break;
                        }
                    }
                }
            }
            catch (Exception e){
            }

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
            setErrorField(R.id.started_year,"Started Year should be 1900-2025");
            return;
        }

        if(sec.emp_count>50 && sec.emp_count<=250){
            if(sec.mownership==1 && !(sec.organization==4 || sec.organization==6)){
                setErrorField(R.id.organization,"Only Public Limited Co or Govt. can be selected");
                return;
            }
            else if((sec.mownership==3) && (sec.organization==1 || sec.organization==4 || sec.organization==6)){
                setErrorField(R.id.organization,"Individual, Public Limited Co or Govt. can not be selected");
                return;
            }
            else if((sec.mownership==2 || sec.mownership==4) && (sec.organization==4 || sec.organization==6)){
                setErrorField(R.id.organization,"Public Limited Co or Govt. can not be selected");
                return;
            }
        }

        if(sec.months>12){
            setErrorField(R.id.months,"Number of months work 0-12");
            return;
        }

        try{
            if(sec.emp_count!=(sec.male_count+sec.female_count)){
                setErrorField(R.id.emp_count,"Male+Female Employees <> Total");
                return;
            }
        }
        catch (Exception e){
            setErrorField(R.id.emp_count,"Male+Female Employees <> Total");
            return;
        }

        try{
            if(sec.emp_count>50 && sec.emp_unpaid!=(sec.male_unpaid+sec.female_unpaid)){
                setErrorField(R.id.emp_unpaid,"Male+Female Unpaid <> Total");
                return;
            }
            if(sec.emp_count>50 && sec.emp_unpaid>sec.emp_count){
                setErrorField(R.id.emp_unpaid,"Total Unpaid cannot be greater than Total Employees");
                return;
            }
        }
        catch (Exception e){
            setErrorField(R.id.emp_unpaid,"Male+Female Unpaid <> Total");
            return;
        }

        String pisc=psic.getSelectedItem().toString();
        int i=pisc.indexOf(" - ");
        if(i>=0){
            pisc=pisc.substring(0,i);
        }
        else{
            pisc=String.valueOf(psic2.getSelectedItemPosition()+9);
        }
        sec.psic=pisc.trim();

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

    private void setOwnership(int vid, int pos){
        Spinner spn=findViewById(vid);
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

    public void showpsic(int p){
            ArrayList<String> nlist=new ArrayList<>();
            ArrayList<String> list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spn_pisc)));
            nlist.add(list.get(0));
            for(String s:list){
                if(s.substring(0,2).equals(String.valueOf(p+9))){
                    nlist.add(s);
                }
            }
            if(nlist.size()>1){
                nlist.remove(1);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,nlist);
            psic.setAdapter(adapter);

    }

    public void decideOrganization(int id, int p){
        if(id==R.id.mownership){
            ArrayList<String> list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spn_organization)));
            if(p==1){
                list.remove(7);
                list.remove(5);
                list.remove(3);
                list.remove(2);
                list.remove(1);
            }
            else if(p==2 || p==4){
                list.remove(6);
                list.remove(4);
            }
            else if(p==3){
                list.remove(6);
                list.remove(4);
                list.remove(1);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,list);
            organization.setAdapter(adapter);
        }

    }

    public void setErrorField(int field, String msg){
        setScrollAndBorderAnimation(findViewById(field));
        mUXToolkit.showAlertDialogue("Failed",msg, alertForEmptyFieldEvent);
        sbtn.setEnabled(true);
        return;
    }
}