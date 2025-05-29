package com.portoitapoa.faturamentofast.error.feign;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.portoitapoa.faturamentofast.error.WSFaturamentoFastException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.Reader;
import java.nio.charset.Charset;

/**
 * @author Carlos Lima on 19/05/2021
 */
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(final String methodKey, final Response response) {

        String message = null;
        Reader reader = null;

        try {
            reader = response.body()
                .asReader(Charset.defaultCharset());
            final String result = CharStreams.toString(reader);
            final ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            final WSFaturamentoFastException exceptionMessage = mapper.readValue(result,
                    WSFaturamentoFastException.class);

            message = exceptionMessage.getMessage();

        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();

            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        return switch (response.status()) {
            case 400 -> new WSFaturamentoFastException(message == null ? "Requisição inválida!" :
                message);
            case 404 -> new WSFaturamentoFastException(message == null ? "Não encontrado!" :
                message);
            case 401 -> new WSFaturamentoFastException(message == null ? "Acesso negado!" :
                message);
            default -> new Exception(response.reason());
        };
    }
}