package simulation.lib.counter;

/**
 * This class implements a discrete time confidence counter
 */
public class DiscreteConfidenceCounter extends DiscreteCounter {

    private double alpha;
    private String type;

    public DiscreteConfidenceCounter(String variable) {
        super(variable);
        this.type = "DiscreteConfidenceCounter";
        this.alpha = 0.05;
    }

    public DiscreteConfidenceCounter(String variable, double alpha) {
        super(variable);
        this.type = "DiscreteConfidenceCounter";
        this.alpha = alpha;
    }

    public DiscreteConfidenceCounter(String variable, double alpha, String type) {
        super(variable);
        this.type = type;
        this.alpha = alpha;
    }

    public double getT(long degsOfFreedom) {
        int row = getRow();
        double[] dof = tAlphaMatrix[0];
        double[] quantiles = tAlphaMatrix[row];

        // Exact match
        for (int i = 0; i < dof.length; i++) {
            if (dof[i] == degsOfFreedom) {
                return quantiles[i];
            }
        }

        // Below minimum
        if (degsOfFreedom < dof[0]) {
            return quantiles[0];
        }

        // Above maximum
        if (degsOfFreedom > dof[dof.length - 1]) {
            return quantiles[quantiles.length - 1];
        }

        // Interpolation
        for (int i = 1; i < dof.length; i++) {
            if (degsOfFreedom < dof[i]) {
                return linearInterpolate(
                        dof[i - 1], dof[i],
                        quantiles[i - 1], quantiles[i],
                        degsOfFreedom
                );
            }
        }

        // Fallback
        return quantiles[quantiles.length - 1];
    }


    private double linearInterpolate(double dflow, double dfhigh, double tlow, double thigh, double degsOfFreedom) {
        return tlow + (thigh - tlow) * ((degsOfFreedom - dflow) / (dfhigh - dflow));
    }

    private int getRow() {
        if (alpha <= 0.011) {
            return 1;
        } else if (alpha <= 0.051) {
            return 2;
        } else {
            return 3;
        }
    }

    /*  tAlphaMatrix:
     *  Row 0: degrees of freedom
     *  Row 1: alpha 0.01
     *  Row 2: alpha 0.05
     *  Row 3: alpha 0.10
     */
    private double tAlphaMatrix[][] = new double[][]{
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 1000000},
            {63.657, 9.925, 5.841, 4.604, 4.032, 3.707, 3.499, 3.355, 3.250, 3.169, 2.845, 2.750, 2.704, 2.678, 2.660, 2.648, 2.639, 2.632, 2.626, 2.576},
            {12.706, 4.303, 3.182, 2.776, 2.571, 2.447, 2.365, 2.306, 2.262, 2.228, 2.086, 2.042, 2.021, 2.009, 2.000, 1.994, 1.990, 1.987, 1.984, 1.960},
            {6.314, 2.920, 2.353, 2.132, 2.015, 1.943, 1.895, 1.860, 1.833, 1.812, 1.725, 1.697, 1.684, 1.676, 1.671, 1.667, 1.664, 1.662, 1.660, 1.645}
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLowerBound() {
        return getMean() - getT(getNumSamples() - 1) * Math.sqrt(getVariance() / getNumSamples());
    }

    public double getUpperBound() {
        return getMean() + getT(getNumSamples() - 1) * Math.sqrt(getVariance() / getNumSamples());
    }

    /**
     * @see Counter#report()
     */
    @Override
    public String report() {
        String out = super.report();
        out += ("" + "\talpha = " + alpha + "\n" +
                "\tt(1-alpha/2) = " + getT(getNumSamples() - 1) + "\n" +
                "\tlower bound = " + getLowerBound() + "\n" +
                "\tupper bound = " + getUpperBound());
        return out;
    }

    /**
     * @see Counter#csvReport(String)
     */
    @Override
    public void csvReport(String outputdir) {
        String content = observedVariable + ";" + getNumSamples() + ";" + getMean() + ";" + getVariance() + ";" + getStdDeviation() + ";" +
                getCvar() + ";" + getMin() + ";" + getMax() + ";" + alpha + ";" + getT(getNumSamples() - 1) + ";" + getLowerBound() + ";" +
                getUpperBound() + "\n";
        String labels = "#counter ; numSamples ; MEAN; VAR; STD; CVAR; MIN; MAX;alpha;t(1-alpha/2);lowerBound;upperBound\n";
        writeCsv(outputdir, content, labels);
    }
}
