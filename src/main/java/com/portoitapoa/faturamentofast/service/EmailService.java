package com.portoitapoa.faturamentofast.service;

import com.portoitapoa.faturamentofast.client.impl.EmailClient;
import com.portoitapoa.faturamentofast.vo.EmailInputSimples;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Fernando de Lima
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService {

    @Value("#{'${email.destinatarios}'.split(',')}")
    private List<String> destinatarios;

    @Value("${token.email}")
    private String token;

    private final EmailClient client;

//    public void enviar(final EmailInputSimples email) {
//        email.setDestinatarios(destinatarios);
//        client.enviarSimples(email);
//    }

    public void enviar(final EmailInputSimples email){
        final String HEADER_AUTH = "Authorization";
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTH, token);


        email.setDestinatarios(destinatarios);
        client.enviarSimples(headers, email);
    }
}
