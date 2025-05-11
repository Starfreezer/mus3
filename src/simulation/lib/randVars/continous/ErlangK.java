package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;

public class ErlangK extends RandVar {

	private final int k;
	private double lambda;

	public ErlangK(RNG rng, int k, double lambda) {
		super(rng);
		if (k <= 0) {
			throw new IllegalArgumentException("k must be > 0 for ErlangK distribution.");
		}
		if (lambda <= 0) {
			throw new IllegalArgumentException("Lambda must be > 0 for ErlangK distribution.");
		}
		this.k = k;
		this.lambda = lambda;
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
		return k / lambda;
	}

	@Override
	public double getVariance() {
		return k / (lambda * lambda);
	}

	@Override
	public void setMean(double m) {
		if (m <= 0) {
			throw new IllegalArgumentException("Mean must be > 0.");
		}
		this.lambda = k / m;
	}

	@Override
	public void setStdDeviation(double s) {
		if (s <= 0) {
			throw new IllegalArgumentException("Standard deviation must be > 0.");
		}
		this.lambda = Math.sqrt(k) / s;
	}

	@Override
	public void setMeanAndStdDeviation(double m, double s) {
		if (m <= 0 || s <= 0) {
			throw new IllegalArgumentException("Mean and standard deviation must be > 0.");
		}
		double expectedMean = s * Math.sqrt(k);
		if (Math.abs(expectedMean - m) > 1e-9) {
			throw new IllegalArgumentException("In ErlangK, mean must equal std deviation * sqrt(k).");
		}
		this.lambda = k / m;
	}

	@Override
	public String getType() {
		return "ErlangK";
	}

	@Override
	public String toString() {
		return String.format(
				"ErlangK distribution with parameters k = %d, λ = %.4f\nMean = %.4f, Variance = %.4f",
				k, lambda, getMean(), getVariance()
		);
	}
}
