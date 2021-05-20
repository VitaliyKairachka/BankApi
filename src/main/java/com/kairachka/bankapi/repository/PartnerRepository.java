package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Partner;

import java.util.List;

public interface PartnerRepository {
    boolean addPartner(Partner partner);

    List<Partner> getAllPartners();
}
