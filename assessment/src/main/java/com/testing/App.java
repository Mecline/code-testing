package com.testing;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class App 
{
    public static void main( String[] args ) throws IOException, ParseException
    {
    	
    	URL CNNurl = new URL ("http://rss.cnn.com/rss/edition.rss");  
    	URL ESPNurl = new URL("https://www.espn.com/espn/rss/news");
    	URL washingtonURL = new URL("https://feeds.a.dj.com/rss/RSSWorldNews.xml");
    	URL meetThePressUrl = new URL("http://podcastfeeds.nbcnews.com/meetthepress");
    	URL BBCurl = new URL("http://feeds.bbci.co.uk/news/scotland/rss.xml");
    	URL nasaHurricane= new URL("https://www.nasa.gov/rss/dyn/hurricaneupdate.rss");
    	
    	
    	Map<String, URL> input = new HashMap<String, URL>();
    	
    	input.put("CNN", CNNurl);
    	input.put("Washington Post", washingtonURL);
    	input.put("Meet the Press", meetThePressUrl);
    	input.put("BBC News", BBCurl);
    	input.put("ESPN", ESPNurl);
    	input.put("NASA Hurricane", nasaHurricane);
        
        System.out.println(RSSFeeds.readRSSFeed(input, 5));
//        System.out.println(RSSTesting.readRSSFeed(input, 1));
        


    }

}
