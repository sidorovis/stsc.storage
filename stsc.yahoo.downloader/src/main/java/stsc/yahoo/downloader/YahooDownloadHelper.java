package stsc.yahoo.downloader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;

import stsc.common.stocks.UnitedFormatStock;
import stsc.yahoo.YahooSettings;

import com.google.common.io.CharStreams;

public class YahooDownloadHelper {

	private static final int waitTriesAmount = 5;
	private static final int waitTimeBetweenTries = 500;

	public static final UnitedFormatStock download(YahooSettings settings, String stockName) throws InterruptedException {
		int tries = 0;
		String error = "";
		UnitedFormatStock newStock = null;
		while (tries < waitTriesAmount) {
			try {
				URL url = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + stockName);
				String stockContent = CharStreams.toString(new InputStreamReader(url.openStream()));
				newStock = UnitedFormatStock.newFromString(stockName, stockContent);
				if (newStock.getDays().isEmpty())
					return null;
				newStock.storeUniteFormat(getPath(settings.getDataFolder(), newStock.getName()));
				return newStock;
			} catch (ParseException | IOException e) {
				error = e.toString();
			}
			tries += 1;
			Thread.sleep(waitTimeBetweenTries);
		}
		if (newStock == null)
			throw new InterruptedException(waitTriesAmount + " tries not enought to download data on " + stockName + " stock. " + error);
		return newStock;
	}

	public static final boolean partiallyDownload(YahooSettings settings, UnitedFormatStock stock, String stockName)
			throws InterruptedException {
		String downloadLink = stock.generatePartiallyDownloadLine();
		if (downloadLink.isEmpty()) {
			return false;
		}
		String error = "";
		String stockNewContent = "";
		int tries = 0;

		while (tries < waitTriesAmount) {
			try {
				URL url = new URL(downloadLink);
				stockNewContent = CharStreams.toString(new InputStreamReader(url.openStream()));
				boolean newDays = stock.addDaysFromString(stockNewContent);
				if (newDays)
					stock.storeUniteFormat(getPath(settings.getDataFolder(), stock.getName()));
				return true;
			} catch (ParseException e) {
				error = "exception " + e.toString() + " with: '" + stockNewContent + "'";
			} catch (IOException e) {
			}
			tries += 1;
			Thread.sleep(waitTimeBetweenTries);
		}
		throw new InterruptedException("" + waitTriesAmount + " tries not enought to partially download data on " + downloadLink
				+ " stock " + error);
	}

	public static String getPath(String folder, String taskName) {
		return UnitedFormatStock.generatePath(folder, taskName);
	}
}