package stsc.general.simulator.multistarter.grid;

import java.util.ArrayList;
import java.util.List;

import stsc.common.FromToPeriod;
import stsc.common.storage.StockStorage;
import stsc.general.simulator.multistarter.AlgorithmSettingsIteratorFactory;
import stsc.general.simulator.multistarter.BadParameterException;
import stsc.general.simulator.multistarter.MpString;

public class SimulatorSettingsGridFactory {

	private List<GridExecutionInitializer> stockInitializers = new ArrayList<>();
	private List<GridExecutionInitializer> eodInitializers = new ArrayList<>();

	private final StockStorage stockStorage;
	private final FromToPeriod period;
	private boolean finished;

	public SimulatorSettingsGridFactory(final StockStorage stockStorage, final FromToPeriod period) {
		this.stockStorage = stockStorage;
		this.period = period;
		this.finished = true;
	}

	// add sub-algorithms

	public SimulatorSettingsGridFactory addStock(String eName, String aName, AlgorithmSettingsGridIterator multiAlgorithmSettings) {
		synchronized (stockInitializers) {
			addInitializer(stockInitializers, new GridExecutionInitializer(eName, aName, multiAlgorithmSettings));
		}
		return this;
	}

	public SimulatorSettingsGridFactory addStock(String eName, String aName, AlgorithmSettingsIteratorFactory factory) {
		return addStock(eName, aName, factory.getGridIterator());
	}

	public SimulatorSettingsGridFactory addEod(String eName, String aName, AlgorithmSettingsGridIterator multiAlgorithmSettings) {
		synchronized (eodInitializers) {
			addInitializer(eodInitializers, new GridExecutionInitializer(eName, aName, multiAlgorithmSettings));
		}
		return this;
	}

	public SimulatorSettingsGridFactory addEod(String eName, String aName, AlgorithmSettingsIteratorFactory factory) {
		return addEod(eName, aName, factory.getGridIterator());
	}

	// add predefined algorithms

	public SimulatorSettingsGridFactory addStock(String eName, String aName, String pName, List<String> values) throws BadParameterException {
		final AlgorithmSettingsIteratorFactory algoFactory = new AlgorithmSettingsIteratorFactory(period);
		algoFactory.add(new MpString(pName, values));
		return addStock(eName, aName, algoFactory.getGridIterator());
	}

	public SimulatorSettingsGridFactory addEod(String eName, String aName, String pName, List<String> values) throws BadParameterException {
		final AlgorithmSettingsIteratorFactory algoFactory = new AlgorithmSettingsIteratorFactory(period);
		algoFactory.add(new MpString(pName, values));
		return addEod(eName, aName, algoFactory.getGridIterator());
	}

	private void addInitializer(List<GridExecutionInitializer> toList, GridExecutionInitializer ei) {
		if (ei.hasNext())
			finished = false;
		toList.add(ei);
	}

	public SimulatorSettingsGridList getList() {
		final SimulatorSettingsGridList result = new SimulatorSettingsGridList(stockStorage, period, stockInitializers, eodInitializers, finished);
		stockInitializers = new ArrayList<>();
		eodInitializers = new ArrayList<>();
		return result;
	}

	public SimulatorSettingsGridCopyList getCopyList() {
		final SimulatorSettingsGridCopyList result = new SimulatorSettingsGridCopyList(stockStorage, period, stockInitializers, eodInitializers, finished);
		stockInitializers = new ArrayList<>();
		eodInitializers = new ArrayList<>();
		return result;
	}

}
