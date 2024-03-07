package br.com.customer.service;

import br.com.customer.openapi.model.AddressFilter;
import br.com.customer.persistence.entity.Address;
import br.com.customer.persistence.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
  private final AddressRepository addressRepository;

  public Address create(Address address) {
    return addressRepository.insert(address);
  }

  public void update(Address address) {
    addressRepository.findByCustomerIdAndId(address.getCustomerId(), address.getId());
    addressRepository.update(address);
  }

  public Address findByCustomerIdAndId(Long customerId, Long id) {
    return addressRepository.findByCustomerIdAndId(customerId, id);
  }

  public Page<Address> findAll(Long customerId, AddressFilter filter) {
    var addresses = addressRepository.findAll(customerId, filter);
    var count = addressRepository.findAllCount(customerId, filter);

    return new PageImpl<>(
        addresses, PageRequest.of(filter.getPageNumber(), filter.getPageSize()), count);
  }

  public void delete(Address address) {
    addressRepository.findByCustomerIdAndId(address.getCustomerId(), address.getId());
    addressRepository.delete(address.getId());
  }
}
