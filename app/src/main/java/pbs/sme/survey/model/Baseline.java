package pbs.sme.survey.model;

public class Baseline extends FormTable {

    public Integer a1;
    public Integer a2;

    public Integer a3a;
    public Integer a3b;
    public Integer a3c;
    public Integer a3d;
    public Integer a3e;
    public Integer b1;
    public Integer b2;
    public String b3;
    public String b3_other;
    public String b4;
    public String b5;
    public String b6;
    public String b6_other;
    public Integer b7;
    public Integer b8;
    public Integer b9;
    public String b10;
    public String b10_other;
    public Integer c1a;
    public Integer c1b;
    public Integer c1c;
    public Integer c1d;
    public Integer c1e;
    //public Integer c1f;
    //public String c1_other;
    public Integer c2a;
    public Integer c2b;
    public Integer c2c;
    public Integer c2d;
    public Integer c2e;
    public Integer c3;
    public Integer wapda;
    public Integer solar;
    public Integer generator;
    public Integer d1;
    public Integer d2;
    public String d2_other;
    public Integer d3a;
    public Integer d3b;
    public Integer d3c;
    public Integer d3d;
    public Integer d3e;
    public Integer d4;
    public String d5;
    public Integer d6;
    public Integer d7a;
    public Integer d7b;
    public Integer d7c;
    public Integer d7d;
    public Integer d7e;
    //public Integer d7f;
    //public String d7_other;
    public String e1;
    public String e1_other;
    public Integer e2;
    public String e3;
    public String e4;
    public String e5;
    public Integer f1;
    public Integer f2;
    public Integer f3;
    public Integer f4;

    public Integer f5a;
    public Integer f5b;
    public Integer f5c;
    public Integer f5d;

    public Integer f6a;
    public Integer f6b;
    public Integer f6c;
    public Integer f6d;
    public Integer f6e;
    //public Integer f6f;
    //public String f6_other;
    public Integer f7;
    public Integer f8;
    public String f9_scheme;
    public String f10;
    public String f10_other;
    public String f11;
    public String f11_other;
    public Integer export1;
    public String export2;
    public String export2_other;
    public String export3;
    public String export4;
    public Integer export5a;
    public Integer export5b;
    public Integer export5c;
    public Integer export5d;
    public Integer export5e;

    @Override
    public String toString() {
        return "Baseline{" +
                "a1=" + a1 +
                ", a2=" + a2 +
                ", a3a=" + a3a +
                ", a3b=" + a3b +
                ", a3c=" + a3c +
                ", a3d=" + a3d +
                ", a3e=" + a3e +
                ", b1=" + b1 +
                ", b2=" + b2 +
                ", b3='" + b3 + '\'' +
                ", b3_other='" + b3_other + '\'' +
                ", b4='" + b4 + '\'' +
                ", b5='" + b5 + '\'' +
                ", b6='" + b6 + '\'' +
                ", b6_other='" + b6_other + '\'' +
                ", b7=" + b7 +
                ", b8=" + b8 +
                ", b9=" + b9 +
                ", b10='" + b10 + '\'' +
                ", b10_other='" + b10_other + '\'' +
                ", c1a=" + c1a +
                ", c1b=" + c1b +
                ", c1c=" + c1c +
                ", c1d=" + c1d +
                ", c1e=" + c1e +
                //", c1f=" + c1f +
                //", c1_other='" + c1_other + '\'' +
                ", c2a=" + c2a +
                ", c2b=" + c2b +
                ", c2c=" + c2c +
                ", c2d=" + c2d +
                ", c2e=" + c2e +
                ", c3=" + c3 +
                ", wapda=" + wapda +
                ", solar=" + solar +
                ", generator=" + generator +
                ", d1=" + d1 +
                ", d2=" + d2 +
                ", d2_other='" + d2_other + '\'' +
                ", d3a=" + d3a +
                ", d3b=" + d3b +
                ", d3c=" + d3c +
                ", d3d=" + d3d +
                ", d3e=" + d3e +
                ", d4=" + d4 +
                ", d5='" + d5 + '\'' +
                ", d6=" + d6 +
                ", d7a=" + d7a +
                ", d7b=" + d7b +
                ", d7c=" + d7c +
                ", d7d=" + d7d +
                ", d7e=" + d7e +
                //", d7f=" + d7f +
                //", d7_other='" + d7_other + '\'' +
                ", e1='" + e1 + '\'' +
                ", e1_other='" + e1_other + '\'' +
                ", e2=" + e2 +
                ", e3='" + e3 + '\'' +
                ", e4='" + e4 + '\'' +
                ", e5='" + e5 + '\'' +
                ", f1=" + f1 +
                ", f2=" + f2 +
                ", f3=" + f3 +
                ", f4=" + f4 +
                ", f5a=" + f5a +
                ", f5b=" + f5b +
                ", f5c=" + f5c +
                ", f5d=" + f5d +
                ", f6a=" + f6a +
                ", f6b=" + f6b +
                ", f6c=" + f6c +
                ", f6d=" + f6d +
                ", f6e=" + f6e +
                //", f6f=" + f6f +
                //", f6_other='" + f6_other + '\'' +
                ", f7=" + f7 +
                ", f8=" + f8 +
                ", f9_other='" + f9_scheme + '\'' +
                ", f10='" + f10 + '\'' +
                ", f10_other='" + f10_other + '\'' +
                ", f11='" + f11 + '\'' +
                ", f11_other='" + f11_other + '\'' +
                ", export1=" + export1 +
                ", export2='" + export2 + '\'' +
                ", export2_other='" + export2_other + '\'' +
                ", export3='" + export3 + '\'' +
                ", export4='" + export4 + '\'' +
                ", export5a=" + export5a +
                ", export5b=" + export5b +
                ", export5c=" + export5c +
                ", export5d=" + export5d +
                ", export5e=" + export5e +
                ", blk_desc='" + blk_desc + '\'' +
                ", sno=" + sno +
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
                ", survey='" + survey + '\'' +
                ", env='" + env + '\'' +
                ", status=" + status +
                '}';
    }
}
