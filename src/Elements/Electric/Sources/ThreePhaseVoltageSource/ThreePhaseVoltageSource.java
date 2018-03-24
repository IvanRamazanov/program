/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Elements.Electric.Sources.ThreePhaseVoltageSource;

import static java.lang.StrictMath.PI;

import ElementBase.ElectricPin;
import ElementBase.SchemeElement;
import Elements.Environment.Subsystem.Subsystem;

/**
 *
 * @author Ivan
 */
public class ThreePhaseVoltageSource extends SchemeElement {

    public ThreePhaseVoltageSource(Subsystem sys){
        super(sys);
//        Dymamic=false;
        addElectricCont(new ElectricPin(this, 7, 4));//A
        addElectricCont(new ElectricPin(this, 26, 4));//B
        addElectricCont(new ElectricPin(this, 43, 4));//C
        addElectricCont(new ElectricPin(this, 26, 66));//N
    }

    public ThreePhaseVoltageSource(boolean Catalog){
        super(Catalog);
    }

    @Override
    public String[] getStringFunction() {
        String A=this.parameters.get(0).toString();
        String fq=this.parameters.get(1).toString();
        String phi=this.parameters.get(2).toString();
        String[] str={  "p.1="+A+"*"+"sin("+(2*PI)+"*"+fq+"*time+"+phi+")+p.4",
                "p.2="+A+"*"+"sin("+(2*PI)+"*"+fq+"*time+"+phi+"-"+(2*PI/3)+")+p.4",
                "p.3="+A+"*"+"sin("+(2*PI)+"*"+fq+"*time+"+phi+"+"+(2*PI/3)+")+p.4","i.1+i.2+i.3+i.4=0"};
        return str;
    }

    @Override
    protected void setParams(){
        this.parameters.add(new ScalarParameter("Amplitude", 10.0));
        this.parameters.add(new ScalarParameter("Frequency", 50.0));
        this.parameters.add(new ScalarParameter("Phase", 0.0));
        setName("Three-phase voltage\nsource");
    }

    @Override
    protected String getDescription(){
        return "This block represents a three-phase voltage source.";
    }
}
