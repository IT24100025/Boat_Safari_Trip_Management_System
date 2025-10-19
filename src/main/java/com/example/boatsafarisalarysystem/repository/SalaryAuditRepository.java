package com.example.boatsafarisalarysystem.repository;

import com.example.boatsafarisalarysystem.entity.SalaryAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalaryAuditRepository extends JpaRepository<SalaryAudit, Integer> {
    List<SalaryAudit> findBySalaryPayment_StaffId(Integer staffId);
    List<SalaryAudit> findBySalaryPayment_SalaryId(Integer salaryId);
}