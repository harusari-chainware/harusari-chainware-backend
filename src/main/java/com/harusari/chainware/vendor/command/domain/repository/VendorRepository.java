package com.harusari.chainware.vendor.command.domain.repository;

import com.harusari.chainware.vendor.command.domain.aggregate.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findVendorByVendorId(Long vendorId);

}