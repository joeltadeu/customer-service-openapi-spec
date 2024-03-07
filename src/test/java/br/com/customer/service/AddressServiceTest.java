package br.com.customer.service;

import static br.com.customer.util.TestUtil.*;
import static br.com.customer.util.TestUtil.getRandomString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.customer.persistence.entity.Address;
import br.com.customer.persistence.repository.AddressRepository;
import br.com.customer.web.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
  @Mock AddressRepository addressRepositoryMock;

  @InjectMocks AddressService addressService;

  @Captor private ArgumentCaptor<Address> addressArgumentCaptor;

  @Test
  public void createAddress_shouldCreateAddress() {
    final var street = getRandomString();
    final var city = getRandomString();
    final var county = getRandomString();
    final var country = getRandomString();
    final var eircode = getRandomString();
    final var addressId = getRandomLong();

    final var incomeAddress = getAddress(null, street, city, county, country, eircode);
    final var outcomeAddress = getAddress(addressId, street, city, county, country, eircode);

    when(addressRepositoryMock.insert(addressArgumentCaptor.capture())).thenReturn(outcomeAddress);

    final var result = addressService.create(incomeAddress);

    assertEquals(outcomeAddress, result);

    final var argumentCaptorValue = addressArgumentCaptor.getValue();

    assertEquals(incomeAddress.getStreet(), argumentCaptorValue.getStreet());
    assertEquals(incomeAddress.getCity(), argumentCaptorValue.getCity());
    assertEquals(incomeAddress.getCounty(), argumentCaptorValue.getCounty());
    assertEquals(incomeAddress.getCountry(), argumentCaptorValue.getCountry());
    assertEquals(incomeAddress.getEircode(), argumentCaptorValue.getEircode());
    assertNull(argumentCaptorValue.getId());

    verify(addressRepositoryMock, times(1)).insert(argumentCaptorValue);
  }

  @Test
  public void updateAddress_shouldUpdateAddress() {
    final var addressId = getRandomLong();
    final var customerId = getRandomLong();

    final var address = getAddress(addressId, customerId);

    when(addressRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong())).thenReturn(address);

    addressService.update(address);

    verify(addressRepositoryMock, times(1)).findByCustomerIdAndId(customerId, addressId);
    verify(addressRepositoryMock, times(1)).update(address);
  }

  @Test
  public void updateAddress_shouldNotUpdateWhenAddressNotFound() {
    final var addressId = getRandomLong();
    final var customerId = getRandomLong();
    final var address = getAddress(addressId, customerId);

    when(addressRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong()))
        .thenThrow(
            new DataNotFoundException(
                String.format("Address with id %s was not found", addressId)));

    final var assertThrows =
        assertThrows(DataNotFoundException.class, () -> addressService.update(address));
    assertEquals(
        "Address with id %s was not found".formatted(addressId), assertThrows.getMessage());
    verify(addressRepositoryMock, times(1)).findByCustomerIdAndId(customerId, addressId);
    verify(addressRepositoryMock, times(0)).update(address);
  }

  @Test
  public void deleteAddressById_shouldDelete() {
    final var customerId = getRandomLong();
    final var customer = getCustomer(customerId);
    final var addressId = getRandomLong();
    final var address = getAddress(addressId, customerId);

    when(addressRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong())).thenReturn(address);
    doNothing().when(addressRepositoryMock).delete(addressId);
    addressService.delete(address);

    verify(addressRepositoryMock, times(1)).findByCustomerIdAndId(anyLong(), anyLong());
    verify(addressRepositoryMock, times(1)).delete(anyLong());
  }

  @Test
  public void findAddressById_shouldFind() {
    final var addressId = getRandomLong();
    final var customerId = getRandomLong();
    final var returnedAddress = getAddress(addressId);

    when(addressRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong())).thenReturn(returnedAddress);

    final var addressReturnedFromDatabase =
        addressService.findByCustomerIdAndId(customerId, addressId);

    assertEquals(returnedAddress, addressReturnedFromDatabase);
    verify(addressRepositoryMock, times(1)).findByCustomerIdAndId(anyLong(), anyLong());
  }

  @Test
  public void findAddressById_shouldNotFind() {
    final var addressId = getRandomLong();
    final var customerId = getRandomLong();

    when(addressRepositoryMock.findByCustomerIdAndId(anyLong(), anyLong()))
        .thenThrow(
            new DataNotFoundException(
                String.format("Address with id %s was not found", addressId)));

    final var assertThrows =
        assertThrows(
            DataNotFoundException.class,
            () -> addressService.findByCustomerIdAndId(customerId, addressId));

    assertEquals(
        "Address with id %s was not found".formatted(addressId), assertThrows.getMessage());

    verify(addressRepositoryMock, times(1)).findByCustomerIdAndId(anyLong(), anyLong());
  }

  @Test
  public void findAllAddress_shouldFind() {
    final var filter = getAddressFilter();
    final var addresses = getAddresses();
    final var customerId = getRandomLong();

    when(addressRepositoryMock.findAll(customerId, filter)).thenReturn(addresses);
    when(addressRepositoryMock.findAllCount(customerId, filter)).thenReturn(addresses.size());

    final var pageAddress = addressService.findAll(customerId, filter);

    assertEquals(addresses.size(), pageAddress.getTotalElements());
    assertEquals(1, pageAddress.getTotalPages());
    assertEquals(10, pageAddress.getSize());
    assertNotNull(pageAddress.getContent());
    assertEquals(0, pageAddress.getNumber());
    assertEquals(addresses.size(), pageAddress.getNumberOfElements());

    verify(addressRepositoryMock, times(1)).findAll(customerId, filter);
    verify(addressRepositoryMock, times(1)).findAllCount(customerId, filter);
  }
}
