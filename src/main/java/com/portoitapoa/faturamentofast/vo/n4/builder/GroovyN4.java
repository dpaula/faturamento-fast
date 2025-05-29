package com.portoitapoa.faturamentofast.vo.n4.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.portoitapoa.faturamentofast.vo.n4.model.N4Return;
import com.portoitapoa.faturamentofast.vo.n4.vo.N4InputVO;
import com.portoitapoa.faturamentofast.vo.n4.vo.N4ParameterVO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Lima on 26/05/2021
 */
@Getter
@NoArgsConstructor
public class GroovyN4 {

	public static Boolean isSuccess(final N4Return n4Return) throws Exception {
		return n4Return.getStatus().equals("SUCCESS");
	}

	public static N4InputVO inputExcludeNull(final Object object) throws Exception {
		return process(object, true);
	}

	public static N4InputVO input(final Object object) throws Exception {
		return process(object, false);
	}

	private static N4InputVO process(final Object object, final boolean excludeNulls) throws Exception {
		Annotation annotationClass = object.getClass().getAnnotation(GroovyClass.class);
		String groovyName = ((GroovyClass)annotationClass).value();
		List<N4ParameterVO> n4ParameterVOList = new ArrayList();
		Field[] var5 = object.getClass().getDeclaredFields();
		int var6 = var5.length;

		for(int var7 = 0; var7 < var6; ++var7) {
			Field field = var5[var7];
			Annotation annotationField = field.getAnnotation(GroovyParameter.class);
			String parameterName = ((GroovyParameter)annotationField).value();
			String parameterValue = excludeNulls ? null : "";
			if (field.get(object) != null) {
				LocalDateTime parameterLocalDateTime;
				DateTimeFormatter formatter;
				if (field.getType().equals(LocalDateTime.class)) {
					formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
					parameterLocalDateTime = (LocalDateTime)field.get(object);
					parameterValue = parameterLocalDateTime.format(formatter);
				} else if (field.getType().equals(LocalDate.class)) {
					formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					parameterLocalDateTime = (LocalDateTime)field.get(object);
					parameterValue = parameterLocalDateTime.format(formatter);
				} else if (field.getType().equals(BigDecimal.class)) {
					BigDecimal parameterBigDecimal = (BigDecimal)field.get(object);
					parameterValue = parameterBigDecimal.toString();
				} else if (field.getType().equals(Long.class)) {
					Long parameterLong = (Long)field.get(object);
					parameterValue = parameterLong.toString();
				} else {
					parameterValue = excludeNulls && field.get(object) == null ? null : (String)field.get(object);
				}
			}

			if (parameterValue != null || !excludeNulls) {
				n4ParameterVOList.add(N4ParameterVO.builder().id(parameterName).value(parameterValue).build());
			}
		}

		return N4InputVO.builder().className(groovyName).parameters(n4ParameterVOList).build();
	}


	public static Object output(final String json, final Type type) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final JavaType javaType = mapper.getTypeFactory()
			.constructType(type);
		final ObjectReader objectReader = mapper.readerFor(javaType);
		return objectReader.readValue(json);
	}
}
