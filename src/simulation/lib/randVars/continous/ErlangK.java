package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;

public class ErlangK extends RandVar {

	private int k;
	private double lambda;

	public ErlangK(RNG rng, int k, double lambda) {
		super(rng);
		if (k <= 0) throw new IllegalArgumentException("k must be > 0 for ErlangK distribution.");
		if (lambda <= 0) throw new IllegalArgumentException("Lambda must be > 0 for ErlangK distribution.");
		this.k = k;
		this.lambda = lambda;
	}

	public ErlangK(RNG rng) {
		super(rng);
		this.k = 2;
		this.lambda = 2.0;
	}


	public ErlangK(RNG rng, double mean, double cvar) {
		super(rng);
		setMeanAndCvar(mean, cvar);
	}

	@Override
	public double getRV() {
		double product = 1.0;
		for (int i = 0; i < k; i++) {
			product *= rng.rnd();
		}
		return -Math.log(product) / lambda;
	}

	@Override
	public double getMean() {
		return (double) k / lambda;
	}

	@Override
	public double getVariance() {
		return (double) k / (lambda * lambda);
	}

	@Override
	public void setMean(double m) {
		if (m <= 0) {
			throw new IllegalArgumentException("Mean must be > 0 for ErlangK distribution.");
		}
		this.lambda = this.k / m;
	}

	@Override
	public void setStdDeviation(double s) {
		if (s <= 0) {
			throw new IllegalArgumentException("Standard deviation must be > 0 for ErlangK distribution.");
		}
		this.lambda = Math.sqrt(this.k) / s;
	}

	public void setMeanAndCvar(double mean, double cvar) {
		if (mean <= 0 || cvar <= 0 || cvar > 1)
			throw new IllegalArgumentException("Mean must be > 0 and Coefficient of variation must be <= 1 and > 0 for ErlangK distribution.");

		// For Erlang-k: CV = 1 / sqrt(k)
		int optimalK = (int) Math.ceil(1.0 / (cvar * cvar));
		this.k = optimalK;
		this.lambda = (double) k / mean;
	}

	@Override
	public void setMeanAndStdDeviation(double m, double s) {
		if (m <= 0 || s <= 0) {
			throw new IllegalArgumentException("Mean and standard deviation must be > 0 for ErlangK distribution.");
		}
		double cvar = s / m;
		// If cvar is 1, then the distribution reduces to a standard exponential
		if (cvar > 1 || cvar <= 0) {
			throw new IllegalArgumentException("Coefficient of variation must be <= 1 and > 0 for ErlangK distribution.");
		}
		setMeanAndCvar(m, cvar);
	}

	@Override
	public String getType() {
		return "Erlang-k distribution (Erlang-"+ k +")";
	}

	@Override
	public String toString() {
		return super.toString() +
			String.format("\tparameters:\n\t\tlambda: %.4f\n\t\tk: %.4f\n", this.lambda, this.k);
	}
}
