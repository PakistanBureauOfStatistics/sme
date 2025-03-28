package pbs.sme.survey.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import pbs.sme.survey.meta.ValueStore;
import pk.gov.pbs.database.annotations.Default;
import pk.gov.pbs.database.annotations.SqlDataType;
import pk.gov.pbs.database.annotations.SqlExclude;
import pk.gov.pbs.database.annotations.SqlPrimaryKey;
import pk.gov.pbs.database.annotations.PrimaryKey;
import pk.gov.pbs.utils.ExceptionReporter;
import pk.gov.pbs.utils.SystemUtils;

public class FormTable implements Serializable {


    @SqlDataType("[VARCHAR](10)")
    public String blk_desc;

    public Integer sno;
    @PrimaryKey
    @SqlPrimaryKey
    @SqlDataType("[VARCHAR(100)]")
    public String uid;

    public Integer userid;
    @SqlDataType("[DATETIME]")
    public String created_time;
    @SqlDataType("[DATETIME]")
    public String modified_time;
    @SqlDataType("[DATETIME]")
    public String sync_time;
    @SqlDataType("[DATETIME]")
    public String deleted_time;

    @Default("0")
    @SqlDataType("[TINYINT]")
    public Integer is_deleted;
    @Nullable
    @SqlExclude
    public String integrityCheck;


    public String pcode;
    public Long  sid;
    public Integer time_spent;
    public Integer rcol1;
    public Integer rcol2;
    public String remarks;
    public Integer flag;

    public String survey;
    public String env;

    @Default("1")
    @SqlExclude
    public Integer status; // 1=incomplete, 2=complete, 3=uploaded

    public FormTable(){
    }

    public String generateHouseUID(){
        return uid = UUID.randomUUID().toString();
    }

    public void setContext(FormTable srcTable){
        this.uid = srcTable.uid;
        this.blk_desc = srcTable.blk_desc;
        this.userid = srcTable.userid;
    }

    public Field findField(String key){
        try {
            Field field = getClass().getDeclaredField(key);
            if (field != null)
                return field;

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        Class<?> modelClass = this.getClass();
        while (modelClass != Object.class){
            modelClass = modelClass.getSuperclass();
            if (modelClass == null)
                break;

            try {
                Field field = getClass().getDeclaredField(key);
                if (field != null)
                    return field;

            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public int set(Map<String, ValueStore> valueStoreMap){
        int failCounts = 0;
        for (String key : valueStoreMap.keySet()){
            try {
                set(key, valueStoreMap.get(key));
            } catch (Exception e) {
                failCounts++;
                ExceptionReporter.handle(e);
            }
        }
        return failCounts;
    }

    //null safe equals
    protected boolean nse(Object value1, Object value2){
        if (value1 == null && value2 == null)
            return true;
        else if (value1 == null || value2 == null)
            return false;
        return String.valueOf(value1)
                .contentEquals(String.valueOf(value2));
    }

    public void set(String prop, @Nullable ValueStore value) throws NoSuchFieldException, IllegalAccessException {
        Field field = findField(prop);
        if (field == null)
            return;

        if (!field.isAccessible())
            field.setAccessible(true);

		if (value == null)
            field.set(this, null);
        else if (field.getType() == String.class)
            field.set(this, value.toString());
        else if (field.getType() == int.class || field.getType() == Integer.class)
            field.set(this, value.toInt());
        else if (field.getType() == long.class || field.getType() == Long.class)
            field.set(this, value.toLong());
        else if (field.getType() == double.class || field.getType() == Double.class)
            field.set(this, value.toDouble());
        else if (field.getType() == boolean.class || field.getType() == Boolean.class)
            field.set(this, value.toBoolean());
        else if (field.getType() == float.class || field.getType() == Float.class)
            field.set(this, value.toFloat());
        else if (field.getType() == char.class || field.getType() == Character.class)
            field.set(this, value.toChar());
    }

    public synchronized void setSynchronized(String prop, @Nullable ValueStore value) throws NoSuchFieldException, IllegalAccessException {
        set(prop, value);
    }

    public boolean set(String prop, @Nullable Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = getClass().getField(prop);
        field.set(this, value);
        return true;
    }

    public synchronized boolean setSynchronized(String prop, @Nullable Object value) throws NoSuchFieldException, IllegalAccessException {
        return set(prop, value);
    }

    public ValueStore get(String prop) throws NoSuchFieldException, IllegalAccessException {
        Field field = getClass().getField(prop);
        if(field.get(this) != null)
            return new ValueStore(field.get(this).toString());
        return null;
    }

    public synchronized ValueStore getSynchronized(String prop) throws NoSuchFieldException, IllegalAccessException {
        return get(prop);
    }

    public boolean hasField(String field){
        try {
            Field f = getClass().getField(field);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public boolean checkDataIntegrity(){
        if(integrityCheck != null){
            String has = integrityCheck;
            integrityCheck = null;
            String is = SystemUtils.MD5(toString());
            integrityCheck = has;
            return has.equalsIgnoreCase(is);
        }

        return false;
    }

    public void setupDataIntegrity(){
        integrityCheck = null;
        integrityCheck = SystemUtils.MD5(toString());
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            Field[] allFields = getClass().getFields();
            for (Field field : allFields){
                if(field.get(this) != null)
                    sb.append(field.get(this).toString());
            }
        } catch (Exception e){
            ExceptionReporter.handle(e);
        }
        return sb.toString();
    }

    public boolean isSame(Table subject,String... except){
        if (subject != null && this.getClass().equals(subject.getClass())) {
            try {
                Field[] allFields = getClass().getFields();
                HashSet<Field> exceptFields = new HashSet<>();

                String[] ignoreFieldsInComparison = new String[] {"created_time", "deleted_time", "sync_time", "modified_time", "integrityCheck", "status"};
                for (String field : ignoreFieldsInComparison) {
                    try {
                        Field oField = getClass().getField(field);
                        exceptFields.add(oField);
                    } catch (Exception ignore) { }
                }

                if (except != null && except.length > 0){
                    for (String field : except) {
                        if (field != null) {
                            try {
                                Field oField = getClass().getField(field);
                                exceptFields.add(oField);
                            } catch (Exception e) {
                                ExceptionReporter.handle(e);
                            }
                        }
                    }
                }

                for (Field field : allFields) {
                    if (!nse(field.get(this), field.get(subject))) {
                        if (!exceptFields.contains(field))
                            return false;
                    }
                }
            } catch (Exception e) {
                ExceptionReporter.handle(e);
                return false;
            }

            return true;
        }
        return false;
    }
}
