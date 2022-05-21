package com.hrms.repository.attn;

import com.hrms.entity.attn.HrTlShftDtl;
import com.hrms.repository.generic.GenericRepository;

public interface HrTlShftDtlRepository extends GenericRepository<HrTlShftDtl> {

    HrTlShftDtl findByTitleContainingIgnoreCase(String shiftTitle);
}
