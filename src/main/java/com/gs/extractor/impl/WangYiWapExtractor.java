package com.gs.extractor.impl;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gs.extractor.Extractor;
import com.gs.utils.URL;

public final class WangYiWapExtractor extends Extractor {

	public WangYiWapExtractor(String html, int level, int topN, int depth) {
		super(html, level, topN, depth);
	}

	@Override
	public Extractor process() {
		String titleregex = "<title>(.+?)_手机网易网</title>";// 去掉最后的尾坠
		Pattern titlept = Pattern.compile(titleregex);
		Matcher titlemt = titlept.matcher(this.html);
		if (titlemt.find()) {
			this.title = titlemt.group(1);
		}
		// /////////////////////////////////////////
		String contentregex = "<div class=\"content\">(.*?)</div>";
		Pattern contentpt = Pattern.compile(contentregex, Pattern.DOTALL);
		Matcher contentmt = contentpt.matcher(this.html);
		this.content = "";
		while (contentmt.find()) {
			this.content += contentmt.group(0);
		}
		this.content = this.content.replaceAll("<.*?>", "");// 抹掉所有尖括号的内容
		this.content = this.content.replaceAll("\\s", "");// 抹掉所有空白
		// ///////////////////////////////////////
		this.set = new HashSet<URL>();
		if (this.level >= this.depth && this.depth != 0) {
			;// 若已经超过抓取深度则不再提取
		} else {
			String linkregex = "<a\\s.*?href=\"([^\"]+)\"[^>]*>(.*?)</a>";
			Pattern linkpt = Pattern.compile(linkregex);
			Matcher linkmt = linkpt.matcher(this.html);
			int counter = 0;// 已抽取的连接计数器
			while (linkmt.find()) {
				String u = linkmt.group(1);
				if (u.startsWith("http://3g.163.com/news")
						&& (u.endsWith("htm") || u.endsWith("html") || u
								.endsWith("shtml"))) {
					URL re = new URL(u, this.level + 1);
					this.set.add(re);// 向结果List中添加
					counter++;
				}else if(u.startsWith("/news")
						&& (u.endsWith("htm") || u.endsWith("html") || u
								.endsWith("shtml"))){
					URL re = new URL("http://3g.163.com"+u, this.level + 1);
					this.set.add(re);// 向结果List中添加
					counter++;
				}
				if (counter > this.topN && this.topN != 0)
					break;// 是否达到topN
			}
		}
		return this;
	}

}
