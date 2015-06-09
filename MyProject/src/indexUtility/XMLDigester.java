package indexUtility;
import org.apache.commons.digester3.Digester;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

import java.io.File;
import java.io.IOException;

public class XMLDigester {
	
	private IndexWriter XmlWriter;
	
	public void digestFeed(IndexWriter writer, Directory indexDir, File f) {
		try 
		{
			this.XmlWriter = writer;
			// instantiate Digester and disable XML validation
			Digester digester = new Digester();
			digester.setValidating(false);
			
			// Push the current object onto the stack
			digester.push(this);
			
			// Creates a new instance of the RssFile class
	        digester.addObjectCreate("rss/channel", RssFile.class);
	        digester.addSetProperties("rss/channel");
	        digester.addBeanPropertySetter("rss/channel/title", "title");
			digester.addSetProperties("rss/channel/link", "link","link");
			digester.addBeanPropertySetter("rss/channel/link", "link");
			digester.addBeanPropertySetter("rss/channel/description", "description");
			digester.addBeanPropertySetter("rss/channel/category", "category");
			digester.addBeanPropertySetter("rss/channel/generator", "generator");
			digester.addBeanPropertySetter("rss/channel/lastBuildDate", "lastBuildDate");
			digester.addBeanPropertySetter("rss/channel/aktuelleLink", "aktuelleLink");
			
			digester.addBeanPropertySetter("rss/channel/item/title","itemTitle");
			digester.addBeanPropertySetter("rss/channel/item/description", "itemDesc");
			digester.addBeanPropertySetter("rss/channel/item/link", "itemLink");
			digester.addBeanPropertySetter("rss/channel/item/pubDate","itemPubDate");
			digester.addBeanPropertySetter("rss/channel/item/ExtractedText","itemExtText");

			

			// Move to next Document
	        digester.addSetNext( "rss/channel", "addRSSFile");

	        //Parse the XML file to get a rssFile instance
			digester.parse(f);
			
		} catch (Exception exc) {
			System.out.println("Error in digest()");
			exc.printStackTrace();
		}
	}	
    
    public void addFeedToIndex(RssFile rssFile) throws IOException {
    	System.out.println("Adding RSS Files to the index");
    	try
    	{
    		Document doc = new Document();
	    	if(rssFile.getTitle() != null)
	    		doc.add(new TextField("title", rssFile.getTitle(), Field.Store.YES));

	    	if(rssFile.getDescription() != null)
	    		doc.add(new TextField("description", rssFile.getDescription(), Field.Store.YES));
	    	
	    	String itemTitle =  rssFile.getItemTitle();
	    	if(itemTitle != null)
	    		doc.add(new TextField("itemTitle", itemTitle, Field.Store.YES));
	    	
	    	String itemDesc = rssFile.getItemDesc();

	    	if(itemDesc != null)
	    		doc.add(new TextField("itemDesc", itemDesc, Field.Store.YES));	    	

	    	String itemLink = rssFile.getItemLink();

	    	if(itemLink != null)
	    		doc.add(new StringField("itemLink", itemLink, Field.Store.YES));

	    	String itemPubDate = rssFile.getItemPubDate();
	    	if(itemPubDate != null)
	    		doc.add(new StringField("itemPubDate", itemPubDate, Field.Store.YES));        

	    	String itemExtText = rssFile.getItemExtText();
	    	if(itemExtText != null)
	    		doc.add(new TextField("itemExtText", itemExtText, Field.Store.YES));
	    	
	    	if(rssFile.getLink() != null)
	    		doc.add(new StringField("link", rssFile.getLink(), Field.Store.YES));
	    	
	    	if(rssFile.getCategory() != null)
	    		doc.add(new StringField("category", rssFile.getCategory(), Field.Store.NO));
	    	
	    	if(rssFile.getGenerator() != null)
	    		doc.add(new TextField("generator", rssFile.getGenerator(), Field.Store.NO));
	    	
	    	if(rssFile.getLastBuildDate() != null)
	    		doc.add(new StringField("lastBuildDate", rssFile.getLastBuildDate(), Field.Store.NO));
	    	
	    	if(rssFile.getAktuelleLink() != null)
	    		doc.add(new StringField("aktuelleLink", rssFile.getAktuelleLink(), Field.Store.NO));	    	
	    	
	    	
	    	XmlWriter.addDocument(doc);
	        
    	}
    	catch (Exception e)
    	{
	    	System.out.println("Error in addRSSFile()");
    		e.printStackTrace();
    	}
	}
}