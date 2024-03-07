package br.com.customer.persistence.repository;

import static br.com.customer.persistence.repository.queries.CustomerQueries.*;

import br.com.customer.openapi.model.CustomerFilter;
import br.com.customer.persistence.entity.Customer;
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
public class CustomerRepository implements IRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public CustomerRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public Customer insert(Customer customer) {
    MapSqlParameterSource namedParameters = getNamedParameters(customer);
    namedParameters.addValue(CUSTOMER_ACTIVE_PARAM, Boolean.TRUE);

    KeyHolder holder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(CUSTOMER_INSERT_QUERY, namedParameters, holder);
    customer.setId(Objects.requireNonNull(holder.getKey()).longValue());

    return customer;
  }

  public void update(Customer customer) {
    var namedParameters = getNamedParameters(customer);
    namedParameters.addValue(CUSTOMER_ID_PARAM, customer.getId());
    namedParameterJdbcTemplate.update(CUSTOMER_UPDATE_QUERY, namedParameters);
  }

  public void delete(Long id) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(CUSTOMER_ID_PARAM, id);
    namedParameterJdbcTemplate.update(CUSTOMER_DELETE_BY_ID_QUERY, namedParameters);
  }

  public Customer findById(Long id) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(CUSTOMER_ID_PARAM, id);
    namedParameters.addValue(CUSTOMER_ACTIVE_PARAM, Boolean.TRUE);
    var optionalCustomer = this.getEntity(namedParameters);

    return optionalCustomer.orElseThrow(
        () -> new DataNotFoundException(String.format("Customer with id %s was not found", id)));
  }

  public List<Customer> findAll(CustomerFilter filter) {
    var query = queryBuilder(CUSTOMER_FIND_ALL_QUERY);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();

    addConditions(query, namedParameters, filter);
    addPagination(
        query, filter.getPageSize(), (long) filter.getPageSize() * filter.getPageNumber());

    try {
      return namedParameterJdbcTemplate.query(
          query.toString(), namedParameters, CustomerRepository::mapRow);
    } catch (Exception e) {
      log.error(e);
      return Collections.emptyList();
    }
  }

  public int findAllCount(CustomerFilter filter) {
    var query = queryBuilder(CUSTOMER_FIND_COUNT_ALL_QUERY);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    addConditions(query, namedParameters, filter);
    var result =
        namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, Integer.class);
    return result != null ? result : 0;
  }

  private MapSqlParameterSource getNamedParameters(Customer customer) {
    var namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(CUSTOMER_FIRST_NAME_PARAM, customer.getFirstName());
    namedParameters.addValue(CUSTOMER_LAST_NAME_PARAM, customer.getLastName());
    namedParameters.addValue(CUSTOMER_BIRTHDAY_PARAM, customer.getBirthday());
    return namedParameters;
  }

  private void addConditions(
      StringBuilder query, MapSqlParameterSource namedParameters, CustomerFilter filter) {

    query.append("WHERE c.is_active = :active");
    namedParameters.addValue(CUSTOMER_ACTIVE_PARAM, Boolean.TRUE);

    if (filter.getFirstName() != null) {
      query.append(" AND c.first_name LIKE :firstName");
      namedParameters.addValue(
          CUSTOMER_FIRST_NAME_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getFirstName()));
    }

    if (filter.getLastName() != null) {
      query.append(" AND c.last_name LIKE :lastName");
      namedParameters.addValue(
          CUSTOMER_LAST_NAME_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getLastName()));
    }
  }

  private static Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
    return Customer.builder()
        .id(rs.getLong("c.id"))
        .birthday(rs.getDate("c.birthday").toLocalDate())
        .firstName(rs.getString("c.first_name"))
        .lastName(rs.getString("c.last_name"))
        .build();
  }

  private Optional<Customer> getEntity(MapSqlParameterSource namedParameters) {
    try {
      return Optional.ofNullable(
          namedParameterJdbcTemplate.queryForObject(
              CUSTOMER_FIND_BY_ID_QUERY, namedParameters, CustomerRepository::mapRow));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
