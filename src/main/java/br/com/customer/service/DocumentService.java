package br.com.customer.service;

import br.com.customer.openapi.model.DocumentFilter;
import br.com.customer.persistence.entity.Document;
import br.com.customer.persistence.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {
  private final DocumentRepository documentRepository;

  public Document create(Document document) {
    return documentRepository.insert(document);
  }

  public void update(Document document) {
    documentRepository.findByCustomerIdAndId(document.getCustomerId(), document.getId());
    documentRepository.update(document);
  }

  public Document findByCustomerIdAndId(Long customerId, Long id) {
    return documentRepository.findByCustomerIdAndId(customerId, id);
  }

  public Page<Document> findAll(Long customerId, DocumentFilter filter) {
    var documents = documentRepository.findAll(customerId, filter);
    var count = documentRepository.findAllCount(customerId, filter);

    return new PageImpl<>(
        documents, PageRequest.of(filter.getPageNumber(), filter.getPageSize()), count);
  }

  public void delete(Document document) {
    documentRepository.findByCustomerIdAndId(document.getCustomerId(), document.getId());
    documentRepository.delete(document.getId());
  }
}
