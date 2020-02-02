package it.polito.dp2.BIB.sol1.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.BiblioType;
import it.polito.dp2.BIB.sol1.jaxb.JournalType;

/**
 * Class that manages the storage of journal information and enables journal searching.
 * A constructor is offered to initialize the object with the information contained
 * in a BiblioType object.
 *
 */
public class Journals {
	private HashMap<String,JournalImpl> journals;	// map of journals with ISSN as key

	public Journals(BiblioType biblio) {
		journals = new HashMap<String,JournalImpl>();
		for (JournalType j : biblio.getJournals().getJournal()) {
			journals.put(j.getISSN(), new JournalImpl(j));
		}
	}

	/**
	 * Retrieve the information about the journals that contain a given keyword in their title
	 * @param keyword the keyword to be searched or null to get all journals
	 * @return a set of JournalReader objects that provide information about the found journals
	 */
	public Set<JournalReader> getJournals(String keyword) {
		Set<JournalReader> retval = new HashSet<JournalReader>();
		for (JournalImpl j : journals.values())
			if (keyword==null || j.getTitle().contains(keyword))
				retval.add(j);
		return retval;
	}

	/**
	 * Get the information about the journal with a given ISSN 
	 * @param issn the ISSN
	 * @return a JournalImpl object that gives access to the journal information
	 */
	public JournalImpl getJournal(String issn) {
		return journals.get(issn);
	}

}
