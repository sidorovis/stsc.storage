package stsc.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import stsc.common.BadSignalException;
import stsc.common.Day;
import stsc.common.algorithms.BadAlgorithmException;
import stsc.common.algorithms.EodAlgorithm;
import stsc.common.algorithms.EodExecutionInstance;
import stsc.common.algorithms.StockAlgorithm;
import stsc.common.algorithms.StockExecutionInstance;
import stsc.common.storage.SignalsStorage;
import stsc.common.trading.Broker;

/**
 * Store executions into necessary (execution) order.<br/>
 * This class provide possibility to start selected executions (algorithm
 * instances) in the necessary order, so each execution will be able to use data
 * from depended executions.
 */
public final class ExecutionInstanceProcessor {

	private final SignalsStorage signalsStorage = new SignalsStorageImpl();

	private final StockNameToAlgorithms stockAlgorithms = new StockNameToAlgorithms();
	private final Map<String, EodAlgorithm> tradeNameToAlgorithms = new HashMap<>();
	private final List<EodAlgorithm> tradeAlgorithms = new ArrayList<>();

	private final String hashCode;

	ExecutionInstanceProcessor(Broker broker, Set<String> stockNames, List<StockExecutionInstance> stockExecutions, List<EodExecutionInstance> eodExecutions) throws BadAlgorithmException {
		for (StockExecutionInstance execution : stockExecutions) {
			for (String stockName : stockNames) {
				final StockAlgorithm algo = execution.getInstance(stockName, signalsStorage);
				stockAlgorithms.addExecutionOnStock(stockName, execution.getExecutionName(), algo);
			}
		}
		for (EodExecutionInstance execution : eodExecutions) {
			final EodAlgorithm algo = execution.getInstance(broker, signalsStorage);
			tradeAlgorithms.add(algo);
			tradeNameToAlgorithms.put(execution.getExecutionName(), algo);
		}
		this.hashCode = generateHashCode(stockExecutions, eodExecutions);
	}

	public void runStockAlgorithms(final String stockName, final Day stockDay) throws BadSignalException {
		stockAlgorithms.simulate(stockName, stockDay);
	}

	public void runEodAlgorithms(final Date today, final HashMap<String, Day> datafeed) throws BadSignalException {
		for (EodAlgorithm i : tradeAlgorithms) {
			i.process(today, datafeed);
		}
	}

	public int getEodAlgorithmsSize() {
		return tradeAlgorithms.size();
	}

	public EodAlgorithm getEodAlgorithm(final String key) {
		return tradeNameToAlgorithms.get(key);
	}

	public int getStockAlgorithmsSize() {
		if (stockAlgorithms.getStockToAlgorithm().isEmpty())
			return 0;
		else
			return stockAlgorithms.getStockToAlgorithm().entrySet().iterator().next().getValue().size();
	}

	public Optional<StockAlgorithm> getStockAlgorithm(final String executionName, final String stockName) {
		final StockAlgorithms sa = stockAlgorithms.getStockToAlgorithm().get(stockName);
		if (sa != null)
			return Optional.ofNullable(sa.getMap().get(executionName));
		return Optional.empty();
	}

	public SignalsStorage getSignalsStorage() {
		return signalsStorage;
	}

	@Override
	public String toString() {
		return "Stocks: " + Integer.toString(stockAlgorithms.size()) + " EodAlgos: " + Integer.toString(tradeAlgorithms.size()) + " StockAlgos:"
				+ Integer.toString(stockAlgorithms.size());
	}

	private String generateHashCode(List<StockExecutionInstance> stockExecutions, List<EodExecutionInstance> eodExecutions) {
		final StringBuilder sb = new StringBuilder();
		for (StockExecutionInstance se : stockExecutions) {
			se.stringHashCode(sb);
		}
		for (EodExecutionInstance ee : eodExecutions) {
			ee.stringHashCode(sb);
		}
		return sb.toString();
	}

	public String stringHashCode() {
		return hashCode;
	}

}
