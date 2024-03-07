package br.com.customer.util;

import br.com.customer.openapi.model.*;
import br.com.customer.persistence.entity.Address;
import br.com.customer.persistence.entity.Customer;
import br.com.customer.persistence.entity.Document;
import br.com.customer.persistence.entity.Email;
import br.com.customer.persistence.entity.enums.DocumentTypeEnum;
import br.com.customer.persistence.entity.enums.EmailTypeEnum;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class TestUtil {
  private static final ObjectMapper mapper;

  static {
    mapper = new ObjectMapper();
    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    dateFormat.setTimeZone(TimeZone.getDefault());
    mapper.setDateFormat(dateFormat);
  }

  public static String getJsonFromObject(Object object) throws JsonProcessingException {
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    return ow.writeValueAsString(object);
  }

  public static Customer getCustomer(Long id) {
    return getCustomer(id, getRandomString(), getRandomString(), getRandomLocalDate());
  }

  public static Customer getCustomer(
      Long id, List<Address> addresses, List<Email> emails, List<Document> documents) {
    var customer = getCustomer(id, getRandomString(), getRandomString(), getRandomLocalDate());
    customer.setAddresses(addresses);
    customer.setDocuments(documents);
    customer.setEmails(emails);
    return customer;
  }

  public static Customer getCustomer(
      Long id, String firstName, String lastName, LocalDate birthday) {
    return Customer.builder()
        .id(id)
        .birthday(birthday)
        .firstName(firstName)
        .lastName(lastName)
        .build();
  }

  public static Document getDocument(Long id) {
    return getDocument(id, DocumentTypeEnum.DRIVER_LICENSE, getRandomString());
  }

  public static Document getDocument(Long id, Long customerId) {
    var document = getDocument(id);
    document.setCustomerId(customerId);
    return document;
  }

  public static Document getDocument(Long id, DocumentTypeEnum type, String documentNumber) {
    return Document.builder().id(id).type(type).documentNumber(documentNumber).build();
  }

  public static Email getEmail(Long id) {
    return getEmail(id, EmailTypeEnum.PERSONAL, getRandomEmail());
  }

  public static Email getEmail(Long id, EmailTypeEnum type, String email) {
    return Email.builder().id(id).type(type).email(email).build();
  }

  public static Email getEmail(Long id, Long customerId) {
    var email = getEmail(id);
    email.setCustomerId(customerId);
    return email;
  }

  public static CustomerRequest getCustomerRequest(
      String firstName, String lastName, LocalDate birthday) {
    return new CustomerRequest(firstName, lastName, birthday);
  }

  public static EmailRequest getEmailRequest() {
    return new EmailRequest(EmailRequest.TypeEnum.valueOf("WORK"), getRandomEmail());
  }

  public static DocumentRequest getDocumentRequest() {
    return new DocumentRequest(
        DocumentRequest.TypeEnum.valueOf("DRIVER_LICENSE"), getRandomString());
  }

  public static AddressRequest getAddressRequest() {
    return new AddressRequest(
        getRandomString(),
        getRandomString(),
        getRandomString(),
        getRandomString(),
        getRandomString());
  }

  public static List<Email> getEmails() {
    return Arrays.asList(
        getEmail(getRandomLong(), EmailTypeEnum.valueOf("WORK"), getRandomEmail()),
        getEmail(getRandomLong(), EmailTypeEnum.valueOf("PERSONAL"), getRandomEmail()));
  }

  public static List<Document> getDocuments() {
    return Arrays.asList(
        getDocument(getRandomLong(), DocumentTypeEnum.valueOf("PASSPORT"), getRandomString()),
        getDocument(getRandomLong(), DocumentTypeEnum.valueOf("PPS"), getRandomString()));
  }

  public static List<Customer> getCustomers() {
    return Arrays.asList(
        getCustomer(getRandomLong(), getRandomString(), getRandomString(), getRandomLocalDate()),
        getCustomer(getRandomLong(), getRandomString(), getRandomString(), getRandomLocalDate()),
        getCustomer(getRandomLong(), getRandomString(), getRandomString(), getRandomLocalDate()));
  }

  public static CustomerFilter getCustomerFilter() {
    return new CustomerFilter();
  }

  public static DocumentFilter getDocumentFilter() {
    return new DocumentFilter();
  }

  public static EmailFilter getEmailFilter() {
    return new EmailFilter();
  }

  public static AddressFilter getAddressFilter() {
    return new AddressFilter();
  }

  public static List<Address> getAddresses() {
    return Arrays.asList(
        getAddress(
            getRandomLong(),
            getRandomString(),
            getRandomString(),
            getRandomString(),
            getRandomString(),
            getRandomString()),
        getAddress(
            getRandomLong(),
            getRandomString(),
            getRandomString(),
            getRandomString(),
            getRandomString(),
            getRandomString()),
        getAddress(
            getRandomLong(),
            getRandomString(),
            getRandomString(),
            getRandomString(),
            getRandomString(),
            getRandomString()));
  }

  public static Address getAddress(Long id) {
    return getAddress(
        id,
        getRandomString(),
        getRandomString(),
        getRandomString(),
        getRandomString(),
        getRandomString());
  }

  public static Address getAddress(Long id, Long customerId) {
    var address =
        getAddress(
            id,
            getRandomString(),
            getRandomString(),
            getRandomString(),
            getRandomString(),
            getRandomString());
    address.setCustomerId(customerId);
    return address;
  }

  public static Address getAddress(
      Long id, String street, String city, String county, String country, String eircode) {
    return Address.builder()
        .id(id)
        .street(street)
        .city(city)
        .county(county)
        .country(country)
        .eircode(eircode)
        .build();
  }

  public static Page<Customer> getPageCustomer() {
    var customers = getCustomers();
    return new PageImpl<>(customers, PageRequest.of(0, 10), customers.size());
  }

  public static Page<Address> getPageAddress() {
    var addresses = getAddresses();
    return new PageImpl<>(addresses, PageRequest.of(0, 10), addresses.size());
  }

  public static Page<Email> getPageEmail() {
    var emails = getEmails();
    return new PageImpl<>(emails, PageRequest.of(0, 10), emails.size());
  }

  public static Page<Document> getPageDocument() {
    var documents = getDocuments();
    return new PageImpl<>(documents, PageRequest.of(0, 10), documents.size());
  }

  public static Long getRandomLong() {
    return Long.valueOf(Math.abs(new Random().nextInt()));
  }

  public static Integer getRandomInteger() {
    return Math.abs(new Random().nextInt());
  }

  public static String getRandomString() {
    return String.valueOf(getRandomInteger());
  }

  public static String getRandomEmail() {
    return getRandomString()
        .concat(".")
        .concat(getRandomString())
        .concat("@")
        .concat(getRandomString())
        .concat("com");
  }

  public static LocalDate getRandomLocalDate() {
    int hundredYears = 100 * 365;
    return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextInt(-hundredYears, hundredYears));
  }
}
