package br.com.customer.persistence.repository;




public interface IRepository {
  public String STRING_SEARCH_PATTERN = "%%%s%%";
  default void addPagination(StringBuilder query, Integer pageSize, Long pageOffset) {
    if (pageSize != null) query.append(" LIMIT ").append(pageSize);
    if (pageOffset != null) query.append(" OFFSET ").append(pageOffset);
  }

  default StringBuilder queryBuilder(String sql) {
    return new StringBuilder(sql);
  }

}
