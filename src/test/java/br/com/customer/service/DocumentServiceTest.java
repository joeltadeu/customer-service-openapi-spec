package br.com.customer.service;

import static br.com.customer.util.TestUtil.*;
import static br.com.customer.util.TestUtil.getRandomString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.customer.persistence.entity.Document;
import br.com.customer.persistence.entity.enums.DocumentTypeEnum;
import br.com.customer.persistence.repository.DocumentRepository;
import br.com.customer.web.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {
  @Mock DocumentRepository documentRepositoryMock;

  @InjectMocks DocumentService documentService;

  @Captor private ArgumentCaptor<Document> documentArgumentCaptor;

  @Test
  public void createDocument_shouldCreateDocument() {
    final var documentNumber = getRandomString();
    final var type = DocumentTypeEnum.PASSPORT;
    final var documentId = getRandomLong();

    final var incomeDocument = getDocument(null, type, documentNumber);
    final var outcomeDocument = getDocument(documentId, type, documentNumber);

    when(documentRepositoryMock.insert(documentArgumentCaptor.capture()))
        .thenReturn(outcomeDocument);

    final var result = documentService.create(incomeDocument);

    assertEquals(outcomeDocument, result);

    final var argumentCaptorValue = documentArgumentCaptor.getValue();

    assertEquals(incomeDocument.getDocumentNumber(), argumentCaptorValue.getDocumentNumber());
    assertEquals(incomeDocument.getType(), argumentCaptorValue.getType());
    assertNull(argumentCaptorValue.getId());

    verify(documentRepositoryMock, times(1)).insert(argumentCaptorValue);
  }

  @Test
  public void updateDocument_shouldUpdateDocument() {
    final var documentId = getRandomLong();
    final var customerId = getRandomLong();

    final var document = getDocument(documentId, customerId);

    when(documentRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong())).thenReturn(document);

    documentService.update(document);

    verify(documentRepositoryMock, times(1)).findByCustomerIdAndId(customerId, documentId);
    verify(documentRepositoryMock, times(1)).update(document);
  }

  @Test
  public void updateDocument_shouldNotUpdateWhenDocumentNotFound() {
    final var documentId = getRandomLong();
    final var customerId = getRandomLong();
    final var document = getDocument(documentId, customerId);

    when(documentRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong()))
        .thenThrow(
            new DataNotFoundException(
                String.format("Document with id %s was not found", documentId)));

    final var assertThrows =
        assertThrows(DataNotFoundException.class, () -> documentService.update(document));
    assertEquals(
        "Document with id %s was not found".formatted(documentId), assertThrows.getMessage());
    verify(documentRepositoryMock, times(1)).findByCustomerIdAndId(customerId, documentId);
    verify(documentRepositoryMock, times(0)).update(document);
  }

  @Test
  public void deleteDocumentById_shouldDelete() {
    final var customerId = getRandomLong();
    final var documentId = getRandomLong();
    final var document = getDocument(documentId, customerId);

    when(documentRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong())).thenReturn(document);
    doNothing().when(documentRepositoryMock).delete(documentId);
    documentService.delete(document);

    verify(documentRepositoryMock, times(1)).findByCustomerIdAndId(anyLong(), anyLong());
    verify(documentRepositoryMock, times(1)).delete(anyLong());
  }

  @Test
  public void findDocumentById_shouldFind() {
    final var documentId = getRandomLong();
    final var customerId = getRandomLong();
    final var returnedDocument = getDocument(documentId);

    when(documentRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong()))
        .thenReturn(returnedDocument);

    final var documentReturnedFromDatabase =
        documentService.findByCustomerIdAndId(customerId, documentId);

    assertEquals(returnedDocument, documentReturnedFromDatabase);
    verify(documentRepositoryMock, times(1)).findByCustomerIdAndId(anyLong(), anyLong());
  }

  @Test
  public void findDocumentById_shouldNotFind() {
    final var documentId = getRandomLong();
    final var customerId = getRandomLong();

    when(documentRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong()))
        .thenThrow(
            new DataNotFoundException(
                String.format("Document with id %s was not found", documentId)));

    final var assertThrows =
        assertThrows(
            DataNotFoundException.class,
            () -> documentService.findByCustomerIdAndId(customerId, documentId));

    assertEquals(
        "Document with id %s was not found".formatted(documentId), assertThrows.getMessage());

    verify(documentRepositoryMock, times(1)).findByCustomerIdAndId(anyLong(), anyLong());
  }

  @Test
  public void findAllDocument_shouldFind() {
    final var filter = getDocumentFilter();
    final var documents = getDocuments();
    final var customerId = getRandomLong();

    when(documentRepositoryMock.findAll(customerId, filter)).thenReturn(documents);
    when(documentRepositoryMock.findAllCount(customerId, filter)).thenReturn(documents.size());

    final var pageDocument = documentService.findAll(customerId, filter);

    assertEquals(documents.size(), pageDocument.getTotalElements());
    assertEquals(1, pageDocument.getTotalPages());
    assertEquals(10, pageDocument.getSize());
    assertNotNull(pageDocument.getContent());
    assertEquals(0, pageDocument.getNumber());
    assertEquals(documents.size(), pageDocument.getNumberOfElements());

    verify(documentRepositoryMock, times(1)).findAll(customerId, filter);
    verify(documentRepositoryMock, times(1)).findAllCount(customerId, filter);
  }
}
