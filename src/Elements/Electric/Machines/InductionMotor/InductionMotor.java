/*
 * The MIT License
 *
 * Copyright 2017 ramazanov_im.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Elements.Electric.Machines.InductionMotor;

import ElementBase.ElectricPin;
import ElementBase.MechPin;
import ElementBase.SchemeElement;
import Elements.Environment.Subsystem.Subsystem;

/**
 *
 * @author ramazanov_im
 */
public class InductionMotor extends SchemeElement {
    ScalarParameter Rs,Ls,J,Rr,Lr,Lm,Zp,Fc;

    public InductionMotor(Subsystem sys){
        super(sys);
        addElectricCont(new ElectricPin(this, 9, 4));
        addElectricCont(new ElectricPin(this, 25, 4));
        addElectricCont(new ElectricPin(this, 40, 4));
        addMechCont(new MechPin(this,45,35));
        addMechCont(new MechPin(this,4,35));
    }

    public InductionMotor(boolean Catalog){
        super(Catalog);
    }

    @Override
    public String[] getStringFunction() {
        double  rs=Rs.getValue(),
                rr=Rr.getValue(),
                lm=Lm.getValue(),
                ls=Ls.getValue()+lm,
                lr=Lr.getValue()+lm,
                jm=J.getValue(),
                zp=Zp.getValue();
        double M12=2.0/3.0*lm,M3=4.0*ls*lr-9.0*M12*M12;
        String  Kuac=Double.toString(8.0*lr/3.0/M3),
                Kubc=Double.toString(4.0*lr/3.0/M3),
                KiA=Double.toString(3.0*Math.sqrt(3.0)*M12*M12*zp/M3)+"*X.5-"+Double.toString(4.0*rs*lr/M3),
                KiB=Double.toString(6.0*Math.sqrt(3.0)*M12*M12*zp/M3)+"*X.5",
                Kia=Double.toString(2.0*Math.sqrt(3.0)*M12/M3)+"*("+Double.toString(lr*zp)+"*X.5*(sin(X.6)*"+Double.toString(Math.sqrt(3.0))+"+cos(X.6))+"+Double.toString(rr)+"*(cos(X.6)*"+Double.toString(Math.sqrt(3.0))+"-sin(X.6)))",
                Kib=Double.toString(4.0*Math.sqrt(3.0)*M12/M3)+"*(X.5*"+Double.toString(lr*zp)+"*cos(X.6)-"+Double.toString(rr)+"*sin(X.6))";
        String[] str=new String[11];
        str[0]="d.X.1="+Kuac+"*(p.1-p.3)-"+Kubc+"*(p.2-p.3)+("+KiA+")*X.1+("+KiB+")*X.2+("+Kia+")*X.3+("+Kib+")*X.4";
        Kuac=Double.toString(4.0*lr/3.0/M3);
        Kubc=Double.toString(8.0*lr/3.0/M3);
        KiA=Double.toString(6.0*Math.sqrt(3.0)*M12*M12*zp/M3)+"*X.5";
        KiB=Double.toString(3.0*Math.sqrt(3.0)*M12*M12*zp/M3)+"*X.5+"+Double.toString(4.0*rs*lr/M3);
        Kib=Double.toString(2.0*Math.sqrt(3.0)*M12/M3)+"*(X.5*"+Double.toString(lr*zp)+"*(sin(X.6)*"+Double.toString(Math.sqrt(3.0))+"-cos(X.6))+"+rr+"*(cos(X.6)*"+Double.toString(Math.sqrt(3.0))+"+sin(X.6)))";
        Kia=Double.toString(4.0*Math.sqrt(3.0)*M12*rr/M3)+"*sin(X.6)-"+Double.toString(4.0*Math.sqrt(3.0)*M12*lr*zp/M3)+"*X.5*cos(X.6)";
        str[1]="d.X.2="+Kubc+"*(p.2-p.3)-"+Kuac+"*(p.1-p.3)-("+KiA+")*X.1-("+KiB+")*X.2+("+Kia+")*X.3+("+Kib+")*X.4";
        Kuac=Double.toString(4.0*M12/M3)+"*cos(X.6)";
        Kubc=Double.toString(2.0*M12/M3)+"*cos(X.6)-"+Double.toString(2.0*M12*Math.sqrt(3.0)/M3)+"*sin(X.6)";
        KiA=Double.toString(2.0*3.0*M12*ls*zp/M3)+"*X.5*sin(X.6)-"+Double.toString(2.0*Math.sqrt(3.0)*M12*ls*zp/M3)+"*X.5*cos(X.6)+"+
                Double.toString(2.0*3.0*M12*rs/M3)+"*cos(X.6)+"+Double.toString(2*Math.sqrt(3)*M12*rs/M3)+"*sin(X.6)";
        KiB=Double.toString(4.0*Math.sqrt(3.0)*M12*rs/M3)+"*sin(X.6)-"+Double.toString(4.0*Math.sqrt(3.0)*M12*ls*zp/M3)+"*X.5*cos(X.6)";
        Kia=Double.toString(3.0*Math.sqrt(3.0)*M12*M12*zp/M3)+"*X.5+"+Double.toString(4.0*rr*ls/M3);
        Kib=Double.toString(6.0*Math.sqrt(3.0)*M12*M12*zp/M3)+"*X.5";
        str[2]="d.X.3=-1*"+Kuac+"*(p.1-p.3)+("+Kubc+")*(p.2-p.3)+("+KiA+")*X.1+("+KiB+")*X.2-("+Kia+")*X.3-("+Kib+")*X.4";
        Kubc=Double.toString(4.0*M12/M3)+"*cos(X.6)";
        Kuac=Double.toString(2.0*M12/M3)+"*cos(X.6)+"+Double.toString(2.0*M12*Math.sqrt(3.0)/M3)+"*sin(X.6)";
        KiB=Double.toString(2.0*3.0*M12*ls*zp/M3)+"*X.5*sin(X.6)+"+Double.toString(2.0*Math.sqrt(3.0)*M12*ls*zp/M3)+"*X.5*cos(X.6)+"+
                Double.toString(2.0*3.0*M12*rs/M3)+"*cos(X.6)-"+Double.toString(2.0*Math.sqrt(3.0)*M12*rs/M3)+"*sin(X.6)";
        KiA=Double.toString(4.0*Math.sqrt(3.0)*M12*ls*zp/M3)+"*X.5*cos(X.6)-"+Double.toString(4.0*Math.sqrt(3.0)*M12*rs/M3)+"*sin(X.6)";
        Kib=Double.toString(3.0*Math.sqrt(3.0)*M12*M12*zp/M3)+"*X.5-"+Double.toString(4.0*rr*ls/M3);
        Kia=Double.toString(6.0*Math.sqrt(3.0)*M12*M12*zp/M3)+"*X.5";
        str[3]="d.X.4=("+Kuac+")*(p.1-p.3)-"+Kubc+"*(p.2-p.3)+("+KiA+")*X.1+("+KiB+")*X.2+"+Kia+"*X.3+("+Kib+")*X.4";
        String M=Double.toString(2.0/3.0*M12*zp)+"*((X.2*X.3-X.1*X.4)*"+Double.toString(Math.sqrt(3.0))+"*cos(X.6)-(2*X.1*X.3+2*X.2*X.4+X.1*X.4+X.2*X.3)*sin(X.6))";
        str[4]="d.X.5=("+M+"-T.1-X.5*"+Fc.toString()+")/"+jm; //I.1 !!!!!!!
        str[5]="d.X.6="+zp+"*X.5";
        str[6]="i.1+i.2+i.3=0";
        str[7]="T.1+T.2=0";
        str[8]="X.1=i.1";
        str[9]="X.2=i.2";
        str[10]="X.5=w.1-w.2";

        return str;
    }

    @Override
    protected void setParams(){
        Rs=new ScalarParameter("Stator reluctance", 1.405);
        this.parameters.add(Rs);
        Rr=new ScalarParameter("Rotor reluctance", 1.395);
        this.parameters.add(Rr);
        Ls=new ScalarParameter("Stator leakage inductance", 0.005839);
        this.parameters.add(Ls);
        Lr=new ScalarParameter("Rotor leakage inductance", 0.005839);
        this.parameters.add(Lr);
        Lm=new ScalarParameter("Magnetizing inductance", 0.1722);
        this.parameters.add(Lm);
        J=new ScalarParameter("Inertia", 0.0131);
        this.parameters.add(J);
        Zp=new ScalarParameter("Pole pairs", 2);
        this.parameters.add(Zp);
        Fc=new ScalarParameter("Friction", 0.002985);
        this.parameters.add(Fc);

        this.initials.add(new InitParam("Stator A phase current", 0));
        this.initials.add(new InitParam("Stator B phase current", 0));
        this.initials.add(new InitParam("Stator a phase current", 0));
        this.initials.add(new InitParam("Stator b phase current", 0));
        this.initials.add(new InitParam("Angular velocity", 0));
        this.initials.add(new InitParam("Rotor angle", 0));
        setName("Asynchronous motor");
    }

    @Override
    protected String getDescription(){
        return "This block represents an asynchronous AC motor with single squirrel cage." +
                "Input: mechanical torque in N*m.\n" +
                "Output: angular velocity in rad/sec.";
    }
}
