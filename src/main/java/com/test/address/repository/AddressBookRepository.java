package com.test.address.repository;

import com.test.address.dto.AddressBookDto;

import java.util.List;
import java.util.Optional;

public interface AddressBookRepository {

    List<AddressBookDto> deleteByNameLike(String name);

    // 전화번호 기준 오름차순
    Optional<AddressBookDto> findByTelephone(String phoneNumber);
    Optional <AddressBookDto> findByEmail(String email);
    List<AddressBookDto> findByAddressContainingOrderByAddressAsc(String address);
    List<AddressBookDto> findByAddressContainingOrderByAddressDesc(String address);

    List<AddressBookDto> findByNameContainingOrderByNameDesc(String name);
    List<AddressBookDto> findByNameContainingOrderByNameAsc(String name);
    Optional<AddressBookDto> deleteByTelephone(String telephone);
    List<AddressBookDto> findAll();
    AddressBookDto save(AddressBookDto addressBookDto);
}
