package br.com.customer.web.controller;

import static br.com.customer.util.TestUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.customer.Application;
import br.com.customer.config.MapperConfig;
import br.com.customer.openapi.model.DocumentFilter;
import br.com.customer.persistence.entity.Document;
import br.com.customer.persistence.entity.enums.DocumentTypeEnum;
import br.com.customer.service.DocumentService;
import br.com.customer.web.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest({DocumentController.class})
@ContextConfiguration(classes = {Application.class, MapperConfig.class})
public class DocumentControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private DocumentService documentServiceMock;

  @Test
  public void createDocument_shouldCreate() throws Exception {

    final var documentId = 1L;
    final var customerId = 1L;
    final var document = getDocument(documentId);
    final var requestJson = getJsonFromObject(getDocumentRequest());

    when(documentServiceMock.create(any(Document.class))).thenReturn(document);

    mockMvc
        .perform(
            post("/v1/customers/{customerId}/documents", customerId)
                .contentType(APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().is(201))
        .andExpect(jsonPath("id").value(documentId));

    verify(documentServiceMock, times(1)).create(any(Document.class));
  }

  @Test
  public void updateDocument_shouldUpdate() throws Exception {

    final var documentId = 1L;
    final var customerId = 1L;
    final var requestJson = getJsonFromObject(getDocumentRequest());

    mockMvc
        .perform(
            put("/v1/customers/{customerId}/documents/{id}", customerId, documentId)
                .contentType(APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().is(200));

    verify(documentServiceMock, times(1)).update(any(Document.class));
  }

  @Test
  public void findDocumentById_shouldFind() throws Exception {

    final var documentId = 1L;
    final var customerId = 1L;
    final var type = DocumentTypeEnum.PASSPORT;
    final var documentNumber = getRandomString();
    final var entity = getDocument(documentId, type, documentNumber);

    when(documentServiceMock.findByCustomerIdAndId(customerId, documentId)).thenReturn(entity);

    mockMvc
        .perform(
            get("/v1/customers/{customerId}/documents/{id}", customerId, documentId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("id").value(documentId))
        .andExpect(jsonPath("type").value(DocumentTypeEnum.PASSPORT.name()))
        .andExpect(jsonPath("documentNumber").value(documentNumber));

    verify(documentServiceMock, times(1)).findByCustomerIdAndId(customerId, documentId);
  }

  @Test
  public void findDocumentById_shouldThrowNotFoundWhenNotFound() throws Exception {
    final var documentId = getRandomLong();
    final var customerId = 1L;
    when(documentServiceMock.findByCustomerIdAndId(customerId, documentId))
        .thenThrow(
            new DataNotFoundException(
                "Document with id %s and customer id %s was not found"
                    .formatted(customerId, documentId)));

    mockMvc
        .perform(
            get("/v1/customers/{customerId}/documents/{id}", customerId, documentId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(
            jsonPath("description")
                .value(
                    "Document with id %s and customer id %s was not found"
                        .formatted(customerId, documentId)));

    verify(documentServiceMock, times(1)).findByCustomerIdAndId(customerId, documentId);
  }

  @Test
  public void deleteDocument_shouldDeleteDocument() throws Exception {
    final var documentId = getRandomLong();
    final var customerId = getRandomLong();

    doNothing().when(documentServiceMock).delete(any(Document.class));

    mockMvc
        .perform(
            delete("/v1/customers/{customerId}/documents/{id}", customerId, documentId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(204));

    verify(documentServiceMock, times(1)).delete(any(Document.class));
  }

  @Test
  public void deleteDocument_shouldThrowNotFoundWhenNotFound() throws Exception {
    final var documentId = getRandomLong();
    final var customerId = getRandomLong();

    doThrow(
            new DataNotFoundException(
                "Document with id %s and customer id %s was not found".formatted(customerId, documentId)))
        .when(documentServiceMock)
        .delete(any(Document.class));

    mockMvc
        .perform(
            delete("/v1/customers/{customerId}/documents/{id}", customerId, documentId)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(
            jsonPath("description")
                .value(
                    "Document with id %s and customer id %s was not found"
                        .formatted(customerId, documentId)));

    verify(documentServiceMock, times(1)).delete(any(Document.class));
  }

  @Test
  public void findDocuments_shouldFind() throws Exception {
    final var customerId = getRandomLong();
    var filter = new DocumentFilter();
    when(documentServiceMock.findAll(customerId, filter)).thenReturn(getPageDocument());

    mockMvc
        .perform(get("/v1/customers/{customerId}/documents", customerId).contentType(APPLICATION_JSON))
        .andExpect(status().is(200));

    verify(documentServiceMock, times(1)).findAll(customerId, filter);
  }
}
