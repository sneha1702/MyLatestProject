package indexUtility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class XMLSearcher implements XMLFileConstants {
	private static final Map<String, Float> boostMap = new HashMap<String, Float>();
	private static Directory dir;
	private static TopScoreDocCollector collector;
	private static IndexSearcher searcher;
	private static IndexReader reader;
	private static int numResults;

	public void search(String q) throws IOException, ParseException {
		int hitsPerPage = 10;
		openIndexDirectory(INDEX_DIR);
		try {

			collector = TopScoreDocCollector.create(hitsPerPage, true);

			StandardAnalyzer analyzer = new StandardAnalyzer();
			// Single word query
			// QueryParser parser = new QueryParser("itemTitle",analyzer);
			// Multi word query
			MultiFieldQueryParser parser = configureQueryParser(analyzer);

			// Normal query
			Query query = parser.parse(q);
			long start = System.currentTimeMillis();
			searcher.search(query, collector);
			long end = System.currentTimeMillis();

			// Calculate search duration
			long duration = end - start;
			System.out.println("Search duration " + duration);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	private static MultiFieldQueryParser configureQueryParser(
			StandardAnalyzer analyzer) {
		// Multiword query
		String[] terms = { "itemTitle", "title", "itemDesc", "itemExtText",
				"itemLink", "description" };

		// Add boost factor according to the position in the terms array
		int len = terms.length;
		for (int i = 0; i < len; i++) {
			boostMap.put(terms[i], (float) (len - i));
		}

		MultiFieldQueryParser parser = new MultiFieldQueryParser(terms,
				analyzer, boostMap);
		parser.setAllowLeadingWildcard(true);
		parser.setDefaultOperator(Operator.AND);// this will ensure that
												// multi-word query "john page"
												// will return results
												// containing john AND page both
		return parser;
	}

	public void closeIndexDirectory() throws IOException {
		reader.close();
		dir.close();
	}

	static Directory openIndexDirectory(String xmlDir) throws IOException {
		System.out.println("Searching directory: " + xmlDir);

		dir = FSDirectory.open(new File(xmlDir));
		reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);

		return dir;
	}

	public List<IndexDocument> retrievePageResults(int offset, int noOfRecords) {
		List<IndexDocument> list = new ArrayList<IndexDocument>();
		IndexDocument iDoc = null;
		try {
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			numResults = hits.length;
			if (numResults < noOfRecords) {
				noOfRecords = numResults;
			}
			for (int x = offset; x < noOfRecords; ++x) {
				iDoc = new IndexDocument();
				int docId = hits[x].doc;
				Document d = searcher.doc(docId);
				iDoc.setItemLink(d.get("itemLink"));
				iDoc.setItemTitle(d.get("itemTitle"));
				iDoc.setItemDesc(d.get("itemDesc"));
				list.add(iDoc);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public int getTotalResultNum() {
		int num = numResults;
		return num;
	}

}