package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;

public class Exponential extends RandVar {
	private double lambda;

	public Exponential(RNG rng, double lambda) {
		super(rng);
		if (lambda <= 0) {
			throw new IllegalArgumentException("Lambda must be > 0 for Exponential distribution.");
		}
		this.lambda = lambda;
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
		if (Math.abs(m - s) > 1e-9) {
			// Since s is the root of the variance and var = 1/lam^2
			throw new IllegalArgumentException("For an exponential distribution, mean and standard deviation must be equal.");
		}
		this.lambda = 1.0 / m;
	}

	@Override
	public String getType() {
		return "Exponential";
	}

	@Override
	public String toString() {
		return String.format(
				"Exponential distribution with λ = %.4f\nMean = %.4f\nVariance = %.4f",
				lambda, getMean(), getVariance()
		);
	}
}
