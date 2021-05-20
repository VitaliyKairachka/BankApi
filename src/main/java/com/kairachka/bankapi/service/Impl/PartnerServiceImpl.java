package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Partner;
import com.kairachka.bankapi.exception.PartnerNotFoundException;
import com.kairachka.bankapi.mapper.PartnerMapper;
import com.kairachka.bankapi.repository.Impl.PartnerRepositoryImpl;
import com.kairachka.bankapi.service.PartnerService;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public class PartnerServiceImpl implements PartnerService {
    private final PartnerRepositoryImpl partnerRepositoryImpl = new PartnerRepositoryImpl();
    private final PartnerMapper partnerMapper = new PartnerMapper();

    public boolean addPartner(HttpExchange exchange) {
        Partner partner = partnerMapper.JsonToPartner(exchange);
        return partnerRepositoryImpl.addPartner(partner);
    }

    public List<Partner> getAllPartners() {
        List<Partner> partnerList = partnerRepositoryImpl.getAllPartners();
        if (!partnerList.isEmpty()) {
            return partnerList;
        } else {
            throw new PartnerNotFoundException("Partner not found exception");
        }
    }
}
