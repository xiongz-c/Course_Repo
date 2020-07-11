package com.sustech.dbproj.repository;

import com.sustech.dbproj.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
