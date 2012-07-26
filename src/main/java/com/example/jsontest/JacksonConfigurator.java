package com.example.jsontest;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

@Provider
@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
@Produces({ MediaType.APPLICATION_JSON, "text/json" })
public class JacksonConfigurator extends ResteasyJacksonProvider {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ";

    private static final Logger log = Logger.getLogger(JacksonConfigurator.class.getName());

    public JacksonConfigurator() {
        super();
        log.info("configuring date handling");
        ObjectMapper mapper = _mapperConfig.getConfiguredMapper();
        if (mapper == null) {
        	mapper = new ObjectMapper();
        	_mapperConfig.setMapper(mapper);
        }
        configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        SerializationConfig serConfig = mapper.getSerializationConfig();
        //serConfig.set(Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        serConfig.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
        deserializationConfig.withDateFormat(new SimpleDateFormat(DATE_FORMAT));

    }

}