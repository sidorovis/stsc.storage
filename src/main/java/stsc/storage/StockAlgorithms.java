package stsc.storage;

import java.util.ArrayList;
import java.util.HashMap;

import stsc.common.BadSignalException;
import stsc.common.Day;
import stsc.common.algorithms.StockAlgorithm;

/**
 * Internal for {@link ExecutionInstanceProcessor} / {@link StockNameToAlgorithms}
 * structure that store stock algorithms. <br/>
 * <u> Represents bunch of instances of algorithms (executions) for one
 * stock.</u><br/>
 * Provide possibility to populate itself with stock algorithms, automatically
 * sort execution name.<br/>
 * Also provide possibility to execute simulation for all stored algorithms in
 * the order how they was added to {@link StockAlgorithms}. IUt required to give
 * user possibility execute algorithms into order that he defined in
 * configuration file. <br/>
 */
final class StockAlgorithms {
	// execution name to stock algorithms
	private final HashMap<String, StockAlgorithm> map = new HashMap<>();
	private final ArrayList<StockAlgorithm> orderedAlgorithms = new ArrayList<>();

	StockAlgorithms() {
	}

	public HashMap<String, StockAlgorithm> getMap() {
		return map;
	}

	void add(final String executionName, final StockAlgorithm algo) {
		if (map.containsKey(executionName))
			return;
		orderedAlgorithms.add(algo);
		map.put(executionName, algo);
	}

	void simulate(final Day newDay) throws BadSignalException {
		for (StockAlgorithm algo : orderedAlgorithms) {
			algo.process(newDay);
		}
	}

	int size() {
		return orderedAlgorithms.size();
	}

}