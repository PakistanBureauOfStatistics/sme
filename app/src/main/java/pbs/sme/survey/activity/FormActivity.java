package pbs.sme.survey.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import pbs.sme.survey.R;
import pbs.sme.survey.meta.ValueStore;
import pbs.sme.survey.model.Baseline;
import pbs.sme.survey.model.Block;
import pbs.sme.survey.model.Constants;
import pbs.sme.survey.model.FormTable;
import pbs.sme.survey.model.Section12;
import pbs.sme.survey.model.Section34;
import pk.gov.pbs.utils.ExceptionReporter;
import pk.gov.pbs.utils.StaticUtils;
import pk.gov.pbs.utils.UXEventListeners;

public class FormActivity extends MyActivity{
    Block mBlock;
    Section12 resumeModel;
    TextView tv_title, tv_sno, tv_type, tv_emp, tv_block;
    Button btnn, btnp;
    Intent intent;


    protected ScrollView scrollView;

    protected UXEventListeners.AlertDialogueEventListener alertForEmptyFieldEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setParent(Context context, Class<?> next){
        mBlock = (Block) getIntent().getSerializableExtra(Constants.EXTRA.IDX_BLOCK);
        resumeModel = (Section12) getIntent().getSerializableExtra(Constants.EXTRA.IDX_HOUSE);

        intent=new Intent(context,next);
        intent.putExtra(Constants.EXTRA.IDX_BLOCK, mBlock);
        intent.putExtra(Constants.EXTRA.IDX_HOUSE, resumeModel);

        btnn=findViewById(R.id.btnn);
        btnp=findViewById(R.id.btnp);

        tv_block=findViewById(R.id.tv_block);
        tv_sno=findViewById(R.id.tv_sno);
        tv_type=findViewById(R.id.tv_type);
        tv_emp=findViewById(R.id.tv_emp);
        tv_title=findViewById(R.id.tv_title);

        tv_block.setText(resumeModel.blk_desc);
        tv_sno.setText(String.valueOf(resumeModel.sno));
        if(resumeModel.emp_count>250){
            tv_type.setText("Large");
        }
        else if(resumeModel.emp_count>50){
            tv_type.setText("Medium");
        }
        else{
            tv_type.setText("Small");
        }
        tv_emp.setText(String.valueOf(resumeModel.emp_count));
        tv_title.setText(resumeModel.title);

        btnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(next==HomeActivity.class){
                    finishAffinity();
                }
                startActivity(intent);

            }
        });
        if(!(context instanceof HomeActivity)){
            btnp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }

    }


    protected FormTable extractValidatedModelFromForm(Context context, FormTable previousModel, boolean focusErroneous, String[] inputValidationString, String cat, Class<?> className, boolean isCombinedCheckBox, View layoutView) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, InvocationTargetException {

        Constructor<?> cons = className.getConstructor();
        FormTable model;
        View previousView = null,cbParent = null;
        String previousStr = "";

        if(previousModel == null) {
            model = (FormTable) cons.newInstance();
        }
        else{
            model = previousModel;
        }

        HashMap<String, Boolean> cbChecks = new HashMap<>();

        for (String fn : inputValidationString) {
            try {
                String viewID = fn+(cat.length()>0?"__"+cat:"");
                View vw = layoutView.findViewById(context.getResources().getIdentifier(viewID, "id", context.getPackageName()));

                if(isCombinedCheckBox && previousView instanceof CheckBox && (!(vw instanceof CheckBox) || !viewID.substring(0,viewID.length()-1).equals(previousStr.substring(0,previousStr.length()-1)))){
                    for (HashMap.Entry<String, Boolean> entry : cbChecks.entrySet()) {
                        if(!(entry.getValue())){
                            setScrollAndBorderAnimation(cbParent);
                            return null;
                        }
                    }
                }

                if(vw instanceof EditText){
                    EditText et = (EditText) vw;
                    if (!et.getText().toString().isEmpty() && et.isShown()){
                        model.set(fn, new ValueStore(et.getText().toString()));
                    }
                    else{
                        model.set(fn,null);
                    }

                    if (focusErroneous && (et.isShown() && (et.isEnabled() && et.getText().toString().isEmpty()))) {
                        StaticUtils.getHandler().post(et::requestFocus);
                        alertForEmptyFieldEvent = null;
                        et.setError("برائے مہربانی اندراج کریں.");
                        return null;
                    }
                }
                else if(vw instanceof Spinner){
                    Spinner sp = (Spinner) vw;
                    if (sp.getSelectedItemId()>0  && sp.isShown()){
                        model.set(fn, new ValueStore(sp.getSelectedItemId()));
                    }
                    else{
                        model.set(fn,null);
                    }

                    if (focusErroneous && (sp.isShown() && (sp.isEnabled() && sp.getSelectedItemId()==0))) {
                        setScrollAndBorderAnimation(vw);
                        return null;
                    }
                }
                else if(vw instanceof RadioGroup){
                    RadioGroup rg = (RadioGroup) vw;
                    if (rg.getCheckedRadioButtonId() != -1  && rg.isShown()){
                        String val = getResources().getResourceEntryName(rg.getCheckedRadioButtonId());
                        try{
                            model.set(fn, new ValueStore(Integer.parseInt(val.substring(val.length()-1))));
                        }catch (Exception e){
                            model.set(fn, new ValueStore(val));
                        }
                    }
                    else{
                        model.set(fn,null);
                    }
                    if (focusErroneous && (rg.isShown() && (rg.isEnabled() && rg.getCheckedRadioButtonId()==-1))) {
                        setScrollAndBorderAnimation(vw);
                        return null;
                    }
                }
                else if(vw instanceof CheckBox){
                    CheckBox cb = (CheckBox) vw;
                    if(isCombinedCheckBox) {
                        String subStrVal = fn.substring(0, fn.length() - 1);
                        if (!cbChecks.containsKey(subStrVal) && cb.isShown()) {
                            cbChecks.put(subStrVal, false);
                            cbParent = (View) vw.getParent().getParent();
                        }
                        if (cb.isChecked() && cb.isShown()) {
                            String val = getResources().getResourceEntryName(cb.getId());
                            String pval = model.get(subStrVal) == null ? "" : model.get(subStrVal).toString();
                            if (!cbChecks.get(subStrVal)) {
                                pval = "";
                            }

                            String cval = val.substring(val.length() - 1);
                            if (pval.contains(cval)) {
                                cbChecks.put(subStrVal, true);
                            } else {
                                try {
                                    model.set(subStrVal, new ValueStore(Integer.parseInt(pval + cval)));
                                } catch (Exception e) {
                                    model.set(subStrVal, new ValueStore(pval + cval));
                                }
                                cbChecks.put(subStrVal, true);
                            }
                        } else if (!cb.isShown()) {
                            model.set(subStrVal, null);
                        }
                    }else{
                        model.set(fn, new ValueStore(cb.isChecked()?1:0));
                    }
                }
                previousView = vw;
                previousStr = viewID;
            } catch (IllegalAccessException | NoSuchFieldException e) {
                ExceptionReporter.handle(e);
            }
        }
        return model;
    }

    protected void setScrollAndBorderAnimation(final View view) {
        alertForEmptyFieldEvent = () -> {

            GradientDrawable drawable;
            try {
                drawable = (GradientDrawable) view.getBackground();
            }catch(Exception e){
                drawable = null;
            }

            if(drawable == null) {
                drawable = new GradientDrawable();
                drawable.setColor(Color.WHITE);
                view.setBackground(drawable);
            }

            int[] colors = {Color.TRANSPARENT, Color.parseColor("#F94449")};

            ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), colors[0],colors[1]);

            animator.setDuration(500);
            animator.setRepeatCount(9);
            animator.setRepeatMode(ValueAnimator.REVERSE); // Reverse direction on repeat

            GradientDrawable finalDrawable = drawable;

            animator.addUpdateListener(animation -> {
                int color = (int) animation.getAnimatedValue();
                finalDrawable.setStroke(5, color);
            });

            if(scrollView != null) {
                scrollView.post(() -> {
                    int[] viewLocation = new int[2];
                    view.getLocationOnScreen(viewLocation);
                    int[] scrollViewLocation = new int[2];
                    scrollView.getLocationOnScreen(scrollViewLocation);
                    int scrollOffset = viewLocation[1] - scrollViewLocation[1];
                    scrollView.smoothScrollBy(0, scrollOffset-25);
                });
                //scrollView.post(() -> scrollView.smoothScrollTo(0, view.getBottom()));
            }
            animator.start();
        };
    }

    protected void setFormFromModel(Context context, FormTable model, String[] inputValidationString, String cat,boolean isCombinedCheckBox, View layoutView) {

        for (String fn : inputValidationString) {
            try {
                String viewID = fn+(cat.length()>0?"__"+cat:"");
                View vw = layoutView.findViewById(context.getResources().getIdentifier(viewID, "id", context.getPackageName()));
                if(vw instanceof EditText){
                    EditText et = (EditText) vw;
                    et.setText(model.get(fn)==null?"":model.get(fn).toString());
                }
                else if(vw instanceof Spinner){
                    Spinner sp = (Spinner) vw;
                    if(model.get(fn)!=null){
                        try{
                            sp.setSelection(model.get(fn).toInt());
                        }catch(Exception e){}
                    }
                }
                else if(vw instanceof RadioGroup){
                    RadioGroup rg = (RadioGroup) vw;
                    if(model.get(fn) != null)
                        rg.check(context.getResources().getIdentifier(fn+model.get(fn).toInt(), "id", context.getPackageName()));
                }
                else if(vw instanceof CheckBox){
                    CheckBox cb = (CheckBox) vw;
                    if(isCombinedCheckBox) {
                        String subStrVal = fn.substring(fn.length() - 1);
                        String val = model.get(fn.substring(0, fn.length() - 1)) == null ? "" : model.get(fn.substring(0, fn.length() - 1)).toString();
                        cb.setChecked(val.contains(subStrVal));
                    }
                    else{
                        cb.setChecked(model.get(fn)==null?false:model.get(fn).toInt()==1?true:false);
                    }
                }
                else if(vw instanceof TextView){
                    ((TextView) vw).setText(model.get(fn)==null?"":model.get(fn).toString());
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                ExceptionReporter.handle(e);
            }
        }
    }


    protected void setCommonFields(FormTable m){
        if(m.created_time==null) {
            m.created_time = getTimeNowwithSeconds();
            m.userid = resumeModel.userid;
            m.is_deleted = 0;
            m.blk_desc = resumeModel.blk_desc;
            m.uid = resumeModel.uid;
            m.sid = resumeModel.sid;
            m.sno = resumeModel.sno;
            m.env=env;
        }else{
            m.sync_time=null;
            m.modified_time = getTimeNowwithSeconds();
            m.env=resumeModel.env;
            m.is_deleted = 0;
            dbHandler.execSql("UPDATE "+ Section12.class.getSimpleName()+" SET sync_time=NULL where ENV='"+env+"' AND  uid='"+resumeModel.uid+"';");
            dbHandler.execSql("UPDATE "+ Section34.class.getSimpleName()+" SET sync_time=NULL where ENV='"+env+"' AND  uid='"+resumeModel.uid+"';");
            dbHandler.execSql("UPDATE "+ Baseline.class.getSimpleName()+" SET sync_time=NULL where ENV='"+env+"' AND  uid='"+resumeModel.uid+"';");
        }
    }


}
