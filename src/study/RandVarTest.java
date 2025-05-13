package study;


import simulation.lib.counter.DiscreteCounter;
import simulation.lib.randVars.RandVar;
import simulation.lib.randVars.continous.ErlangK;
import simulation.lib.randVars.continous.Exponential;
import simulation.lib.randVars.continous.HyperExponential;
import simulation.lib.randVars.continous.Uniform;
import simulation.lib.rng.RNG;
import simulation.lib.rng.StdRNG;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
    Stuff that can not be instantiated
    Exponential: Exponential cvar always needs to be 1. Thus cvar 0.1 and 2 cannot be instantiated.
    ErlangK: Cvar always <= 1. Thus cannot instantiate cvar 2;
    H2: Cvar always >= 1. Thus cannot instaniate cvar 0.1
 */


public class RandVarTest {
    private int n;
    private List<RandVar> vars;
    private DiscreteCounter counter;
    private StdRNG rng;
    private double[] cvars = {0.1,1.0,2.0};

    public RandVarTest() {
        this.n = (int) Math.pow(10,6);
        this.rng = new  StdRNG();
        this.vars = new ArrayList<RandVar>();
        for (double v : cvars) {

            Uniform uniform = new Uniform(rng);
            uniform.setMean(1.0);
            uniform.setCvar(v);
            vars.add(uniform);

            if(v == 1.0){
                Exponential exponential = new Exponential(rng);
                exponential.setMean(1.0);
                exponential.setCvar(v);
                vars.add(exponential);
            }

            if(v <= 1.0){
                ErlangK erlangK = new ErlangK(rng);
                erlangK.setMeanAndStdDeviation(1.0,v);
                vars.add(erlangK);
            }

            if(v >= 1.0){
                HyperExponential hyperExponential = new HyperExponential(rng,1.0,v);
                vars.add(hyperExponential);
            }

        }


    }


    public RandVarTest(int n) {
        this.n = n;
    }


    public void runTest() {
        for (RandVar var : vars) {
            DiscreteCounter counter1 = new DiscreteCounter(var.getType());
            for(int k = 0; k < n; k++) {
                counter1.count(var.getRV());
            }
            System.out.println(var.toString());
            System.out.println(counter1.report());
            System.out.println("\n\n");
            counter1.reset();
        }
    }


    public double[] getCvars() {
        return cvars;
    }

    public void setCvars(double[] cvars) {
        this.cvars = cvars;
    }
}
