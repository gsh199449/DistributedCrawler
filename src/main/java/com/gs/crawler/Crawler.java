package com.gs.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.crawlDB.CrawlDB;
import com.gs.extractor.impl.DefaultContentExtractor;
import com.gs.extractor.impl.DefaultLinkExtractor;
import com.gs.extractor.impl.DefaultTitleExtractor;
import com.gs.extractor.impl.HTMLDownloader;
import com.gs.extractor.impl.SinaNewsContentExtractor;
import com.gs.extractor.impl.SinaNewsLinkExtractor;
import com.gs.extractor.impl.SinaNewsTitleExtractor;
import com.gs.extractor.impl.TecentNewsTitleExtractor;
import com.gs.extractor.impl.TencentNewsContentExtractor;
import com.gs.extractor.impl.TencentNewsLinkExtractor;
import com.gs.extractor.impl.WangYiWapNewsContentExtractor;
import com.gs.extractor.impl.WangYiWapNewsLinkExtractor;
import com.gs.extractor.impl.WangYiWapNewsTitleExtractor;
import com.gs.model.PagePOJO;
import com.gs.utils.URL;

public class Crawler {
	private String id;
	private int topN;
	private int deepth;
	private String seed;
	public LinkedList<URL> queue = new LinkedList<URL>();
	private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);
	private CrawlDB db;
	private Context context;
	private static final int maxGenerate = 20;

	public Crawler(String id, String seed, int topN, int deepth, CrawlDB db,
			Context context) {
		this.id = id;
		this.topN = topN;
		this.deepth = deepth;
		this.seed = seed;
		this.db = db;
		this.context = context;
	}

	public Set<PagePOJO> start() throws IOException, InterruptedException {
		long startTime = System.currentTimeMillis();
		LOG.info("Crawler start");
		Set<PagePOJO> indexSet = new HashSet<PagePOJO>();
		int counter = 0;// 当前的Crawler的计数器
		queue.add(new URL(seed, 0));
		while (!db.isEmpty() || !queue.isEmpty()) {
			queue.addAll(db.generate(maxGenerate));
			LOG.info("Generate URLs . Queue size : "+queue.size());
			Set<URL> toCrawl = new HashSet<URL>();// 本次从DB里索取的所有链接的子连接
			while (!queue.isEmpty()) {
				URL u = queue.remove();// 从队列里面拿出一个URL
				String html = HTMLDownloader.down(u);// 下载html
				if (html == null || html.equals(""))// 判断html是否为空
					continue;
				String content = null;
				String title = "";
				switch (NewsCenterMatcher.match(u.url)) {
				case Tencent:
					toCrawl.addAll(new TencentNewsLinkExtractor(deepth - 1,
							topN).extractFromHtml(html, u.level));
					content = new TencentNewsContentExtractor()
							.extractFromHtml(html);
					title = new TecentNewsTitleExtractor()
							.extractFromHtml(html);
					break;
				case Sina:
					toCrawl.addAll((new SinaNewsLinkExtractor(deepth - 1, topN)
							.extractFromHtml(html, u.level)));
					content = new SinaNewsContentExtractor()
							.extractFromHtml(html);
					title = new SinaNewsTitleExtractor().extractFromHtml(html);
					break;
				case WangYiWap:
					toCrawl.addAll(new WangYiWapNewsLinkExtractor(deepth - 1,
							topN).extractFromHtml(html, u.level));
					content = new WangYiWapNewsContentExtractor()
							.extractFromHtml(html);
					title = new WangYiWapNewsTitleExtractor()
							.extractFromHtml(html);
					break;
				case Unknow:
					toCrawl.addAll(new DefaultLinkExtractor(deepth - 1, topN)
							.extractFromHtml(html, u.level));
					content = new DefaultContentExtractor()
							.extractFromHtml(html);
					title = new DefaultTitleExtractor().extractFromHtml(html);
					break;
				default:
					break;
				}
				if (content == null || content.trim().equals("")) {// 如果内容为空,跳过
					LOG.info("Blank Content . Skip .");
					continue;
				}
				PagePOJO pojo = new PagePOJO();
				pojo.setId(Integer.parseInt(this.id + counter++));// 设置pageid
				pojo.setUrl(u.url);
				pojo.setContent(content);
				pojo.setTitle(title);
				indexSet.add(pojo);
				context.write(NullWritable.get(),
						new Text(pojo.toJson() + "\r"));// 将结果写入上下文当中
				LOG.info("Download Page . Level : " + u.level + "URL : " + u.url);// 打印当前Queue状态
			}
			db.inject(toCrawl);
			LOG.info("Inject URLs . Number : " + toCrawl.size());
		}
		LOG.info("Crawler exit . Use Time : "+(System.currentTimeMillis()-startTime));
		return indexSet;
	}
}
