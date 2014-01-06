package com.gs.indexer.solr;

import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.gs.dao.PageDAO;
import com.gs.dao.impl.PageDAOHBaseImpl;
import com.gs.model.PagePOJO;


public class SolrSearcher {
	public static final Set<PagePOJO> search(final String queryString,
			final String serverurl) throws SolrServerException {
		PageDAO dao = new PageDAOHBaseImpl();
		SolrServer server = new HttpSolrServer(serverurl);
		SolrQuery query = new SolrQuery(queryString);
		query.setStart(0);
		query.setRows(100);
		QueryResponse response = server.query(query);
		SolrDocumentList docs = response.getResults();
		Set<PagePOJO> result = new HashSet<PagePOJO>();
		for (SolrDocument doc : docs) {
			String id = (String) doc.getFieldValue("id");
			result.add(dao.loadPage(id));
		}
		return result;
	}
}
