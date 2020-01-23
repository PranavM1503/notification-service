package com.assignment.notification.services;

import com.assignment.notification.dto.SmsDetailsForElasticSearch;
import org.apache.kafka.clients.producer.Producer;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ElasticSearchService {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);
    @Autowired
    RestHighLevelClient client;

    public void indexSmsData(SmsDetailsForElasticSearch smsDetailsForElasticSearch) throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("id", smsDetailsForElasticSearch.getId());
        jsonMap.put("request_id", smsDetailsForElasticSearch.getRequestId().toString());
        jsonMap.put("phone_number", smsDetailsForElasticSearch.getPhoneNumber());
        jsonMap.put("message", smsDetailsForElasticSearch.getMessage());
        jsonMap.put("created_at", smsDetailsForElasticSearch.getCreatedAt());
        jsonMap.put("updated_at", smsDetailsForElasticSearch.getCreatedAt());

        IndexRequest indexRequest = new IndexRequest("smsdata", "smsdataelastic").id(Integer.toString(smsDetailsForElasticSearch.getId())).source(jsonMap);
//        IndexRequest indexRequest = new IndexRequest("smsData").id(Integer.toString(smsDetailsForElasticSearch.getId())).source(jsonMap);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
//        try {
//            indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        logger.info(indexResponse.getResult().toString());
    }

    public void getSmsDatabyId(String smsId) throws IOException {
        GetRequest getRequest = new GetRequest("smsdata", smsId);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        logger.info(getResponse.toString());
    }




}
