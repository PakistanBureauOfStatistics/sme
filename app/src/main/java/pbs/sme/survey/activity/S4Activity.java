package pbs.sme.survey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pbs.sme.survey.R;
import pbs.sme.survey.helper.AdditionTextWatcher;
import pbs.sme.survey.helper.NumberHelper;
import pbs.sme.survey.model.Section34;
import pk.gov.pbs.utils.StaticUtils;

public class S4Activity extends FormActivity {

    private Button sbtn;
    private List<Section34> modelDatabase;
    EditText total;
    Integer input=0, output=0, balance=0;


    private final String[] inputValidationOrder= new String[]{
            "value"
    };



    private final String[] codeList= new String[]{
            "401","402","403","404","405","406","400","408"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s4);
        setDrawer(this,"Section 4: Ouput");
        setParent(this, BaseActivity.class);


        EditText totalEditText = findViewById(R.id.value__400);

        AdditionTextWatcher additionTextWatcher = new AdditionTextWatcher(totalEditText);

        for(int i = 0; i < codeList.length-2; i++) {
            EditText et = findViewById(getResources().getIdentifier("value__"+codeList[i], "id", getPackageName()));
            et.removeTextChangedListener(additionTextWatcher);
            et.addTextChangedListener(additionTextWatcher);
        }

        sbtn = findViewById(R.id.btns);
        sbtn.setOnClickListener(v -> {
            sbtn.requestFocus();
            StaticUtils.getHandler().post(this::saveForm);
        });

        total = findViewById(R.id.value__400);
        total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Called when the text is changed
            }

            @Override
            public void afterTextChanged(Editable s) {
                setOutput(s.toString());
            }
        });



    }

    private void displayNumbertoWord(){
        String n="0";
        List<Section34> list= dbHandler.query(Section34.class,"section="+3+" AND uid='"+resumeModel.uid+"' AND CODE='300' AND (is_deleted=0 OR is_deleted is null)");
        if(list!=null && list.size()>0){
            if(list.get(0).value!=null){
                input=list.get(0).value;
                n=String.valueOf(input);
            }
            else{
                input=0;
            }

        }
        //TextView[] inputs= new TextView[10];
        while (n.length() < 10) {
            n = "0" + n;
        }
        for (int i = 0; i < 10; i++) {
            TextView et = findViewById(getResources().getIdentifier("input__"+((10-i)), "id", getPackageName()));
            et.setText(String.valueOf(n.charAt(i)));
        }

        setOutput(total.getText().toString());


    }

    public void setOutput(String n){
        while (n.length() < 10) {
            n = "0" + n;
        }
        for (int i = 0; i < 10; i++) {
            TextView et = findViewById(getResources().getIdentifier("output__"+((10-i)), "id", getPackageName()));
            et.setText(String.valueOf(n.charAt(i)));
        }

        try{
            if(n!=null && !n.isEmpty()){
                output=Integer.parseInt(n);
                balance=output-input;
                n=String.valueOf(balance);
                n=n.replace("-","");
                while (n.length() < 10) {
                    n = "0" + n;
                }
                for (int i = 0; i < 10; i++) {
                    TextView et = findViewById(getResources().getIdentifier("balance__"+((10-i)), "id", getPackageName()));
                    et.setText(String.valueOf(n.charAt(i)));
                }
                TextView et = findViewById(R.id.balance);
                if(balance<0){
                    et.setText("- Balance");
                    et.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    et.setText("+ Balance");
                    et.setTextColor(getResources().getColor(R.color.settGreen));
                }
            }
        }
        catch (Exception e){

        }
    }
    private void saveForm() {
        sbtn.setEnabled(false);
        List<Section34> list=new ArrayList<>();

        for(int i = 0; i < codeList.length; i++) {

            Section34 m = null;
            if(modelDatabase != null && modelDatabase.size() == codeList.length){
                m = modelDatabase.get(i);
            }

            try {
                m = (Section34) extractValidatedModelFromForm(this, m, true, inputValidationOrder, codeList[i], Section34.class, false, this.findViewById(android.R.id.content));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            if (m == null) {
                mUXToolkit.showAlertDialogue("Failed", "فارم کو محفوظ نہیں کر سکتے، براہ کرم آگے بڑھنے سے پہلے تمام ڈیٹا درج کریں۔خالی اندراج یا غلط جوابات دیکھنے کے لیے \"OK\" پر کلک کریں۔", alertForEmptyFieldEvent);
                sbtn.setEnabled(true);
                return;
            }


            list.add(m);
            setCommonFields(m);
            m.section=4;
            m.code=codeList[i];

        }


        /////TODO CHECKS////////////////////////////


        List<Long> iid = dbHandler.replace(list);
        for(Long i:iid){
            if (i != null && i < 0) {
                mUXToolkit.showToast("Failed");
                sbtn.setEnabled(true);
                return;
            }
        }
        mUXToolkit.showToast("Success");
        sbtn.setEnabled(true);
        btnn.callOnClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadForm();
    }


    private void loadForm(){
        List<Section34> list= dbHandler.query(Section34.class,"section="+4+" AND uid='"+resumeModel.uid+"' AND (is_deleted=0 OR is_deleted is null)");
        for(Section34 s: list){
            setFormFromModel(this, s, inputValidationOrder, s.code, false, this.findViewById(android.R.id.content));
        }

        displayNumbertoWord();

    }
}