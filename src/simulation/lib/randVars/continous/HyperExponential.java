package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;
import static java.lang.Math.*;

/*
 * TODO Problem 3.1.2 - implement this class (section 3.2.4 in course syllabus)
 * !!! If an abstract class method does not make sense to be implemented in a particular RandVar class,
 * an UnsupportedOperationException should be thrown !!!
 *
 * Hyperexponential distributed random variable.
 */
public class HyperExponential extends RandVar {
	private double p1;
	private double p2;
	private double mean;
	private double cvar;
	private double lambda1;
	private double lambda2;

	public HyperExponential(RNG rng, double p1, double p2, double mean, double cvar) {
		super(rng);
		this.p1 = p1;
		this.p2 = p2;
		this.mean = mean;
		this.cvar = cvar;
		calculateLambdas();
	}

	@Override
	public double getRV() {
		double u1 = rng.rnd();
		double u2 = rng.rnd();
		double lambda = this.p1 >= u1 ? lambda1 : lambda2;
		double x = - (Math.log(u2) / lambda);
		return x;
	}

	@Override
	public double getMean() {
		return (p1/lambda1) + (p2/lambda2);
	}


	@Override
	public double getVariance() {
		double mean = getMean();
		// Calculation of second moment taken from https://en.wikipedia.org/wiki/Exponential_distribution
		// E[X^n] = n!/lambda^n
		double secondMoment = (2 * p1) / (lambda1 * lambda1) + (2 * p2) / (lambda2 * lambda2);
		// (Chapter 1.3.4, Equation 2.21)
		return secondMoment - mean * mean;
	}

	@Override
	public void setMean(double m) {
		this.mean = m;
		calculateLambdas();

	}

	@Override
	public void setStdDeviation(double s) {
		throw new UnsupportedOperationException("Not supported on HyperExponential");

	}

	@Override
	// TODO: This should work, but too lazy rn fix it later.
	public void setMeanAndStdDeviation(double m, double s) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getType() {
		return "HyperExponential(H2)";
	}


	@Override
	public String toString() {
		return String.format(
				"HyperExponential (H2) Distribution\n" +
						"p1: %.4f, p2: %.4f\n" +
						"λ1: %.4f, λ2: %.4f\n" +
						"Mean: %.4f, Variance: %.4f, CV: %.4f",
				p1, p2, lambda1, lambda2, getMean(), getVariance(), cvar
		);
	}

	private void calculateLambdas() {

		double cvarSquared = Math.pow(cvar, 2);
		double sqrtTerm = Math.sqrt((cvarSquared - 1) / (cvarSquared + 1));
		double factor = 1.0 / mean;

		this.lambda1 = factor * (1 + sqrtTerm);
		this.lambda2 = factor * (1 - sqrtTerm);
	}

}
