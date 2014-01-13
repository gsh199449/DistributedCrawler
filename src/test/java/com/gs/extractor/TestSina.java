package com.gs.extractor;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.gs.extractor.impl.SinaExtractor;
import com.gs.extractor.impl.SinaNewsContentExtractor;

public class TestSina {

	@Test
	public void test() throws IOException {
		System.out.println(new SinaNewsContentExtractor()
				.extractFromHtml(FileUtils.readFileToString(new File(
						"D://Test//sina.txt"),"gb2312")));
	}
	
	@Test
	public void testNew() throws IOException{
		Extractor e = new SinaExtractor(FileUtils.readFileToString(new File(
				"D://Test//sina.txt"),"gb2312"),1,100,4).process();
		System.out.println(e.getContent());
		System.out.println(e.getTitle());
		System.out.println(e.getUrls());
	}

}
