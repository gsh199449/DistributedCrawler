package com.gs.extractor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.gs.extractor.impl.TencentExtractor;

public class TestTencent {
	
	@Test
	public void test() throws IOException{
		Extractor te = new TencentExtractor(FileUtils.readFileToString(new File("D://Test//tencent.txt"),"gb2312"), 1, 100, 3);
		long start = System.currentTimeMillis();
		te.process();
		System.out.println(te.getContent());
		System.out.println(te.getTitle());
		System.out.println(te.getUrls());
		System.out.println(System.currentTimeMillis() - start);
	}
}
