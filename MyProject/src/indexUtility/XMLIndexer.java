package indexUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class XMLIndexer implements XMLFileConstants {
	private static IndexWriter writer;
	private static int counter;
	private static Set <String> fileNameSet = new HashSet<String>();
	
	public static void main(String[] args) throws Exception {
		String indexDir = INDEX_DIR;
		String dataDir = DATA_DIR;
		
		long start = System.currentTimeMillis();
		index(indexDir,dataDir);
		long end = System.currentTimeMillis();
		
		System.out.println("Indexing: " + counter + " files took " + (end - start) + " ms");
		
	}
	
	private static void index(String iDir,String dataDir) throws Exception {
		try
		{
			Directory indexDir = openFeedsDirectory(iDir);
			createIndex(indexDir);
			iterateFeedsDirectory(dataDir);		// fill the xml files filepath set
			XMLDigester digester= new XMLDigester();
			for(String path:fileNameSet){
				File file = new File(path);
				digester.digestFeed(writer, indexDir, file);
				deleteFeed(file);
			}
		}
		finally
		{
			counter = writer.numDocs();
			closeIndex();
		}
	}
	
	/**
	 * @param indexDir
	 * @throws IOException
	 */
	static Directory openFeedsDirectory(String indexDir) throws IOException {
		Directory dir = FSDirectory.open(new File(indexDir));
		return dir;
	}
	
	private static void createIndex(Directory dir) throws IOException {
		try
		{
			StandardAnalyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig luceneConfig = new IndexWriterConfig(Version.LATEST,analyzer);
			luceneConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			writer = new IndexWriter(dir, luceneConfig);
			
		}
		catch (Exception e)
		{
			System.out.println("Error in XMLFileIndexer::createIndex()");
			System.out.println(e.getMessage());
			e.printStackTrace();
	
		}
	}

	private static void closeIndex() throws IOException {
		writer.commit();
		writer.close();
	}
	
	
	private static void iterateFeedsDirectory(String Dir) {
		System.out.println("Searching directory: " + Dir);
		File directory = new File(Dir);
		File[] dirs = directory.listFiles();
		for (int i = 0; i < dirs.length; i++) {
			if (dirs[i].isFile()) {
				String fileName = dirs[i].getName();
				String fileExtension = fileName
						.substring(fileName.length() - 3);
				if (fileExtension.equals("txt")) {
					System.out.println("Searching file "+ fileName);
					readFeedContents(dirs[i]);		
					deleteFeed(dirs[i]);
				}
			}
			else
				iterateFeedsDirectory(dirs[i].getAbsolutePath());
		}
	}
	
	private static void deleteFeed(File file) {
		file.setWritable(true);
		file.delete();
	}

	private static void readFeedContents(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (true) {
				String text = br.readLine();
				if (text == null)
					break;				
				else {
					System.out.println("Found file: " + text);
					fileNameSet.add(text);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
