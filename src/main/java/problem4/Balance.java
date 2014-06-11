package problem4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class Balance {

	/**
	 * A map that allows allocation of a balance value to a cashpoint (or vice
	 * versa to be precise).
	 * 
	 * We use the object reference as "internal id" and its getName() property
	 * as "print id".
	 */
	private HashMap<Cashpoint, Double>	balanceMap	= new HashMap<Cashpoint, Double>();
	/**
	 * This list is used to store all cashpoints in the order of their balance.
	 * Otherwise useless.
	 */
	private ArrayList<Cashpoint>		cpList		= new ArrayList<>();

	private final Comparator<Cashpoint>	comparator	= new CashComperator();

	/**
	 * Increase the cashpoint's associated balance by `value`. If the cash did
	 * not have an associated value before it will be added. This is a
	 * synchronized action.
	 */
	public synchronized void increase(Cashpoint cp, double value) {
		if (!balanceMap.containsKey(cp)) {
			balanceMap.put(cp, value);
			cpList.add(cp);
		} else
			balanceMap.put(cp, balanceMap.get(cp) + value);

		out("Added %.2f to balance of %s", value, cp.getName());

		updateList();
	}

	/**
	 * Sort the internal list. This is a synchronized action.
	 */
	private synchronized void updateList() {
		Collections.sort(cpList, comparator);
		StringBuilder sb = new StringBuilder();
		for (Cashpoint cp : cpList) {
			sb.append(String.format(" %s: %.02f;", cp.getName(), balanceMap.get(cp)));
		}
		out("Updated balance list:%s", sb);
	}

	/**
	 * Allows comparison of two cashpoints based on their associated balance.
	 */
	class CashComperator implements Comparator<Cashpoint> {

		@Override
		public int compare(Cashpoint o1, Cashpoint o2) {
			// We want the list to be descending, so multiply with -1
			return -1 * Double.compare(balanceMap.get(o1), balanceMap.get(o2));
		}

	}

	private static void out(String msg, Object... args) {
		System.out.format(msg + "%n", args);
	}
}
