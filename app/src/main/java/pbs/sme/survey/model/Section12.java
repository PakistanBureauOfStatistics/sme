package pbs.sme.survey.model;

import pk.gov.pbs.database.annotations.PrimaryKey;

public class Section12 extends FormTable {
    public Integer  id;
    public String title;
    public String owner;
    public String name;
    public String designation;
    public Integer phone_type;
    public String phone_code;
    public String phone_number;
    public Integer reason_no_phone;
    public String email;
    public String website;
    public Integer started_year;
    public Integer is_registered;
    public String agency;
    public Integer is_maintaining;
    public Integer ownership;
    public String ownership_other;
    public String activity;
    public String psic;
    public Integer  is_seasonal;
    public Integer jan;
    public Integer feb;
    public Integer mar;
    public Integer  apr;
    public Integer  may;
    public Integer jun;
    public Integer jul;
    public Integer aug;
    public Integer sep;
    public Integer oct;
    public Integer nov;
    public Integer dec;
    public Integer months;
    public Integer is_establishement;
    public Integer emp_count;
    public Integer emp_cost;
    public Double lat;
    public Double lon;
    public Double alt;
    public Double hac;
    public Double vac;
    public String provider;
    public String access_time;
    public Integer zoom_level;
    public String map_type;
    public Integer is_outside;
    public Integer m_outside;
    public String r_outside;
    public String env;
    public String vcode;
    public Double mlat;
    public Double mlon;
    public String monthly;

    public Section12(){}

    @Override
    public String toString() {
        return "Section12{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", designation='" + designation + '\'' +
                ", phone_type=" + phone_type +
                ", phone_code='" + phone_code + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", reason_no_phone=" + reason_no_phone +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", started_year=" + started_year +
                ", is_registered=" + is_registered +
                ", agency='" + agency + '\'' +
                ", is_maIntegeraining=" + is_maintaining +
                ", ownership=" + ownership +
                ", ownership_other='" + ownership_other + '\'' +
                ", activity='" + activity + '\'' +
                ", psic='" + psic + '\'' +
                ", is_seasonal=" + is_seasonal +
                ", jan=" + jan +
                ", feb=" + feb +
                ", mar=" + mar +
                ", apr=" + apr +
                ", may=" + may +
                ", jun=" + jun +
                ", jul=" + jul +
                ", aug=" + aug +
                ", sep=" + sep +
                ", oct=" + oct +
                ", nov=" + nov +
                ", dec=" + dec +
                ", months=" + months +
                ", is_establishement=" + is_establishement +
                ", emp_count=" + emp_count +
                ", emp_cost=" + emp_cost +
                ", lat=" + lat +
                ", lon=" + lon +
                ", alt=" + alt +
                ", hac=" + hac +
                ", vac=" + vac +
                ", provider='" + provider + '\'' +
                ", access_time='" + access_time + '\'' +
                ", zoom_level=" + zoom_level +
                ", map_type='" + map_type + '\'' +
                ", is_outside=" + is_outside +
                ", m_outside=" + m_outside +
                ", r_outside='" + r_outside + '\'' +
                ", env='" + env + '\'' +
                ", vcode='" + vcode + '\'' +
                ", mlat=" + mlat +
                ", mlon=" + mlon +
                ", monthly='" + monthly + '\'' +
                ", blk_desc='" + blk_desc + '\'' +
                ", hno=" + sno +
                ", uid='" + uid + '\'' +
                ", userid=" + userid +
                ", created_time='" + created_time + '\'' +
                ", modified_time='" + modified_time + '\'' +
                ", sync_time='" + sync_time + '\'' +
                ", deleted_time='" + deleted_time + '\'' +
                ", is_deleted=" + is_deleted +
                ", integrityCheck='" + integrityCheck + '\'' +
                ", pcode='" + pcode + '\'' +
                ", sid=" + sid +
                ", time_spent=" + time_spent +
                ", rcol1=" + rcol1 +
                ", rcol2=" + rcol2 +
                ", remarks='" + remarks + '\'' +
                ", flag=" + flag +
                ", survey=" + survey +
                ", status=" + status +
                '}';
    }

    public Section12(String blockCode, int houseNo, String title, int emp){
        this.blk_desc = blockCode;
        this.sno = houseNo;
        this.title = title;
        this.emp_count=emp;
        generateHouseUID();
    }
}
