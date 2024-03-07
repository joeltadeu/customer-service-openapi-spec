package br.com.customer.persistence.repository;

import static br.com.customer.persistence.repository.queries.AddressQueries.*;

import br.com.customer.openapi.model.AddressFilter;
import br.com.customer.persistence.entity.Address;
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
public class AddressRepository implements IRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public AddressRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public Address insert(Address address) {
    MapSqlParameterSource namedParameters = getNamedParameters(address);

    KeyHolder holder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(ADDRESS_INSERT_QUERY, namedParameters, holder);
    address.setId(Objects.requireNonNull(holder.getKey()).longValue());

    return address;
  }

  public void update(Address address) {
    var namedParameters = getNamedParameters(address);
    namedParameters.addValue(ADDRESS_ID_PARAM, address.getId());
    namedParameterJdbcTemplate.update(ADDRESS_UPDATE_QUERY, namedParameters);
  }

  public void deleteByCustomerId(Long customerId) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(ADDRESS_CUSTOMER_ID_PARAM, customerId);
    namedParameterJdbcTemplate.update(ADDRESS_DELETE_BY_CUSTOMER_ID_QUERY, namedParameters);
  }

  public void delete(Long id) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(ADDRESS_ID_PARAM, id);
    namedParameterJdbcTemplate.update(ADDRESS_DELETE_BY_ID_QUERY, namedParameters);
  }

  public Address findByCustomerIdAndId(Long customerId, Long id) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(ADDRESS_CUSTOMER_ID_PARAM, customerId);
    namedParameters.addValue(ADDRESS_ID_PARAM, id);

    var optionalAddress = this.getEntity(ADDRESS_FIND_BY_CUSTOMER_ID_QUERY, namedParameters);

    return optionalAddress.orElseThrow(
        () ->
            new DataNotFoundException(
                String.format(
                    "Address with id %s and customer id %s was not found", id, customerId)));
  }

  public Optional<Address> findById(Long id) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(ADDRESS_ID_PARAM, id);

    return this.getEntity(ADDRESS_FIND_BY_ID, namedParameters);
  }

  public List<Address> findAllByCustomerId(Long customerId) {

    var query = queryBuilder(ADDRESS_FIND_ALL_BY_CUSTOMER_ID_QUERY);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(ADDRESS_CUSTOMER_ID_PARAM, customerId);

    try {
      return namedParameterJdbcTemplate.query(
          query.toString(), namedParameters, AddressRepository::mapRow);
    } catch (Exception e) {
      log.error(e);
      return Collections.emptyList();
    }
  }

  public List<Address> findAll(Long hotelId, AddressFilter filter) {
    var query = queryBuilder(ADDRESS_FIND_ALL_QUERY);
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();

    addConditions(query, namedParameters, filter, hotelId);
    addPagination(
        query, filter.getPageSize(), (long) filter.getPageSize() * filter.getPageNumber());

    try {
      return namedParameterJdbcTemplate.query(
          query.toString(), namedParameters, AddressRepository::mapRow);
    } catch (Exception e) {
      log.error(e);
      return Collections.emptyList();
    }
  }

  public int findAllCount(Long hotelId, AddressFilter filter) {
    var query = queryBuilder(ADDRESS_FIND_COUNT_ALL_QUERY);

    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    addConditions(query, namedParameters, filter, hotelId);

    var result =
        namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, Integer.class);
    return result != null ? result : 0;
  }

  private void addConditions(
      StringBuilder query,
      MapSqlParameterSource namedParameters,
      AddressFilter filter,
      Long customerId) {

    query.append("WHERE");
    query.append(" a.customer_id = :customerId");
    namedParameters.addValue(ADDRESS_CUSTOMER_ID_PARAM, customerId);

    if (filter.getStreet() != null) {
      query.append(" AND a.street LIKE :street");
      namedParameters.addValue(
          ADDRESS_STREET_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getStreet()));
    }

    if (filter.getCity() != null) {
      query.append(" AND a.city LIKE :city");
      namedParameters.addValue(
          ADDRESS_CITY_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getCity()));
    }

    if (filter.getEircode() != null) {
      query.append(" AND a.eircode LIKE :eircode");
      namedParameters.addValue(
          ADDRESS_EIRCODE_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getEircode()));
    }

    if (filter.getCounty() != null) {
      query.append(" AND a.county LIKE :county");
      namedParameters.addValue(
          ADDRESS_COUNTY_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getCounty()));
    }

    if (filter.getCountry() != null) {
      query.append(" AND a.country LIKE :country");
      namedParameters.addValue(
          ADDRESS_COUNTRY_PARAM, String.format(STRING_SEARCH_PATTERN, filter.getCountry()));
    }
  }

  private MapSqlParameterSource getNamedParameters(Address address) {
    var namedParameters = new MapSqlParameterSource();
    namedParameters.addValue(ADDRESS_CUSTOMER_ID_PARAM, address.getCustomerId());
    namedParameters.addValue(ADDRESS_STREET_PARAM, address.getStreet());
    namedParameters.addValue(ADDRESS_COMPLEMENT_PARAM, address.getComplement());
    namedParameters.addValue(ADDRESS_CITY_PARAM, address.getCity());
    namedParameters.addValue(ADDRESS_COUNTY_PARAM, address.getCounty());
    namedParameters.addValue(ADDRESS_COUNTRY_PARAM, address.getCountry());
    namedParameters.addValue(ADDRESS_EIRCODE_PARAM, address.getEircode());
    return namedParameters;
  }

  private static Address mapRow(ResultSet rs, int rowNum) throws SQLException {
    return Address.builder()
        .id(rs.getLong("a.id"))
        .customerId(rs.getLong("a.customer_id"))
        .street(rs.getString("a.street"))
        .complement(rs.getString("a.complement"))
        .city(rs.getString("a.city"))
        .county(rs.getString("a.county"))
        .country(rs.getString("a.country"))
        .eircode(rs.getString("a.eircode"))
        .build();
  }

  private Optional<Address> getEntity(String sql, MapSqlParameterSource namedParameters) {
    try {
      return Optional.ofNullable(
          namedParameterJdbcTemplate.queryForObject(
              sql, namedParameters, AddressRepository::mapRow));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
