package br.com.customer.web.controller;

import br.com.customer.openapi.api.DocumentControllerApi;
import br.com.customer.openapi.model.*;
import br.com.customer.persistence.entity.Document;
import br.com.customer.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DocumentController implements DocumentControllerApi, IController {
  private final DocumentService service;
  private final ObjectMapper objectMapper;

  @Override
  public ResponseEntity<DocumentResponse> createDocument(Long customerId, DocumentRequest request) {
    var document = objectMapper.convertValue(request, Document.class);
    document.setCustomerId(customerId);
    var savedDocument = service.create(document);
    var documentResponse = objectMapper.convertValue(savedDocument, DocumentResponse.class);

    return ResponseEntity.created(getURI(documentResponse.getId())).body(documentResponse);
  }

  @Override
  public ResponseEntity<Void> updateDocument(Long customerId, Long id, DocumentRequest request) {
    var document = objectMapper.convertValue(request, Document.class);
    document.setId(id);
    document.setCustomerId(customerId);
    service.update(document);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<DocumentResponse> findDocumentById(Long customerId, Long id) {

    final var document = service.findByCustomerIdAndId(customerId, id);
    final var documentResponse = objectMapper.convertValue(document, DocumentResponse.class);

    return ResponseEntity.ok(documentResponse);
  }

  @Override
  public ResponseEntity<Void> deleteDocument(Long customerId, Long id) {
    var document = Document.builder().id(id).customerId(customerId).build();
    service.delete(document);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<PageDocumentResponse> listAllDocuments(
      Long customerId, DocumentFilter filter) {

    Page<Document> documents = service.findAll(customerId, filter);

    var fetchedList =
        documents.stream()
            .map(document -> objectMapper.convertValue(document, DocumentResponse.class))
            .collect(Collectors.toList());

    var page = new PageImpl<>(fetchedList, documents.getPageable(), documents.getTotalElements());
    var pageResponse = objectMapper.convertValue(page, PageDocumentResponse.class);
    return ResponseEntity.ok(pageResponse);
  }
}
