package com.gs.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.crawlDB.CrawlDB;
import com.gs.crawler.Crawler;
import com.gs.indexer.solr.SolrIndex;

/**
 * Crawler运行主类，包括Mapper和Main. 我们认为url.txt中的连接为0深度，所设置的deepth为页面level。
 * @author gaoshen
 */
public class MainClass {
	private static final String rootPath = "hdfs://gs-pc:9000/home/test/";
	private static final String dst = rootPath + "qq.txt";// 首页的链接暂存地
	private static final int depth = 3;// 深度
	private static final int topN = 80;// 每页抓取的最大链接数
	private static final String outputPath = rootPath + "output";// 最终结果的输出路径
	private static final String jobName = "DistributeCrawler";// Job的名称
	private static final String crawlDBHost = "localhost";//CrawlDB的IP
	private static final int crawlDBPost = 6377;//CrawlDB的端口
	private static final String crawlDBPassword = "940409";//CrawlDB的密码
	private static final int crawlDBTimeout = 1000;//CrawlDB的连接超时时间
	private static final int crawlDBToCrawlDB = 0;//CrawlDB的待抓取的数据库编号
	private static final int crawlDBCrawledDB = 1;//CrawlDB的已抓取的数据库编号
	private static final String SolrURL = "http://localhost:8888/solr";//Solr服务器URL
	private static final Logger LOG = LoggerFactory.getLogger(MainClass.class);

	/**
	 * Mapper类
	 * 
	 * @author gaoshen
	 */
	public static class CrawlMapper extends
			Mapper<LongWritable, Text, NullWritable, Text> {

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			CrawlDB db = new CrawlDB(crawlDBHost, crawlDBPost, crawlDBPassword, crawlDBTimeout, crawlDBToCrawlDB, crawlDBCrawledDB);
			Crawler c = new Crawler(key.toString(), value.toString(), topN,
					depth, db, context);// 以Input文件的行偏移量作为crawler的id
			try {
				SolrIndex.index(c.start(), SolrURL);
			} catch (SolrServerException e) {
				LOG.error(e.getMessage());
				e.printStackTrace();
			}
			LOG.info("Map finish");
		}
	}

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		LOG.info("Job init "+jobName+" start time : "+new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date(startTime)));
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		if (fs.exists(new Path(outputPath))) {// 如果输出路径存在的话，删除他。
			LOG.info("Delete the output path" + outputPath);
			fs.delete(new Path(outputPath), true);
		}
		ExternalJARAdder adder = new ExternalJARAdder(fs, conf);
		adder.add(rootPath + "libs/gson-2.2.4.jar");
		adder.add(rootPath + "libs/solr-solrj-4.0.0.jar");
		adder.add(rootPath + "libs/commons-dbcp-1.4.jar");
		adder.add(rootPath + "libs/commons-pool-1.5.4.jar");
		adder.add(rootPath + "libs/httpclient-4.1.3.jar");
		adder.add(rootPath + "libs/httpcore-4.1.4.jar");
		adder.add(rootPath + "libs/httpmime-4.1.3.jar");
		Job job = new Job(conf, jobName);
		job.setJarByClass(MainClass.class);
		job.setMapperClass(CrawlMapper.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(dst));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		LOG.info("Job finish . Use : "+(startTime-System.currentTimeMillis()));
	}

}
