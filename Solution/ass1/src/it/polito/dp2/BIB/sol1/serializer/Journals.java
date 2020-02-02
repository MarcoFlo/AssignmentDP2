package it.polito.dp2.BIB.sol1.serializer;

import java.util.Collection;
import java.util.HashMap;

import it.polito.dp2.BIB.sol1.jaxb.JournalType;

import java.math.BigInteger;

/**
 * Class to manage journal information
 * It stores journal information indexed by ISSN
 */
public class Journals {
	private HashMap<String,JournalType> journals;	// ordered map of journals with ISSN as key

	public Journals() {
		journals = new HashMap<String,JournalType>();
	}

	// retrieves information for one journal given its ISSN key
	public JournalType getJournal(String issn) {
		return journals.get(issn);
	}
	
	// retrieves the issue id of the issue with given number in journal with given ISSN
	public BigInteger getIssueId(String issn, int number) {
		JournalType j = journals.get(issn);
		if (j==null)
			return BigInteger.valueOf(-1);
		else {
			for (JournalType.Issue i :j.getIssue()) {
				if (i.getNumber().longValue()==number)
					return i.getId();
			}
			return BigInteger.valueOf(-1);
		}
	}
	
	// stores information for one journal
	public void putJournal(JournalType journal) {
		journals.put(journal.getISSN(), journal);
	}

	// gets journal information for all stored journals
	public Collection<? extends JournalType> getJournals() {
		return journals.values();
	}
}
