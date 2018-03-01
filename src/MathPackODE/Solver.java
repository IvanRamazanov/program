/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathPackODE;

import ElementBase.DynamMathElem;
import ElementBase.MathInPin;
import ElementBase.OutputElement;
import ElementBase.SchemeElement;
import MathPack.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import raschetkz.ModelState;

/**
 *
 * @author Ivan
 */
abstract public class Solver {
    protected List<StringGraph> newtonFunc;
    protected List<OutputElement> mathOuts;
    protected List<DynamMathElem> mathDynamics;
    protected List<List<StringGraph>> symbJacobian,invJacob;
    protected List<StringGraph> algSystem;
    protected WorkSpace vars;
    protected DAE dae;
    protected List<MathInPin> inps;
    //protected List<String> varNames;
    protected ArrayList<Double> vector;
    protected int jacobEstType,diffRank;
    protected double[]
            //s,
            x0,vals;
    private double[][] J;
    private int[] ind;
    public static double dt, time;
    public static SimpleDoubleProperty progress=new SimpleDoubleProperty();
    protected Task cancelFlag;

    private List<Double> valsBkp;
    private double[] x0Bkp;

    abstract public void simpleUpdate(List<Double> x_old,List<Double> dx);

    abstract public void evalNextStep();

    public void init(DAE daeSys,ModelState state, Task tsk){
        time=0;
        progress.setValue(0.0);
        dae=daeSys;
        vars=daeSys.getVars();
        inps=daeSys.getInps();
        symbJacobian=daeSys.getJacob();
        invJacob=daeSys.getInvJacob();
        newtonFunc=daeSys.getNewtonFunc();
        algSystem=daeSys.getAlgSystem();
        mathOuts=daeSys.getMathOuts();
        mathDynamics=daeSys.getDynMaths();
        cancelFlag=tsk;

        dt=state.getDt().doubleValue();
        //s=new double[algSystem.size()];
        x0=new double[algSystem.size()];

        vals=new double[algSystem.size()];
        J=new double[algSystem.size()][algSystem.size()];

        ind=new int[algSystem.size()];
        //varNames=new ArrayList();
        vector=vars.getVarList();

        valsBkp=new ArrayList<>(vector.size());
        valsBkp.addAll(vector);
        x0Bkp=new double[algSystem.size()];

        int i=0,j=0;
        for(String var:vars.getVarNameList()){
            if(!var.startsWith("X.")){
                //varNames.add(var);
                x0[i]=vars.get(var);
                ind[i]=j;
                //s[i]=vars.get(var);
                i++;
                if(var.startsWith("d.X."))
                    diffRank++;
            }
            j++;
        }

        for(SchemeElement elem:state.getElems()){
            elem.init();
        }
        for(DynamMathElem del:mathDynamics){
            del.init();
        }
        for(OutputElement elem:mathOuts){
            elem.init();
        }
        jacobEstType=state.getJacobianEstimationType();
        evalSysState(); //zerotime init
        for(OutputElement elem:mathOuts){
            elem.updateData(time);
        }
        selfInit();
    }

    public void solve(double tEnd){
        for(time=dt;time<=tEnd;time=time+dt){
            if(cancelFlag.isCancelled())
                break;
            evalNextStep();
            progress.set(time);
        }
    }

    public void evalSysState(){
        double timeBkp=time;

        for(int i=0;i<vector.size();i++){
            valsBkp.set(i,vector.get(i));
        }
        for(int i=0;i<x0.length;i++){
            x0Bkp[i]=x0[i];
        }

        int cnt=0;

        if(symbJacobian!=null)
            if(!symbJacobian.isEmpty()){
                boolean faultflag=false;

                while(true){
                    if(cancelFlag.isCancelled())
                        break;
                    //eval F(x)=0
                    MathPack.MatrixEqu.putValuesFromSymbRow(vals,algSystem,vars,inps);
                    double norm=MathPack.MatrixEqu.norm(vals);
                    if(norm<0.000001)
                        break;

                    switch(jacobEstType){
                        case 0: //full symbolic
                            MathPack.MatrixEqu.putValuesFromSymbRow(vals,newtonFunc,vars,inps);
                            break;
                        case 1: //inverse symbolic
                            List<List<Double>> invJ=MathPack.MatrixEqu.evalSymbMatr(invJacob, vars, inps);
                            vals=MathPack.MatrixEqu.mulMatxToRow(invJ,vals);
                            break;
                        case 2: //only jacob symb
//                            invJ=MathPack.MatrixEqu.invMatr(MathPack.MatrixEqu.evalSymbMatr(Jacob,vars, inps));
//                            vals=MathPack.MatrixEqu.mulMatxToRow(invJ,vals);
                            MathPack.MatrixEqu.putValuesFromSymbMatr(J,symbJacobian,vars, inps);
                            MatrixEqu.solveLU(J,vals); // first 'vals' contains F(x)
                            break;
                        default:
                            vals=null;
                    }
                    for(int i=0;i<x0.length;i++){
                        //if(Math.abs(val)>maxDiffer) maxDiffer=Math.abs(val);
                        if(Double.isNaN(vals[i])){
                            vals[i]=vars.get(i);
                            throw new Error(vars.getName(i)+" is not a number!");
                        }else if(Double.isInfinite(vals[i])){
                            vals[i]=vars.get(i);
                            throw new Error(vars.getName(i)+" is not finite!");

                        }

                        double y=x0[i]-vals[i];
                        vector.set(ind[i], y);
                        x0[i]=y;
                    }
//                    for(int i=0;i<x0.length;i++){
//                        vars.setValue(i, s[i]);
//                    }
                    //                if(MathPack.MatrixEqu.normOfDiffer(x0, x)<0.0001){


                    //System.arraycopy(s, 0, x0, 0, x0.length);

                    //                if(maxDiffer<0.000001){
                    //                    //exit
                    ////                            for(int j=0;j<diffSystem.size();j++){ //update Xes
                    ////                                vars.setValue("X."+(j+1), diffSystem.get(j).evaluate(vars, inps));
                    ////                            }
                    //                    break;
                    //                }else{
                    //                    System.arraycopy(s, 0, s0, 0, s0.length);
                    //                }
                    cnt++;
                    if(cnt>500){
                        if(false) { // for debug
                            for (double[] row : J) {
                                System.out.println(Arrays.toString(row));
                            }
                            System.out.println("x:");
                            System.out.println(Arrays.toString(vals));
                            System.out.println("F(x):");
                            MathPack.MatrixEqu.putValuesFromSymbRow(vals, algSystem, vars, inps);
                            System.out.println(Arrays.toString(vals));
                        }

                        cnt=0;
                        if(faultflag){
//



                        }else {
                            double newStepSize=dt/2;
                            time-=newStepSize;
                            if(newStepSize < 1E-13)
                                throw new Error("Dead loop!");
                            for(int j=0;j<vector.size();j++)
                                vector.set(j,valsBkp.get(j));
                            for(int j=0;j<x0.length;j++)
                                x0[j]=x0Bkp[j];

//                            faultflag = true;
//                            int ii = 0;
//                            for (String var : vars.getVarNameList()) {
//
//                                if (!var.startsWith("X.")) {
//                                    vars.setValue(var, 0);
//                                    x0[ii] = 0;
//                                    //s[ii]=0;
//                                    ii++;
//                                }
//                            }
                        }
                    }
                }
            }
        //for logout
        if(Rechatel.IS_LOGGING) {
            if (time == 0) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\NetBeansLogs\\XesLog.txt"))) {
                    bw.write("t ");
                    for (String entry : vars.getVarNameList()) {
                        //if(entry.getKey().startsWith("X.")){
                        bw.write(entry + " ");
                        //}
                    }
                    bw.write(" numOfJac");
                    bw.newLine();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\NetBeansLogs\\XesLog.txt", true))) {
                bw.write(Double.toString(time) + " ");
                for (Double entry : vars.getVarList()) {
                    bw.write(entry + " ");
                }
                bw.write(Integer.toString(cnt));
                bw.newLine();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    protected void selfInit(){}


}

