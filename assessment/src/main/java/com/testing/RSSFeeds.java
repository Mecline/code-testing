package com.testing;

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RSSFeeds {

	int i = 0;

	public static Date getDate(int days) throws ParseException {
		
		// accepts a given number of days, so you can set back the calendar
		// to see content that hasn't been updated past a certain date
		// EX: setting days as "7" will show you RSS Feeds not updated in 7 days
		
		Calendar cal = Calendar.getInstance();
		
		// assumes # of days is above 0. If not, code will automatically revert to 1 day back, most recent 
		// day that is going back in time.
		
		if (days > 0){
			cal.add(Calendar.DATE, -days);
		}
		else{
			cal.add(Calendar.DATE, -1);
		}
		Date daySet = cal.getTime();
		    
		DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String returnDate = dateFormat.format(daySet);
        Date returnFinalDate = dateFormat.parse(returnDate);
        
		return returnFinalDate;
		
	}

	public static Date valueGet(URL url) throws ParseException {
		// assuming that every RSS Feed provided has a valid <pubDate> 
		// assuming that the first <pubDate> encountered is the most recent pubDate. Not using lastBuildDate as this could
		// indicate edits that weren't in relationship to actual content published
		
		BufferedReader in = null;
		String finalLine = "";
		String dateLine = "";
		Date lineDate = null;

		try {

			in = new BufferedReader(new InputStreamReader(url.openStream()));

			String sourceCode = "";
			String line;

			// getting <pubDate> line in date format, will accept first pubDate that it comes across,
			// which is the most recent content having been published
			// will break after it finds a pubDate
			while ((line = in.readLine()) != null) {
				if (lineDate != null){
					break;
				}
				if (line.contains("<pubDate>")) {
					int firstPos = line.indexOf("<pubDate>");
					String temp = line.substring(firstPos);
					temp = temp.replace("<pubDate>", "");
					int lastPos = temp.indexOf("</pubDate>");
					temp = temp.substring(0, lastPos);
					dateLine = (sourceCode += temp + "\n");
					finalLine = dateLine.substring(4, 16);
					lineDate=new SimpleDateFormat("dd MMM yyyy").parse(finalLine);
				}
			}
		} catch (IOException e) {
			System.out.println("EXCEPTION LAST LEVEL THROWN");
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lineDate;
	}

	public static ArrayList clean(ArrayList update, ArrayList noUpdate){
		
		for(int i=0; i < noUpdate.size(); i++){
			// if company names are found in both lists, company name will be removed from not updated list
			// assumes that company key is going to be used twice in the dictionary, which should not happen
			// but added as a fail-safe
			if (noUpdate.contains(update.get(i))){
				noUpdate.remove(i);
		}
	}	
		return noUpdate;
	}
	
	public static ArrayList readRSSFeed(Map<String, URL> rssDict, int days) throws IOException, ParseException {
		
		// Initializing variables
		Date linePass = null;
		ArrayList<String> updatedList = new ArrayList<String>();
		ArrayList<String> notUpdated = new ArrayList<String>();
		ArrayList<String> finalListNotUpdated = new ArrayList<String>();
		
		for (Map.Entry<String, URL> output : rssDict.entrySet()) {

			// will return the most recent published date from the RSS feed provided
			linePass = valueGet(output.getValue());

			// comparing the published date to see if it is after the specified time requested
			// will update lists accordingly
			if ((RSSFeeds.getDate(days).before(linePass)) == true) {
				updatedList.add((output.getKey()));
			} else {
				notUpdated.add(output.getKey());
			}
			
		}
		
		 finalListNotUpdated = RSSFeeds.clean(updatedList, notUpdated);
		
		System.out.println("Complete");
//		Uncomment below line to also see the updated list
//		System.out.println("Updated within " + days + " days: " + updatedList);
		System.out.print("Not updated within " + days + " days: ");
		return finalListNotUpdated;
	}

}
