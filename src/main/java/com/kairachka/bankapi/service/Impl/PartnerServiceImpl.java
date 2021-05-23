package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Partner;
import com.kairachka.bankapi.exception.PartnerNotFoundException;
import com.kairachka.bankapi.repository.Impl.PartnerRepositoryImpl;
import com.kairachka.bankapi.repository.PartnerRepository;
import com.kairachka.bankapi.service.PartnerService;

import java.util.List;

public class PartnerServiceImpl implements PartnerService {
    private PartnerRepository partnerRepository = new PartnerRepositoryImpl();

    public PartnerServiceImpl() {
    }

    public PartnerServiceImpl(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Override
    public boolean addPartner(Partner partner) {
        return partnerRepository.addPartner(partner);
    }

    @Override
    public List<Partner> getAllPartners() throws PartnerNotFoundException {
        List<Partner> partnerList = partnerRepository.getAllPartners();
        if (!partnerList.isEmpty()) {
            return partnerList;
        } else {
            throw new PartnerNotFoundException();
        }
    }
}
