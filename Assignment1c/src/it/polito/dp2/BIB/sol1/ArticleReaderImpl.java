package it.polito.dp2.BIB.sol1;

import it.polito.dp2.BIB.ArticleReader;
import it.polito.dp2.BIB.IssueReader;
import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.JournalReader;

import java.util.Set;

public class ArticleReaderImpl implements ArticleReader {
    @Override
    public JournalReader getJournal() {
        return null;
    }

    @Override
    public IssueReader getIssue() {
        return null;
    }

    @Override
    public String[] getAuthors() {
        return new String[0];
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSubtitle() {
        return null;
    }

    @Override
    public Set<ItemReader> getCitingItems() {
        return null;
    }
}
