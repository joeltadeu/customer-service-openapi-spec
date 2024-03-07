package br.com.customer.persistence.repository.queries;

public class CustomerQueries {

  public static final String CUSTOMER_ID_PARAM = "id";
  public static final String CUSTOMER_FIRST_NAME_PARAM = "firstName";
  public static final String CUSTOMER_LAST_NAME_PARAM = "lastName";
  public static final String CUSTOMER_BIRTHDAY_PARAM = "birthday";
  public static final String CUSTOMER_ACTIVE_PARAM = "active";
  public static final String CUSTOMER_INSERT_QUERY =
      """
          INSERT INTO `customer`(
                    first_name,
                    last_name,
                    birthday,
                    is_active)
          VALUES (
                    :firstName,
                    :lastName,
                    :birthday,
                    :active)
          """;

  public static final String CUSTOMER_UPDATE_QUERY =
      """
          UPDATE `customer`
          SET
              first_name = :firstName,
              last_name = :lastName,
              birthday = :birthday
          WHERE id = :id
          """;

  public static final String CUSTOMER_FIND_BY_ID_QUERY =
      "SELECT c.* FROM `customer` c WHERE c.id = :id AND c.is_active = :active";

  public static final String CUSTOMER_FIND_ALL_QUERY = "SELECT c.* FROM `customer` c ";
  public static final String CUSTOMER_FIND_COUNT_ALL_QUERY = "SELECT COUNT(c.id) FROM `customer` c ";
  public static final String CUSTOMER_DELETE_BY_ID_QUERY = "DELETE FROM `customer` WHERE id  = :id";
}
