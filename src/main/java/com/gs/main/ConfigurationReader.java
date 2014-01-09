package com.gs.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationFactory.ConfigurationBuilder;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;

public class ConfigurationReader {
	public static Configuration parseJsonConf(String path) throws IOException {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		Configuration conf = null;
		conf = builder.getConfiguration();
		try {
			JsonFactory jfactory = new JsonFactory();
			JsonParser jParser = jfactory.createJsonParser(new File(path));
			while (jParser.nextToken() != JsonToken.END_OBJECT) {
				String fieldname = jParser.getCurrentName();
				if ("rootPath".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("rootPath", jParser.getText());
				} else if ("seeds".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("seeds", jParser.getText());
				} else if ("depth".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("depth", jParser.getIntValue());
				} else if ("topN".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("topN", jParser.getIntValue());
				} else if ("outputPath".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("outputPath", jParser.getText());
				} else if ("jobName".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("jobName", jParser.getText());
				} else if ("crawlDBHost".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("crawlDBHost", jParser.getText());
				} else if ("hbaseTableName".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("hbaseTableName", jParser.getText());
				} else if ("libsPath".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("libsPath", jParser.getText());
				} else if ("crawlDBPort".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("crawlDBPort", jParser.getIntValue());
				} else if ("crawlDBPassword".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("crawlDBPassword", jParser.getText());
				} else if ("crawlDBTimeout".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("crawlDBTimeout", jParser.getIntValue());
				} else if ("crawlDBToCrawlDB".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("crawlDBToCrawlDB", jParser.getIntValue());
				} else if ("crawlDBCrawledDB".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("crawlDBCrawledDB", jParser.getIntValue());
				} else if ("solrURL".equals(fieldname)) {
					jParser.nextToken();
					conf.addProperty("solrURL", jParser.getText());
				} else if ("jarsName".equals(fieldname)) {
					jParser.nextToken();
					List<String> jarlist = new LinkedList<String>();
					while (jParser.nextToken() != JsonToken.END_ARRAY) {
						jarlist.add(jParser.getText());
					}
					conf.addProperty("jarsName", jarlist);
				}
			}
			jParser.close();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conf;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(parseJsonConf("/home/gaoshen/crawler.json")
				.getString("libsPath"));
	}
}
