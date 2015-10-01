package stsc.storage;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import stsc.common.BadSignalException;
import stsc.common.signals.SignalContainer;
import stsc.common.signals.SignalsSerie;
import stsc.common.signals.SerieSignal;
import stsc.common.storage.SignalsStorage;

/**
 * Storage for signals from all algorithms that was executed by one simulation.
 * Used for simulation, provide possibility to store and retrieve signals from
 * algorithms and by algorithms. (Also by management code afterwards for tests /
 * testing executions and etc.) <br/>
 * <u>Use case</u><br/>
 * 1. Register new serie for algorithm by: <br/>
 * 1.a. registerStockAlgorithmSerie(...); <br/>
 * 1.b. registerEodAlgorithmSerie(...). <br/>
 * This series represents (encapsulate mechanism that store signal serie
 * (array)). For example it could be ArrayList<SignalType>, Map<Date,
 * SignalType>. For more information you can look for {@link SignalsSerie}
 * class. <br/>
 * 2. add signals (register signals) from algorithms by: <br/>
 * 2.a. addStockSignal(...); <br/>
 * 2.b. addEodSignal(...). <br/>
 * 3. retrieve / get signals (additional information about signals for serie)
 * from storage using: <br/>
 * 3.a. getStockSignal(...); by date <br/>
 * 3.b. getStockSignal(...); by index <br/>
 * 3.c. getIndexSize(...) - last index value for stock execution <br/>
 * 3.d. getEodSignal(...); by date <br/>
 * 3.e. getEodSignal(...); by index <br/>
 * 3.f. getIndexSize(...) - last index value for end-of-day execution. <br/>
 * 
 * @mark Thread Safe for some reason :), probably should not because simulation
 *       is one-thread process.
 */
public final class SignalsStorageImpl implements SignalsStorage {

	final private HashMap<String, SignalsSerie<SerieSignal>> stockSignals = new HashMap<>();
	final private HashMap<String, SignalsSerie<SerieSignal>> eodSignals = new HashMap<>();

	public SignalsStorageImpl() {
	}

	@Override
	public void registerStockAlgorithmSerie(String stockName, String executionName, Optional<SignalsSerie<SerieSignal>> serie) {
		if (serie.isPresent()) {
			final String key = stockAlgorithmKey(stockName, executionName);
			synchronized (stockSignals) {
				stockSignals.put(key, serie.get());
			}
		}
	}

	@Override
	public void addStockSignal(final String stockName, final String executionName, final Date date, final SerieSignal signal) throws BadSignalException {
		final String key = stockAlgorithmKey(stockName, executionName);
		synchronized (stockSignals) {
			stockSignals.get(key).addSignal(date, signal);
		}
	}

	@Override
	public SignalContainer<? extends SerieSignal> getStockSignal(final String stockName, final String executionName, final Date date) {
		final String key = stockAlgorithmKey(stockName, executionName);
		SignalsSerie<SerieSignal> ess;
		synchronized (stockSignals) {
			ess = stockSignals.get(key);
		}
		if (ess != null) {
			final SignalContainer<? extends SerieSignal> result = ess.getSignal(date);
			if (result != null) {
				return result;
			}
		}
		return SignalContainer.empty(date);
	}

	@Override
	public SignalContainer<? extends SerieSignal> getStockSignal(final String stockName, final String executionName, final int index) {
		final String key = stockAlgorithmKey(stockName, executionName);
		SignalsSerie<SerieSignal> ess;
		synchronized (stockSignals) {
			ess = stockSignals.get(key);
		}
		if (ess != null) {
			final SignalContainer<? extends SerieSignal> result = ess.getSignal(index);
			if (result != null) {
				return result;
			}
		}
		return SignalContainer.empty(index);
	}

	@Override
	public int getIndexSize(String stockName, String executionName) {
		final String key = stockAlgorithmKey(stockName, executionName);
		SignalsSerie<SerieSignal> ess;
		synchronized (stockSignals) {
			ess = stockSignals.get(key);
		}
		if (ess != null)
			return ess.size();
		return 0;
	}

	private String stockAlgorithmKey(String stockName, String executionName) {
		return stockName + "#" + executionName;
	}

	// EOD

	@Override
	public void registerEodAlgorithmSerie(String executionName, SignalsSerie<SerieSignal> serie) {
		synchronized (eodSignals) {
			eodSignals.put(executionName, serie);
		}
	}

	@Override
	public void addEodSignal(final String executionName, final Date date, SerieSignal signal) throws BadSignalException {
		synchronized (eodSignals) {
			final SignalsSerie<SerieSignal> s = eodSignals.get(executionName);
			if (s != null)
				eodSignals.get(executionName).addSignal(date, signal);
			else
				throw new BadSignalException("No such execution '" + executionName + "'");
		}
	}

	@Override
	public SignalContainer<? extends SerieSignal> getEodSignal(final String executionName, final Date date) {
		SignalsSerie<SerieSignal> ess = null;
		synchronized (eodSignals) {
			ess = eodSignals.get(executionName);
		}
		if (ess != null)
			return ess.getSignal(date);
		return SignalContainer.empty(date);
	}

	@Override
	public SignalContainer<? extends SerieSignal> getEodSignal(final String executionName, final int index) {
		SignalsSerie<SerieSignal> ess = null;
		synchronized (eodSignals) {
			ess = eodSignals.get(executionName);
		}
		if (ess != null)
			return ess.getSignal(index);
		return SignalContainer.empty(index);
	}

	@Override
	public int getIndexSize(final String executionName) {
		SignalsSerie<SerieSignal> ess = null;
		synchronized (eodSignals) {
			ess = eodSignals.get(executionName);
		}
		if (ess != null)
			return ess.size();
		return 0;
	}

}
