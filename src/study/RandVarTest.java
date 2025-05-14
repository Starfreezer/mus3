package study;


import simulation.lib.counter.DiscreteCounter;
import simulation.lib.randVars.RandVar;
import simulation.lib.randVars.continous.ErlangK;
import simulation.lib.randVars.continous.Exponential;
import simulation.lib.randVars.continous.HyperExponential;
import simulation.lib.randVars.continous.Uniform;
import simulation.lib.rng.RNG;
import simulation.lib.rng.StdRNG;
import simulation.lib.histogram.DiscreteHistogram;
import simulation.lib.histogram.Histogram;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
    Stuff that can not be instantiated
    Exponential: Exponential cvar always needs to be 1. Thus, cvar 0.1 and 2 cannot be instantiated.
    ErlangK: Cvar always <= 1. Thus, cannot instantiate cvar 2;
    H2: Cvar always >= 1. Thus, cannot instantiate cvar 0.1
 */


public class RandVarTest {
    private int n;
    private List<RandVar> vars;
    private DiscreteCounter counter;
    private StdRNG rng;
    private double mean = 1.0;
    private double[] cvars = {0.1,1.0,2.0};

    public RandVarTest() {
        this.n = (int) Math.pow(10,6);
        this.rng = new  StdRNG();
        this.mean = 1.0;
        this.vars = new ArrayList<RandVar>();
        for (double v : cvars) {
            try {
                Uniform uniform = new Uniform(rng, mean, v);
                vars.add(uniform);
            } catch (Exception e) {
                System.err.println("Error occurred with uniform distribution for mean: " + mean + " and cvar: " + v);
                System.err.println("Error message: " + e.getMessage() + "\n\n\n");
            }

            try {
                Exponential exponential = new Exponential(rng, mean, v);
                vars.add(exponential);
            } catch (Exception e) {
                System.err.println("Error occurred with exponential distribution for mean: " + mean + " and cvar: " + v);
                System.err.println("Error message: " + e.getMessage() + "\n\n\n");
            }

            try {
                ErlangK erlangK = new ErlangK(rng, mean, v);
                vars.add(erlangK);
            } catch (Exception e) {
                System.err.println("Error occurred with Erlang-k distribution for mean: " + mean + " and cvar: " + v);
                System.err.println("Error message: " + e.getMessage() + "\n\n\n");
            }

            try {
                HyperExponential hyperExponential = new HyperExponential(rng, mean, v);
                vars.add(hyperExponential);
            } catch (Exception e) {
                System.err.println("Error occurred with hyperexponential distribution for mean: " + mean + " and cvar: " + v);
                System.err.println("Error message: " + e.getMessage() + "\n\n\n");
            }
        }
    }


    public RandVarTest(int n) {
        this.n = n;
    }


    public void runTest() {
        String directoryName = "./histogram";
        int lowerBound = 0;
        int upperBound = 10;
        int interval = 100;
        for (RandVar var : vars) {
            DiscreteCounter counter1 = new DiscreteCounter(var.getType());
            DiscreteHistogram histogram = new DiscreteHistogram(var.getType(), interval, lowerBound, upperBound);
            for(int k = 0; k < n; k++) {
                double rv = var.getRV();
                counter1.count(rv);
                histogram.count(rv);
            }
            System.out.println(var.toString());
            System.out.println(counter1.report());
            System.out.println("\n\n");
            counter1.reset();
            

            
            // Specify the folder where the histogram CSV files should be saved to avoid overwriting them
            // it makes plotting easier and more organized
            // Check the cvar value and set the directory name accordingly -> can only be used for 0.1, 1.0, 2.0
            double cvar = var.getCvar();
            if (Math.abs(cvar - 0.1) < 1e-3) {
                // System.out.println("Cvar is approximately: 0.1, Type: " + var.getType());
                directoryName = "./histogram/hist-0_1";
            } else if (Math.abs(cvar - 1.0) < 1e-3) {
                // System.out.println("Cvar is approximately: 1.0, Type: " + var.getType());
                directoryName = "./histogram/hist-1";
            } else if (Math.abs(cvar - 2.0) < 1e-3) {
                // System.out.println("Cvar is approximately: 2.0, Type: " + var.getType());
                directoryName = "./histogram/hist-2";
            } else {
                System.out.println("Cvar does not match expected values: " + cvar + ", Type: " + var.getType() + ". You need to change the code to handle this case (other cvars).");
                directoryName = "./histogram/hist-unknown-cvar";
            }
            histogram.csvReport(directoryName);
            histogram.reset();
        }
    }


    public double[] getCvars() {
        return cvars;
    }

    public void setCvars(double[] cvars) {
        this.cvars = cvars;
    }
}
