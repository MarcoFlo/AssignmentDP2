package it.polito.dp2.BIB.sol1;

import it.polito.dp2.BIB.IssueReader;
import it.polito.dp2.BIB.sol1.jaxb.JournalType;

import java.util.*;
import java.util.stream.Collectors;

public class JournalReaderImpl implements it.polito.dp2.BIB.JournalReader {

    private JournalType journalType;
    private TreeMap<Integer, List<IssueReaderImpl>> issueYear;

    public JournalReaderImpl(JournalType journalType) {
        this.journalType = journalType;
        issueYear = (TreeMap<Integer, List<IssueReaderImpl>>) journalType.getIssue().stream().map(issue -> new IssueReaderImpl(issue, this)).collect(Collectors.groupingBy(IssueReaderImpl::getYear));
    }

    @Override
    public String getISSN() {
        return journalType.getISSN();
    }

    @Override
    public String getPublisher() {
        return journalType.getPublisher();
    }

    @Override
    public String getTitle() {
        return journalType.getTitle();
    }

    @Override
    public Set<IssueReader> getIssues(int since, int to) {
        if (since <= to)
            return issueYear.subMap(since, to + 1).values().stream().flatMap(List::stream).collect(Collectors.toSet());
        else
            return Collections.emptySet();
    }

    @Override
    public IssueReader getIssue(int year, int number) {
        if (issueYear.containsKey(year))
            return issueYear.get(year).get(number);
        else
            return null;
    }
}
