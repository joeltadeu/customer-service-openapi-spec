package br.com.customer.persistence.repository.queries;

public class DocumentQueries {

  public static final String DOCUMENT_ID_PARAM = "id";
  public static final String DOCUMENT_CUSTOMER_ID_PARAM = "customerId";
  public static final String DOCUMENT_NUMBER_PARAM = "documentNumber";
  public static final String DOCUMENT_TYPE_PARAM = "type";
  public static final String DOCUMENT_INSERT_QUERY =
      """
        INSERT INTO `customer_document` (
                  customer_id,
                  type,
                  document_number)
        VALUES (
                  :customerId,
                  :type,
                  :documentNumber);
        """;

  public static final String DOCUMENT_UPDATE_QUERY =
      """
        UPDATE `customer_document`
        SET
            customer_id = :customerId,
            type = :type,
            document_number = :documentNumber
        WHERE id = :id
        """;

  public static final String DOCUMENT_DELETE_BY_CUSTOMER_ID_QUERY =
      "DELETE FROM `customer_document` WHERE customer_id = :customerId";

  public static final String DOCUMENT_DELETE_BY_ID_QUERY =
      "DELETE FROM `customer_document` WHERE id = :id";

  public static final String DOCUMENT_FIND_BY_CUSTOMER_ID_QUERY =
      "SELECT d.* FROM `customer_document` d WHERE d.customer_id = :customerId AND d.id = :id";

  public static final String DOCUMENT_FIND_BY_ID_QUERY =
      "SELECT d.* FROM `customer_document` WHERE d.id = :id";

  public static final String DOCUMENT_FIND_ALL_BY_CUSTOMER_ID_QUERY =
      "SELECT d.* FROM `customer_document` d WHERE d.customer_id = :customerId";

  public static final String DOCUMENT_FIND_ALL_QUERY = "SELECT d.* FROM `customer_document` d ";
  public static final String DOCUMENT_FIND_COUNT_ALL_QUERY = "SELECT COUNT(d.id) FROM `customer_document` d ";
}
