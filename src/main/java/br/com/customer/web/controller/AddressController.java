package br.com.customer.web.controller;

import br.com.customer.openapi.api.AddressControllerApi;
import br.com.customer.openapi.model.*;
import br.com.customer.persistence.entity.Address;
import br.com.customer.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AddressController implements AddressControllerApi, IController {
  private final AddressService service;
  private final ObjectMapper objectMapper;

  @Override
  public ResponseEntity<AddressResponse> createAddress(Long customerId, AddressRequest request) {
    var address = objectMapper.convertValue(request, Address.class);
    address.setCustomerId(customerId);
    var savedAddress = service.create(address);
    var addressResponse = objectMapper.convertValue(savedAddress, AddressResponse.class);

    return ResponseEntity.created(getURI(addressResponse.getId())).body(addressResponse);
  }

  @Override
  public ResponseEntity<Void> deleteAddress(Long customerId, Long id) {
    var address = Address.builder().id(id).customerId(customerId).build();
    service.delete(address);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<AddressResponse> findAddressById(Long customerId, Long id) {
    final var address = service.findByCustomerIdAndId(customerId, id);
    final var addressResponse = objectMapper.convertValue(address, AddressResponse.class);

    return ResponseEntity.ok(addressResponse);
  }

  @Override
  public ResponseEntity<PageAddressResponse> listAllAddresses(
      Long customerId, AddressFilter filter) {

    Page<Address> addresses = service.findAll(customerId, filter);

    var fetchedList =
        addresses.stream()
            .map(address -> objectMapper.convertValue(address, AddressResponse.class))
            .collect(Collectors.toList());

    var page = new PageImpl<>(fetchedList, addresses.getPageable(), addresses.getTotalElements());
    var pageResponse = objectMapper.convertValue(page, PageAddressResponse.class);
    return ResponseEntity.ok(pageResponse);
  }

  @Override
  public ResponseEntity<Void> updateAddress(Long customerId, Long id, AddressRequest request) {
    var address = objectMapper.convertValue(request, Address.class);
    address.setId(id);
    address.setCustomerId(customerId);
    service.update(address);
    return ResponseEntity.ok().build();
  }
}
