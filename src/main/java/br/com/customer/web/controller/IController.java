package br.com.customer.web.controller;

import br.com.customer.openapi.model.PageDocumentResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

public interface IController{
    default URI getURI(Long id) {
        return ServletUriComponentsBuilder.fromCurrentServletMapping().path("/{id}").build()
                .expand(id).toUri();
    }
}
