package it.polito.dp2.BIB.sol1.parser;

import it.polito.dp2.BIB.ArticleReader;
import it.polito.dp2.BIB.IssueReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.ArticleType;

/**
 * Implementation of the ArticleReader interface which returns the bibliographic information
 * retrieved from an ArticleType object.
 *
 */
public class ArticleImpl extends ItemImpl implements ArticleReader {
	JournalImpl journal;
	IssueImpl issue;

	public ArticleImpl(ArticleType item, Items items, Journals journals) {
		super(item, items);
		journal = journals.getJournal(item.getJournal());
		issue = journal.getIssue(item.getIssue());
		issue.addArticleReader(this);
	}

	@Override
	public JournalReader getJournal() {
		return journal;
	}

	@Override
	public IssueReader getIssue() {
		return issue;
	}

}
