package com.gs.extractor;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.gs.extractor.impl.WangYiWapExtractor;

public class TestWangYi {

	@Test
	public void test() throws IOException {
		Extractor e = new WangYiWapExtractor(FileUtils.readFileToString(new File("D://Test//wangyi.txt"), "gb2312"), 1, 100, 4).process();
		System.out.println(e.getContent());
		System.out.println(e.getTitle());
		System.out.println(e.getUrls());
	}

}
