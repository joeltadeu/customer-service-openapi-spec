package br.com.customer.persistence.repository.queries;

public class EmailQueries {

  public static final String EMAIL_ID_PARAM = "id";
  public static final String EMAIL_CUSTOMER_ID_PARAM = "customerId";
  public static final String EMAIL_PARAM = "email";
  public static final String EMAIL_TYPE_PARAM = "type";
  public static final String EMAIL_INSERT_QUERY =
      """
        INSERT INTO `customer_email`(
                  customer_id,
                  type,
                  email)
        VALUES (
                  :customerId,
                  :type,
                  :email);
        """;

  public static final String EMAIL_UPDATE_QUERY =
      """
        UPDATE `customer_email`
        SET
            customer_id = :customerId,
            type = :type,
            email = :email
        WHERE id = :id
        """;

  public static final String EMAIL_DELETE_BY_CUSTOMER_ID_QUERY =
      "DELETE FROM `customer_email` WHERE customer_id = :customerId";

  public static final String EMAIL_DELETE_BY_ID_QUERY = "DELETE FROM `customer_email` WHERE id = :id";

  public static final String EMAIL_FIND_BY_CUSTOMER_ID_QUERY =
      "SELECT e.* FROM `customer_email` e WHERE e.customer_id = :customerId AND e.id = :id ";

  public static final String EMAIL_FIND_BY_ID_QUERY = "SELECT e.* FROM `customer_email` e WHERE e.id = :id";
  public static final String EMAIL_FIND_ALL_BY_CUSTOMER_ID_QUERY =
      "SELECT e.* FROM `customer_email` e WHERE e.customer_id = :customerId";

  public static final String EMAIL_FIND_ALL_QUERY = "SELECT e.* FROM `customer_email` e ";
  public static final String EMAIL_FIND_COUNT_ALL_QUERY = "SELECT COUNT(e.id) FROM `customer_email` e ";
}
