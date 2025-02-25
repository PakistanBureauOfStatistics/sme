package pbs.sme.survey.online;

import androidx.annotation.Keep;

import java.util.List;

import pbs.sme.survey.model.Baseline;
import pbs.sme.survey.model.Household;
import pbs.sme.survey.model.Section12;
import pbs.sme.survey.model.Section34;

@Keep
public class Sync2 {
    protected List<Section12> s12;
    protected List<Section34> s34;
    protected List<Baseline> base;

    public List<Section12> getS12() {
        return s12;
    }

    public void setS12(List<Section12> s12) {
        this.s12 = s12;
    }

    public List<Section34> getS34() {
        return s34;
    }

    public void setS34(List<Section34> s34) {
        this.s34 = s34;
    }

    public List<Baseline> getBase() {
        return base;
    }

    public void setBase(List<Baseline> base) {
        this.base = base;
    }
}
