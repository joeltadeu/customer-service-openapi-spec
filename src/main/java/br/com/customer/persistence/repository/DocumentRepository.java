package br.com.customer.persistence.repository;

import static br.com.customer.persistence.repository.queries.DocumentQueries.*;

import br.com.customer.openapi.model.DocumentFilter;
import br.com.customer.persistence.entity.Document;
import br.com.customer.persistence.entity.enums.DocumentTypeEnum;
import br.com.customer.web.exception.DataNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Log4j2
public class DocumentRepository implements IRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public DocumentRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public Document insert(Document document) {
    MapSqlParameterSource namedParameters = getNamedParameters(document);
    KeyHolder holder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(DOCUMENT_INSERT_QUERY, namedParameters, holder);
    document.setId(Objects.requireNonNull(holder.getKey()).longValue());

    return document;
  }

  public void update(Document document) {
    var namedParameters = getNamedParameters(document);
    namedParameters.addValue(DOCUMENT_ID_PARAM, document.getId());

    namedParameterJdbcTemplate.update(DOCUMENT_UPDATE_QUERY, namedParameters);
  }

  public void deleteByCustomerId(Long customerId) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(DOCUMENT_CUSTOMER_ID_PARAM, customerId);
    namedParameterJdbcTemplate.update(DOCUMENT_DELETE_BY_CUSTOMER_ID_QUERY, namedParameters);
  }

  public void delete(Long id) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(DOCUMENT_ID_PARAM, id);
    namedParameterJdbcTemplate.update(DOCUMENT_DELETE_BY_ID_QUERY, namedParameters);
  }

  public Document findByCustomerIdAndId(Long customerId, Long id) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(DOCUMENT_CUSTOMER_ID_PARAM, customerId);
    namedParameters.addValue(DOCUMENT_ID_PARAM, id);

    var optionalDocument = this.getEntity(DOCUMENT_FIND_BY_CUSTOMER_ID_QUERY, namedParameters);

    return optionalDocument.orElseThrow(
        () ->
            new DataNotFoundException(
                String.format(
                    "Document with id %s and customer id %s was not found", id, customerId)));
  }

  public List<Document> findAllByCustomerId(Long customerId) {
    var query = queryBuilder(DOCUMENT_FIND_ALL_BY_CUSTOMER_ID_QUERY);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(DOCUMENT_CUSTOMER_ID_PARAM, customerId);

    try {
      return namedParameterJdbcTemplate.query(
          query.toString(), namedParameters, DocumentRepository::mapRow);
    } catch (Exception e) {
      log.error(e);
      return Collections.emptyList();
    }
  }

  public List<Document> findAll(Long customerId, DocumentFilter filter) {
    var query = queryBuilder(DOCUMENT_FIND_ALL_QUERY);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();

    addConditions(query, namedParameters, filter, customerId);
    addPagination(
        query, filter.getPageSize(), (long) filter.getPageSize() * filter.getPageNumber());

    try {
      return namedParameterJdbcTemplate.query(
          query.toString(), namedParameters, DocumentRepository::mapRow);
    } catch (Exception e) {
      log.error(e);
      return Collections.emptyList();
    }
  }

  public int findAllCount(Long customerId, DocumentFilter filter) {
    var query = queryBuilder(DOCUMENT_FIND_COUNT_ALL_QUERY);

    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    addConditions(query, namedParameters, filter, customerId);

    var result =
        namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, Integer.class);
    return result != null ? result : 0;
  }

  private void addConditions(
      StringBuilder query,
      MapSqlParameterSource namedParameters,
      DocumentFilter filter,
      Long customerId) {

    query.append("WHERE");
    query.append(" d.customer_id = :customerId");
    namedParameters.addValue(DOCUMENT_CUSTOMER_ID_PARAM, customerId);

    if (filter.getDocumentNumber() != null) {
      query.append(" AND d.document_number LIKE :documentNumber");
      namedParameters.addValue(
          DOCUMENT_NUMBER_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getDocumentNumber()));
    }

    if (filter.getType() != null) {
      query.append(" AND d.type LIKE :type");
      namedParameters.addValue(
          DOCUMENT_TYPE_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getType().toString()));
    }
  }

  private MapSqlParameterSource getNamedParameters(Document document) {
    var namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(DOCUMENT_CUSTOMER_ID_PARAM, document.getCustomerId());
    namedParameters.addValue(DOCUMENT_TYPE_PARAM, document.getType().toString());
    namedParameters.addValue(DOCUMENT_NUMBER_PARAM, document.getDocumentNumber());
    return namedParameters;
  }

  private static Document mapRow(ResultSet rs, int rowNum) throws SQLException {
    return Document.builder()
        .id(rs.getLong("d.id"))
        .customerId(rs.getLong("d.customer_id"))
        .documentNumber(rs.getString("d.document_number"))
        .type(DocumentTypeEnum.valueOf(rs.getString("d.type")))
        .build();
  }

  private Optional<Document> getEntity(String sql, MapSqlParameterSource namedParameters) {
    try {
      return Optional.ofNullable(
          namedParameterJdbcTemplate.queryForObject(
              sql, namedParameters, DocumentRepository::mapRow));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
