package com.gs.extractor.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gs.extractor.Extractor;
import com.gs.utils.URL;

public final class TencentExtractor extends Extractor {

	public TencentExtractor(String html, int level, int topN, int depth) {
		super(html, level, topN, depth);
	}

	@Override
	public void proceed() {
		String titleRegex = "<title>(.+?)_新闻_腾讯网</title>";// 去掉最后的尾坠
		Pattern titlept = Pattern.compile(titleRegex);
		Matcher titlemt = titlept.matcher(this.html);
		if (titlemt.find()) {
			this.title = titlemt.group(1);
		}
		// ///////////////////////////////////////////
		String contentregex = "<div id=\"Cnt-Main-Article-QQ\".*?>(.*?)</P>.?</div>";// 正文最后是</p>换行</div>或者</P></div>为结束标志的
		Pattern contentpt = Pattern.compile(contentregex, Pattern.DOTALL);
		Matcher contentmt = contentpt.matcher(this.html);
		if (contentmt.find()) {
			this.content = contentmt.group(1);
		}
		Pattern pt1 = Pattern.compile("<script>.*?</script>", Pattern.DOTALL);
		Matcher mt1 = pt1.matcher(this.content);
		this.content = mt1.replaceAll("");
		Pattern pt2 = Pattern.compile("<style>.*?</style>", Pattern.DOTALL);
		Matcher mt2 = pt2.matcher(this.content);
		this.content = mt2.replaceAll("");
		this.content = this.content.replaceAll("<.*?>", "");// 抹掉所有尖括号的内容
		this.content = this.content.replaceAll("\\s", "");// 抹掉所有空白
		this.content = this.content.replaceAll("正在播放", "");// 停用词
		this.content = this.content.replaceAll("资料图", "");// 停用词
		// //////////////////////////////////////////
		if (this.level >= this.depth && this.depth != 0) {
			;// 若已经超过抓取深度则不再提取
		} else {
			this.set = new HashSet<URL>();
			String linkregex = "<a\\s.*?href=\"([^\"]+)\"[^>]*>(.*?)</a>";
			Pattern linkpt = Pattern.compile(linkregex);
			Matcher linkmt = linkpt.matcher(this.html);
			int counter = 0;// 已抽取的连接计数器
			while (linkmt.find()) {
				String u = linkmt.group(1);
				if (u.startsWith("http://news.qq.com")
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
	}

}
