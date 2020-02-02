package it.polito.dp2.BIB.sol1.parser;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import it.polito.dp2.BIB.IssueReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.JournalType;
import it.polito.dp2.BIB.sol1.jaxb.JournalType.Issue;

/**
 * Implementation of the JournalReader interface which returns the bibliographic information
 * retrieved from a JournalType object.
 * This implementation also provides the IssueReader implementation of an issue of this journal
 * given its id.
 *
 */
public class JournalImpl implements JournalReader {
	private HashMap<BigInteger,IssueImpl> issuesById;	// map of issues id as key
	private TreeMap<Integer,Set<IssueImpl>> issuesByYear;	// map of issues with year as key, sorted by year
	private JournalType journal;

	public JournalImpl(JournalType j) {
		journal = j;		
		issuesById = new HashMap<BigInteger,IssueImpl>();
		issuesByYear = new TreeMap<Integer,Set<IssueImpl>>();
		for (Issue i: j.getIssue()) {
			// create IssueImpl
			IssueImpl issueImpl = new IssueImpl(i, this);
			// put into issuesById
			issuesById.put(i.getId(), issueImpl);
			// put into issuesByYear
			int year = issueImpl.getYear();
			if (issuesByYear.containsKey(year))
				issuesByYear.get(year).add(issueImpl);
			else {
				HashSet<IssueImpl> hs = new HashSet<IssueImpl>();
				hs.add(issueImpl);
				issuesByYear.put(year,hs);
			}
		}
	}

	@Override
	public String getISSN() {
		return journal.getISSN();
	}

	@Override
	public String getPublisher() {
		return journal.getPublisher();
	}

	@Override
	public String getTitle() {
		return journal.getTitle();
	}

	@Override
	public Set<IssueReader> getIssues(int since, int to) {
		Set<IssueReader> retval = new HashSet<IssueReader>();
		for (Set<IssueImpl> s: issuesByYear.subMap(since, to+1).values())
			retval.addAll(s);
		return retval;
	}

	@Override
	public IssueReader getIssue(int year, int number) {
		Set<IssueImpl> s = issuesByYear.get(year);
		if (s==null)
			return null;
		else
			for (IssueImpl i : s)
				if (i.getNumber()==number)
					return i;
		return null;
	}

	public IssueImpl getIssue(BigInteger issue) {
		return issuesById.get(issue);
	}

}
