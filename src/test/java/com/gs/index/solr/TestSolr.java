package com.gs.index.solr;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.gs.indexer.solr.SolrIndex;
import com.gs.indexer.solr.SolrSearcher;
import com.gs.model.PagePOJO;

public class TestSolr {

	@Test
	public void test() throws SolrServerException, IOException {
		String url = "http://localhost:8888/solr";
		SolrServer server = new HttpSolrServer(url);
		SolrInputDocument doc1 = new SolrInputDocument();
		doc1.addField("id", "1");
		doc1.addField("title", "习近平亲民广获赞誉 美媒称领导中共魅力攻势");
		doc1.addField("content", "最近一段时间，国家主席习近平排队吃包子、办公室发表新年贺词引起了广泛关注。人们发现，国家最高领导人吃的是寻常百姓饭，办公室里摆的也是家人照片。");
		server.add(doc1);
		server.commit();
	}
	
	@Test
	public void testSearch() throws SolrServerException{
		String url = "http://localhost:8888/solr";
		  SolrServer server = new HttpSolrServer(url);
		  SolrQuery query = new SolrQuery("火箭");
		  query.setStart(0);
		  query.setRows(100);
		   QueryResponse response = server.query(query);
		   SolrDocumentList docs = response.getResults();
		   System.out.println("文档个数：" + docs.getNumFound());
		   System.out.println("查询时间：" + response.getQTime());
		   for (SolrDocument doc : docs) {
		    System.out.println("id: " + doc.getFieldValue("id"));
		    System.out.println("title: " + doc.getFieldValue("title"));
		    System.out.println("contect: "+doc.getFieldValue("content"));
		   }
	}
	
	@Test
	public void testIndex() throws SolrServerException, IOException{
		Set<PagePOJO> set = new HashSet<PagePOJO>();
		set.add(new PagePOJO("http://news.qq.com",1,"哈哈哈","hhh"));
		SolrIndex.index(set, "http://localhost:8888/solr");
	}
	
	@Test
	public void testSearch2() throws SolrServerException{
		for(PagePOJO pojo : SolrSearcher.search("中国", "http://localhost:8888/solr")){
			System.out.println(pojo);
		}
	}

}
