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
	private static String rootPath = "hdfs://gs-pc:9000/home/test/";
	private static String seeds = rootPath + "qq.txt";// 首页的链接暂存地
	private static int depth = 3;// 深度
	private static int topN = 100;// 每页抓取的最大链接数
	private static String outputPath = rootPath + "output";// 最终结果的输出路径
	private static String jobName = "DistributeCrawler";// Job的名称
	private static String crawlDBHost = "gs-pc";//CrawlDB的IP
	private static int crawlDBPort = 6377;//CrawlDB的端口
	private static String crawlDBPassword = "940409";//CrawlDB的密码
	private static int crawlDBTimeout = 1000;//CrawlDB的连接超时时间
	private static int crawlDBToCrawlDB = 0;//CrawlDB的待抓取的数据库编号
	private static int crawlDBCrawledDB = 1;//CrawlDB的已抓取的数据库编号
	private static String solrURL = "http://gs-pc:8888/solr";//Solr服务器URL
	private static String libsPath = "hdfs://gs-pc:9000/home/test/libs/";//Solr服务器URL
	private static String[] jarsName = {"gson-2.2.4.jar"};//Solr服务器URL
	private static Logger LOG = LoggerFactory.getLogger(MainClass.class);

	/**
	 * Mapper类
	 * 
	 * @author gaoshen
	 */
	public static class CrawlMapper extends
			Mapper<LongWritable, Text, NullWritable, Text> {

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			CrawlDB db = new CrawlDB(crawlDBHost, crawlDBPort, crawlDBPassword, crawlDBTimeout, crawlDBToCrawlDB, crawlDBCrawledDB);
			Crawler c = new Crawler(key.toString(), value.toString(), topN,
					depth, db, context);// 以Input文件的行偏移量作为crawler的id
			try {
				SolrIndex.index(c.start(), solrURL);
			} catch (SolrServerException e) {
				LOG.error(e.getMessage());
				e.printStackTrace();
			}
			LOG.info("Map finish");
		}
	}

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		org.apache.commons.configuration.Configuration conf = ConfigurationReader.parseJsonConf("conf/json.json");
		rootPath = conf.getString("rootPath");
		seeds = conf.getString("seeds");
		depth = conf.getInt("depth");
		topN = conf.getInt("topN");
		outputPath = conf.getString("outputPath");
		jobName = conf.getString("jobName");
		crawlDBHost = conf.getString("crawlDBHost");
		crawlDBPort = conf.getInt("crawlDBPort");
		crawlDBPassword = conf.getString("crawlDBPassword");
		crawlDBTimeout = conf.getInt("crawlDBTimeout");
		crawlDBToCrawlDB = conf.getInt("crawlDBToCrawlDB");
		crawlDBCrawledDB = conf.getInt("crawlDBCrawledDB");
		solrURL = conf.getString("solrURL");
		libsPath = conf.getString("libsPath");
		jarsName = conf.getStringArray("jarsName");
		LOG.info("Job init "+jobName+" start time : "+new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date(startTime)));
		Configuration hadoopConf = new Configuration();
		FileSystem fs = FileSystem.get(hadoopConf);
		if (fs.exists(new Path(outputPath))) {// 如果输出路径存在的话，删除他。
			LOG.info("Delete the output path" + outputPath);
			fs.delete(new Path(outputPath), true);
		}
		ExternalJARAdder adder = new ExternalJARAdder(fs, hadoopConf);
		if(!libsPath.endsWith("/")){libsPath = libsPath + "/";}
		for(String jar : jarsName){
			adder.add(libsPath + jar);
		}
		Job job = new Job(hadoopConf, jobName);
		job.setJarByClass(MainClass.class);
		job.setMapperClass(CrawlMapper.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(seeds));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		LOG.info("Job finish . Use : "+(startTime-System.currentTimeMillis()));
	}

}
