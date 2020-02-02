package it.polito.dp2.BIB.sol2;

import it.polito.dp2.BIB.ass2.CitationFinder;
import it.polito.dp2.BIB.ass2.CitationFinderException;

/**
 * Concrete Factory class for the solution of DP2 Lab 4, exercise 1 (AY 2019/20)
 *
 */
public class CitationFinderFactory extends it.polito.dp2.BIB.ass2.CitationFinderFactory {

	@Override
	public CitationFinder newCitationFinder() throws CitationFinderException {
		return new CitationFinderImpl();
	}

}
