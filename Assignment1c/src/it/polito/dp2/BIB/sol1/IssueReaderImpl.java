package it.polito.dp2.BIB.sol1;

import it.polito.dp2.BIB.ArticleReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.JournalType;

import java.util.HashSet;
import java.util.Set;

public class IssueReaderImpl implements it.polito.dp2.BIB.IssueReader {
    JournalType.Issue issue;
    JournalReaderImpl journalReader;
    Set<ArticleReader> articleReaderSet;

    public IssueReaderImpl(JournalType.Issue issue, JournalReaderImpl journalReader) {
        this.issue = issue;
        this.journalReader = journalReader;
        articleReaderSet = new HashSet<>();
    }

    @Override
    public JournalReader getJournal() {
        return journalReader;
    }

    @Override
    public int getYear() {
        return issue.getYear().getYear();
    }

    @Override
    public int getNumber() {
        return issue.getNumber().intValue();
    }

    @Override
    public Set<ArticleReader> getArticles() {
        return articleReaderSet;
    }


    public void addArticle(ArticleReaderImpl articleReader) {
        articleReaderSet.add(articleReader);
    }
}
