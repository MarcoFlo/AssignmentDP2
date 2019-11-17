package it.polito.dp2.BIB.sol1;

import it.polito.dp2.BIB.IssueReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.JournalType;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class JournalReaderImpl implements JournalReader {

    private JournalType journalType;
    private TreeMap<Integer, List<BigInteger>> issueByYear;
    private HashMap<BigInteger, IssueReaderImpl> issueById;

    public JournalReaderImpl(JournalType journalType) {
        this.journalType = journalType;
        issueByYear = journalType.getIssue().stream().collect(Collectors.groupingBy(issue -> issue.getYear().getYear(), TreeMap::new, mapping(JournalType.Issue::getId, toList())));
        issueById = journalType.getIssue().stream().collect(Collectors.toMap(JournalType.Issue::getId, issue -> new IssueReaderImpl(issue, this), (prev, post) -> post, HashMap::new));
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
            return issueByYear.subMap(since, to + 1).values().stream().flatMap(List::stream).map(issueId -> issueById.get(issueId)).collect(Collectors.toSet());
        else
            return Collections.emptySet();
    }

    @Override
    public IssueReader getIssue(int year, int number) {
        if (issueByYear.containsKey(year)) {
            Optional<IssueReaderImpl> numberExist = issueByYear.get(year).stream().map(issueId -> issueById.get(issueId)).filter(issueReader -> issueReader.getNumber() == number).findFirst();
            if (numberExist.isPresent())
                return numberExist.get();
        }

        return null;
    }

    IssueReaderImpl getIssueReaderById(BigInteger bigInteger) {
        return issueById.get(bigInteger);
    }
}
