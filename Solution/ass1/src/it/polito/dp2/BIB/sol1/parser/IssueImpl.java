package it.polito.dp2.BIB.sol1.parser;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.BIB.ArticleReader;
import it.polito.dp2.BIB.IssueReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.JournalType.Issue;

/**
 * Implementation of the IssueReader interface which returns the bibliographic information
 * retrieved from an Issue object.
 * This implementation is initialized with an empty set of articles.
 * Articles have to be added after initialization
 */
public class IssueImpl implements IssueReader {
	Issue issue;
	JournalImpl journal;
	Set<ArticleReader> articles;

	/**
	 * Constructor
	 * @param i the Issue object
	 * @param journal the JournalImpl object of the journal this issue belongs to
	 */
	public IssueImpl(Issue i, JournalImpl journal) {
		this.issue = i;
		this.journal = journal;
		articles = new HashSet<ArticleReader>(); // initially set of articles is empty (will be populated by adding articles afterwards).
	}
	
	/**
	 * Add an article to this issue
	 * @param article an ArticleReader that provides information about the article
	 */
	public void addArticleReader(ArticleReader article) {
		articles.add(article);
	}

	@Override
	public JournalReader getJournal() {
		return journal;
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
		return new HashSet<ArticleReader>(articles);
	}

}
