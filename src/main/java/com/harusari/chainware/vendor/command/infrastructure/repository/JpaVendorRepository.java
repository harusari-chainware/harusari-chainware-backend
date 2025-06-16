package com.harusari.chainware.vendor.command.infrastructure.repository;

import com.harusari.chainware.vendor.command.domain.aggregate.Vendor;
import com.harusari.chainware.vendor.command.domain.repository.VendorRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVendorRepository extends VendorRepository, JpaRepository<Vendor, Long> {
}
