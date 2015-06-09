package Archiv;
import indexUtility.XMLFileConstants;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class XMLSearcher implements XMLFileConstants {
	private static final Map<String, Float> boostMap = new HashMap<String, Float>();
	private static Directory dir;

	public static String search(String q) throws IOException, ParseException {
		int hitsPerPage = 10;
		Directory dir = openIndexDirectory(INDEX_DIR);
		String opHtml = null;
		try {
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(
					hitsPerPage, true);

			StandardAnalyzer analyzer = new StandardAnalyzer();
			// Single word query
			// QueryParser parser = new QueryParser("itemTitle",analyzer);
			// Multi word query
			MultiFieldQueryParser parser = configureQueryParser(analyzer);
			
			StringTokenizer st = new StringTokenizer(q);
			long start;
			long end;
			if(st.countTokens()>1)
			{
				BooleanQuery bQuery = new BooleanQuery();				
				List<String> tokens = splitQueryTerms(q, analyzer);
				for (String token : tokens) {
					bQuery.add(parser.parse(token.replace("~", "") + "~"),
							Occur.MUST);
				}
				start = System.currentTimeMillis();
				searcher.search(bQuery, collector);
				end = System.currentTimeMillis();
			}
			else	// Normal query
			{
				Query query = parser.parse(q); 
				start = System.currentTimeMillis();
				searcher.search(query, collector);
				end = System.currentTimeMillis();
			}
				
			/*
			 * Range Query DateFormat dateFormat = new
			 * SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Date date = new Date();
			 * 
			 * String start = dateFormat.format(date); String end =
			 * dateFormat.format(date);
			 * 
			 * 
			 * Term startTerm = new Term("itemPubDate", start); Term endTerm =
			 * new Term("itemPubDate", end); Query query = new
			 * TermRangeQuery(startTerm, endTerm, inclusive);
			 * booleanQuery.add(NumericRangeQuery.newIntRange("page_count", 10,
			 * 20, true, true),Occur.MUST);
			 */


			// Calculate search duration
			long duration = end - start;
			System.out.println("Search duration "+duration);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			int num = hits.length;
			for (int x = 0; x < num; ++x) {
				int docId = hits[x].doc;
				Document d = searcher.doc(docId);
				opHtml += "<tr>" + "<p><width = 10%><b><a href=\""
						+ d.get("itemLink") + "\">" + d.get("itemTitle")
						+ "</a></b></width></p>" + "<p>" + d.get("itemLink")
						+ "</p>" + "<p>" + d.get("itemDesc") + "</p>" + "</tr>";

				System.out.println("item desc " + d.get("itemDesc"));
			}

			// reader can only be closed when there is no need to access the
			// documents any more
			reader.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		closeIndexDirectory();
		return opHtml;
	}

	/**
	 * @param q
	 * @param analyzer
	 * @return
	 * @throws IOException
	 */
	private static List<String> splitQueryTerms(String q, StandardAnalyzer analyzer)
			throws IOException {
		// Split the search string into separate search terms by word
		TokenStream tokenStream = analyzer.tokenStream(null,
				new StringReader(q));
		CharTermAttribute termAttribute = tokenStream
				.getAttribute(CharTermAttribute.class);
		List<String> tokens = new ArrayList<String>();
		while (tokenStream.incrementToken()) {
			tokens.add(termAttribute.toString());
		}
		return tokens;
	}

	/**
	 * @param analyzer
	 * @return
	 */
	private static MultiFieldQueryParser configureQueryParser(StandardAnalyzer analyzer) {
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

	private static void closeIndexDirectory() throws IOException {
		dir.close();
	}

	static Directory openIndexDirectory(String xmlDir) throws IOException {
		System.out.println("Searching directory: " + xmlDir);

		dir = FSDirectory.open(new File(xmlDir));
		return dir;
	}

}