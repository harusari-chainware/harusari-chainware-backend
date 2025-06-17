package com.harusari.chainware.vendor.command.domain.repository;

import com.harusari.chainware.vendor.command.domain.aggregate.Vendor;

import java.util.Optional;


public interface VendorRepository {

    Vendor save(Vendor vendor);

    Optional<Vendor> findById(Long vendorId);
}
