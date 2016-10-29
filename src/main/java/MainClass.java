import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MainClass {

	public static void main(String[] args) {
		String url = args[0];
		String htmlPreview="";
		try {
			Document doc = Jsoup.connect(url).get();
			
			String title = extractTitle(doc);
		    String imageUrl = extractImage(doc);
		    String description = extractDescription(doc);
		    
		    htmlPreview= "<html><body><h1>"+title+"</h1><h2>"+description+"</h2><img src=\""+imageUrl+"\"/></body></html>";
	    
		    File htmlFile= new File("imagePreview.html");
		    FileWriter fw = new FileWriter(htmlFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(htmlPreview);
			bw.close();
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String extractDescription(Document doc) {
		String desc= "";
		Element elementDescription = doc.select("meta[property=og:description], meta[name=description]").first();
		if(elementDescription!=null){
			desc = elementDescription.attr("content");
		}
		return desc;
	}

	private static String extractImage(Document doc) {
		String imageUrl = null;
		Element elementImage = doc.select("meta[property=og:image]").first();
		if (elementImage!=null && !elementImage.attr("content").isEmpty()) {
		    imageUrl = elementImage.attr("content");
		}else{
			elementImage = doc.select("link[rel=image_src]").first();
			if(elementImage!=null && !elementImage.attr("href").isEmpty()){
				imageUrl = elementImage.attr("href");
			}else{
				elementImage = doc.select("img[src~=(?i)\\.(png|jpe?g)]").first();
				if(elementImage!=null){
					imageUrl= elementImage.attr("src");
				}
			}
		}
		return imageUrl;
	}

	private static String extractTitle(Document doc) {
		String title= "";
		Elements elementTitle = doc.select("meta[property=og:title],meta[name=title]");
		if(elementTitle!=null){
			title = elementTitle.attr("content");
		}else {
			title = doc.title();
		}
		return title;
	}

}
