package stsc.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import stsc.algorithms.EodOutput;
import stsc.algorithms.Output;
import stsc.common.algorithms.AlgorithmNameGenerator;
import stsc.common.algorithms.BadAlgorithmException;
import stsc.common.algorithms.EodExecutionInstance;
import stsc.common.algorithms.MutableAlgorithmConfiguration;
import stsc.common.algorithms.StockExecutionInstance;
import stsc.common.trading.Broker;

/**
 * Storage for list of stock executions and list of end-of-day executions.<br/>
 * Used for simulation initialization process.
 */
public final class ExecutionInstancesStorage implements Cloneable {

	final private List<StockExecutionInstance> stockExecutions;
	final private List<EodExecutionInstance> eodExecutions;

	public ExecutionInstancesStorage() {
		this.stockExecutions = new ArrayList<>();
		this.eodExecutions = new ArrayList<>();
	}

	private ExecutionInstancesStorage(final ExecutionInstancesStorage cloneFrom) {
		this.stockExecutions = new ArrayList<>(cloneFrom.stockExecutions.size());
		for (StockExecutionInstance se : cloneFrom.stockExecutions) {
			this.stockExecutions.add(se.clone());
		}
		this.eodExecutions = new ArrayList<>(cloneFrom.eodExecutions.size());
		for (EodExecutionInstance ee : cloneFrom.eodExecutions) {
			this.eodExecutions.add(ee.clone());
		}
	}

	public void addStockExecution(StockExecutionInstance execution) {
		stockExecutions.add(execution);
	}

	public void addEodExecution(EodExecutionInstance execution) {
		eodExecutions.add(execution);
	}

	public ExecutionInstanceProcessor initialize(final Broker broker, final Set<String> stockNames) throws BadAlgorithmException {
		return new ExecutionInstanceProcessor(broker, stockNames, stockExecutions, eodExecutions);
	}

	public String stringHashCode() {
		final StringBuilder sb = new StringBuilder();
		for (StockExecutionInstance se : stockExecutions) {
			se.stringHashCode(sb);
		}
		for (EodExecutionInstance ee : eodExecutions) {
			ee.stringHashCode(sb);
		}
		return sb.toString();
	}

	public List<StockExecutionInstance> getStockExecutions() {
		return stockExecutions;
	}

	public List<EodExecutionInstance> getEodExecutions() {
		return eodExecutions;
	}

	@Override
	public ExecutionInstancesStorage clone() {
		return new ExecutionInstancesStorage(this);
	}

	@Override
	public String toString() {
		String result = "";
		result += "StockExecutions = ";
		for (StockExecutionInstance se : stockExecutions) {
			result += se.getExecutionName();
			if (se != stockExecutions.get(stockExecutions.size() - 1))
				result += ", ";
		}
		result += "\n";
		for (StockExecutionInstance se : stockExecutions) {
			result += se.toString() + "\n";
		}
		result += "EodExecutions = ";
		for (EodExecutionInstance ee : eodExecutions) {
			result += ee.getExecutionName();
			if (ee != eodExecutions.get(eodExecutions.size() - 1))
				result += ", ";
		}
		result += "\n";
		for (EodExecutionInstance se : eodExecutions) {
			result += se.toString() + "\n";
		}
		return result;
	}

	/**
	 * !!! Careful this method could / should be called once.<br/>
	 * Generate {@link Output} executions for all existed stock executions. Add generated executions to common stock executions list.
	 * 
	 * @return list of newly generated execution names.
	 */
	public List<String> generateOutForStocks() {
		final ArrayList<String> names = new ArrayList<>();
		final ArrayList<StockExecutionInstance> initialList = new ArrayList<>(getStockExecutions());
		for (StockExecutionInstance stockExecution : initialList) {
			final MutableAlgorithmConfiguration as = stockExecution.getSettings().createAlgorithmConfiguration();
			final String executionName = stockExecution.getExecutionName();
			as.addSubExecutionName(executionName);
			names.add(executionName);
			stockExecutions.add(new StockExecutionInstance(AlgorithmNameGenerator.generateOutAlgorithmName(executionName), Output.class, as));
		}
		return names;
	}

	/**
	 * !!! Careful this method could / should be called once.<br/>
	 * Generate {@link Output} executions for all existed eod executions. Add generated executions to common eod executions list.
	 * 
	 * @return list of newly generated execution names.
	 */
	public List<String> generateOutForEods() {
		final ArrayList<String> names = new ArrayList<>();
		final ArrayList<EodExecutionInstance> initialList = new ArrayList<>(getEodExecutions());
		for (EodExecutionInstance eodExecution : initialList) {
			final MutableAlgorithmConfiguration as = eodExecution.getSettings().createAlgorithmConfiguration();
			final String executionName = eodExecution.getExecutionName();
			as.addSubExecutionName(executionName);
			names.add(executionName);
			eodExecutions.add(new EodExecutionInstance(AlgorithmNameGenerator.generateOutAlgorithmName(executionName), EodOutput.class, as));
		}
		return names;
	}

}
