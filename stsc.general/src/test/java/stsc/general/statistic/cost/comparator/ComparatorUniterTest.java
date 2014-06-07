package stsc.general.statistic.cost.comparator;

import org.joda.time.LocalDate;

import stsc.general.statistic.Statistics;
import stsc.general.testhelper.TestHelper;
import junit.framework.TestCase;

public class ComparatorUniterTest extends TestCase {
	public void testComparatorUniter() {
		final ComparatorUniter cu = new ComparatorUniter();
		cu.addComparator(new WeightedSumComparator(), 0.5);
		cu.addComparator(new WeightedSumComparator().addParameter("getWinProb", 0.6), 0.8);

		for (int i = 1; i < 6; ++i) {
			final Statistics leftStat = TestHelper.getStatistics(50, 150, new LocalDate(2013, 5, i));
			for (int u = i + 20; u < 25; ++u) {
				if (i != u) {
					final Statistics rightStat = TestHelper.getStatistics(50, 150, new LocalDate(2013, 5, u));
					final int r = cu.compare(leftStat, rightStat) * cu.compare(rightStat, leftStat);
					if (r != 0)
						assertEquals(-1, r);
				}
			}
		}

	}
}
