package com.gs.indexer.solr;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.lucene.document.Field.Store;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.dao.PageDAO;
import com.gs.dao.impl.hbase.PageDAOHBaseImpl;
import com.gs.model.PagePOJO;

public class SolrSearcher {
	private static final Logger LOG = LoggerFactory.getLogger(SolrSearcher.class);
	/**
	 * @author GS
	 * @param queryString 查询字符串
	 * @param serverurl Solr服务器的URL,例如:http://localhost:8983/solr/
	 * @return 结果集
	 * @throws SolrServerException
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ZooKeeperConnectionException 
	 */
	public static final Set<PagePOJO> search(final String queryString,
			final String serverurl) throws SolrServerException{
		PageDAO dao = null;
		try {
			dao = new PageDAOHBaseImpl("page");
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SolrServer server = new HttpSolrServer(serverurl);
		SolrQuery query = new SolrQuery("content:"+queryString);
		//query.addSortField("content", SolrQuery.ORDER.asc);
		/*query.setHighlight(true); // 开启高亮组件
        query.addHighlightField("title");// 高亮字段
        query.setHighlightSimplePre("<font color=\"red\">");// 标记
        query.setHighlightSimplePost("</font>");
        query.setHighlightSnippets(1);//结果分片数，默认为1
        query.setHighlightFragsize(1000);//每个分片的最大长度，默认为100
	query.setStart(0);
		query.setRows(100);*/	
		QueryResponse response = server.query(query);
		SolrDocumentList docs = response.getResults();
		
		Set<PagePOJO> result = new HashSet<PagePOJO>();
		String[] queryStrings = queryString.split(" ");
		for (SolrDocument doc : docs) {
			String url = (String) doc.getFieldValue("url");
			PagePOJO pojo = null;
			try {
				pojo = dao.loadPage(url);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
			for (String q : queryStrings) {
				pojo.setContent(pojo.getContent().replaceAll(q,
						"<font color=\"red\">" + q + "</font>"));
			}
			result.add(pojo);
		}
		try {
			dao.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
