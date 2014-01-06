package com.gs.indexer.solr;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import com.gs.model.PagePOJO;

public class SolrIndex {
	
	/**
	 * @author GS
	 * @param set 待索引的PagePOJO集合
	 * @param SERVER_URL Solr服务器的ServerURL,例如:http://localhost:8983/solr/
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public static final void index(final Set<PagePOJO> set,
			final String SERVER_URL) throws SolrServerException, IOException {
		SolrServer server = new HttpSolrServer(SERVER_URL);
		Set<SolrInputDocument> result = new HashSet<SolrInputDocument>();
		for (PagePOJO pojo : set) {
			SolrInputDocument doc;
			doc = new SolrInputDocument();
			doc.addField("id", pojo.getId());
			doc.addField("title", pojo.getTitle());
			doc.addField("content", pojo.getContent());
			result.add(doc);
		}
		server.add(result);
		server.commit();
	}
}
