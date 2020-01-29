package it.polito.dp2.BIB.sol1;

import it.polito.dp2.BIB.ArticleReader;
import it.polito.dp2.BIB.IssueReader;
import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.ArticleType;

import java.util.HashSet;
import java.util.Set;

public class ArticleReaderImpl implements ArticleReader {
    private ArticleType articleType;
    private JournalReaderImpl journalReader;
    private IssueReaderImpl issueReader;
    private Set<ItemReader> citingItems;

    public ArticleReaderImpl(ArticleType articleType, JournalReaderImpl journalReader, IssueReaderImpl issueReader) {
        this.articleType = articleType;
        this.journalReader = journalReader;
        this.issueReader = issueReader;
        citingItems = new HashSet<>();
    }

    @Override
    public JournalReader getJournal() {
        return journalReader;
    }

    @Override
    public IssueReader getIssue() {
        return issueReader;
    }

    @Override
    public String[] getAuthors() {
        return articleType.getAuthor().toArray(new String[0]);
    }

    @Override
    public String getTitle() {
        return articleType.getTitle();
    }

    @Override
    public String getSubtitle() {
        return articleType.getSubtitle();
    }

    @Override
    public Set<ItemReader> getCitingItems() {
        return citingItems;
    }

    void addCitingItem(ItemReader itemReader)
    {
        citingItems.add(itemReader);
    }

}
