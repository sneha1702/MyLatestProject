package indexUtility;

public class RssFile {
    private String title;
    private String link;
    private String description;
    private String category;
    private String generator;
    private String lastBuildDate;
    private String aktuelleLink;
    
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
      
    }    
    
    public String getLink() {
        return link;
    }
 
    public void setLink(String link) {
        this.link = link;
    }   
    
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }  
    
    public String getCategory() {
        return category;
    }
 
    public void setCategory(String category) {
        this.category = category;
    }  
    
    public String getGenerator() {
        return generator;
    }
 
    public void setGenerator(String generator) {
        this.generator = generator;
    }  
    
    public String getLastBuildDate() {
        return lastBuildDate;
    }
 
    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }  
    
    public String getAktuelleLink() {
        return aktuelleLink;
    }
 
    public void setAktuelleLink(String aktuelleLink) {
        this.aktuelleLink = aktuelleLink;
    }  
    
    private String itemTitle;
	private String itemDesc;
	private String itemLink;
	private String itemPubDate;
	private String itemExtText;

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
		  System.out.println("title>> " + itemTitle);
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getItemLink() {
		return itemLink;
	}

	public void setItemLink(String itemLink) {
		this.itemLink = itemLink;
	}

	public String getItemPubDate() {
		return itemPubDate;
	}

	public void setItemPubDate(String itemPubDate) {
		this.itemPubDate = itemPubDate;
	}
	
	public String getItemExtText() {
		return itemExtText;
	}

	public void setItemExtText(String itemExtText) {
		this.itemExtText = itemExtText;
	}
    
}
