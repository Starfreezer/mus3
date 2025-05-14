package study;

import simulation.lib.counter.DiscreteConfidenceCounter;
import simulation.lib.counter.DiscreteCounter;
import simulation.lib.randVars.continous.Exponential;
import simulation.lib.randVars.continous.HyperExponential;
import simulation.lib.randVars.continous.Normal;
import simulation.lib.rng.StdRNG;

import java.util.Dictionary;
import java.util.Hashtable;

/*
 * TODO Problem 3.2.3 and 3.2.4 - implement this class
 */
public class DCCTest {

    public static void main(String[] args) {
        testDCC();
    }

    public static void testDCC() {
        RandVarTest test = new RandVarTest();
        test.runTest();
        // studentTest();
    }

    private static void studentTest() {
        double mean = 10.0;
        double alpha = 0.05;
        double[] cvars = {0.25, 0.5, 1.0, 2.0, 4.0};
        int[] nSamples = {5, 10, 50, 100};


        StdRNG rng = new StdRNG();
        int nExperiments = 500;
        int skipIter = 0;


        for (double cvar : cvars) {
            skipIter = 0;
            try {
                Dictionary<Integer, Integer> dict = new Hashtable<Integer, Integer>();
                for (int sample : nSamples) {
                    dict.put(sample, 0);
                }

                /**
                 * Change randvar here to appropriate dist
                 * Too lazy to extract function
                 */
                Normal randVar = new Normal(rng);
                randVar.setMeanAndCvar(mean, cvar);

                for (int i = 0; i < nExperiments; i++) {
                    for (int nSample : nSamples) {
                        DiscreteConfidenceCounter discreteConfidenceCounter = new DiscreteConfidenceCounter("Var", alpha);
                        for (int j = 0; j < nSample; j++) {
                            discreteConfidenceCounter.count(randVar.getRV());
                        }
                        if (mean >= discreteConfidenceCounter.getLowerBound() && mean <= discreteConfidenceCounter.getUpperBound()) {
                            dict.put(nSample, dict.get(nSample) + 1);
                        }

                    }
                }
                System.out.println(nExperiments + " experiments. With cvar " + cvar + " and alpha " + alpha);
                System.out.println(dict);
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
                // Lord forgive me for this hack i am lazy
                skipIter = 1;
            }
        }

        if (skipIter != 1) {
            alpha = 0.10;
            for (double variance : cvars) {
                Dictionary<Integer, Integer> dict = new Hashtable<Integer, Integer>();
                for (int sample : nSamples) {
                    dict.put(sample, 0);
                }

                Normal normal = new Normal(rng, mean, variance);

                for (int i = 0; i < nExperiments; i++) {
                    for (int nSample : nSamples) {
                        DiscreteConfidenceCounter discreteConfidenceCounter = new DiscreteConfidenceCounter("Var", alpha);
                        for (int j = 0; j < nSample; j++) {
                            discreteConfidenceCounter.count(normal.getRV());
                        }
                        if (mean >= discreteConfidenceCounter.getLowerBound() && mean <= discreteConfidenceCounter.getUpperBound()) {
                            dict.put(nSample, dict.get(nSample) + 1);
                        }

                    }
                }
                System.out.println(nExperiments + " experiments. With cvar " + variance + " and alpha " + alpha);
                System.out.println(dict);
                System.out.println();
            }


        }
    }

}
