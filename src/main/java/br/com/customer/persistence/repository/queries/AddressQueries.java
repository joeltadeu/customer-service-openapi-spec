package br.com.customer.persistence.repository.queries;

public class AddressQueries {

  public static final String ADDRESS_ID_PARAM = "id";
  public static final String ADDRESS_CUSTOMER_ID_PARAM = "customerId";
  public static final String ADDRESS_STREET_PARAM = "street";
  public static final String ADDRESS_COMPLEMENT_PARAM = "complement";
  public static final String ADDRESS_CITY_PARAM = "city";
  public static final String ADDRESS_COUNTY_PARAM = "county";
  public static final String ADDRESS_COUNTRY_PARAM = "country";
  public static final String ADDRESS_EIRCODE_PARAM = "eircode";

  public static final String ADDRESS_INSERT_QUERY =
      """
          INSERT INTO `customer_address`(
                    customer_id,
                    street,
                    complement,
                    city,
                    county,
                    country,
                    eircode)
          VALUES (
                    :customerId,
                    :street,
                    :complement,
                    :city,
                    :county,
                    :country,
                    :eircode);
          """;

  public static final String ADDRESS_UPDATE_QUERY =
      """
          UPDATE `customer_address`
          SET
              customer_id = :customerId,
              street = :street,
              complement = :complement,
              city = :city,
              county = :county,
              country = :country,
              eircode = :eircode
          WHERE id = :id
          """;

  public static final String ADDRESS_FIND_BY_CUSTOMER_ID_QUERY =
      "SELECT a.* FROM `customer_address` a WHERE a.customer_id = :customerId AND a.id = :id";

  public static final String ADDRESS_FIND_ALL_BY_CUSTOMER_ID_QUERY =
      "SELECT a.* from `customer_address` a WHERE a.customer_id = :customerId";

  public static final String ADDRESS_FIND_BY_ID = "SELECT a.* from `customer_address` WHERE a.id = :id";
  public static final String ADDRESS_FIND_ALL_QUERY = "SELECT a.* FROM `customer_address` a ";
  public static final String ADDRESS_FIND_COUNT_ALL_QUERY = "SELECT COUNT(a.id) FROM `customer_address` a ";
  public static final String ADDRESS_DELETE_BY_ID_QUERY = "DELETE FROM `customer_address` WHERE id = :id";
  public static final String ADDRESS_DELETE_BY_CUSTOMER_ID_QUERY =
      "DELETE FROM `customer_address` WHERE customer_id = :customerId";
}
