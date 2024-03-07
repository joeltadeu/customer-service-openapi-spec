package br.com.customer.persistence.repository;

import static br.com.customer.persistence.repository.queries.EmailQueries.*;

import br.com.customer.openapi.model.EmailFilter;
import br.com.customer.persistence.entity.Email;
import br.com.customer.persistence.entity.enums.EmailTypeEnum;
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
public class EmailRepository implements IRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public EmailRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public Email insert(Email email) {
    MapSqlParameterSource namedParameters = getNamedParameters(email);
    KeyHolder holder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(EMAIL_INSERT_QUERY, namedParameters, holder);
    email.setId(Objects.requireNonNull(holder.getKey()).longValue());

    return email;
  }

  public void update(Email email) {
    var namedParameters = getNamedParameters(email);
    namedParameters.addValue(EMAIL_ID_PARAM, email.getId());

    namedParameterJdbcTemplate.update(EMAIL_UPDATE_QUERY, namedParameters);
  }

  public void deleteByCustomerId(Long customerId) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(EMAIL_CUSTOMER_ID_PARAM, customerId);
    namedParameterJdbcTemplate.update(EMAIL_DELETE_BY_CUSTOMER_ID_QUERY, namedParameters);
  }

  public void delete(Long id) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(EMAIL_ID_PARAM, id);
    namedParameterJdbcTemplate.update(EMAIL_DELETE_BY_ID_QUERY, namedParameters);
  }

  public Email findByCustomerIdAndId(Long customerId, Long id) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(EMAIL_CUSTOMER_ID_PARAM, customerId);
    namedParameters.addValue(EMAIL_ID_PARAM, id);

    var optionalEmail = this.getEntity(EMAIL_FIND_BY_CUSTOMER_ID_QUERY, namedParameters);

    return optionalEmail.orElseThrow(
        () ->
            new DataNotFoundException(
                String.format(
                    "Email with id %s and customer id %s was not found", id, customerId)));
  }

  public List<Email> findAllByCustomerId(Long customerId) {
    var query = queryBuilder(EMAIL_FIND_ALL_BY_CUSTOMER_ID_QUERY);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(EMAIL_CUSTOMER_ID_PARAM, customerId);

    try {
      return namedParameterJdbcTemplate.query(
          query.toString(), namedParameters, EmailRepository::mapRow);
    } catch (Exception e) {
      log.error(e);
      return Collections.emptyList();
    }
  }

  public List<Email> findAll(Long customerId, EmailFilter filter) {
    var query = queryBuilder(EMAIL_FIND_ALL_QUERY);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();

    addConditions(query, namedParameters, filter, customerId);
    addPagination(
        query, filter.getPageSize(), (long) filter.getPageSize() * filter.getPageNumber());

    try {
      return namedParameterJdbcTemplate.query(
          query.toString(), namedParameters, EmailRepository::mapRow);
    } catch (Exception e) {
      log.error(e);
      return Collections.emptyList();
    }
  }

  public int findAllCount(Long customerId, EmailFilter filter) {
    var query = queryBuilder(EMAIL_FIND_COUNT_ALL_QUERY);

    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    addConditions(query, namedParameters, filter, customerId);

    var result =
        namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, Integer.class);
    return result != null ? result : 0;
  }

  private void addConditions(
      StringBuilder query,
      MapSqlParameterSource namedParameters,
      EmailFilter filter,
      Long customerId) {

    query.append("WHERE");
    query.append(" e.customer_id = :customerId");
    namedParameters.addValue(EMAIL_CUSTOMER_ID_PARAM, customerId);

    if (filter.getEmail() != null) {
      query.append(" AND e.email LIKE :email");
      namedParameters.addValue(
          EMAIL_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getEmail()));
    }

    if (filter.getType() != null) {
      query.append(" AND e.type LIKE :type");
      namedParameters.addValue(
          EMAIL_TYPE_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getType().toString()));
    }
  }

  private MapSqlParameterSource getNamedParameters(Email email) {
    var namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(EMAIL_CUSTOMER_ID_PARAM, email.getCustomerId());
    namedParameters.addValue(EMAIL_TYPE_PARAM, email.getType().toString());
    namedParameters.addValue(EMAIL_PARAM, email.getEmail());
    return namedParameters;
  }

  private static Email mapRow(ResultSet rs, int rowNum) throws SQLException {
    return Email.builder()
        .id(rs.getLong("e.id"))
        .customerId(rs.getLong("e.customer_id"))
        .email(rs.getString("e.email"))
        .type(EmailTypeEnum.valueOf(rs.getString("e.type")))
        .build();
  }

  private Optional<Email> getEntity(String sql, MapSqlParameterSource namedParameters) {
    try {
      return Optional.ofNullable(
          namedParameterJdbcTemplate.queryForObject(sql, namedParameters, EmailRepository::mapRow));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
