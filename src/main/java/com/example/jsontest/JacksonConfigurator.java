package com.example.jsontest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.module.SimpleModule;
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
		serConfig.set(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		SimpleModule module = new SimpleModule("MyModule", new Version(1, 0, 0, null));
		mapper.registerModule(module);
		module.addSerializer(new JsonSerializer<java.sql.Date>() {

			@Override
			public void serialize(java.sql.Date value, JsonGenerator jgen, SerializerProvider provider)
					throws IOException, JsonProcessingException {
				if (value == null) {
					jgen.writeNull();
					return;
				}

				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
				String f = format.format(value);
				jgen.writeString(f);

			}

			@Override
			public Class<java.sql.Date> handledType() {
				return java.sql.Date.class;
			}
		});

		serConfig.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
		deserializationConfig.withDateFormat(new SimpleDateFormat(DATE_FORMAT));
	}

}