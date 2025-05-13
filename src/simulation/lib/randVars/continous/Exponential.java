package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;

public class Exponential extends RandVar {
	private double lambda;

	public Exponential(double lambda, RNG rng) {
		super(rng);
		if (lambda <= 0) {
			throw new IllegalArgumentException("Lambda must be > 0 for Exponential distribution.");
		}
		this.lambda = lambda;
	}

	public Exponential(RNG rng) {
		super(rng);
		this.lambda = 1.0;
	}

	public Exponential(RNG rng, double mean) {
		super(rng);
		setMean(mean);
	}

	public Exponential(RNG rng, double mean, double cvar) {
		super(rng);
		if (cvar != 1.0) {
			throw new UnsupportedOperationException("Cvar must be 1.0 for Exponential distribution.");
		}
		setMean(mean);
	}

	public Exponential(double mean, double sdev, RNG rng) {
		super(rng);
		setMeanAndStdDeviation(mean, sdev);
	}

	@Override
	public double getRV() {
		double u = rng.rnd();
		return -Math.log(u) / lambda;
	}

	@Override
	public double getMean() {
		return 1.0 / lambda;
	}

	@Override
	public double getVariance() {
		return 1.0 / (lambda * lambda);
	}

	@Override
	public void setMean(double m) {
		if (m <= 0) {
			throw new IllegalArgumentException("Mean must be > 0 for Exponential distribution.");
		}
		this.lambda = 1.0 / m;
	}

	@Override
	public void setStdDeviation(double s) {
		if (s <= 0) {
			throw new IllegalArgumentException("Standard deviation must be > 0 for Exponential distribution.");
		}
		this.lambda = 1.0 / s;
	}

	@Override
	public void setMeanAndStdDeviation(double m, double s) {
		if (m <= 0 || s <= 0) {
			throw new IllegalArgumentException("Mean and standard deviation must be > 0 for Exponential distribution.");
		}
		if (m != s)
			throw new UnsupportedOperationException("Mean must be equal to standard deviation for exponential distribution.");
			
		this.lambda = 1.0 / m;
	}

	@Override
	public String getType() {
		return "Exponential distribution";
	}

	@Override
	public String toString() {
		return super.toString() + 
			String.format("\tparameters:\n\t\tlambda: %.4f\n", this.lambda);
	}
}
