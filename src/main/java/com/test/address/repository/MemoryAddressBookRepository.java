package com.test.address.repository;

import com.test.address.dto.AddressBookDto;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class MemoryAddressBookRepository implements AddressBookRepository {

    private static Map<String, AddressBookDto> addressBook = new ConcurrentHashMap<>();


    /**
     * 이름을 라이크 검색하여 해당 이름의 주소록을 모두 삭제 후 삭제한 데이터 전달
     *
     * @param name
     * @return
     */
    @Override
    public List<AddressBookDto> deleteByNameLike(String name) {
        List<AddressBookDto> deletedEntries = new ArrayList<>();

        addressBook.entrySet().removeIf(entry -> {
            if (entry.getValue().getName().contains(name)) {
                deletedEntries.add(entry.getValue());
                return true;
            }
            return false;
        });

        return deletedEntries;
    }

    /**
     * 폰넘버를 검색하여 해당 주소록 리스트를 조회
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public Optional<AddressBookDto> findByTelephone(String phoneNumber) {
        return Optional.ofNullable(addressBook.get(phoneNumber));
    }

    /**
     * email로 검색하여 해당 주소록을 조회-중복 이메일은 없으므로 단일 객체로 리턴
     *
     * @param email
     * @return
     */
    @Override
    public Optional<AddressBookDto> findByEmail(String email) {
        return addressBook.values().stream()
                .filter(addressBook -> addressBook.getEmail().equals(email))
                .findAny();
    }

    /**
     * 주소를 라이크 검색하여 해당 주소록 리스트를 조회, 오름차순으로
     *
     * @param address
     * @return
     */
    @Override
    public List<AddressBookDto> findByAddressContainingOrderByAddressAsc(String address) {
        Comparator<AddressBookDto> comparator = Comparator.comparing(AddressBookDto::getAddress)
                .thenComparing(AddressBookDto::getTelephone)
                .thenComparing(AddressBookDto::getEmail)
                .thenComparing(AddressBookDto::getName);

        return addressBook.values().stream()
                .filter(customer -> customer.getAddress().contains(address))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * 주소를 라이크 검색하여 해당 주소록 리스트를 조회, 내림차순으로
     *
     * @param address
     * @return
     */
    @Override
    public List<AddressBookDto> findByAddressContainingOrderByAddressDesc(String address) {
        Comparator<AddressBookDto> comparator = Comparator.comparing(AddressBookDto::getAddress)
                .thenComparing(AddressBookDto::getTelephone)
                .thenComparing(AddressBookDto::getEmail)
                .thenComparing(AddressBookDto::getName);

        comparator = comparator.reversed();

        return addressBook.values().stream()
                .filter(customer -> customer.getAddress().contains(address))
                .sorted(comparator)
                .collect(Collectors.toList());

    }

    /**
     * 이름을 라이크 검색하여 해당 주소록 리스트를 이름기준으로 내림차순으로 조회
     *
     * @param name
     * @return
     */
    @Override
    public List<AddressBookDto> findByNameContainingOrderByNameDesc(String name) {
        Comparator<AddressBookDto> comparator = Comparator.comparing(AddressBookDto::getName)
                .thenComparing(AddressBookDto::getTelephone)
                .thenComparing(AddressBookDto::getEmail)
                .thenComparing(AddressBookDto::getAddress);

        comparator = comparator.reversed();

        return addressBook.values().stream()
                .filter(customer -> customer.getName().contains(name))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * 이름을 라이크 검색하여 해당 주소록 리스트를 이름기준으로 오름차순으로 조회
     *
     * @param name
     * @return
     */
    @Override
    public List<AddressBookDto> findByNameContainingOrderByNameAsc(String name) {
        Comparator<AddressBookDto> comparator = Comparator.comparing(AddressBookDto::getName)
                .thenComparing(AddressBookDto::getTelephone)
                .thenComparing(AddressBookDto::getEmail)
                .thenComparing(AddressBookDto::getAddress);

        return addressBook.values().stream()
                .filter(customer -> customer.getName().contains(name))
                .sorted(comparator)
                .collect(Collectors.toList());

    }

    /**
     * 폰넘버를 받아 해당 내용의 데이터를 주소록에서 모두 삭제한다.(삭제)
     *
     * @param telephone
     * @return
     */
    @Override
    public Optional<AddressBookDto> deleteByTelephone(String telephone) {
        Optional<AddressBookDto> deletedEntry = addressBook.entrySet().stream()
                .filter(entry -> entry.getKey().equals(telephone))
                .map(entry -> {
                    AddressBookDto deletedDto = entry.getValue();
                    addressBook.remove(entry.getKey());
                    return deletedDto;
                })
                .findFirst();

        return deletedEntry;
    }

    @Override
    public List<AddressBookDto> findAll() {
        return new ArrayList<>(addressBook.values());
    }

    @Override
    public AddressBookDto save(AddressBookDto addressBookDto) {
        addressBook.put(addressBookDto.getTelephone(), addressBookDto);
        return addressBookDto;
    }

    public void clearAddressBook() {
        addressBook.clear();
    }
}
