package com.assignment.notification.services;

import com.assignment.notification.dto.*;
import com.assignment.notification.models.ElasticQueryScrollResponse;
import com.assignment.notification.models.ElasticQueryScrollResponse2;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;

import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
//@Slf4j
public class ElasticSearchService {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    private final RestHighLevelClient client;

    @Autowired
    public ElasticSearchService(RestHighLevelClient restHighLevelClient){
        this.client = restHighLevelClient;
    }

    public void indexSmsData(SmsDetailsForElasticSearch smsDetailsForElasticSearch) throws IOException {

        Map<String, Object> jsonMap = getMapping(smsDetailsForElasticSearch);
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

//    public List<ElasticQueryForSMSDTO> getAllSmsWithGivenText(String text) throws IOException {
//
//        List <ElasticQueryForSMSDTO> requiredSmsDetails = new ArrayList<>();
//
//        MatchQueryBuilder matchQueryBuilder;
//        matchQueryBuilder = QueryBuilders.matchQuery("message", text)
//                .fuzziness(Fuzziness.AUTO)
//                .prefixLength(2)
//                .maxExpansions(10);
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(matchQueryBuilder);
//        sourceBuilder.from(0);
//        sourceBuilder.size(10);
//        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
//
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices("smsdata");
//        searchRequest.source(sourceBuilder);
//
//        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
//        for(SearchHit searchHit : searchResponse.getHits().getHits()){
////            logger.info(searchHit.getSourceAsString() + "\n");
//            ElasticQueryForSMSDTO smsDetail = new ObjectMapper().readValue(searchHit.getSourceAsString(),ElasticQueryForSMSDTO.class);
//            requiredSmsDetails.add(smsDetail);
//        }
//        return requiredSmsDetails;
//    }

    public ElasticQueryScrollResponse getAllSmsWithGivenText(SmsElasticQueryDTO smsElasticQueryDTO) throws IOException {

        List <ElasticQueryForSMSDTO> requiredSmsDetails = new ArrayList<>();
        String scrollId = smsElasticQueryDTO.getScrollId();

        if(scrollId.equals("")){
            MatchQueryBuilder matchQueryBuilder;
            matchQueryBuilder = QueryBuilders.matchQuery("message", smsElasticQueryDTO.getText());

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(matchQueryBuilder);
            sourceBuilder.size(2);
            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("smsdata");
            searchRequest.source(sourceBuilder);
            searchRequest.scroll(scroll);

            SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
            String scrollIdLatest = searchResponse.getScrollId();
            for(SearchHit searchHit : searchResponse.getHits().getHits()){
    //            logger.info(searchHit.getSourceAsString() + "\n");
                ElasticQueryForSMSDTO smsDetail = new ObjectMapper().readValue(searchHit.getSourceAsString(),ElasticQueryForSMSDTO.class);
                requiredSmsDetails.add(smsDetail);
            }

            ElasticQueryScrollResponse response = new ElasticQueryScrollResponse(requiredSmsDetails, scrollIdLatest);
            return response;
        }

        else{
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueSeconds(30));
            SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchScrollResponse.getScrollId();

            for(SearchHit searchHit : searchScrollResponse.getHits().getHits()){
                //            logger.info(searchHit.getSourceAsString() + "\n");
                ElasticQueryForSMSDTO smsDetail = new ObjectMapper().readValue(searchHit.getSourceAsString(),ElasticQueryForSMSDTO.class);
                requiredSmsDetails.add(smsDetail);
            }

            ElasticQueryScrollResponse response = new ElasticQueryScrollResponse(requiredSmsDetails, scrollId);
            return response;

        }
    }

    public ElasticQueryScrollResponse2 getSmsBetweenGivenTime(SmsTimeQueryRequestDTO smsTimeQueryRequestDTO) throws IOException{
        List<SmsTimeQueryDTO> requiredSmsDetails = new ArrayList<>();
        String scrollId = smsTimeQueryRequestDTO.getScrollId();

        if(scrollId.equals("")){
            BoolQueryBuilder resultQuery = this.getSmsBetweenGivenTimeQueryBuilder(smsTimeQueryRequestDTO);


            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(resultQuery);
//        sourceBuilder.from(0);
            sourceBuilder.size(3);
            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("smsdata");
            searchRequest.source(sourceBuilder);
            searchRequest.scroll(scroll);

            SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
            String scrollIdLatest = searchResponse.getScrollId();
            for(SearchHit searchHit : searchResponse.getHits().getHits()){
//            logger.info(searchHit.getSourceAsString() + "\n");
                ElasticQueryForSMSDTO smsDetail = new ObjectMapper().readValue(searchHit.getSourceAsString(),ElasticQueryForSMSDTO.class);
                SmsTimeQueryDTO smsTimeQueryDTO = new SmsTimeQueryDTO(smsDetail.getRequest_id(), smsDetail.getMessage());
                requiredSmsDetails.add(smsTimeQueryDTO);
            }

            ElasticQueryScrollResponse2 response = new ElasticQueryScrollResponse2(requiredSmsDetails, scrollIdLatest);
            return response;
        }
        else{
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueSeconds(30));
            SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchScrollResponse.getScrollId();

            for(SearchHit searchHit : searchScrollResponse.getHits().getHits()){
                ElasticQueryForSMSDTO smsDetail = new ObjectMapper().readValue(searchHit.getSourceAsString(),ElasticQueryForSMSDTO.class);
                SmsTimeQueryDTO smsTimeQueryDTO = new SmsTimeQueryDTO(smsDetail.getRequest_id(), smsDetail.getMessage());
                requiredSmsDetails.add(smsTimeQueryDTO);
            }

            ElasticQueryScrollResponse2 response = new ElasticQueryScrollResponse2(requiredSmsDetails, scrollId);
            return response;
        }

    }


    public BoolQueryBuilder getSmsBetweenGivenTimeQueryBuilder(SmsTimeQueryRequestDTO smsTimeQueryRequestDTO){
        String phone_number = smsTimeQueryRequestDTO.getPhone_number();
        String startDateTime = smsTimeQueryRequestDTO.getStartDateTime();
        String endDateTime = smsTimeQueryRequestDTO.getEndDateTime();

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("phone_number", phone_number);
        RangeQueryBuilder rangeQueryForTime = QueryBuilders.rangeQuery("created_at").gte(startDateTime).lte(endDateTime);

        BoolQueryBuilder resultQuery = QueryBuilders.boolQuery().must(matchQueryBuilder).must(rangeQueryForTime);

        return resultQuery;
    }

    public Map<String, Object> getMapping(SmsDetailsForElasticSearch smsDetailsForElasticSearch){
        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("id", smsDetailsForElasticSearch.getId());
        jsonMap.put("request_id", smsDetailsForElasticSearch.getRequestId().toString());
        jsonMap.put("phone_number", smsDetailsForElasticSearch.getPhoneNumber());
        jsonMap.put("message", smsDetailsForElasticSearch.getMessage());
        jsonMap.put("created_at", smsDetailsForElasticSearch.getCreatedAt());
        jsonMap.put("updated_at", smsDetailsForElasticSearch.getCreatedAt());
        return jsonMap;
    }

}
