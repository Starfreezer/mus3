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

/**
 * I don't see why setStdDeviation and setMean could not be implemented. Assuming we are always dealing with an H2 dist.
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
		if (p1 < 0 || p2 < 0 || p1 > 1 || p2 > 1) {
			throw new IllegalArgumentException("Probabilities (p1, p2) must be >= 0 and <= 1 for hyperexponential distribution.");
		}

    	if (Math.abs(p1 + p2 - 1.0) > 1e-9) {
        	throw new IllegalArgumentException("Probabilities (p1, p2) must sum to 1 for hyperexponential distribution.");
   		}

		// If cvar is 1, then the distribution reduces to a standard exponential, see calculateLambdas()
		if (cvar < 1) {
			throw new IllegalArgumentException("Coefficient of variation must be > 1 for hyperexponential distribution.");
		}

		if (mean <= 0) {
			throw new IllegalArgumentException("Mean must be > 0 for hyperexponential distribution.");
		}


		this.p1 = p1;
		this.p2 = p2;
		this.mean = mean;
		this.cvar = cvar;
		calculateLambdas();
	}

	public HyperExponential(RNG rng, double mean, double cvar) {
		super(rng);
		// If cvar is 1, then the distribution reduces to a standard exponential, see calculateLambdas()
		if (cvar < 1) {
			throw new IllegalArgumentException("Coefficient of variation must be >= 1 for hyperexponential distribution.");
		}
		if (mean <= 0) {
			throw new IllegalArgumentException("Mean must be > 0 for hyperexponential distribution.");
		}

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
		if (m <= 0) {
			throw new IllegalArgumentException("Mean must be > 0 for hyperexponential distribution.");
		}
		this.mean = m;
		calculateLambdas();
	}

	@Override
	public void setStdDeviation(double s) {
		throw new UnsupportedOperationException();
	}


	@Override
	public void setMeanAndStdDeviation(double m, double s) {
		throw new UnsupportedOperationException();
	}


	@Override
	public String getType() {
		return "Hyperexponential distribution (H2)";
	}


	@Override
	public String toString() {
		return super.toString() +
			String.format("\tparameters:\n\t\tp1: %.4f\n\t\tp2: %.4f\n\t\tlambda1: %.4f\n\t\tlambda2: %.4f\n",
					  this.p1, this.p2, this.lambda1, this.lambda2);
	}

	private void calculateLambdas() {

		// If CV = 1, then the distribution reduces to a standard exponential:
		// The variance equals the square of the mean, so lambda1 = lambda2 = 1/mean.
		// Any mixture of identical exoponential is just a single exponential.
		// cvar == 1.0
		if (Math.abs(cvar - 1.0) < 1e-6) {
			this.lambda1 = 1.0 / mean;
			this.lambda2 = this.lambda1;
			this.p1 = 0.5;
			this.p2 = 0.5;
			return;
		}

		double cvarSquared = Math.pow(cvar, 2);


		double sqrtTerm = Math.sqrt((cvarSquared - 1) / (cvarSquared + 1));
		double factor = 1.0 / mean;

		this.lambda1 = factor * (1 + sqrtTerm);
		this.lambda2 = factor * (1 - sqrtTerm);

		/**
		 * See PDF for derivation
		 */
		double p1 = (lambda1 * (mean*lambda2 - 1)) / (lambda2 - lambda1);
		double p2 = 1 - p1;

		this.p1 = p1;
		this.p2 = p2;
	}

}
