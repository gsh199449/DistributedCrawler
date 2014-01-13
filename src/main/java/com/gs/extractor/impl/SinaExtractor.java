package com.gs.extractor.impl;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gs.extractor.Extractor;
import com.gs.utils.URL;

public final class SinaExtractor extends Extractor {

	public SinaExtractor(String html, int level, int topN, int depth) {
		super(html, level, topN, depth);
	}

	@Override
	public Extractor process() {
		String titleregex = "<title>(.+?)_新浪新闻</title>";// 去掉最后的尾坠
		Pattern titlept = Pattern.compile(titleregex);
		Matcher titlemt = titlept.matcher(this.html);
		if (titlemt.find()) {
			this.title = titlemt.group(1);
		}
		// /////////////////////////////////////////
		String contentregex = "<div class=\"Main clearfix\">(.*?)<div class=\"show_author\">";
		Pattern contentpt = Pattern.compile(contentregex, Pattern.DOTALL);
		Matcher contentmt = contentpt.matcher(this.html);
		if (contentmt.find()) {
			this.content = contentmt.group(1);
		}
		Pattern pt1 = Pattern.compile("<p>(.*?)</p>", Pattern.DOTALL);
		Matcher mt1 = pt1.matcher(this.content);
		this.content = "";
		while (mt1.find()) {
			this.content += mt1.group(1);
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
				if (u.startsWith("http://news.sina.com.cn/")
						&& (u.endsWith("htm") || u.endsWith("html") || u
								.endsWith("shtml"))) {
					URL re = new URL(u, this.level + 1);
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
